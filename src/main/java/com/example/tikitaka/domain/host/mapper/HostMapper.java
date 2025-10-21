package com.example.tikitaka.domain.host.mapper;

import com.example.tikitaka.domain.host.dto.HostCreate;
import com.example.tikitaka.domain.host.entity.Host;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class HostMapper {
    public Host toHost(HostCreate req) {
        return Host.builder()
                .member(req.getUser())
                .exhibition(req.getExhibition())
                .isRoot(req.getIsRoot())
                .build();
    }
}
