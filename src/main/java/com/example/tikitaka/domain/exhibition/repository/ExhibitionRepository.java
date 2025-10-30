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

    // 초대코드로 전시 찾기
    Optional<Exhibition> findByCode(String code);

    Optional<Exhibition> findByExhibitionId(Long exhibitionId);

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
    SELECT e.*, COUNT(s.scrap_id) AS scrap_count
    FROM exhibition e
    LEFT JOIN scrap s ON s.exhibition_id = e.exhibition_id
    WHERE
        (e.start_time < e.end_time
          AND (COALESCE(e.start_date, CURRENT_DATE) <= CURRENT_DATE)
          AND (CURRENT_DATE <= e.end_date)
          AND (CURRENT_TIME >= e.start_time AND CURRENT_TIME < e.end_time)
        )
        OR
        (e.end_time <= e.start_time
          AND (COALESCE(e.start_date, CURRENT_DATE) <= CURRENT_DATE)
          AND (CURRENT_DATE <= e.end_date)
          AND (CURRENT_TIME >= e.start_time)
        )
        OR
        (e.end_time <= e.start_time
          AND (COALESCE(e.start_date, CURRENT_DATE) <= CURRENT_DATE - INTERVAL 1 DAY)
          AND (CURRENT_DATE - INTERVAL 1 DAY <= e.end_date)
          AND (CURRENT_TIME < e.end_time)
        )
    GROUP BY e.exhibition_id
    ORDER BY scrap_count DESC
    LIMIT 10
    """, nativeQuery = true)
    List<Exhibition> findPopularExhibitions();

}
