package com.example.tikitaka.domain.exhibition.service;

import com.example.tikitaka.domain.exhibition.dto.RecentExhibition;
import com.example.tikitaka.domain.exhibition.dto.response.RecentExhibitionListResponse;
import com.example.tikitaka.domain.exhibition.entity.Category;
import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.exhibition.repository.ExhibitionRepository;
import com.example.tikitaka.global.dto.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HomeService {
    private final ExhibitionRepository exhibitionRepository;

    public RecentExhibitionListResponse findRecentExhibition(String category, int pageNum, int limit) {
        PageRequest pageRequest = PageRequest.of(pageNum, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Exhibition> exhibitions;

        if(category == null){
            exhibitions = exhibitionRepository.findAll(pageRequest);
        }

        else {
            Category tag = Category.valueOf(category.toUpperCase());
            exhibitions = exhibitionRepository.findByCategory(tag, pageRequest);
        }

        PageInfo pageInfo = PageInfo.of(pageNum, limit, exhibitions.getTotalPages(), exhibitions.getTotalElements());
        List<RecentExhibition> recentExhibitions = exhibitions.getContent().stream().map(RecentExhibition::from).toList();

        return new RecentExhibitionListResponse(recentExhibitions, pageInfo);
    }

}
