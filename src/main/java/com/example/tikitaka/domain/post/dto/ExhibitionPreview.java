package com.example.tikitaka.domain.post.dto;


import com.example.tikitaka.domain.comment.entity.Comment;
import com.example.tikitaka.domain.post.entity.Post;
import com.example.tikitaka.global.util.formatting.DateFormatting;
import com.example.tikitaka.global.util.formatting.PostWriterFormatting;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExhibitionPreview implements ExhibitionPost{
    private Long postId;
    private PostWriterFormatting writer;
    private DateFormatting createdAt;
    private String content;

    @JsonProperty("isWriter")
    private boolean isWriter;
    private boolean hasAnswer;
    private String answer;
    private DateFormatting answerCreatedAt;
    private Long answerId;
    @JsonProperty("isDeleted")
    private boolean isDeleted;

    public static ExhibitionPreview of(Post post, Comment comment, boolean isWriter) {
        if (comment == null) {
            return ExhibitionPreview.builder()
                    .postId(post.getPostId())
                    .writer(new PostWriterFormatting(post.getDisplayNo()))
                    .createdAt(new DateFormatting(post.getCreatedAt()))
                    .content(post.getContent())
                    .isWriter(isWriter)
                    .hasAnswer(post.isHasAnswer())
                    .answer(null)
                    .answerCreatedAt(null)
                    .answerId(null)
                    .isDeleted(post.isDeleted())
                    .build();
        }
        return ExhibitionPreview.builder()
                .postId(post.getPostId())
                .writer(new PostWriterFormatting(post.getDisplayNo()))
                .createdAt(new DateFormatting(post.getCreatedAt()))
                .content(post.getContent())
                .isWriter(isWriter)
                .hasAnswer(post.isHasAnswer())
                .answer(comment.getContent())
                .answerId(comment.getCommentId())
                .answerCreatedAt(new DateFormatting(comment.getCreatedAt()))
                .isDeleted(post.isDeleted())
                .build();

    }
}
