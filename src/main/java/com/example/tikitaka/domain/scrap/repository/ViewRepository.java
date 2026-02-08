package com.example.tikitaka.domain.scrap.repository;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.scrap.entity.View;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ViewRepository extends JpaRepository<View, Long> {
    boolean existsByMemberAndExhibition(Member member, Exhibition exhibition);

    Optional<View> findByMemberAndExhibition(Member member, Exhibition exhibition);
}
