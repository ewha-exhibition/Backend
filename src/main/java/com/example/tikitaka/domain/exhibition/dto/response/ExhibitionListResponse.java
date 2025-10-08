package com.example.tikitaka.domain.exhibition.dto.response;

import com.example.tikitaka.domain.exhibition.dto.RecentExhibition;
import com.example.tikitaka.global.dto.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ExhibitionListResponse {
    private List<RecentExhibition> exhibitions;
    private PageInfo pageInfo;
}
