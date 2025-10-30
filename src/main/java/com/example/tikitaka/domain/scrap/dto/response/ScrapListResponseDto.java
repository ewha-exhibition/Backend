package com.example.tikitaka.domain.scrap.dto.response;

import com.example.tikitaka.domain.scrap.dto.ScrapListItemDto;
import com.example.tikitaka.global.dto.PageInfo;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ScrapListResponseDto {
    private String username;
    private List<ScrapListItemDto> exhibitions;
    private PageInfo pageInfo;

    public static ScrapListResponseDto from(
            String username,
            List<ScrapListItemDto> exhibitions,
            PageInfo pageInfo
    ) {
        return ScrapListResponseDto.builder()
                .username(username)
                .exhibitions(exhibitions)
                .pageInfo(pageInfo)
                .build();
    }
}