package com.example.tikitaka.domain.host.dto;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HostCreate {
    private Member user;
    private Exhibition exhibition;
    private Boolean isRoot;

    public static HostCreate of(
            Member member,
            Exhibition exhibition,
            Boolean isRoot
    ) {
        return new HostCreate(
                member,
                exhibition,
                isRoot
        );
    }
}
