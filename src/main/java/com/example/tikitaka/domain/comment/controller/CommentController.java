package com.example.tikitaka.domain.comment.controller;

import com.example.tikitaka.domain.comment.dto.CommentPostRequest;
import com.example.tikitaka.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/{postId}")
    public void commentAdd(
            @AuthenticationPrincipal
            String memberId,
            @PathVariable
            Long postId,
            @RequestBody
            CommentPostRequest commentPostRequest) {
        commentService.addComment(memberId, postId, commentPostRequest);
    }

    @DeleteMapping("/{commentId}")
    public void commentDelete(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }
}
