package com.example.tikitaka.domain.post.entity;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//    추후 User 수정 후 주석 해제 예정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id", nullable = false)
    private Exhibition exhibition;

    public static Post toEntity(Exhibition exhibition, ReviewPostRequest reviewPostRequest, PostType postType, Long displayNo) {
        return Post.builder()
                .postType(postType)
                .content(reviewPostRequest.getContent())
                .hasAnswer(false)
                .exhibition(exhibition)
                .displayNo(displayNo)
                .build();
    }


}
