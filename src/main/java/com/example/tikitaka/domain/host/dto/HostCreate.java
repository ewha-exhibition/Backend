package com.example.tikitaka.domain.host.dto;

import lombok.Getter;

@Getter
public class HostCreate {
    private String memberId;
    private String exhibitionId;
    private Boolean isRoot;
}
