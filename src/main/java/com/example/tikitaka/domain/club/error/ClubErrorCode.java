package com.example.tikitaka.domain.club.error;

import com.example.tikitaka.global.exception.BaseErrorCode;
import com.example.tikitaka.global.exception.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ClubErrorCode implements BaseErrorCode {
    CLUB_ALREADY_EXIST(BAD_REQUEST, "CLUB_400_1", "해당 단체가 이미 존재합니다");

    private HttpStatus status;
    private String code;
    private String reason;


    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status.value(), code, reason);
    }
}
