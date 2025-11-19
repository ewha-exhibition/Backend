package com.example.tikitaka.domain.host.dto;

import com.example.tikitaka.domain.exhibition.dto.RecentExhibition;
import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.global.dto.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class HostExhibitionListResponse {
    private List<Exhibition> exhibitions;
    private PageInfo pageInfo;
}
