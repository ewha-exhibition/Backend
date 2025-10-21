package com.example.tikitaka.domain.scrap.service;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.exhibition.repository.ExhibitionRepository;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.repository.MemberRepository;
import com.example.tikitaka.domain.scrap.dto.ScrapListItemDto;
import com.example.tikitaka.domain.scrap.dto.response.ScrapListResponseDto;
import com.example.tikitaka.domain.scrap.entity.Scrap;
import com.example.tikitaka.domain.scrap.repository.ScrapRepository;
import com.example.tikitaka.global.dto.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // service 빈 등록
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScrapService {
    private final ScrapRepository scrapRepository;
    private final MemberRepository memberRepository;
    private final ExhibitionRepository exhibitionRepository;

    // S3
//    private final S3UrlHandler s3UrlHandler;

    // 1. 스크랩 목록 조회 (페이지네이션)
    public ScrapListResponseDto findScrapList(Long memberId, int pageNum, int limit) {
        Pageable pageable = PageRequest.of(Math.max(pageNum - 1, 0), limit);

        Page<Scrap> page = scrapRepository.findPageByMemberIdOrderByEndDateAndViewed(memberId, pageable);

        // Exhibition DTO 변환
        List<ScrapListItemDto> exhibitions = page.getContent().stream()
                .map(s -> ScrapListItemDto.builder()
                        .exhibitionId(s.getExhibition().getExhibitionId())
                        .exhibitionName(s.getExhibition().getExhibitionName())
                        .posterUrl(s.getExhibition().getPosterUrl())
                        .place(s.getExhibition().getPlace())
                        .startDate(s.getExhibition().getStartDate())
                        .endDate(s.getExhibition().getEndDate())
                        .isViewed(s.getIsViewed())
                        .build())
                .toList();

        // username은 Scrap → Member를 통해 접근
        String username = page.isEmpty() ? null : page.getContent().get(0).getMember().getUsername();

        PageInfo pageInfo = PageInfo.of(
                pageNum,
                limit,
                page.getTotalPages(),
                page.getTotalElements()
        );

        // username 추가
        return ScrapListResponseDto.builder()
                .username(username)
                .exhibitions(exhibitions)
                .pageInfo(pageInfo)
                .build();
    }



}
