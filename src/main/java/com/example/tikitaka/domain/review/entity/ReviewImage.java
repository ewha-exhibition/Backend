package com.example.tikitaka.domain.review.entity;

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
@Table(name = "review_image")
public class ReviewImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_image_id")
    private Long reviewImageId;

    @Column(name = "review_image_url", nullable = false)
    private String reviewImageUrl;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;
}
