package com.example.tikitaka.domain.post.dto.response;

import com.example.tikitaka.domain.post.dto.ExhibitionPost;
import com.example.tikitaka.domain.post.dto.PostCard;
import com.example.tikitaka.global.dto.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class GuestBookResponse {
    private List<PostCard> posts;
    private PageInfo pageInfo;

    public static GuestBookResponse of(List<PostCard> posts, PageInfo pageInfo) {
        return GuestBookResponse.builder()
                .posts(posts)
                .pageInfo(pageInfo)
                .build();
    }
}
