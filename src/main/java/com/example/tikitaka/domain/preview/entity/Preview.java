package com.example.tikitaka.domain.preview.entity;

import com.example.tikitaka.global.entity.Comment;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("PREVIEW")
public class Preview extends Comment {
    @Column(name = "is_question", nullable = false)
    private boolean isQuestion;

}
