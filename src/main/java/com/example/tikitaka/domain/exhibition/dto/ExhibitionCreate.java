package com.example.tikitaka.domain.exhibition.dto;

import com.example.tikitaka.domain.exhibition.vo.ExhibitionImageVo;
import com.example.tikitaka.domain.exhibition.vo.ExhibitionVo;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
public class ExhibitionCreate {
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
