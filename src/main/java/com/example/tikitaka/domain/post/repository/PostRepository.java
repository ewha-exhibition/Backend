package com.example.tikitaka.domain.post.repository;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.post.entity.Post;
import com.example.tikitaka.domain.post.entity.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByMember_MemberIdAndPostType(Long memberId, PostType postType, Pageable pageable);

    @Query("""
    SELECT p
    FROM Post p
    WHERE p.exhibition = :exhibition AND p.postType = 'REVIEW' AND NOT p.isDeleted
    """
    )
    Page<Post> findReviewByExhibition(Exhibition exhibition, Pageable pageable);

    @Query("""
    SELECT p
    FROM Post p
    WHERE p.exhibition = :exhibition AND p.postType = :postType AND NOT p.isDeleted
    """
    )
    Page<Post> findByExhibitionAndPostType(Exhibition exhibition, PostType postType, Pageable pageable);

    Page<Post> findReviewByPostType(PostType postType, Pageable pageable);

    Post findByMemberAndExhibitionAndPostType(Member member, Exhibition exhibition, PostType postType);

    @Query("""
    SELECT p
    FROM Post p
    WHERE p.member.memberId = :memberId AND p.postId = :postId
    """)
    Optional<Post> findByMemberIdAndPostId(Long memberId, Long postId);
}
