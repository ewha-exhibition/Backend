package com.example.tikitaka.domain.exhibition.service;

import com.example.tikitaka.domain.club.entity.Club;
import com.example.tikitaka.domain.club.repository.ClubRepository;
import com.example.tikitaka.domain.exhibition.dto.CreateExhibitionRequest;
import com.example.tikitaka.domain.exhibition.entity.Category;
import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.exhibition.entity.Status;
import com.example.tikitaka.domain.exhibition.repository.ExhibitionRepository;
import com.example.tikitaka.domain.host.entity.Host;
import com.example.tikitaka.domain.host.repository.HostRepository;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExhibitionService {
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final ExhibitionRepository exhibitionRepository;
    private final HostRepository hostRepository;

    @Transactional
    public void addExhibition(CreateExhibitionRequest request) {
        // 멤버 찾기
        Member member = memberRepository.findByMemberId(request.getMemberId())
                .orElseThrow();

        // 클럽 찾기 (없으면 생성 있으면 참조)
        Club club = clubRepository.findByName(request.getClub())
                .orElseGet(() -> clubRepository.save(
                        Club.builder()
                                .name(request.getClub())
                                .build()
                ));

        // 랜덤 코드 생성
        String code = UUID.randomUUID().toString().substring(0, 8);


        // 전시 생성
        var exhibition = Exhibition.builder()
                .exhibitionName(request.getExhibitionName())
                .posterUrl(request.getPosterUrl())
                .place(request.getPlace())
                .startDate(request.getStartDate())
                .endDate(request.getStartDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .dateException(request.getDateException())
                .price(request.getPrice())
                .link(request.getLink())
                .content(request.getContent())
                .code(code)
                .scrapCount(0)
                .reviewCount(0)
                .commentCount(0)
                .viewCount(0)
                .club(club)
                .category(Category.valueOf(request.getCategory()))
                .status(Status.ACTIVE)
                .build();

        exhibitionRepository.save(exhibition);

        // host 등록
        Host host = Host.builder()
                .member(member)
                .exhibition(exhibition)
                .isRoot(true)
                .build();
        hostRepository.save(host);

    }
}
