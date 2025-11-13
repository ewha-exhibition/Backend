package com.example.tikitaka.domain.exhibition.repository;

import com.example.tikitaka.domain.exhibition.entity.Category;
import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Override
    @Query("""
    SELECT e
    FROM Exhibition e
    WHERE e.isDeleted = false
    """)
    Page<Exhibition> findAll(Pageable pageable);

    // 초대코드로 전시 찾기
    @Query("""
    SELECT e
    FROM Exhibition e
    WHERE e.code = :code AND e.isDeleted = false
    """)
    Optional<Exhibition> findByCode(String code);

    @Query("""
    SELECT e
    FROM Exhibition e
    WHERE e.exhibitionId = :exhibitionId AND e.isDeleted = false
    """)
    Optional<Exhibition> findByExhibitionId(Long exhibitionId);

    @Query("""
    SELECT e
    FROM Exhibition e
    WHERE e.category = :category AND e.isDeleted = false
    """)
    Page<Exhibition> findByCategory(Category category, Pageable pageable);

    @Query("""
    SELECT e
    FROM Exhibition e
    WHERE e.exhibitionName LIKE CONCAT('%', :keyword, '%')
       OR e.content LIKE CONCAT('%', :keyword, '%')
       OR e.club.name LIKE CONCAT('%', :keyword, '%')
    """)
    Page<Exhibition> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = """
    SELECT
        e.*,
        COUNT(s.scrap_id) AS today_scrap_count
    FROM exhibition e
    LEFT JOIN scrap s
        ON s.exhibition_id = e.exhibition_id
        AND DATE(s.created_at) = CURRENT_DATE
    WHERE
        e.end_date >= CURRENT_DATE AND e.is_deleted = false
    GROUP BY
        e.exhibition_id
    ORDER BY
        today_scrap_count DESC,
        e.end_date ASC
    LIMIT 10
    """, nativeQuery = true)
    List<Exhibition> findPopularExhibitions();

}
