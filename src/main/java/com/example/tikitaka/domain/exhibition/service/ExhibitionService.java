package com.example.tikitaka.domain.exhibition.service;

import com.example.tikitaka.domain.club.entity.Club;
import com.example.tikitaka.domain.club.service.ClubService;
import com.example.tikitaka.domain.exhibition.dto.ExhibitionCreate;
import com.example.tikitaka.domain.exhibition.dto.request.ExhibitionPatchRequest;
import com.example.tikitaka.domain.exhibition.dto.request.ExhibitionPostRequest;
import com.example.tikitaka.domain.exhibition.dto.response.ExhibitionDetailResponse;
import com.example.tikitaka.domain.exhibition.entity.Category;
import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.exhibition.entity.ExhibitionImage;
import com.example.tikitaka.domain.exhibition.mapper.ExhibitionMapper;
import com.example.tikitaka.domain.exhibition.repository.ExhibitionImageRepository;
import com.example.tikitaka.domain.exhibition.repository.ExhibitionRepository;
import com.example.tikitaka.domain.exhibition.util.HostCodeGenerator;
import com.example.tikitaka.domain.exhibition.validator.ExhibitionValidator;
import com.example.tikitaka.domain.host.dto.HostCreate;
import com.example.tikitaka.domain.host.service.HostService;
import com.example.tikitaka.domain.host.validator.HostValidator;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.validator.MemberValidator;
import com.example.tikitaka.domain.scrap.validator.ScrapValidator;
import com.example.tikitaka.infra.s3.S3Url;
import com.example.tikitaka.infra.s3.S3UrlHandler;
import lombok.RequiredArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Consumer;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExhibitionService {
    public static final String EXHIBITION_POSTER = "https://greenknockbucket.s3.ap-northeast-2.amazonaws.com/exhibition/posters/default_image.png";

    // 서비스
    private final ClubService clubService;
    private final HostService hostService;

    // 레포지토리
    private final ExhibitionRepository exhibitionRepository;
    private final ExhibitionImageRepository exhibitionImageRepository;

    // validator
    private final MemberValidator memberValidator;
    private final ExhibitionValidator exhibitionValidator;
    private final ScrapValidator scrapValidator;
    private final HostValidator hostValidator;

    // Mapper
    private final ExhibitionMapper exhibitionMapper;

    // S3
    private final S3UrlHandler s3UrlHandler;

    // generaor
    private final HostCodeGenerator hostCodeGenerator;
    private final ExhibitionImageService exhibitionImageService;

    @Transactional
    public void addExhibition(Long memberId, ExhibitionPostRequest request) {
        Member member = memberValidator.validateMember(memberId);

        // 클럽 찾기 (없으면 생성 있으면 참조)
        Club club = clubService.clubGetOrAdd(request.getClub().getName());

        // 전시 생성
        String posterUrl = request.getExhibition().getPosterUrl();
        if (posterUrl == null) {
            posterUrl = EXHIBITION_POSTER;
        }

        Exhibition exhibition = exhibitionMapper.toExhibition(request.getExhibition(), club, s3UrlHandler.extractKeyFromUrl(posterUrl));
        exhibitionRepository.save(exhibition);

        // 전시 이미지 등록
        if (!request.getImages().isEmpty()) {
            List<ExhibitionImage> images = exhibitionMapper.toExhibitionImages(exhibition, request.getImages());
            exhibitionImageRepository.saveAll(images);
        }

        // 초대 코드 생성(8자리) -> 코드 중복 해결
        String code;
        do {
            code = hostCodeGenerator.generator();
        } while (exhibitionRepository.findByCode(code).isPresent());
        exhibition.setCode(code);

        // host 등록
        hostService.hostAdd(HostCreate.of(
                member,
                exhibition,
                true));


    }

    public ExhibitionDetailResponse findExhibition(Long memberId, Long exhibitionId) {
        Exhibition exhibition = exhibitionValidator.validateExhibition(exhibitionId);
        List<ExhibitionImage> images = exhibitionImageRepository.findByExhibitionIdOrderBySequenceAsc(exhibitionId);

        boolean isHost = false;
        boolean isScrap = false;

        if (memberId != null) {
            Member member = memberValidator.validateMember(memberId);
            isHost = hostValidator.validateRole(member, exhibition);
            isScrap = scrapValidator.existsByMemberAndExhibition(member, exhibition);
        }

        return exhibitionMapper.toDetailResponse(isHost, isScrap, exhibition, images);

    }

    @Transactional
    public void deleteExhibition(Long memberId, Long exhibitionId) {
        Member member = memberValidator.validateMember(memberId);
        Exhibition exhibition = exhibitionValidator.validateExhibition(exhibitionId);

        hostValidator.validateHostOrThrow(member, exhibition);
        exhibition.markAsDeleted();
        exhibitionRepository.save(exhibition);
    }

    @Transactional
    public void updateExhibition(Long memberId, Long exhibitionId, ExhibitionPatchRequest request) {
        Member member = memberValidator.validateMember(memberId);
        Exhibition exhibition = exhibitionValidator.validateExhibition(exhibitionId);


        hostValidator.validateRole(member, exhibition);

        updateIfPresent(request.getExhibitionName(), exhibition::setExhibitionName);
        updateIfPresent(request.getPosterUrl(), url -> exhibition.setPosterUrl(s3UrlHandler.extractKeyFromUrl(url)));
        updateIfPresent(request.getPlace(), exhibition::setPlace);
        updateIfPresent(request.getStartDate(), exhibition::setStartDate);
        updateIfPresent(request.getEndDate(), exhibition::setEndDate);
        updateIfPresent(request.getStartTime(), exhibition::setStartTime);
        updateIfPresent(request.getEndTime(), exhibition::setEndTime);
        updateIfPresent(request.getDateException(), exhibition::setDateException);
        updateIfPresent(request.getPrice(), exhibition::setPrice);
        updateIfPresent(request.getLink(), exhibition::setLink);
        updateIfPresent(request.getContent(), exhibition::setContent);

        if (request.getCategory().isPresent()) {
            Category category = Category.valueOf(request.getCategory().get());
            exhibition.setCategory(category);
        }

        if (request.getClubName().isPresent()) {
            Club club = clubService.clubGetOrAdd(request.getClubName().get());
            exhibition.setClub(club);
        }

        if (request.getImages().isPresent()) {
            exhibitionImageService.updateExhibitionImage(exhibition, request.getImages().get());
        }


    }

    public S3Url getImageUploadUrl() {
        return s3UrlHandler.handle("exhibition/posters");
    }

    private <T> void updateIfPresent(JsonNullable<T> nullable, Consumer<T> setter) {
        if (nullable != null && nullable.isPresent()) {
            setter.accept(nullable.get());
        }
    }
}
