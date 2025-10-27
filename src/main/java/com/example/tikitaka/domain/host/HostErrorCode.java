package com.example.tikitaka.domain.host;

import com.example.tikitaka.global.exception.BaseErrorCode;
import com.example.tikitaka.global.exception.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.CONFLICT;

@Getter
@AllArgsConstructor
public enum HostErrorCode implements BaseErrorCode {

    // 404: 초대 코드 없음
    CODE_NOT_FOUND(NOT_FOUND, "HOST_404_1", "해당하는 초대코드를 조회할 수 없습니다."),
    // 409: 이미 호스트임
    ALREADY_HOST(CONFLICT, "HOST_409_1", "이미 전시에 등록된 호스트입니다.");


    private HttpStatus status;
    private String code;
    private String reason;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status.value(), code, reason);
    }

}
