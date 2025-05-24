package org.spring.dojooo.Image.Service;

import lombok.RequiredArgsConstructor;
import org.spring.dojooo.Image.domain.Image;
import org.spring.dojooo.Image.repository.ImageRepository;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.S3.S3FileService;
import org.spring.dojooo.main.users.domain.User;
import org.spring.dojooo.main.users.exception.NotFoundUserException;
import org.spring.dojooo.main.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final S3FileService s3Uploader;
    private final UserRepository userRepository;

    @Transactional
    public String uploadProfileImage(MultipartFile file,String email) throws IOException {
        //이메일로 유저 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new NotFoundUserException(ErrorCode.NOT_FOUND_USER));
        //기존에 등록되어 있는 이미지가 있는지 => 있으면 삭제 (새로 업데이트 하기 위해서)
        imageRepository.findByUserId(user.getId())
                .ifPresent(oldImage -> {
                    s3Uploader.deleteImageFromS3(oldImage.getUrl()); //S3에서 삭제
                    imageRepository.delete(oldImage);//Repository 에서 삭제
                });
        //S3에 이미지 업로드
        String uploadedUrl = s3Uploader.upload(file,"profile");//profile/--

        //Image 엔티티 저장
        Image image = Image.builder()
                .fileName(file.getOriginalFilename())
                .url(uploadedUrl)
                .user(user)
                .build();

        //Repository 저장
        imageRepository.save(image);

        return uploadedUrl;

    }

}
