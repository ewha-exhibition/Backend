package com.example.tikitaka.domain.post.service;

import com.example.tikitaka.domain.post.entity.Post;
import com.example.tikitaka.domain.post.entity.PostImage;
import com.example.tikitaka.domain.post.repository.PostImageRepository;
import com.example.tikitaka.infra.s3.S3Url;
import com.example.tikitaka.infra.s3.S3UrlHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostImageService {
    private final S3UrlHandler s3UrlHandler;
    private final PostImageRepository postImageRepository;

    public S3Url getImageUploadUrl(String prefix) {
        return s3UrlHandler.handle(prefix);
    }

    public List<String> getReviewImageUrls(Post post) {
        return postImageRepository.findByPost(post).stream().map(PostImage::getPostImageUrl).toList();
    }

    public void createReviewImages(Post post, String url) {
        postImageRepository.save(PostImage.toEntity(post, url));
    }

    public void deletePostImages(Post post) {
        List<PostImage> postImages = postImageRepository.findAllByPost(post);
        // TODO: 추후 S3 url 삭제 로직 구현
        postImageRepository.deleteAll(postImages);
    }


}
