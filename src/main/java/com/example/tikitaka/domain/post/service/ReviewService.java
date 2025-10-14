package com.example.tikitaka.domain.post.service;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.exhibition.validator.ExhibitionValidator;
import com.example.tikitaka.domain.post.dto.ExhibitionReview;
import com.example.tikitaka.domain.post.dto.request.ReviewPostRequest;
import com.example.tikitaka.domain.post.dto.response.ExhibitionReviewListResponse;
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
public class ReviewService {

    private final ExhibitionValidator exhibitionValidator;
    private final PostRepository postRepository;
    private final ReviewImageService reviewImageService;

    // TODO: 추후 유저 추가
    @Transactional
    public void addReview(Long exhibitionId, ReviewPostRequest reviewPostRequest) {
        // TODO: 유저 조회

        // 전시 조회
        Exhibition exhibition = exhibitionValidator.validateExhibition(exhibitionId);

        // TODO: 작성 경험 존재한 유저인지 확인 (있으면 number 꺼내쓰고, 없으면 exhibition에 no + 1)
        Long number = exhibition.getReviewNo() + 1;

        // 리뷰 생성
        Post review = Post.toEntity(exhibition, reviewPostRequest, PostType.REVIEW, number);
        postRepository.save(review);
        exhibition.increaseReviewNo();
        exhibition.increaseReviewCount();

        // 리뷰 이미지 저장
        for (String url : reviewPostRequest.getImages()) {
            reviewImageService.createReviewImages(review, url);
        }



    }

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
