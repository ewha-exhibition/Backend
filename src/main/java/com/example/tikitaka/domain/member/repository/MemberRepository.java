package com.example.tikitaka.domain.member.repository;

import com.example.tikitaka.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
