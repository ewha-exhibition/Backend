package com.example.tikitaka.domain.club.validator;

import com.example.tikitaka.domain.club.ClubErrorCode;
import com.example.tikitaka.domain.club.repository.ClubRepository;
import com.example.tikitaka.global.exception.BaseErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClubValidator {
    private final ClubRepository clubRepository;

    public void alreadyExist(String name) {
        if(clubRepository.existsByName(name)) {
            throw new BaseErrorException(ClubErrorCode.CLUB_ALREADY_EXIST);
        }
    }
}
