package com.example.tikitaka.domain.exhibition.service;

import com.example.tikitaka.domain.club.entity.Club;
import com.example.tikitaka.domain.club.repository.ClubRepository;
import com.example.tikitaka.domain.club.service.ClubService;
import com.example.tikitaka.domain.exhibition.dto.ExhibitionCreate;
import com.example.tikitaka.domain.exhibition.dto.ExhibitionPostRequest;
import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.exhibition.mapper.ExhibitionMapper;
import com.example.tikitaka.domain.exhibition.repository.ExhibitionRepository;
import com.example.tikitaka.domain.host.dto.HostCreate;
import com.example.tikitaka.domain.host.entity.Host;
import com.example.tikitaka.domain.host.repository.HostRepository;
import com.example.tikitaka.domain.host.service.HostService;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.repository.MemberRepository;
import com.example.tikitaka.domain.member.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExhibitionService {
    private final ExhibitionRepository exhibitionRepository;
    private final MemberValidator memberValidator;
    private final ClubService clubService;
    private final HostService hostService;
    private final ExhibitionMapper exhibitionMapper;

    @Transactional
    public void addExhibition(ExhibitionPostRequest request) {
        // 멤버 찾기
        Member member = memberValidator.validateMember(request.getMemberId());

        // 클럽 찾기 (없으면 생성 있으면 참조)
        String clubId = clubService.clubGetOrAdd(request.getClub());

        // 전시 생성
        Exhibition exhibition = exhibitionMapper.toExhibition(request.getExhibition(), clubId);
        exhibitionRepository.save(exhibition);

        // host 등록
        hostService.hostAdd(HostCreate.of(
                member.getMemberId(),
                exhibition.getExhibitionId(),
                true));

    }
}
