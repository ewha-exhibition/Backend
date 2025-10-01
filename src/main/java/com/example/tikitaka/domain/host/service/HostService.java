package com.example.tikitaka.domain.host.service;

import com.example.tikitaka.domain.host.dto.HostCreate;
import com.example.tikitaka.domain.host.entity.Host;
import com.example.tikitaka.domain.host.mapper.HostMapper;
import com.example.tikitaka.domain.host.repository.HostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HostService {
    private final HostMapper hostMapper;
    private final HostRepository hostRepository;

    public void hostAdd(HostCreate hostCreate) {
        Host host = hostMapper.toHost(hostCreate);
        hostRepository.save(host);
    }
}
