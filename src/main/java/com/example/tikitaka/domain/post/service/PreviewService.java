package com.example.tikitaka.domain.post.service;

import com.example.tikitaka.domain.comment.validator.CommentValidator;
import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.exhibition.validator.ExhibitionValidator;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.validator.MemberValidator;
import com.example.tikitaka.domain.post.dto.ExhibitionPost;
import com.example.tikitaka.domain.post.dto.ExhibitionPreview;
import com.example.tikitaka.domain.post.dto.request.PreviewPostRequest;
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
public class PreviewService {
    private final ExhibitionValidator exhibitionValidator;
    private final CommentValidator commentValidator;
    private final PostRepository postRepository;
    private final MemberValidator memberValidator;

    @Transactional
    public void addPreview(Long memberId, Long exhibitionId, PreviewPostRequest previewPostRequest, PostType postType) {
        Member member = memberValidator.validateMember(memberId);
        Exhibition exhibition = exhibitionValidator.validateExhibition(exhibitionId);

        // 추후 유저가 이미 post를 생성한 경우를 고려하기 위한 변수
        Post post = postRepository.findByMemberAndExhibitionAndPostType(member, exhibition, postType);

        Long number = 0L;
        if (post == null && postType == PostType.QUESTION) {
            number = exhibition.getQuestionNo() + 1;
            exhibition.increaseQuestionNo();
            exhibition.increaseQuestionCount();
        }
        else if (post == null && postType == PostType.CHEER) {
            number = exhibition.getCheerNo() + 1;
            exhibition.increaseCheerNo();
            exhibition.increaseCheerCount();
        }
        else {
            number = post.getDisplayNo();
            exhibition.increaseCheerCount();
        }


        Post cheer = Post.toPreviewEntity(member, exhibition, previewPostRequest, postType, number);
        postRepository.save(cheer);

    }

    public ExhibitionPostListResponse getExhibitionPreviews(
            Long memberId,
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
                preview -> (preview.isHasAnswer())?(ExhibitionPost) ExhibitionPreview.of(preview, commentValidator.validateCommentContent(preview), Objects.equals(
                        memberId,
                        String.valueOf(preview.getMember().getMemberId())
                )): (ExhibitionPost) ExhibitionPreview.of(preview, null, Objects.equals(
                        memberId,
                        String.valueOf(preview.getMember().getMemberId())
                ))
        ).toList();

        return ExhibitionPostListResponse.of(exhibitionCheers, pageInfo);
    }
}

