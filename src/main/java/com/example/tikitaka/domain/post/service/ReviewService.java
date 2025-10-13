package com.example.tikitaka.domain.post.service;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.exhibition.validator.ExhibitionValidator;
import com.example.tikitaka.domain.post.dto.ExhibitionReview;
import com.example.tikitaka.domain.post.dto.response.ExhibitionReviewListResponse;
import com.example.tikitaka.domain.post.entity.Post;
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
public class ReviewService {

    private final ExhibitionValidator exhibitionValidator;
    private final PostRepository postRepository;
    private final ReviewImageService reviewImageService;

    public ExhibitionReviewListResponse getExhibitionReviews(
            Long exhibitionId,
            int pageNum,
            int limit
    ) {
        Exhibition exhibition = exhibitionValidator.validateExhibition(exhibitionId);

        PageRequest pageRequest = PageRequest.of(pageNum, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> reviews = postRepository.findReviewByExhibition(exhibition, pageRequest);
        PageInfo pageInfo = PageInfo.of(pageNum, limit, reviews.getTotalPages(), reviews.getTotalElements());

        List<ExhibitionReview> exhibitionReviews = reviews.getContent().stream().map(
                review -> ExhibitionReview.of(review, reviewImageService.getReviewImageUrls(review.getPostId()))
        ).toList();

        return ExhibitionReviewListResponse.of(exhibitionReviews, pageInfo);
    }


}
