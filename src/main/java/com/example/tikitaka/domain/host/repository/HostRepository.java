package com.example.tikitaka.domain.host.repository;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.host.entity.Host;
import com.example.tikitaka.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HostRepository extends JpaRepository<Host, Long> {

    boolean existsByMember_MemberIdAndExhibition_ExhibitionId(Long memberId, Long exhibitionId);
    long countByExhibition_ExhibitionId(Long exhibitionId);
    List<Host> findByExhibition_ExhibitionId(Long exhibitionId);
}
