package com.example.tikitaka.domain.scrap.entity;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.global.config.auth.user.User;
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

    @ManyToOne
    @JoinColumn(name = "member_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "exhibition_id")
    private Exhibition exhibition;

    @Column(name = "is_viewed", nullable = false)
    private Boolean isViewed;
}
