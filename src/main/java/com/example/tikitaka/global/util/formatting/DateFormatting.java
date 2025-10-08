package com.example.tikitaka.global.util.formatting;

import com.fasterxml.jackson.annotation.JsonValue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record DateFormatting(LocalDate start, LocalDate end) {
    private static final DateTimeFormatter FULL = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    private static final DateTimeFormatter SHORT = DateTimeFormatter.ofPattern("MM.dd");

    @JsonValue
    public String asString() {
        if (start == null) return null;
        if (end == null) return FULL.format(start);

        LocalDate s = start, e = end;
        if (s.isAfter(e)) { var t = s; s = e; e = t; }

        if (s.getYear() == e.getYear()) {
            return FULL.format(s) + " - " + SHORT.format(e);
        }

        return FULL.format(s) + " - " + FULL.format(e);
    }
}
