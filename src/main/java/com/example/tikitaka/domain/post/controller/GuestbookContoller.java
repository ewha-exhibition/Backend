package com.example.tikitaka.domain.post.controller;

import com.example.tikitaka.domain.post.dto.response.GuestBookResponse;
import com.example.tikitaka.domain.post.service.PreviewService;
import com.example.tikitaka.domain.post.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/guestbooks")
public class GuestbookContoller {
    private final ReviewService reviewService;
    private final PreviewService previewService;

    @GetMapping("/reviews")
    public GuestBookResponse reviewsList(
            @RequestParam int pageNum,
            @RequestParam int limit
    )
    {
        return reviewService.getGuestbooks(pageNum, limit);
    }

}
