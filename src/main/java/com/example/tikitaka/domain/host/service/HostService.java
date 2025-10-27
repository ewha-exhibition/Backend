package com.example.tikitaka.domain.host.service;

import com.example.tikitaka.domain.exhibition.ExhibitionErrorCode;
import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.exhibition.repository.ExhibitionRepository;
import com.example.tikitaka.domain.exhibition.util.HostCodeGenerator;
import com.example.tikitaka.domain.host.HostErrorCode;
import com.example.tikitaka.domain.host.dto.HostCreate;
import com.example.tikitaka.domain.host.entity.Host;
import com.example.tikitaka.domain.host.mapper.HostMapper;
import com.example.tikitaka.domain.host.repository.HostRepository;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.global.exception.BaseErrorException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HostService {
    private final HostMapper hostMapper;
    private final HostRepository hostRepository;
    private final HostCodeGenerator hostCodeGenerator;

    private final ExhibitionRepository exhibitionRepository;

    // 1. 전시 생성 직후: 루트 호스트 생성 ( 초대코드 발급은 ExhibitionService에서)
    @Transactional
    public void hostAdd(HostCreate hostCreate) {
        Host host = hostMapper.toHost(hostCreate);
        hostRepository.save(host);  //루트 호스트 생성
    }

    // 2. 초대 코드로 공동 호스트 합류 (idempotent)
    public void joinByInviteCode(Long memberId, String code) {
        // 2.1. 초대 코드에 해당하는 전시가 없는 경우 -> 404
        Exhibition exhibition = exhibitionRepository.findByCode(code)
                .orElseThrow(
                        () -> new BaseErrorException(HostErrorCode.CODE_NOT_FOUND));

        // 2.2. 이미 루트 호스트|| 등록한 멤버가 접근한 거라면, -> 이미 호스트 입니다
        boolean alreadyHost = hostRepository
                .existsByMember_MemberIdAndExhibition_ExhibitionId(memberId, exhibition.getExhibitionId());
        if (alreadyHost) {
            throw new BaseErrorException(HostErrorCode.ALREADY_HOST);
        }

        // 2.3. 공동 호스트 등록 : 초대 코드와 일치하는 전시가 있는 경우
        Host co = hostMapper.toHost(HostCreate.of(
                Member.builder().memberId(memberId).build(),
                exhibition,
                false
        ));
        hostRepository.save(co);
    }

}
