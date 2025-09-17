package com.example.tikitaka.domain.club.service;

import com.example.tikitaka.domain.club.dto.ClubCreate;
import com.example.tikitaka.domain.club.entity.Club;
import com.example.tikitaka.domain.club.repository.ClubRepository;
import com.example.tikitaka.domain.club.validator.ClubValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubService {
    private final ClubValidator clubValidator;
    private final ClubRepository clubRepository;

    @Transactional
    public String clubAdd(ClubCreate clubCreateDto) {
        // 입력 받은 단체명의 공백 제거
        String name = clubCreateDto.getName().replace(" ", "");

        // 이미 존재하는 단체인지 확인 (이미 존재하는 단체면 CLUB_ALREADY_EXIST 에러 발생)
        clubValidator.alreadyExist(clubCreateDto.getName());

        // 단체 생성 (이 부분 추후 정적 팩토리 메서드로 리팩토링 예정)
        String clubId = UUID.randomUUID().toString();

        Club club = Club.builder()
                .clubId(clubId)
                .name(name)
                .build();

        clubRepository.save(club);

        return club.getClubId();

    }

}
