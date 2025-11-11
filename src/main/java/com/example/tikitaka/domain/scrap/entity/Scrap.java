package com.example.tikitaka.domain.scrap.entity;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scrap")
public class Scrap extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scrap_id")
    private Long scrapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id")
    private Exhibition exhibition;

    @Column(name = "is_viewed", nullable = false)
    private Boolean isViewed;

    @Column(name = "is_reviewed", nullable = false)
    private Boolean isReviewed;

    // JPA의 변경 감지(dirty checking) 덕분에 가능한 구조 (그냥 여기서 처리하는 게 더 단순)
    public void setViewed(boolean viewed) { this.isViewed = viewed; }

    public void updateReview() { this.isReviewed = !this.isReviewed; }

}
