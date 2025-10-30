package com.example.tikitaka.domain.post.service;

import com.example.tikitaka.domain.comment.validator.CommentValidator;
import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.exhibition.validator.ExhibitionValidator;
import com.example.tikitaka.domain.post.dto.ExhibitionPost;
import com.example.tikitaka.domain.post.dto.ExhibitionPreview;
import com.example.tikitaka.domain.post.dto.PostCard;
import com.example.tikitaka.domain.post.dto.request.PreviewPostRequest;
import com.example.tikitaka.domain.post.dto.response.ExhibitionPostListResponse;
import com.example.tikitaka.domain.post.dto.response.GuestBookResponse;
import com.example.tikitaka.domain.post.entity.Post;
import com.example.tikitaka.domain.post.entity.PostType;
import com.example.tikitaka.domain.post.repository.PostRepository;
import com.example.tikitaka.global.dto.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PreviewService {
    private final ExhibitionValidator exhibitionValidator;
    private final CommentValidator commentValidator;
    private final PostRepository postRepository;

    @Transactional
    public void addPreview(Long exhibitionId, PreviewPostRequest previewPostRequest, PostType postType) {
        Exhibition exhibition = exhibitionValidator.validateExhibition(exhibitionId);

        // 추후 유저가 이미 post를 생성한 경우를 고려하기 위한 변수
        boolean flag = true;

        Long number = 0L;
        if (flag & postType == PostType.QUESTION) {
            number = exhibition.getQuestionNo() + 1;
            exhibition.increaseQuestionNo();
            exhibition.increaseQuestionCount();
        }
        else if (flag & postType == PostType.CHEER) {
            number = exhibition.getCheerNo() + 1;
            exhibition.increaseCheerNo();
            exhibition.increaseCheerCount();
        }


        Post cheer = Post.toPreviewEntity(exhibition, previewPostRequest, postType, number);
        postRepository.save(cheer);

    }

    public ExhibitionPostListResponse getExhibitionPreviews(
            Long exhibitionId,
            PostType postType,
            int pageNum,
            int limit
    ) {
        Exhibition exhibition = exhibitionValidator.validateExhibition(exhibitionId);

        PageRequest pageRequest = PageRequest.of(pageNum, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> previews = postRepository.findByExhibitionAndPostType(exhibition, postType, pageRequest);
        PageInfo pageInfo = PageInfo.of(pageNum, limit, previews.getTotalPages(), previews.getTotalElements());

        List<ExhibitionPost> exhibitionCheers = previews.getContent().stream().map(
                preview -> (preview.isHasAnswer())?(ExhibitionPost) ExhibitionPreview.of(preview, commentValidator.validateCommentContent(preview)): (ExhibitionPost) ExhibitionPreview.of(preview, null)
        ).toList();

        return ExhibitionPostListResponse.of(exhibitionCheers, pageInfo);
    }

    public GuestBookResponse getGuestbooks (
            int pageNum,
            int limit
    ) {
        PageRequest pageRequest = PageRequest.of(pageNum, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> reviews = postRepository.findReviewByPostType(PostType.CHEER, pageRequest);
        PageInfo pageInfo = PageInfo.of(pageNum, limit, reviews.getTotalPages(), reviews.getTotalElements());

        List<PostCard> postCards = reviews.getContent().stream().map(
                review -> PostCard.of(review, null)
        ).toList();

        return GuestBookResponse.of(postCards, pageInfo);
    }
}

