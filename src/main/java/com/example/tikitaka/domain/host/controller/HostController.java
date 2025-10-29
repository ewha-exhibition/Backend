package com.example.tikitaka.domain.host.controller;

import com.example.tikitaka.domain.exhibition.dto.ExhibitionCreate;
import com.example.tikitaka.domain.exhibition.dto.request.ExhibitionPostRequest;
import com.example.tikitaka.domain.exhibition.dto.response.ExhibitionDetailResponse;
import com.example.tikitaka.domain.host.service.HostService;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.infra.s3.S3Url;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hosts")
public class HostController {

    private final HostService hostService;

    // 1. 공동호스트 합류 : 초대코드 입력
    @PostMapping("/join")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void joinByCode(@AuthenticationPrincipal Long memberId,
                           @RequestParam String code) {
        hostService.joinByInviteCode(memberId, code.trim());
    }


}
