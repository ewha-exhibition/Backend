package com.example.tikitaka.domain.exhibition.vo;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ExhibitionVo {
    private String exhibitionName;
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
    private String category;
}
