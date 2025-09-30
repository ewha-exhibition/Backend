package com.example.tikitaka.domain.host.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HostCreate {
    private Long memberId;
    private Long exhibitionId;
    private Boolean isRoot;

    public static HostCreate of(
            Long memberId,
            Long exhibitionId,
            Boolean isRoot
    ) {
        return new HostCreate(
                memberId,
                exhibitionId,
                isRoot
        );
    }
}
