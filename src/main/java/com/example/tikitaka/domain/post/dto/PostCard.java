package com.example.tikitaka.domain.post.dto;

import com.example.tikitaka.domain.post.entity.Post;
import com.example.tikitaka.domain.post.entity.PostImage;
import com.example.tikitaka.domain.post.entity.PostType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostCard {
    private Long postId;
    private String posterUrl;
    private String title;
    private String body;
    private int imageCount;
    private Long exhibitionId;
    private List<String> imageUrls;

    public static PostCard of(Post post, List<String> imageUrls) {
        return new PostCard(
                post.getPostId(),
                post.getExhibition().getPosterUrl(),
                post.getExhibition().getExhibitionName(),
                post.getContent(),
                imageUrls == null?0:imageUrls.size(),
                post.getExhibition().getExhibitionId(),
                imageUrls
        );
    }
}
