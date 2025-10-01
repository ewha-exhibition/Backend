package com.example.tikitaka.domain.host.dto;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.global.config.auth.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HostCreate {
    private User user;
    private Exhibition exhibition;
    private Boolean isRoot;

    public static HostCreate of(
            User user,
            Exhibition exhibition,
            Boolean isRoot
    ) {
        return new HostCreate(
                user,
                exhibition,
                isRoot
        );
    }
}
