package com.example.tikitaka.domain.post.repository;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.post.entity.Post;
import com.example.tikitaka.domain.post.entity.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("""
    SELECT p
    FROM Post p
    WHERE p.exhibition = :exhibition AND p.postType = 'REVIEW'
    """
    )
    Page<Post> findReviewByExhibition(Exhibition exhibition, Pageable pageable);

    @Query("""
    SELECT p
    FROM Post p
    WHERE p.exhibition = :exhibition AND p.postType = :postType
    """
    )
    Page<Post> findByExhibitionAndPostType(Exhibition exhibition, PostType postType, Pageable pageable);
}
