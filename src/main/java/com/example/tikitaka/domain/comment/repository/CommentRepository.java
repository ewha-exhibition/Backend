package com.example.tikitaka.domain.comment.repository;

import com.example.tikitaka.domain.comment.entity.Comment;
import com.example.tikitaka.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("""
    SELECT c.content
    FROM Comment c
    WHERE c.post = :post
    """)
    Optional<String> findContentByPost(Post post);
}
