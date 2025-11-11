package com.example.tikitaka.domain.post.entity;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.post.dto.request.PreviewPostRequest;
import com.example.tikitaka.domain.post.dto.request.ReviewPostRequest;
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
@Table(name = "posts")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type", nullable = false)
    private PostType postType;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "has_answer", nullable = false)
    boolean hasAnswer;

    @Column(name = "display_no", nullable = false)
    private Long displayNo; // '벗0'에서 숫자 의미

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id", nullable = false)
    private Exhibition exhibition;

    public void markAsDeleted() {
        this.isDeleted = true;
    }

    public void switchAsAnswered() {
        this.hasAnswer = !this.hasAnswer;
    }

    public static Post toReviewEntity(Member member, Exhibition exhibition, ReviewPostRequest reviewPostRequest, PostType postType, Long displayNo) {
        return Post.builder()
                .member(member)
                .postType(postType)
                .content(reviewPostRequest.getContent())
                .hasAnswer(false)
                .exhibition(exhibition)
                .displayNo(displayNo)
                .isDeleted(false)
                .build();
    }

    public static Post toPreviewEntity(Member member, Exhibition exhibition, PreviewPostRequest previewPostRequest, PostType postType, Long displayNo) {
        return Post.builder()
                .member(member)
                .postType(postType)
                .content(previewPostRequest.getContent())
                .hasAnswer(false)
                .exhibition(exhibition)
                .displayNo(displayNo)
                .isDeleted(false)
                .build();
    }


}
