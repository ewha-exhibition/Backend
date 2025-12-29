package com.example.tikitaka.domain.post.dto.response;

import com.example.tikitaka.domain.post.dto.MyPreview;
import com.example.tikitaka.global.dto.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class MyPreviewGetResponse {
    private List<MyPreview> previews;
    private PageInfo pageInfo;

    public static MyPreviewGetResponse of(List<MyPreview> previews, PageInfo pageInfo) {
        return MyPreviewGetResponse.builder()
                .previews(previews)
                .pageInfo(pageInfo)
                .build();
    }
}
