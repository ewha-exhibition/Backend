package com.example.tikitaka.domain.exhibition.service;

import com.example.tikitaka.domain.club.entity.Club;
import com.example.tikitaka.domain.club.service.ClubService;
import com.example.tikitaka.domain.exhibition.dto.ExhibitionCreate;
import com.example.tikitaka.domain.exhibition.dto.request.ExhibitionPostRequest;
import com.example.tikitaka.domain.exhibition.dto.response.ExhibitionDetailResponse;
import com.example.tikitaka.domain.exhibition.entity.Category;
import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.exhibition.entity.ExhibitionImage;
import com.example.tikitaka.domain.exhibition.mapper.ExhibitionMapper;
import com.example.tikitaka.domain.exhibition.repository.ExhibitionImageRepository;
import com.example.tikitaka.domain.exhibition.repository.ExhibitionRepository;
import com.example.tikitaka.domain.exhibition.validator.ExhibitionValidator;
import com.example.tikitaka.domain.host.dto.HostCreate;
import com.example.tikitaka.domain.host.service.HostService;
import com.example.tikitaka.domain.host.validator.HostValidator;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.validator.MemberValidator;
import com.example.tikitaka.infra.s3.S3Url;
import com.example.tikitaka.infra.s3.S3UrlHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExhibitionService {
    // 서비스
    private final ClubService clubService;
    private final HostService hostService;

    // 레포지토리
    private final ExhibitionRepository exhibitionRepository;
    private final ExhibitionImageRepository exhibitionImageRepository;

    // validator
    private final MemberValidator userValidator;
    private final ExhibitionValidator exhibitionValidator;

    // Mapper
    private final ExhibitionMapper exhibitionMapper;
    private final HostValidator hostValidator;

    // S3
    private final S3UrlHandler s3UrlHandler;


    @Transactional
    public void addExhibition(ExhibitionPostRequest request) {
        // 멤버 찾기
        Member member = userValidator.validateMember(request.getUserId());

        // 클럽 찾기 (없으면 생성 있으면 참조)
        Club club = clubService.clubGetOrAdd(request.getClub());

        // 전시 생성
        Exhibition exhibition = exhibitionMapper.toExhibition(request.getExhibition(), club);
        exhibitionRepository.save(exhibition);

        // 전시 이미지 등록
        if (request.getImages().isEmpty()) {
            List<ExhibitionImage> images = exhibitionMapper.toExhibitionImages(exhibition, request.getImages());
            exhibitionImageRepository.saveAll(images);
        }

        // host 등록
        hostService.hostAdd(HostCreate.of(
                member,
                exhibition,
                true));

    }

    public ExhibitionDetailResponse findExhibition(Long exhibitionId) {
        Exhibition exhibition = exhibitionValidator.validateExhibition(exhibitionId);
        List<String> images = exhibitionImageRepository.findByExhibitionIdOrderBySequenceAsc(exhibitionId);


        return exhibitionMapper.toDetailResponse(exhibition, images);

    }

    @Transactional
    public void deleteExhibition(Long exhibitionId) {
        Exhibition exhibition = exhibitionValidator.validateExhibition(exhibitionId);

        //exhibitionValidator.validateExhibition(hostValidator.validateRole(member, exhibition));
        exhibition.markAsDeleted();
        exhibitionRepository.save(exhibition);
    }

    @Transactional
    public void updateExhibition(Long exhibitionId, ExhibitionCreate request) {
        Exhibition exhibition = exhibitionValidator.validateExhibition(exhibitionId);

        //exhibitionValidator.validateExhibition(hostValidator.validateRole(member, exhibition));


        // TODO: 추후 리팩토링 - null 바인딩 여부 확인 후 값 수정
        exhibition.setExhibitionName(request.getExhibitionName());
        exhibition.setPosterUrl(request.getPosterUrl());
        exhibition.setPlace(request.getPlace());
        exhibition.setStartDate(request.getStartDate());
        exhibition.setEndDate(request.getEndDate());
        exhibition.setDateException(request.getDateException());
        exhibition.setPrice(request.getPrice());
        exhibition.setLink(request.getLink());
        exhibition.setContent(request.getContent());
        exhibition.setCategory(Category.valueOf(request.getCategory()));

    }

    public S3Url getImageUploadUrl() {
        return s3UrlHandler.handle("exhibition/posters");
    }
}
