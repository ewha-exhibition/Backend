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
    public void deletePost(String memberId, Long postId) {
        Post post = postRepository.findByMemberIdAndPostId(Long.parseLong(memberId), postId)
                .orElseThrow(() -> new BaseErrorException(PostErrorCode.POST_FORBIDDEN));

        postImageService.deletePostImages(post);
        post.markAsDeleted();
    }

}
