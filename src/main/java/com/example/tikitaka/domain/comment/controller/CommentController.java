package com.example.tikitaka.domain.comment.controller;

import com.example.tikitaka.domain.comment.dto.CommentPostRequest;
import com.example.tikitaka.domain.comment.repository.CommentRepository;
import com.example.tikitaka.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/{postId}")
    public void commentAdd(
            @PathVariable
            Long postId,
            CommentPostRequest commentPostRequest) {
        commentService.addComment(postId, commentPostRequest);
    }

    @DeleteMapping("/{commentId}")
    public void commentDelete(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }
}
