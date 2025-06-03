package org.spring.dojooo.main.contents.service;

import lombok.RequiredArgsConstructor;
import org.spring.dojooo.auth.jwt.dto.CustomUserDetails;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.main.contents.domain.TechLog;
import org.spring.dojooo.main.contents.dto.TechLogAddRequest;
import org.spring.dojooo.main.contents.dto.TechLogEditRequest;
import org.spring.dojooo.main.contents.exception.DuplicateTechLogTitleException;
import org.spring.dojooo.main.contents.exception.MaxTechTitleException;
import org.spring.dojooo.main.contents.exception.NotFoundTechLogException;
import org.spring.dojooo.main.contents.repository.TechLogRepository;
import org.spring.dojooo.main.users.domain.User;
import org.spring.dojooo.main.users.exception.NotFoundUserException;
import org.spring.dojooo.main.users.exception.NotUserEqualsCurrentUserException;
import org.spring.dojooo.main.users.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TechLogService {
    private final TechLogRepository techLogRepository;
    private final UserRepository userRepository;

    @Transactional //처음 작성 - 저장
    public TechLog techLogSave(Long userId, TechLogAddRequest techLogAddRequest, Authentication authentication) {
        //사용자 본인만 작성가능하도록
        User user = getCurrentUser(userId, authentication);
        //20자 이내의 제목
        techLogTitleCheckLength(techLogAddRequest.getTitle());
        //중복된 제목인지 확인
        techLogTitleCheckDuplicate(techLogAddRequest.getTitle());

        TechLog saveTechLog = techLogAddRequest.toEntity(user);
        techLogRepository.save(saveTechLog);

        return saveTechLog;
    }
    //작성한 글 수정
    @Transactional
    public TechLog techLogEdit(Long userId, TechLogEditRequest techLogEditRequest, Authentication authentication) {
        //사용자 본인만 수정 가능하도록
        User user = getCurrentUser(userId, authentication);
        String title = techLogEditRequest.getTitle();
        //기존 글 찾고
        TechLog techLog = techLogRepository.findByTechLogId(techLogEditRequest.getId())
                .orElseThrow(() -> new NotFoundTechLogException(ErrorCode.NOTFOUND_TECHLOG_EXCEPTION));
        if(techLogEditRequest.getTitle() != null && !techLogEditRequest.getTitle().isBlank() && !techLogEditRequest.getTitle().equals(techLog.getTitle())) {
            techLogTitleCheckLength(title); //20자 이내의 제목인지 확인
            techLog.updateTechLogTitle(title);
        }
        techLog.updateTechLogContent(techLogEditRequest.getContent());

        return techLog;
    }
    //현재 로그인한 사용자 정보 == 로그인한 사용자와 정보가 같은지 확인
    public User getCurrentUser(Long userId, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long currentUserId = customUserDetails.getId();
        if(!userId.equals(currentUserId)) {
            throw new NotUserEqualsCurrentUserException(ErrorCode.NOT_USER_EQUALS_CURRENTUSER);
        }
        return userRepository.findByIdAndIsDeletedFalse(currentUserId).orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER));

    }
    //20자 내외
    public void techLogTitleCheckLength(String title) {
        if(title.length() > 20){
            throw new MaxTechTitleException(ErrorCode.MAX_TITLE_LENGTH_EXCEPTION);
        }
    }
    //중복된 제목인지 확인
    public void techLogTitleCheckDuplicate(String title) {
        if(techLogRepository.findByTechLogTitle(title).isPresent()) {
            throw new DuplicateTechLogTitleException(ErrorCode.DUPLICATE_TITLE_EXCEPTION);
        }
    }


}
