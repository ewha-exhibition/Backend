package com.example.tikitaka.global.util.formatting;

import com.fasterxml.jackson.annotation.JsonValue;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public record TimeFormatting(LocalTime start, LocalTime end) {
    private static final DateTimeFormatter FULL = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter SHORT = DateTimeFormatter.ofPattern("H:mm");

    @JsonValue
    public String asString() {
        LocalTime s = start, e = end;

        if (s.isAfter(e)) { var t = s; s = e; e = t;}

        if(s.getHour() < 10) {
            if (e.getHour() < 10) {
                return SHORT.format(s) + " - " + SHORT.format(e);
            } else {
                return SHORT.format(s) + " - " + FULL.format(e);
            }
        }
        else {
            return FULL.format(s) + " - " + FULL.format(e);
        }
    }

}
