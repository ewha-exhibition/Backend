package com.example.tikitaka.domain.scrap.validator;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.scrap.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScrapValidator {
    private final ScrapRepository scrapRepository;

    public boolean existsByMemberAndExhibition(Member member, Exhibition exhibition) {
        return scrapRepository.existsByMemberAndExhibition(member, exhibition);
    }
}
