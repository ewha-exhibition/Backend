package com.example.tikitaka.domain.scrap.repository;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.post.entity.PostType;
import com.example.tikitaka.domain.scrap.dto.ViewsDto;
import com.example.tikitaka.domain.scrap.entity.View;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ViewRepository extends JpaRepository<View, Long> {
    boolean existsByMemberAndExhibition(Member member, Exhibition exhibition);

    Optional<View> findByMemberAndExhibition(Member member, Exhibition exhibition);

    @Query(
            value = """
    select new com.example.tikitaka.domain.scrap.dto.ViewsDto(
        e.exhibitionId,
        e.exhibitionName,
        e.posterUrl,
        e.place,
        e.startDate,
        e.endDate,
        case when exists (
            select 1
            from Post p
            where p.member = v.member
              and p.exhibition = e
              and p.postType = :postType
        ) then true else false end
    )
    from View v
    join v.exhibition e
    where v.member.memberId = :memberId
      and (e.isDeleted = false or e.isDeleted is null)
    group by
        e.exhibitionId, e.exhibitionName, e.posterUrl, e.place, e.startDate, e.endDate,
        v.member
    order by max(v.createdAt) desc
  """,
            countQuery = """
    select count(v)
    from View v
    join v.exhibition e
    where v.member.memberId = :memberId
      and (e.isDeleted = false or e.isDeleted is null)
    """
    )
    Page<ViewsDto> findPageByMemberId(@Param("memberId") Long memberId, @Param("postType") PostType postType, Pageable pageable);

}
