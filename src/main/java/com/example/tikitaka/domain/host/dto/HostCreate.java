package com.example.tikitaka.domain.host.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HostCreate {
    private String memberId;
    private String exhibitionId;
    private Boolean isRoot;

    public static HostCreate of(
            String memberId,
            String exhibitionId,
            Boolean isRoot
    ) {
        return new HostCreate(
                memberId,
                exhibitionId,
                isRoot
        );
    }
}
