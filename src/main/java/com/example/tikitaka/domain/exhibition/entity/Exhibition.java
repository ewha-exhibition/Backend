package com.example.tikitaka.domain.exhibition.entity;

import com.example.tikitaka.domain.club.entity.Club;
import com.example.tikitaka.global.entity.BaseEntity;
import io.micrometer.core.annotation.Counted;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exhibition")
public class Exhibition extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exhibition_id")
    private Long exhibitionId;

    @Column(name = "exhibition_name", nullable = false)
    private String exhibitionName;

    @Column(name = "poster_url", nullable = false)
    private String posterUrl;

    @Column(name = "place", nullable = false)
    private String place;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = true)
    private LocalDate endDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "date_exception", nullable = true)
    private String dateException;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "link", nullable = false)
    private String link;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "scrap_count", nullable = false)
    private int scrapCount;

    @Column(name = "review_count", nullable = false)
    private int reviewCount;

    @Column(name = "comment_count", nullable = false)
    private int commentCount;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

}
