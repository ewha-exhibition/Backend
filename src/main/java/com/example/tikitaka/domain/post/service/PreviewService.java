package com.example.tikitaka.domain.post.service;

import com.example.tikitaka.domain.comment.validator.CommentValidator;
import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.exhibition.validator.ExhibitionValidator;
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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PreviewService {
    private final ExhibitionValidator exhibitionValidator;
    private final CommentValidator commentValidator;
    private final PostRepository postRepository;

    @Transactional
    public void addCheer(Long exhibitionId, PreviewPostRequest previewPostRequest) {
        Exhibition exhibition = exhibitionValidator.validateExhibition(exhibitionId);

        Long number = exhibition.getCheerNo() + 1;
        exhibition.increaseCheerNo();

        Post cheer = Post.toPreviewEntity(exhibition, previewPostRequest, PostType.CHEER, number);
        postRepository.save(cheer);

        exhibition.increaseCheerNo();
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
}

