package com.example.tikitaka.domain.host.validator;

import com.example.tikitaka.domain.host.repository.HostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HostValidator {
    private final HostRepository hostRepository;

//    public boolean validateRole(Member member, Exhibition exhibition) {
//        return hostRepository.existsByMemberAndExhibition(member, exhibition);
//    }

}
