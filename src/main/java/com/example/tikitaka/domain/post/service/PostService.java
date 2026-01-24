package com.example.tikitaka.domain.post.service;

import com.example.tikitaka.domain.post.PostErrorCode;
import com.example.tikitaka.domain.post.entity.Post;
import com.example.tikitaka.domain.post.entity.PostType;
import com.example.tikitaka.domain.post.repository.PostRepository;
import com.example.tikitaka.domain.scrap.service.ScrapService;
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
    private final ScrapService scrapService;

    @Transactional
    public void deletePost(Long memberId, Long postId) {
        Post post = postRepository.findByMemberIdAndPostId(memberId, postId)
                .orElseThrow(() -> new BaseErrorException(PostErrorCode.POST_FORBIDDEN));

        postImageService.deletePostImages(post);
        post.markAsDeleted();

        if (post.getPostType() == PostType.REVIEW) {
            scrapService.markReviewed(memberId, post.getExhibition().getExhibitionId()); // TODO: N+1 문제 해결
            post.getExhibition().decreaseReviewCount();
        }
        if (post.getPostType() == PostType.CHEER) {
            post.getExhibition().decreaseCheerCount();
        }
        if (post.getPostType() == PostType.QUESTION) {
            post.getExhibition().decreaseQuestionCount();
        }
    }

    @Transactional
    public void switchHasAnswer(Post post) {
        post.switchAsAnswered();
    }

}
