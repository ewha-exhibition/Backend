package com.example.tikitaka.domain.scrap.repository;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.scrap.dto.ScrapListItemDto;
import com.example.tikitaka.domain.scrap.entity.Scrap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    boolean existsByMemberAndExhibition(Member member, Exhibition exhibition);

    // /scraps?pageNum=1&limit=3
    //→ “1페이지(첫 3개)만 주세요”
    @Query(
            value = """
        select s
        from Scrap s
        join fetch s.exhibition e
        join fetch s.member m
        where s.member.memberId = :memberId
          and (e.isDeleted = false or e.isDeleted is null)
        order by e.endDate asc, s.isViewed asc
    """,
            countQuery = """
        select count(s)
        from Scrap s
        join s.exhibition e
        where s.member.memberId = :memberId
          and (e.isDeleted = false or e.isDeleted is null)
    """
    )
    Page<Scrap> findPageByMemberIdOrderByEndDateAndViewed(@Param("memberId") Long memberId, Pageable pageable);

    boolean existsByMember_MemberIdAndExhibition_ExhibitionId(Long memberId, Long exhibitionId);

    Optional<Scrap> findByMember_MemberIdAndExhibition_ExhibitionId(Long memberId, Long exhibitionId);

    void deleteByMember_MemberIdAndExhibition_ExhibitionId(Long memberId, Long exhibitionId);

    Page<Scrap> findPageByMember_MemberIdAndIsViewed(Long memberId, boolean isViewed, Pageable pageable);

}
