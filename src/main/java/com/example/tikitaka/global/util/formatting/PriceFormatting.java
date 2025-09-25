package com.example.tikitaka.global.util.formatting;

import com.fasterxml.jackson.annotation.JsonValue;

import java.text.NumberFormat;
import java.util.Locale;

public record PriceFormatting(int price) {
    private static final NumberFormat FORMATTER = NumberFormat.getInstance(Locale.KOREA);

    @JsonValue
    public String asStirng() {
        if (price <= 0) return "무료";
        return "₩"+ FORMATTER.format(price);
    }

}
