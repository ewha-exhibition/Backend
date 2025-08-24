package com.example.tikitaka.domain.review.entity;

import com.example.tikitaka.global.entity.Comment;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Builder
@NoArgsConstructor
@Table(name = "review")
public class Review extends Comment {
}
