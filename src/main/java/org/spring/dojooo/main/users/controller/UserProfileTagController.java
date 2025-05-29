package org.spring.dojooo.main.users.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.dojooo.global.domain.Tag;
import org.spring.dojooo.global.response.ApiResponse;
import org.spring.dojooo.main.users.domain.ProfileTag;
import org.spring.dojooo.main.users.dto.ProfileTagRequest;
import org.spring.dojooo.main.users.dto.ProfileTagUpdateRequest;
import org.spring.dojooo.main.users.dto.TagDetails;
import org.spring.dojooo.main.users.dto.TagDetailsList;
import org.spring.dojooo.main.users.service.ProfileTagService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/profile/tags")
public class UserProfileTagController {
    private final ProfileTagService profileTagService;


    @Operation(
            summary = "사용자가 생성한 전체 태그 조회",
            description = "사용자가 생성한 기술 스택 태그를 조회합니다. 이 태그들은 프로필에 등록하여 기술을 소개할 수 있습니다."
    )
    @GetMapping("{userId}")
    public ResponseEntity<ApiResponse<TagDetailsList>> getProfileTags(@PathVariable Long userId, Authentication authentication) {
        TagDetailsList tagDetailsList = profileTagService.getProfileTag(userId, authentication);
        return ResponseEntity.ok(ApiResponse.ok(tagDetailsList));
    }

    @Operation(summary = "프로필에 등록할 테그 설정", description = "프로필에 스택과 같은 기술을 소개할수있는 태그를 생성합니다")
    @PostMapping("/save/{userId}")
    public ResponseEntity<ApiResponse<TagDetails>> saveTag(@PathVariable Long userId, @RequestBody ProfileTagRequest profileTagUpdateRequest ,Authentication authentication) {
        ProfileTag savedTag = profileTagService.saveTag(userId,profileTagUpdateRequest, authentication);
        TagDetails response = TagDetails.from(savedTag.getUser(), savedTag);
        return ResponseEntity.ok(ApiResponse.ok(response));

    }
    @Operation(summary = "설정한 테그 수정", description = "프로필에 등록하기위해 설정한 테그를 수정합니다")
    @PatchMapping("/edit/{userId}")
    public ResponseEntity<ApiResponse<TagDetails>>editTag(@PathVariable Long userId, @RequestBody ProfileTagUpdateRequest profileTagUpdateRequest, Authentication authentication) {
        ProfileTag updateTag = profileTagService.updateTag(userId, profileTagUpdateRequest, authentication);
        TagDetails response = TagDetails.from(updateTag.getUser(), updateTag);
        return ResponseEntity.ok(ApiResponse.ok(response));

    }

    @Operation(summary = "태그 삭제", description = "프로필에 등록하기 위해 설정한 태그를 삭제합니다")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse> deleteTag(
            @PathVariable Long userId,
            @RequestBody ProfileTagRequest profileTagRequest,
            Authentication authentication
    ) {
        String deletedTagName = profileTagService.deleteTag(userId, profileTagRequest, authentication);
        return ResponseEntity.ok(ApiResponse.of(200, "태그 삭제가 완료되었습니다", deletedTagName));
    }
}
