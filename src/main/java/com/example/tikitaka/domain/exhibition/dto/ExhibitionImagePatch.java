package com.example.tikitaka.domain.exhibition.dto;

import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
public class ExhibitionImagePatch {
    private JsonNullable<Long> id;
    private JsonNullable<String> url;
    private JsonNullable<Integer> sequence;
}
