package com.example.tikitaka.domain.host.repository;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.host.entity.Host;
import com.example.tikitaka.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.util.Optional;

import java.util.Optional;

import java.util.List;

@Repository
public interface HostRepository extends JpaRepository<Host, Long> {


    // 내가 호스트인 전시들 페이징 조회 (member.memberId 기준)
    @EntityGraph(attributePaths = "exhibition")   // N+1 방지: exhibition 같이 로딩
    Page<Host> findPageByMember_MemberId(Long memberId, Pageable pageable);
    boolean existsByMemberAndExhibition(Member member, Exhibition exhibition);
    Optional<Host> findByMemberAndExhibition(Member member, Exhibition exhibition);

    boolean existsByMember_MemberIdAndExhibition_ExhibitionId(Long memberId, Long exhibitionId);
    long countByExhibition_ExhibitionId(Long exhibitionId);
    List<Host> findByExhibition_ExhibitionId(Long exhibitionId);

}
