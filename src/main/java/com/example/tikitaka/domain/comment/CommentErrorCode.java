package com.example.tikitaka.domain.comment;

import com.example.tikitaka.global.exception.BaseErrorCode;
import com.example.tikitaka.global.exception.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum CommentErrorCode implements BaseErrorCode {
    COMMENT_NOT_FOUND(NOT_FOUND, "COMMENT_404_1", "해당하는 답변을 조회할 수 없습니다."),
    COMMENT_FORBIDDEN(FORBIDDEN, "COMMENT_403_1", "해당 답변을 삭제할 권한이 없습니다."),;

    private HttpStatus status;
    private String code;
    private String reason;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status.value(), code, reason);
    }
}
