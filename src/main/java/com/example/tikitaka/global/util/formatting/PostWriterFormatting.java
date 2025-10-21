package com.example.tikitaka.global.util.formatting;

import com.fasterxml.jackson.annotation.JsonValue;

import java.text.NumberFormat;
import java.util.Locale;

public record PostWriterFormatting(Long displayNo) {
    private static final NumberFormat FORMAT = NumberFormat.getNumberInstance(Locale.KOREA);

    @JsonValue
    public String asString() {
        return "벗" + FORMAT.format(displayNo);
    }
}
