package com.example.tikitaka.domain.scrap;

import com.example.tikitaka.global.exception.BaseErrorCode;
import com.example.tikitaka.global.exception.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ViewErrorCode implements BaseErrorCode {
    ALREADE_EXIST_VIEW(HttpStatus.CONFLICT, "VIEW_409_1", "이미 관람한 내역입니다."),
    NOT_FOUND_VIEW(HttpStatus.NOT_FOUND, "VIEW_404_1", "존재하지 않는 관람 내역입니다.");

    private HttpStatus status;
    private String code;
    private String reason;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status.value(), code, reason);
    }
}
