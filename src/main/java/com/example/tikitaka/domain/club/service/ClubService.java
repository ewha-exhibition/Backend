package com.example.tikitaka.domain.club.service;

import com.example.tikitaka.domain.club.dto.ClubCreate;
import com.example.tikitaka.domain.club.dto.ClubRead;
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
    public Club clubAdd(ClubCreate clubCreate) {
        // 입력 받은 단체명의 공백 제거
        String name = clubCreate.getName().replace(" ", "");

        // 이미 존재하는 단체인지 확인 (이미 존재하는 단체면 CLUB_ALREADY_EXIST 에러 발생)
        clubValidator.alreadyExist(name);

        Club club = Club.builder()
                .name(name)
                .build();

        clubRepository.save(club);

        return club;

    }

    public Club clubGetOrAdd(String clubName) {
        // clubAdd 메서드와 동일 로직 작동 -> 코드 중복 줄이는 리팩토링 필요
        return clubRepository.findByName(clubName).orElseGet(() -> {
            Club newClub = Club.builder()
                    .name(clubName)
                    .build();
            return clubRepository.save(newClub);
        });
    }

}
