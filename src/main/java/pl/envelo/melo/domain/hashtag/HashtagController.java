package pl.envelo.melo.domain.hashtag;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/hashtags")
@CrossOrigin("${melo.cors-origin}")
public class HashtagController {

    private final HashtagService hashtagService;

    @PreAuthorize("hasAnyAuthority(@securityConfiguration.getAdminRole(), @securityConfiguration.getEmployeeRole())")
    @GetMapping()
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
    @GetMapping("/statistic")
    public ResponseEntity<?> showHashtagsStatistic() {
        return hashtagService.listHashtagStatistic();
    }
}
