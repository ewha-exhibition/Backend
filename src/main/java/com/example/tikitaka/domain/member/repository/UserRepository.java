package com.example.tikitaka.domain.member.repository;

import com.example.tikitaka.global.config.auth.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(Long userId);
}
