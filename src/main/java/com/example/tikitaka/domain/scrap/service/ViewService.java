package com.example.tikitaka.domain.scrap.service;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.exhibition.service.ExhibitionService;
import com.example.tikitaka.domain.exhibition.validator.ExhibitionValidator;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.validator.MemberValidator;
import com.example.tikitaka.domain.scrap.ViewErrorCode;
import com.example.tikitaka.domain.scrap.entity.View;
import com.example.tikitaka.domain.scrap.repository.ViewRepository;
import com.example.tikitaka.global.exception.BaseErrorException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ViewService {

    private final ViewRepository viewRepository;
    private final MemberValidator memberValidator;
    private final ExhibitionValidator exhibitionValidator;

    public void addView(Long memberId, Long exhibitionId) {
        Member member = memberValidator.validateMember(memberId);
        Exhibition exhibition = exhibitionValidator.validateExhibition(exhibitionId);

        if (viewRepository.existsByMemberAndExhibition(member, exhibition)) {
            throw new BaseErrorException(ViewErrorCode.ALREADE_EXIST_VIEW);
        }

        View view = View.builder()
                    .member(member)
                    .exhibition(exhibition)
                    .build();

        viewRepository.save(view);
    }
}
