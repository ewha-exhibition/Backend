package com.example.tikitaka.domain.member.validator;

import com.example.tikitaka.domain.member.MemberErrorCode;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.repository.MemberRepository;
import com.example.tikitaka.global.exception.BaseErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberValidator {
    private final MemberRepository memberRepository;

    public Member validateUser(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new BaseErrorException(MemberErrorCode.USER_NOT_FOUND));
    }


}
