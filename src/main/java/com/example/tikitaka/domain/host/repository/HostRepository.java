package com.example.tikitaka.domain.host.repository;

import com.example.tikitaka.domain.host.entity.Host;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HostRepository extends JpaRepository<Host, Long> {
}
