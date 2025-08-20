package com.example.tikitaka.domain.answer.entity;

import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.global.entity.BaseEntity;
import com.example.tikitaka.global.entity.Comment;
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
@Table(name = "answer")
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long answerId;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
