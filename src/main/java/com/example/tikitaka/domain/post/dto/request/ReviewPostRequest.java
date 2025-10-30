package com.example.tikitaka.domain.post.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ReviewPostRequest {
    private String content;
    private List<String> images;
}
