package com.example.tikitaka.domain.exhibition.entity;

import com.example.tikitaka.domain.club.entity.Club;
import com.example.tikitaka.global.entity.BaseEntity;
import io.micrometer.core.annotation.Counted;
import jakarta.persistence.*;
import lombok.*;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
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

    @Column(name = "start_date", nullable = true)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
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

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "scrap_count", nullable = false)
    private int scrapCount;

    @Column(name = "review_count", nullable = false)
    private int reviewCount;

    @Column(name = "cheering_count", nullable = false)
    private int cheeringCount;

    @Column(name = "question_count", nullable = false)
    private int questionCount;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "review_no", nullable = false)
    private Long reviewNo;

    @Column(name = "cheer_no", nullable = false)
    private Long cheerNo;

    @Column(name = "question_no", nullable = false)
    private Long questionNo;




//    @OneToMany(mappedBy = "exhibition", cascade = CascadeType.ALL, orphanRemoval = true)
//    @OrderBy("sequence ASC")
//    private List<ExhibitionImage> images = new ArrayList<>();
//
//    public void addImage(ExhibitionImage image) {
//        if (image == null) return;
//        if (!this.images.contains(image)) {
//            this.images.add(image);
//        }
//
//        image.setExhibition(this);
//    }
//
//    public void removeImage(ExhibitionImage image) {
//        if (image == null) return;
//        this.images.remove(image);
//        image.setExhibition(null);
//    }

    public void markAsDeleted() {
        this.isDeleted = true;
    }

    public boolean isEnded() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime exhibitionEndMoment = crossesMidnight()
                ? LocalDateTime.of(endDate.plusDays(1), endTime)
                : LocalDateTime.of(endDate, endTime);

        return !now.isBefore(exhibitionEndMoment);
    }

    private boolean crossesMidnight() {
        return endTime.isBefore(startTime);
    }

    public void increaseReviewNo() {
        this.reviewNo++;
    }

    public void increaseCheerNo() {
        this.cheerNo++;
    }

    public void increaseQuestionNo() {
        this.questionNo++;
    }

}
