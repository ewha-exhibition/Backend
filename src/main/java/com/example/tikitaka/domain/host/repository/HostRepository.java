package com.example.tikitaka.domain.host.repository;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.host.entity.Host;
import com.example.tikitaka.global.config.auth.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HostRepository extends JpaRepository<Host, Long> {
    boolean existsByMemberAndExhibition(User user, Exhibition exhibition);
}
