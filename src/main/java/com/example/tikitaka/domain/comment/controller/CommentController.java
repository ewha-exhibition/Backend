package com.example.tikitaka.domain.comment.controller;

import com.example.tikitaka.domain.comment.dto.CommentPostRequest;
import com.example.tikitaka.domain.comment.repository.CommentRepository;
import com.example.tikitaka.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
