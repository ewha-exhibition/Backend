package com.example.tikitaka.domain.exhibition.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
public class CreateExhibitionRequest {
    private long memberId; // 인증 인가 구현 후 삭제 예정

    private String exhibitionName;
    private String category;
    private String title;
    private String posterUrl;
    private String place;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String dateException;
    private int price;
    private String link;
    private String content;
    private String club;
    private List<String> imageUrls;
}
