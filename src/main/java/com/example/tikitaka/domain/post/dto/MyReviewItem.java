package com.example.tikitaka.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyReviewItem {
    private Long postId;
    private String content;
    private boolean isMine;

    private Long exhibitionId;
    private String exhibitionName;
    private String posterUrl;

    private List<String> imageUrls;
}
