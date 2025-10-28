package com.example.tikitaka.domain.host;

import com.example.tikitaka.global.exception.BaseErrorCode;
import com.example.tikitaka.global.exception.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum HostErrorCode implements BaseErrorCode {
    HOST_NOT_FOUND(HttpStatus.NOT_FOUND, "HOST_404_1", "호스트를 찾을 수 없습니다."),
    HOST_FORBIDDEN(HttpStatus.FORBIDDEN, "HOST_403_1", "권한이 존재하지 않습니다."),;

    private HttpStatus status;
    private String code;
    private String reason;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status.value(), code, reason);
    }

}
