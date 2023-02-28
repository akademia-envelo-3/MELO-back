package pl.envelo.melo.domain.hashtag;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.envelo.melo.domain.request.dto.CategoryRequestDto;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/hashtags")
public class HashtagController {

    private final HashtagService hashtagService;

    @PreAuthorize("hasAnyAuthority(@securityConfiguration.getAdminRole(), @securityConfiguration.getEmployeeRole())")
    @Operation(summary = "Show list of all hashtags",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Show list of hashtags",
                            content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = HashtagDto.class))
                    }),
            })
    @GetMapping("")
    public ResponseEntity<List<HashtagDto>> showAllHashtags(Principal principal) {
        return hashtagService.listAllHashtag(principal);
    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getAdminRole())")
    @PatchMapping("/{id}/flag")
    @Operation(summary = "Change visibility Flag for hashtag",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Hashtag visibility was changed"),
                    @ApiResponse(responseCode = "404", description = "Hashtag dose not exist")
            })
    public ResponseEntity<?> setHashtagHideFlag(@PathVariable("id") int id, @RequestParam("hide") boolean hide) {
        return hashtagService.setHashtagHiddenFlag(id, hide);
    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getAdminRole())")
    @Operation(summary = "Show list of all hashtags with number of global usage",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Show list of hashtags",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = Hashtag.class))
                            }),
            })
    @GetMapping("/statistic")
    public ResponseEntity<?> showHashtagsStatistic() {
        return hashtagService.listHashtagStatistic();
    }
}
