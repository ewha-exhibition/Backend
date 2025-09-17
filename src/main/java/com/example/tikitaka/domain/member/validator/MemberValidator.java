package com.example.tikitaka.domain.member.validator;

import com.example.tikitaka.domain.member.MemberErrorCode;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.repository.MemberRepository;
import com.example.tikitaka.global.exception.BaseErrorException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberValidator {
    private final MemberRepository memberRepository;

    public Member validateMember(String memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BaseErrorException(MemberErrorCode.MEMBER_NOT_FOUND));

        return member;
    }
}
