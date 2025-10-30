package com.example.tikitaka.domain.scrap.repository;

import com.example.tikitaka.domain.exhibition.entity.Category;
import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.exhibition.repository.ExhibitionRepository;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.entity.RegisterPath;
import com.example.tikitaka.domain.member.repository.MemberRepository;
import com.example.tikitaka.domain.scrap.entity.Scrap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test") //  test 프로필 강제
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ScrapRepositoryTest {

    @Autowired ScrapRepository scrapRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ExhibitionRepository exhibitionRepository;

    private Member saveMember(String name) {
        Member m = Member.builder()
                .email(name+"@ex.com")
                .username(name)
                .profileUrl("p")
                .path(RegisterPath.SELF)
                .status(com.example.tikitaka.domain.member.entity.Status.ACTIVE)
                .build();
        return memberRepository.save(m);
    }

    private Exhibition saveEx(String name, LocalDate start, LocalDate end, boolean deleted) {
        Exhibition e = Exhibition.builder()
                .exhibitionName(name)
                .posterUrl("u")
                .place("hall")
                .startDate(start)
                .endDate(end)
                .startTime(LocalTime.of(10,0))
                .endTime(LocalTime.of(18,0))
                .price(0)
                .link("l")
                .content("c")
                .code(name+"_code")
                .category(Category.ETC)
                .status(com.example.tikitaka.domain.exhibition.entity.Status.ACTIVE)
                .scrapCount(0)
                .reviewCount(0)
                .cheeringCount(0)
                .questionCount(0)
                .viewCount(0)
                .isDeleted(deleted)
                .build();
        return exhibitionRepository.save(e);
    }

    private Scrap saveScrap(Member m, Exhibition e, boolean viewed, boolean reviewed) {
        Scrap s = Scrap.builder()
                .member(m)
                .exhibition(e)
                .isViewed(viewed)
                .isReviewed(reviewed)
                .build();
        return scrapRepository.save(s);
    }

    @Test
    void 페이지네이션_정렬_카운트_검증() {
        // given
        Member m1 = saveMember("heewon");
        // endDate: 10/05(true), 10/10(false), 10/20(false), 10/01(true - but deleted)
        Exhibition e1 = saveEx("반고흐", LocalDate.of(2025,10,1),  LocalDate.of(2025,10,5),  false);
        Exhibition e2 = saveEx("모네",   LocalDate.of(2025,10,2),  LocalDate.of(2025,10,10), false);
        Exhibition e3 = saveEx("피카소", LocalDate.of(2025,10,10), LocalDate.of(2025,10,20), false);
        Exhibition e4 = saveEx("고야",   LocalDate.of(2025,9,29),  LocalDate.of(2025,10,1),  true); // 삭제됨

        saveScrap(m1, e1, true, false);
        saveScrap(m1, e2, false, false);
        saveScrap(m1, e3, false, false);
        saveScrap(m1, e4, false, false); // 삭제 전시와의 스크랩(필터링 대상)

        // when: pageSize=2, page0
        Pageable pageable = PageRequest.of(0, 2); // 기본 정렬은 레포 JPQL order by
        Page<Scrap> page = scrapRepository.findPageByMemberIdOrderByEndDateAndViewed(m1.getMemberId(), pageable);

        // then
        // totalElements: 삭제된 전시 제외 → 3개
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2); // 3 / 2 = 1.5 → 2 페이지

        // 정렬: endDate ASC, isViewed ASC(false 우선)
        // endDate: 10/05(반고흐, viewed=true), 10/10(모네, false), 10/20(피카소, false)
        // 같은 endDate일 때 isViewed=false 먼저지만 여기선 날짜가 모두 다름
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent().get(0).getExhibition().getExhibitionName()).isEqualTo("반고흐");
        assertThat(page.getContent().get(1).getExhibition().getExhibitionName()).isEqualTo("모네");

        // when: page1
        Page<Scrap> page1 = scrapRepository.findPageByMemberIdOrderByEndDateAndViewed(m1.getMemberId(), PageRequest.of(1,2));
        assertThat(page1.getContent()).hasSize(1);
        assertThat(page1.getContent().get(0).getExhibition().getExhibitionName()).isEqualTo("피카소");
    }
}
