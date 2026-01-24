package com.example.tikitaka.domain.post.dto;

import com.example.tikitaka.domain.post.entity.Post;
import com.example.tikitaka.global.util.formatting.DateFormatting;
import com.example.tikitaka.global.util.formatting.PostWriterFormatting;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ExhibitionReview implements ExhibitionPost {
    private Long postId;
    private PostWriterFormatting writer;
    private DateFormatting createdAt;
    private String content;

    @JsonProperty("isWriter")
    private boolean isWriter;
    private List<String> images;
    private int imageCount;
    @JsonProperty("isDeleted")
    private boolean isDeleted;


    public static ExhibitionReview of(Post post, List<String> images, boolean isWriter) {
        return ExhibitionReview.builder()
                .postId(post.getPostId())
                .writer(new PostWriterFormatting(post.getDisplayNo()))
                .createdAt(new DateFormatting(post.getCreatedAt()))
                .content(post.getContent())
                .isWriter(isWriter)
                .images(images)
                .imageCount(images.size())
                .build();
    }

}
