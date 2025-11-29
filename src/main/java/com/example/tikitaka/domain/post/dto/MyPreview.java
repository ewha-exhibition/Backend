package com.example.tikitaka.domain.post.dto;

import com.example.tikitaka.domain.post.entity.Post;
import com.example.tikitaka.global.util.formatting.DateFormatting;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPreview {
    private Long exhibitionId;
    private String exhibitionName;
    private String posterUrl;
    private Long postId;
    private DateFormatting createdAt;
    private String content;

    public static MyPreview of(Post post) {
        return MyPreview.builder()
                .exhibitionId(post.getExhibition().getExhibitionId())
                .exhibitionName(post.getExhibition().getExhibitionName())
                .posterUrl(post.getExhibition().getPosterUrl())
                .postId(post.getPostId())
                .createdAt(new DateFormatting(post.getCreatedAt()))
                .content(post.getContent())
                .build();
    }
}
