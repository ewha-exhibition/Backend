package com.example.tikitaka.domain.scrap.dto.response;

import com.example.tikitaka.domain.scrap.dto.ScrapListItemDto;
import com.example.tikitaka.domain.scrap.dto.ViewsDto;
import com.example.tikitaka.global.dto.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ViewsResponseDto {
    private String username;
    private List<ViewsDto> exhibitions;
    private PageInfo pageInfo;

    public static ViewsResponseDto from(
            String username,
            List<ViewsDto> exhibitions,
            PageInfo pageInfo
    ) {
        return ViewsResponseDto.builder()
                .username(username)
                .exhibitions(exhibitions)
                .pageInfo(pageInfo)
                .build();
    }
}