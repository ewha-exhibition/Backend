package com.example.tikitaka.domain.post.dto;

import com.example.tikitaka.domain.post.entity.Post;
import com.example.tikitaka.global.util.formatting.DateFormatting;
import com.example.tikitaka.global.util.formatting.PostWriterFormatting;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ExhibitionReview {
    private Long postId;
    private PostWriterFormatting writer;
    private DateFormatting createdAt;
    private String content;
    private boolean isWriter;
    private List<String> images;
    private int imageCount;

    public static ExhibitionReview of(Post post, List<String> images) {
        return ExhibitionReview.builder()
                .postId(post.getPostId())
                .writer(new PostWriterFormatting(post.getDisplayNo()))
                .createdAt(new DateFormatting(post.getCreatedAt()))
                .content(post.getContent())
                .isWriter(false) // TODO: User 연동 후 수정
                .images(images)
                .imageCount(images.size())
                .build();
    }

}
