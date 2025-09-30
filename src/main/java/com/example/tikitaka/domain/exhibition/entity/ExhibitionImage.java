package com.example.tikitaka.domain.exhibition.entity;

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
@Table(name = "exhibition_image")
public class ExhibitionImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exhibition_image_idx")
    private Long exhibitionImageIdx;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "sequence", nullable = false)
    private int sequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition")
    private Exhibition exhibition;

//    void setExhibition(Exhibition exhibition) {
//        this.exhibition = exhibition;
//    }
}
