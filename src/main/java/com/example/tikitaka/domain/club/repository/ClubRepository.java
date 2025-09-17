package com.example.tikitaka.domain.club.repository;

import com.example.tikitaka.domain.club.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findByName(String name);
    Boolean existByName(String name);

    @Query("SELECT c.clubId FROM Club c WHERE c.name = :name")
    Optional<String> findClubIdByName(String name);
}
