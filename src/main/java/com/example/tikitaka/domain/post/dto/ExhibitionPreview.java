package com.example.tikitaka.domain.post.dto;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.post.entity.Post;
import com.example.tikitaka.global.util.formatting.DateFormatting;
import com.example.tikitaka.global.util.formatting.PostWriterFormatting;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExhibitionPreview implements ExhibitionPost{
    private Long postId;
    private PostWriterFormatting writer;
    private DateFormatting createdAt;
    private String content;
    private boolean isWriter;
    private boolean hasAnswer;
    private String answer;

    public static ExhibitionPreview of(Post post, String answer, boolean isWriter) {
        return ExhibitionPreview.builder()
                .postId(post.getPostId())
                .writer(new PostWriterFormatting(post.getDisplayNo()))
                .createdAt(new DateFormatting(post.getCreatedAt()))
                .content(post.getContent())
                .isWriter(isWriter)
                .hasAnswer(post.isHasAnswer())
                .answer(answer)
                .build();

    }
}
