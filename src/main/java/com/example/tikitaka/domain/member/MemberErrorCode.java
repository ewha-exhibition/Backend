package com.example.tikitaka.domain.member;

import com.example.tikitaka.global.exception.BaseErrorCode;
import com.example.tikitaka.global.exception.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {
    USER_NOT_FOUND(NOT_FOUND, "MEMBER_404_1", "해당하는 유저를 조회할 수 없습니다.");

    private HttpStatus status;
    private String code;
    private String reason;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status.value(), code, reason);
    }
}
