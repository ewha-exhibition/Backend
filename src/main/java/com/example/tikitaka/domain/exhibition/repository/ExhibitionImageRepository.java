package com.example.tikitaka.domain.exhibition.repository;

import com.example.tikitaka.domain.exhibition.entity.ExhibitionImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExhibitionImageRepository extends JpaRepository<ExhibitionImage, Long> {
    @Query("""
            SELECT ei.imageUrl
            FROM ExhibitionImage ei
            WHERE ei.exhibition.exhibitionIdx = :exhibitionIdx
            ORDER BY ei.sequence ASC
            """)
    List<String> findByExhibitionIdxOrderBySequenceAsc(Long exhibitionIdx);
}
