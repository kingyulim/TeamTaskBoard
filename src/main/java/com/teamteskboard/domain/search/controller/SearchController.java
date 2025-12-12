package com.teamteskboard.domain.search.controller;

import com.teamteskboard.common.dto.ApiResponse;
import com.teamteskboard.domain.search.dto.SearchResponse;
import com.teamteskboard.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    /**
     * 검색어 검색 기능
     * @param query (검색 키워드)
     * @return 검색 키워드가 포함된 응답 DTO 반환
     */
    @GetMapping("/api/search")
    public ResponseEntity<ApiResponse<SearchResponse>> search(
            @RequestParam(required = false) String query
    ) {
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("검색어를 입력해주세요."));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("검색 성공", searchService.search(query)));
    }
}
