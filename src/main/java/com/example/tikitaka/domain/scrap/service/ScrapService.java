package com.example.tikitaka.domain.scrap.service;

import com.example.tikitaka.domain.exhibition.ExhibitionErrorCode;
import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.exhibition.repository.ExhibitionRepository;
import com.example.tikitaka.domain.exhibition.validator.ExhibitionValidator;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.repository.MemberRepository;
import com.example.tikitaka.domain.member.validator.MemberValidator;
import com.example.tikitaka.domain.scrap.ScrapErrorCode;
import com.example.tikitaka.domain.scrap.dto.ScrapListItemDto;
import com.example.tikitaka.domain.scrap.dto.response.ScrapListResponseDto;
import com.example.tikitaka.domain.scrap.entity.Scrap;
import com.example.tikitaka.domain.scrap.repository.ScrapRepository;
import com.example.tikitaka.domain.scrap.repository.ViewRepository;
import com.example.tikitaka.global.dto.PageInfo;
import com.example.tikitaka.global.exception.BaseErrorException;
import jakarta.persistence.EntityNotFoundException;
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
    private final ViewRepository viewRepository;
    private final MemberValidator memberValidator;
    private final ExhibitionValidator exhibitionValidator;

    // S3
//    private final S3UrlHandler s3UrlHandler;



    // 1. 스크랩 목록 조회 (페이지네이션)
    public ScrapListResponseDto findScrapList(Long memberId, int pageNum, int limit) {
        Member member = memberValidator.validateMember(memberId);
        Pageable pageable = PageRequest.of(Math.max(pageNum - 1, 0), limit);

        Page<ScrapListItemDto> page = scrapRepository.findPageByMemberIdOrderByEndDateAndViewed(memberId, pageable);

        List<ScrapListItemDto> exhibitions = page.getContent();

        // username은 Scrap → Member를 통해 접근
        String username = member.getUsername();

        PageInfo pageInfo = PageInfo.of(
                pageNum,
                limit,
                page.getTotalPages(),
                page.getTotalElements()
        );

        // username 추가
        return ScrapListResponseDto.from(
                username,
                exhibitions,
                pageInfo
        );
    }


    /**
     * 스크랩 추가 (idempotent)
     * - 이미 스크랩되어 있으면 아무 동작 없이 리턴
     * - 새로 추가되면 isViewed=false 기본 설정
     */
    @Transactional
    public void addScrap(Long memberId, Long exhibitionId) {
        // 이미 존재 -> 400에러
        if (scrapRepository.existsByMember_MemberIdAndExhibition_ExhibitionId(memberId, exhibitionId)) {
            throw new BaseErrorException(ScrapErrorCode.SCRAP_ALREADY_EXIST);
        }

        // 연관 엔티티(프록시) 참조
        Member member = memberRepository.getReferenceById(memberId);
        Exhibition exhibition = exhibitionRepository.findByExhibitionId(exhibitionId).orElseThrow(
                () -> new BaseErrorException(ExhibitionErrorCode.EXHIBITION_NOT_FOUND)
        );



        // (선택) 전시 삭제/만료 정책 체크가 필요하면 여기서 검사
        // if (Boolean.TRUE.equals(exhibition.getIsDeleted())) { throw new IllegalStateException("삭제된 전시입니다."); }

        Scrap scrap = Scrap.builder()
                .member(member)
                .exhibition(exhibition)
                .build();

        scrapRepository.save(scrap);

        // (선택) 집계 필드 업데이트가 필요하면 여기서 처리
        exhibition.increaseScrapCount();
    }

    /**
     * 스크랩 취소 (idempotent)
     * - 존재하지 않아도 조용히 통과
     */
    @Transactional
    public void removeScrap(Long memberId, Long exhibitionId) {
        // 먼저 존재 여부 확인 없이 바로 삭제 호출 가능하지만,
        // DB에 따라 반환 값이 필요하면 exists 체크 or 커스텀 delete 쿼리 사용
        boolean exists = scrapRepository.existsByMember_MemberIdAndExhibition_ExhibitionId(memberId, exhibitionId);
        if (!exists) {
            throw new BaseErrorException(ScrapErrorCode.SCRAP_NOT_FOUND);
        }

        // 실제 스크랩 삭제
        scrapRepository.deleteByMember_MemberIdAndExhibition_ExhibitionId(memberId, exhibitionId);


        // (선택) 집계 필드 감소
         try {
             Exhibition exhibition = exhibitionRepository.findByExhibitionId(exhibitionId).orElseThrow(
                     () -> new BaseErrorException(ExhibitionErrorCode.EXHIBITION_NOT_FOUND)
             );

             exhibition.decreaseScrapCount();
         } catch (EntityNotFoundException ignore) {}
    }
}
