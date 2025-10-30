package com.example.tikitaka.domain.host.validator;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.host.HostErrorCode;
import com.example.tikitaka.domain.host.entity.Host;
import com.example.tikitaka.domain.host.repository.HostRepository;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.global.exception.BaseErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HostValidator {
    private final HostRepository hostRepository;

    public Host validateHostOrThrow(Member member, Exhibition exhibition) {
        return hostRepository.findByMemberAndExhibition(member, exhibition)
                .orElseThrow(() -> new BaseErrorException(HostErrorCode.HOST_FORBIDDEN));
    }

    public boolean validateRole(Member member, Exhibition exhibition) {
        return hostRepository.existsByMemberAndExhibition(member, exhibition);
    }


}
