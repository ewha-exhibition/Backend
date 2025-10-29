package com.example.tikitaka.domain.scrap;

import com.example.tikitaka.global.exception.BaseErrorCode;
import com.example.tikitaka.global.exception.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum ScrapErrorCode implements BaseErrorCode {
    SCRAP_ALREADY_EXIST(BAD_REQUEST, "SCRAP_400_1", "해당 스크랩이 이미 존재합니다");

    private HttpStatus status;
    private String code;
    private String reason;


    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status.value(), code, reason);
    }
}
