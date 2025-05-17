package org.spring.dojooo.main.users.service;

import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.main.users.dto.UserSignUpRequest;
import org.spring.dojooo.main.users.exception.DuplicateUserException;
import org.spring.dojooo.main.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Long saveUser(UserSignUpRequest userSignUpRequest) {
        //이미 존재하는 회원인지 아닌지 확인
        validateDuplicateUser(userSignUpRequest);
        return userRepository
                .save(userSignUpRequest.toEntity())
                .getId();
    }


    private void validateDuplicateUser(UserSignUpRequest userSignUpRequest) {
        if (userRepository.existsByEmail(userSignUpRequest.getEmail())) {
            throw new DuplicateUserException(ErrorCode.CONFLICT_ERROR);
        }
    }

}
