package pl.envelo.melo.domain.hashtag;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/hashtags")
public class HashtagController {

    private final HashtagService hashtagService;

    @PreAuthorize("hasAnyAuthority(@securityConfiguration.getAdminRole(), @securityConfiguration.getEmployeeRole())")
    @GetMapping()
    public ResponseEntity<List<HashtagDto>> showAllHashtags(Principal principal) {
        return hashtagService.listAllHashtag(principal);
    }

    public ResponseEntity<Hashtag> hideHashtag(int id) {
        return null;
    }

    public ResponseEntity<Hashtag> unhideHashtag(int id) {
        return null;
    }
    @PreAuthorize("hasAuthority(@securityConfiguration.getAdminRole())")
    @GetMapping("/statistic")
    public ResponseEntity<?> showHashtagsStatistic(){
        return hashtagService.listHashtagStatistic();
    }
}
