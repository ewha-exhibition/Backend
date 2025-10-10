package com.example.tikitaka.domain.member.repository;

import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.entity.RegisterPath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndPath(String email, RegisterPath path);
}
