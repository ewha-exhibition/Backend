package com.example.tikitaka.domain.post.dto;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;

public interface ExhibitionPost {
    public static Exhibition of(){ return new Exhibition(); }
}
