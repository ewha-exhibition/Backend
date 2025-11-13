package com.example.tikitaka.domain.comment.service;

import com.example.tikitaka.domain.comment.dto.CommentPostRequest;
import com.example.tikitaka.domain.comment.entity.Comment;
import com.example.tikitaka.domain.comment.repository.CommentRepository;
import com.example.tikitaka.domain.comment.validator.CommentValidator;
import com.example.tikitaka.domain.host.validator.HostValidator;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.validator.MemberValidator;
import com.example.tikitaka.domain.post.entity.Post;
import com.example.tikitaka.domain.post.service.PostService;
import com.example.tikitaka.domain.post.validator.PostValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final PostValidator postValidator;
    private final MemberValidator memberValidator;
    private final CommentValidator commentValidator;

    private final CommentRepository commentRepository;
    private final HostValidator hostValidator;
    private final PostService postService;

    @Transactional
    public void addComment(Long memberId, Long postId, CommentPostRequest commentPostRequest) {
        Member member = memberValidator.validateMember(memberId);
        Post post = postValidator.validatePostByPostId(postId);
        commentRepository.save(Comment.toEntity(member, post, commentPostRequest.getContent()));
        postService.switchHasAnswer(post);

    }

    @Transactional
    public void deleteComment(Long memberId, Long commentId) {
        Comment comment = commentValidator.validateCommentByMemberIdAndCommentId(memberId, commentId);
        commentRepository.delete(comment);
        postService.switchHasAnswer(comment.getPost()); // TODO: N+1 문제 해결
    }

}
