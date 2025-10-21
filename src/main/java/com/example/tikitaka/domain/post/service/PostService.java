package com.example.tikitaka.domain.post.service;

import com.example.tikitaka.domain.post.PostErrorCode;
import com.example.tikitaka.domain.post.entity.Post;
import com.example.tikitaka.domain.post.repository.PostRepository;
import com.example.tikitaka.global.exception.BaseErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostImageService postImageService;

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseErrorException(PostErrorCode.POST_NOT_FOUND));

        postImageService.deletePostImages(post);
        post.markAsDeleted();
    }

}
