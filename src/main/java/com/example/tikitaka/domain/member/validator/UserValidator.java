package com.example.tikitaka.domain.member.validator;

import com.example.tikitaka.domain.member.MemberErrorCode;
import com.example.tikitaka.domain.member.repository.UserRepository;
import com.example.tikitaka.global.config.auth.user.User;
import com.example.tikitaka.global.exception.BaseErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    public User validateUser(Long userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new BaseErrorException(MemberErrorCode.USER_NOT_FOUND));
    }


}
