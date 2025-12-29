package com.example.tikitaka.domain.post.dto.response;

import com.example.tikitaka.domain.post.dto.MyReviewItem;
import com.example.tikitaka.global.dto.PageInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter @Builder
public class MyReviewListResponse {
    private List<MyReviewItem> items;
    private PageInfo pageInfo;

    public static MyReviewListResponse of(List<MyReviewItem> items, PageInfo pageInfo) {
        return MyReviewListResponse.builder().items(items).pageInfo(pageInfo).build();
    }
}
