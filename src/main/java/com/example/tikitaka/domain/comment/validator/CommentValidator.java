package com.example.tikitaka.domain.comment.validator;

import com.example.tikitaka.domain.comment.CommentErrorCode;
import com.example.tikitaka.domain.comment.entity.Comment;
import com.example.tikitaka.domain.comment.repository.CommentRepository;
import com.example.tikitaka.domain.post.entity.Post;
import com.example.tikitaka.global.exception.BaseErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentValidator {
    private final CommentRepository commentRepository;

    public String validateCommentContent(Post post) {
        return commentRepository.findContentByPost(post).orElseThrow(
                () -> new BaseErrorException(CommentErrorCode.COMMENT_NOT_FOUND)
        );
    }

    public Comment validateCommentByMemberIdAndCommentId(String memberId, Long commentId) {
        return commentRepository.findByMemberIdAndCommentId(Long.parseLong(memberId), commentId).orElseThrow(
                () -> new BaseErrorException(CommentErrorCode.COMMENT_FORBIDDEN)
        );
    }
}
