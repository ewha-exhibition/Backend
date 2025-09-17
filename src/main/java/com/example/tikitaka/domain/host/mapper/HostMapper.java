package com.example.tikitaka.domain.host.mapper;

import com.example.tikitaka.domain.host.dto.HostCreate;
import com.example.tikitaka.domain.host.entity.Host;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class HostMapper {
    public Host toHost(HostCreate req) {
        String code = UUID.randomUUID().toString();
        return Host.builder()
                .hostId(code)
                .memberId(req.getMemberId())
                .exhibitionId(req.getExhibitionId())
                .isRoot(req.getIsRoot())
                .build();
    }
}
