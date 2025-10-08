package com.example.tikitaka.domain.exhibition.repository;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {
//    @Query("""
//    SELECT DISTINCT e
//    FROM Exhibition e
//    LEFT JOIN FETCH e.images
//    WHERE e.exhibitionIdx = :exhibitionIdx
//    """)
//    Optional<Exhibition> findDetailById(Long exhibitionIdx);

    Optional<Exhibition> findByExhibitionId(Long exhibitionId);
}
