package com.example.tikitaka.domain.scrap.validator;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.validator.MemberValidator;
import com.example.tikitaka.domain.scrap.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScrapValidator {
    private final ScrapRepository scrapRepository;
    private final MemberValidator memberValidator;

    public boolean existsByMemberAndExhibition(Member member, Exhibition exhibition) {
        return scrapRepository.existsByMemberAndExhibition(member, exhibition);
    }

    public boolean existsByMemberIdAndExhibition(Long memberId, Exhibition exhibition) {
        if (memberId == null) {
            return false;
        } else {
            Member member = memberValidator.validateMember(memberId);
            return scrapRepository.existsByMemberAndExhibition(member, exhibition);
        }
    }
}
