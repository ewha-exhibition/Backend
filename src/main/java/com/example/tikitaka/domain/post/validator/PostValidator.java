package com.example.tikitaka.domain.post.validator;

import com.example.tikitaka.domain.post.PostErrorCode;
import com.example.tikitaka.domain.post.entity.Post;
import com.example.tikitaka.domain.post.repository.PostRepository;
import com.example.tikitaka.global.exception.BaseErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostValidator {
    private final PostRepository postRepository;
    public Post validatePostByPostId(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new BaseErrorException(PostErrorCode.POST_FORBIDDEN)
        );
    }
}
