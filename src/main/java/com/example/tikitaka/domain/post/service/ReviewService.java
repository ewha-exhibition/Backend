package com.example.tikitaka.domain.post.service;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.exhibition.validator.ExhibitionValidator;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.validator.MemberValidator;
import com.example.tikitaka.domain.post.dto.ExhibitionPost;
import com.example.tikitaka.domain.post.dto.ExhibitionReview;
import com.example.tikitaka.domain.post.dto.request.ReviewPostRequest;
import com.example.tikitaka.domain.post.dto.response.ExhibitionPostListResponse;
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
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ExhibitionValidator exhibitionValidator;
    private final PostRepository postRepository;
    private final PostImageService postImageService;
    private final MemberValidator memberValidator;


    @Transactional
    public void addReview(String memberId, Long exhibitionId, ReviewPostRequest reviewPostRequest) {
        Member member = memberValidator.validateMember(Long.parseLong(memberId));

        // 전시 조회
        Exhibition exhibition = exhibitionValidator.validateExhibition(exhibitionId);

        Post post = postRepository.findByMemberAndExhibitionAndPostType(member, exhibition, PostType.REVIEW);

        Long number = 0L;

        if (post != null) {
            number = post.getDisplayNo();
        } else {
            number = exhibition.getReviewNo() + 1;
            exhibition.increaseReviewNo();
        }

        exhibition.increaseReviewCount();

        // 리뷰 생성
        Post review = Post.toReviewEntity(member, exhibition, reviewPostRequest, PostType.REVIEW, number);
        postRepository.save(review);

        exhibition.increaseReviewCount();

        // 리뷰 이미지 저장
        for (String url : reviewPostRequest.getImages()) {
            postImageService.createReviewImages(review, url);
        }



    }

    public ExhibitionPostListResponse getExhibitionReviews(
            String memberId,
            Long exhibitionId,
            int pageNum,
            int limit
    ) {
        Exhibition exhibition = exhibitionValidator.validateExhibition(exhibitionId);

        PageRequest pageRequest = PageRequest.of(pageNum, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> reviews = postRepository.findReviewByExhibition(exhibition, pageRequest);
        PageInfo pageInfo = PageInfo.of(pageNum, limit, reviews.getTotalPages(), reviews.getTotalElements());

        List<ExhibitionPost> exhibitionReviews = reviews.getContent().stream().map(
                review -> (ExhibitionPost) ExhibitionReview.of(review, postImageService.getReviewImageUrls(review),Objects.equals(memberId, String.valueOf(review.getMember().getMemberId())))
        ).toList();

        return ExhibitionPostListResponse.of(exhibitionReviews, pageInfo);
    }


}
