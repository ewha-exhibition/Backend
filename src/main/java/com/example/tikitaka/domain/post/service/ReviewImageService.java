package com.example.tikitaka.domain.post.service;

import com.example.tikitaka.domain.post.repository.PostImageRepository;
import com.example.tikitaka.domain.post.repository.PostRepository;
import com.example.tikitaka.infra.s3.S3Url;
import com.example.tikitaka.infra.s3.S3UrlHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewImageService {
    private final S3UrlHandler s3UrlHandler;
    private final PostImageRepository postImageRepository;

    public S3Url getImageUploadUrl(String prefix) {
        return s3UrlHandler.handle(prefix);
    }

    public List<String> getReviewImageUrls(Long postId) {
        return postImageRepository.findPostImageUrlsByPostId(postId);
    }
}
