package com.example.tikitaka.domain.exhibition.validator;

import com.example.tikitaka.domain.exhibition.ExhibitionErrorCode;
import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.exhibition.repository.ExhibitionRepository;
import com.example.tikitaka.global.exception.BaseErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ExhibitionValidator {
    private final ExhibitionRepository exhibitionRepository;

    public Exhibition validateExhibition(Long exhibitionIdx) {
        return exhibitionRepository.findByExhibitionIdx(exhibitionIdx).orElseThrow(
                () -> new BaseErrorException(ExhibitionErrorCode.EXHIBITION_NOT_FOUND)
        );
//        return exhibitionRepository.findDetailById(exhibitionIdx).orElseThrow(
//                () -> new BaseErrorException(ExhibitionErrorCode.EXHIBITION_NOT_FOUND)
//        );
    }

    public void validateAuthority(boolean role) {
        if (!role) {
            throw new BaseErrorException(ExhibitionErrorCode.EXHIBITION_ACCESS_FORBIDDEN);
        }
    }
}
