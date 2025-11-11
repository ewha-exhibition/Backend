package com.example.tikitaka.domain.exhibition.dto.request;

import com.example.tikitaka.domain.exhibition.dto.ExhibitionImageCreate;
import com.example.tikitaka.domain.exhibition.dto.ExhibitionImagePatch;
import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
public class ExhibitionPatchRequest {
    private JsonNullable<String> exhibitionName = JsonNullable.undefined();
    private JsonNullable<String> posterUrl = JsonNullable.undefined();
    private JsonNullable<String> place = JsonNullable.undefined();
    private JsonNullable<LocalDate> startDate = JsonNullable.undefined();
    private JsonNullable<LocalDate> endDate = JsonNullable.undefined();
    private JsonNullable<LocalTime> startTime = JsonNullable.undefined();
    private JsonNullable<LocalTime> endTime = JsonNullable.undefined();
    private JsonNullable<String> dateException = JsonNullable.undefined();
    private JsonNullable<Integer> price = JsonNullable.undefined();
    private JsonNullable<String> link = JsonNullable.undefined();
    private JsonNullable<String> content = JsonNullable.undefined();
    private JsonNullable<String> category = JsonNullable.undefined();
    private JsonNullable<String> name = JsonNullable.undefined();

    private JsonNullable<String> clubName = JsonNullable.undefined();

    private JsonNullable<List<ExhibitionImagePatch>> images = JsonNullable.undefined();
}
