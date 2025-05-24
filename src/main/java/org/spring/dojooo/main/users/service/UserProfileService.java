package org.spring.dojooo.main.users.service;

import lombok.RequiredArgsConstructor;
import org.spring.dojooo.auth.jwt.dto.CustomUserDetails;
import org.spring.dojooo.auth.jwt.security.CustomUserDetailsService;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.global.exception.NotFoundException;
import org.spring.dojooo.main.users.domain.Profile;
import org.spring.dojooo.main.users.domain.User;
import org.spring.dojooo.main.users.dto.ProfileDetails;
import org.spring.dojooo.main.users.dto.ProfileEditRequest;
import org.spring.dojooo.main.users.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileService {

    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;

    //프로필 조회
    public ProfileDetails getProfile(Long userId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long currentUserId = userDetails.getId();
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
        String imageUrl = user.getProfile().getProfileImage();
        //본인 프로필
        if(user.getId().equals(currentUserId)) {
            return ProfileDetails.of(user, true);
        }
        //본인 프로필이 아닌 경우
        else {
            return ProfileDetails.of(user, false);
        }
    }
    //

}

