package com.example.tikitaka.domain.exhibition.dto;

import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
public class ExhibitionImagePatch {
    private JsonNullable<Long> id = JsonNullable.undefined();
    private JsonNullable<String> url = JsonNullable.undefined();
    private JsonNullable<Integer> sequence = JsonNullable.undefined();
}
