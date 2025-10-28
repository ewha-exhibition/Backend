package com.example.tikitaka.domain.host.repository;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.host.entity.Host;
import com.example.tikitaka.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HostRepository extends JpaRepository<Host, Long> {
    boolean existsByMemberAndExhibition(Member member, Exhibition exhibition);
    Optional<Host> findByMemberAndExhibition(Member member, Exhibition exhibition);
}
