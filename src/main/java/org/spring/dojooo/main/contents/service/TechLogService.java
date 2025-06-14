package org.spring.dojooo.main.contents.service;

import lombok.RequiredArgsConstructor;
import org.spring.dojooo.auth.jwt.dto.CustomUserDetails;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.Redis.RedisUtil;
import org.spring.dojooo.global.S3.S3FileService;
import org.spring.dojooo.main.contents.domain.TechLog;
import org.spring.dojooo.main.contents.dto.*;
import org.spring.dojooo.main.contents.exception.*;
import org.spring.dojooo.main.contents.repository.TechLogRepository;
import org.spring.dojooo.main.users.domain.User;
import org.spring.dojooo.main.users.exception.NotFoundUserException;
import org.spring.dojooo.main.users.exception.NotUserEqualsCurrentUserException;
import org.spring.dojooo.main.users.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TechLogService {
    private final TechLogRepository techLogRepository;
    private final UserRepository userRepository;
    private final RedisUtil redisUtil;
    private final TechLogTempService techLogTempService;
    private final S3FileService s3FileService;

    /**
     * 기술 블로그 정식 저장
     * - 제목 중복 및 길이 검증
     * - 정식 저장 후 Redis에 임시 저장본이 존재하면 삭제
     */

    @Transactional //처음 작성 - 저장
    public TechLogWithUser techLogSave(Long userId,String tempUuid,TechLogAddRequest techLogAddRequest, Authentication authentication) {
        //사용자 본인만 작성가능하도록
        User user = getCurrentUser(userId, authentication);
        validateTitle(techLogAddRequest.getTitle(), techLogAddRequest.getTechLogId());
        TechLog saveTechLog = techLogAddRequest.toEntity(user);
        techLogRepository.save(saveTechLog);
        //임시저장된 글 있는지확인하고 -> 있으면 삭제
        if (tempUuid != null && !tempUuid.isBlank()) {
            redisUtil.deleteTempLog(userId, tempUuid);
        }
        return new TechLogWithUser(saveTechLog, user);
    }
    //작성한 글 수정
    @Transactional
    public TechLog techLogEdit(Long userId, Long techLogId, TechLogEditRequest techLogEditRequest, Authentication authentication) {
        //사용자 본인만 수정 가능하도록
        User user = getCurrentUser(userId, authentication);
        String title = techLogEditRequest.getTitle();
        //기존 글 찾고
        TechLog techLog = techLogRepository.findByTechLogidAndIsDeletedFalse(techLogId)
                .orElseThrow(() -> new NotFoundTechLogException(ErrorCode.NOTFOUND_TECHLOG_EXCEPTION));
        if(techLogEditRequest.getTitle() != null && !techLogEditRequest.getTitle().isBlank() && !techLogEditRequest.getTitle().equals(techLog.getTitle())) {
            validateTitle(techLogEditRequest.getTitle(), techLog.getTechLogid());
            techLog.updateTechLogTitle(title);
        }

        techLog.updateTechLogContent(techLogEditRequest.getContent());
        return techLog;

    }

    //공개여부 설정
    @Transactional
    public void updateVisibility(Long userId, Long id, boolean isPublic, Authentication authentication) {
        User user = getCurrentUser(userId, authentication);
        TechLog techLog = techLogRepository.findByTechLogidAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundTechLogException(ErrorCode.NOTFOUND_TECHLOG_EXCEPTION));
        if(!techLog.getUser().getId().equals(user.getId())) {
            throw new NotUserEqualsCurrentUserException(ErrorCode.NOT_USER_EQUALS_CURRENTUSER);
        }
        techLog.changeIsPublic(isPublic);
    }

    @Transactional
    public Long deleteTechLog(Long userId, TechLogDeleteRequest techLogdeleteRequest, Authentication authentication) {
        User user = getCurrentUser(userId, authentication);

        TechLog techLog = techLogRepository.findByTechLogidAndIsDeletedFalse(techLogdeleteRequest.getTechLogId())
                .orElseThrow(() -> new NotFoundTechLogException(ErrorCode.NOTFOUND_TECHLOG_EXCEPTION));
        String thumbnailImageUrl= techLog.getThumbnailImageUrl();
        String contentImageUrl = techLog.getContentImageUrl();

        //삭제 권한
        if(!techLog.getUser().getId().equals(user.getId())) {
            throw new NotUserEqualsCurrentUserException(ErrorCode.NOT_USER_EQUALS_CURRENTUSER);
        }
        if (thumbnailImageUrl!= null && !thumbnailImageUrl.isBlank()) {
            s3FileService.deleteImageFromS3(thumbnailImageUrl);
        }
        if(contentImageUrl != null && !contentImageUrl.isBlank()) {
            s3FileService.deleteImageFromS3(contentImageUrl);
        }
            //soft delete
        techLog.changeIsDeleted(true);
        return techLog.getTechLogid();
    }
    @Transactional(readOnly = true)
    public TechLog loadTechLog(Long userId, Long techLogId, Authentication authentication) {
        TechLog techLog = techLogRepository.findByTechLogidAndIsDeletedFalse(techLogId)
                .orElseThrow(() -> new NotFoundTechLogException(ErrorCode.NOTFOUND_TECHLOG_EXCEPTION));

        if (!techLog.isPublic()) {
            User user = getCurrentUser(userId, authentication);
            if (!techLog.getUser().getId().equals(user.getId())) {
                throw new NotUserEqualsCurrentUserException(ErrorCode.SECRET_TECHLOG_EXCEPTION);
            }
        }
        //LAZY 초기화
        techLog.getUser().getNickname();
        return techLog;
    }
    //전체 조회(공개, 사용자 상관없이)
    @Transactional(readOnly = true)
    public List<TechLogLoadResponse> findAllTechLogs() {
        List<TechLog> techLogs = techLogRepository.findAllByIsPublic();
        return techLogs.stream()
                .map(techLog -> TechLogLoadResponse.from(techLog, techLog.getUser()))
                .toList();
    }
    //본인 글 전체조회(공개,비공개 상관없이)
    @Transactional(readOnly = true)
    public List<TechLogLoadResponse> findAllMyTechLogs(Long userId, Authentication authentication) {
        User currentUser = getCurrentUser(userId, authentication);
        List<TechLog> techLogs = techLogRepository.findAllByUserId(currentUser.getId());
        return techLogs.stream()
                .map(techLog -> TechLogLoadResponse.from(techLog,currentUser))
                .toList();
    }

    //현재 로그인한 사용자 정보 == 로그인한 사용자와 정보가 같은지 확인
    public User getCurrentUser(Long userId, Authentication authentication) {
        Long currentUserId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        if(!userId.equals(currentUserId)) {
            throw new NotUserEqualsCurrentUserException(ErrorCode.NOT_USER_EQUALS_CURRENTUSER);
        }
        return userRepository.findByIdAndIsDeletedFalse(currentUserId).orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER));

    }
    //제목 중복 및 20자
    private void validateTitle(String title, Long currentTechLogId) {
        Optional<TechLog> existing = techLogRepository.findByTitleAndIsDeletedFalse(title);
        if (existing.isPresent() && !existing.get().getTechLogid().equals(currentTechLogId)) {
            throw new DuplicateTechLogTitleException(ErrorCode.DUPLICATE_TITLE_EXCEPTION);
        }
        if (title.length() > 20) {
            throw new MaxTechTitleException(ErrorCode.MAX_TITLE_LENGTH_EXCEPTION);
        }
    }

}
