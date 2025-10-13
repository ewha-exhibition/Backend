package com.example.tikitaka.global.util.formatting;

import com.fasterxml.jackson.annotation.JsonValue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record DateFormatting(LocalDateTime createdAt) {
    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    @JsonValue
    public String asString() {
        return createdAt.format(FORMATTER);
    }
}
