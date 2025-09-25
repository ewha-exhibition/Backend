package com.example.tikitaka.domain.exhibition;

import com.example.tikitaka.global.exception.BaseErrorCode;
import com.example.tikitaka.global.exception.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ExhibitionErrorCode implements BaseErrorCode {
    EXHIBITION_NOT_FOUND(NOT_FOUND, "EXHIBITION_404_1", "해당하는 전시를 조회할 수 없습니다.");

    private HttpStatus status;
    private String code;
    private String reason;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status.value(), code, reason);
    }
}
