package com.example.tikitaka.domain.post;

import com.example.tikitaka.global.exception.BaseErrorCode;
import com.example.tikitaka.global.exception.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Getter
@AllArgsConstructor
public enum PostErrorCode implements BaseErrorCode {
    POST_FORBIDDEN(FORBIDDEN, "POST_403_1", "해당하는 글을 삭제할 권한이 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST_404_1", "해당하는 글을 찾을 수 없습니다.")
    ;

    private HttpStatus status;
    private String code;
    private String reason;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status.value(), code, reason);
    }
}
