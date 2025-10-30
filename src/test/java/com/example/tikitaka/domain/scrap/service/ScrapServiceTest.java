package com.example.tikitaka.domain.scrap.service;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.scrap.dto.ScrapListItemDto;
import com.example.tikitaka.domain.scrap.dto.response.ScrapListResponseDto;
import com.example.tikitaka.domain.scrap.entity.Scrap;
import com.example.tikitaka.domain.scrap.repository.ScrapRepository;
import com.example.tikitaka.domain.exhibition.repository.ExhibitionRepository;
import com.example.tikitaka.domain.member.repository.MemberRepository;
import com.example.tikitaka.global.dto.PageInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ScrapServiceTest {

    @Mock ScrapRepository scrapRepository;
    @Mock MemberRepository memberRepository;
    @Mock ExhibitionRepository exhibitionRepository;
    @InjectMocks ScrapService scrapService;

    @Test
    void findScrapList_페이지_매핑_검증() {
        // given
        Long memberId = 1L;
        Member m = Member.builder().memberId(memberId).username("heewon").email("h@e.com").profileUrl("p").build();

        Exhibition e1 = Exhibition.builder().exhibitionId(10L).exhibitionName("전시A").posterUrl("u").place("p").startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(1)).build();
        Exhibition e2 = Exhibition.builder().exhibitionId(20L).exhibitionName("전시B").posterUrl("u").place("p").startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(2)).build();

        Scrap s1 = Scrap.builder().scrapId(100L).member(m).exhibition(e1).isViewed(false).build();
        Scrap s2 = Scrap.builder().scrapId(101L).member(m).exhibition(e2).isViewed(true).build();

        Page<Scrap> page = new PageImpl<>(List.of(s1, s2), PageRequest.of(0, 10), 2);
        when(scrapRepository.findPageByMemberIdOrderByEndDateAndViewed(eq(memberId), any(Pageable.class)))
                .thenReturn(page);

        // when
        ScrapListResponseDto resp = scrapService.findScrapList(memberId, 1, 10);

        // then
        assertThat(resp.getUsername()).isEqualTo("heewon");
        assertThat(resp.getExhibitions()).hasSize(2);
        ScrapListItemDto item0 = resp.getExhibitions().get(0);
        assertThat(item0.getExhibitionId()).isEqualTo(10L);
        assertThat(item0.getExhibitionName()).isEqualTo("전시A");
        assertThat(item0.isViewed()).isFalse();

        PageInfo pi = resp.getPageInfo();
        assertThat(pi.getPageNum()).isEqualTo(1);
        assertThat(pi.getLimit()).isEqualTo(10);
        assertThat(pi.getTotalElements()).isEqualTo(2);
        assertThat(pi.getTotalPages()).isEqualTo(1);
    }

    @Test
    void addScrap_idempotent() {
        // given
        Long memberId = 1L, exId = 10L;
        when(scrapRepository.existsByMember_MemberIdAndExhibition_ExhibitionId(memberId, exId)).thenReturn(true);

        // when
        scrapService.addScrap(memberId, exId);

        // then
        verify(scrapRepository, times(0)).save(any());
    }

    @Test
    void addScrap_정상추가() {
        Long memberId = 1L, exId = 10L;
        when(scrapRepository.existsByMember_MemberIdAndExhibition_ExhibitionId(memberId, exId)).thenReturn(false);
        when(memberRepository.getReferenceById(memberId)).thenReturn(Member.builder().memberId(memberId).build());
        when(exhibitionRepository.getReferenceById(exId)).thenReturn(Exhibition.builder().exhibitionId(exId).build());

        scrapService.addScrap(memberId, exId);

        verify(scrapRepository).save(argThat(s ->
                s.getMember().getMemberId().equals(memberId)
                        && s.getExhibition().getExhibitionId().equals(exId)
                        && Boolean.FALSE.equals(s.getIsViewed())
        ));
    }

    @Test
    void removeScrap_idempotent() {
        Long memberId = 1L, exId = 10L;
        scrapService.removeScrap(memberId, exId);
        verify(scrapRepository).deleteByMember_MemberIdAndExhibition_ExhibitionId(memberId, exId);
    }

    @Test
    void markViewed_스크랩있을때만_허용() {
        Long memberId = 1L, exId = 10L;
        Scrap s = Scrap.builder().scrapId(1L).member(Member.builder().memberId(memberId).build())
                .exhibition(Exhibition.builder().exhibitionId(exId).build()).isViewed(false).build();

        when(scrapRepository.findByMember_MemberIdAndExhibition_ExhibitionId(memberId, exId))
                .thenReturn(Optional.of(s));

        scrapService.markViewed(memberId, exId, true);

        assertThat(s.getIsViewed()).isTrue();
    }

    @Test
    void markViewed_스크랩없으면_예외() {
        Long memberId = 1L, exId = 10L;
        when(scrapRepository.findByMember_MemberIdAndExhibition_ExhibitionId(memberId, exId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> scrapService.markViewed(memberId, exId, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("스크랩하지 않은 전시는 관람표시할 수 없습니다.");
    }
}
