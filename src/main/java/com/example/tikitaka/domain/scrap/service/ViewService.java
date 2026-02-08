package com.example.tikitaka.domain.scrap.service;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.exhibition.validator.ExhibitionValidator;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.validator.MemberValidator;
import com.example.tikitaka.domain.post.entity.PostType;
import com.example.tikitaka.domain.scrap.ViewErrorCode;
import com.example.tikitaka.domain.scrap.dto.ViewsDto;
import com.example.tikitaka.domain.scrap.dto.response.ViewsResponseDto;
import com.example.tikitaka.domain.scrap.entity.View;
import com.example.tikitaka.domain.scrap.repository.ViewRepository;
import com.example.tikitaka.global.dto.PageInfo;
import com.example.tikitaka.global.exception.BaseErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ViewService {

    private final ViewRepository viewRepository;
    private final MemberValidator memberValidator;
    private final ExhibitionValidator exhibitionValidator;

    @Transactional
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

    @Transactional
    public void addViewByReview(Member member, Exhibition exhibition) {
        if (viewRepository.existsByMemberAndExhibition(member, exhibition)) {
            throw new BaseErrorException(ViewErrorCode.ALREADE_EXIST_VIEW);
        }

        View view = View.builder()
                .member(member)
                .exhibition(exhibition)
                .build();

        viewRepository.save(view);
    }

    @Transactional
    public void removeView(Long memberId, Long exhibitionId) {
        Member member = memberValidator.validateMember(memberId);
        Exhibition exhibition = exhibitionValidator.validateExhibition(exhibitionId);

        View view = viewRepository.findByMemberAndExhibition(member, exhibition)
                .orElseThrow(() -> new BaseErrorException(ViewErrorCode.NOT_FOUND_VIEW));

        viewRepository.delete(view);
    }

    public ViewsResponseDto findViewList(Long memberId, int pageNum, int limit) {
        Member member = memberValidator.validateMember(memberId);
        Pageable pageable = PageRequest.of(Math.max(pageNum - 1, 0), limit);

        Page<ViewsDto> page = viewRepository.findPageByMemberId(memberId, PostType.REVIEW, pageable);

        List<ViewsDto> exhibitions = page.getContent();

        // username은 Scrap → Member를 통해 접근
        String username = member.getUsername();

        PageInfo pageInfo = PageInfo.of(
                pageNum,
                limit,
                page.getTotalPages(),
                page.getTotalElements()
        );

        // username 추가
        return ViewsResponseDto.from(
                username,
                exhibitions,
                pageInfo
        );
    }
}
