package pl.envelo.melo.domain.hashtag;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.AuthorizationService;
import pl.envelo.melo.authorization.admin.Admin;
import pl.envelo.melo.authorization.admin.AdminRepository;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.domain.category.Category;
import pl.envelo.melo.mappers.HashtagMapper;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HashtagService {
    private final HashtagRepository hashtagRepository;
    private final EmployeeRepository employeeRepository;
    private final AdminRepository adminRepository;
    private final HashtagMapper hashtagMapper;
    private final AuthorizationService authorizationService;

    public Hashtag insertNewHashtag(HashtagDto hashtagDto) {

        Hashtag hashtag = hashtagMapper.toEntity(hashtagDto);

        if (hashtagRepository.existsByContent(hashtag.getContent().toLowerCase())) {
            hashtag = hashtagRepository.findByContent(hashtag.getContent().toLowerCase()).get();
            incrementHashtagGlobalCount(hashtag.getId());
        } else {
            hashtag.setGlobalUsageCount(1);
            hashtag.setContent(hashtag.getContent().toLowerCase());
            hashtagRepository.save(hashtag);
        }
        return hashtag;
    }

    public ResponseEntity<?> incrementHashtagGlobalCount(int id) {

        if (hashtagRepository.existsById(id)) {
            Hashtag hashtag = hashtagRepository.getById(id);
            hashtag.setGlobalUsageCount(hashtag.getGlobalUsageCount() + 1);
            hashtagRepository.save(hashtag);
            return ResponseEntity.ok(hashtag);
        } else {
            return ResponseEntity.status(404).body("Hashtag by this ID do not exist");
        }
    }

    public ResponseEntity<Hashtag> decrementHashtagGlobalCount(int id) {
        return null;
    }

    public ResponseEntity<?> setHashtagHiddenFlag(int id, boolean hide) {
        Optional<Hashtag> hashtag = hashtagRepository.findById(id);
        if (hashtag.isPresent()){
            hashtag.get().setHidden(hide);
            hashtagRepository.save(hashtag.get());
            return ResponseEntity.ok(hashtag.get().isHidden());
        }
        return ResponseEntity.status(404).body("Hashtag with Id " + id + " does not exist");
    }

    public ResponseEntity<List<HashtagDto>> listAllHashtag(Principal principal) {
        authorizationService.inflateUser(principal);
        List<Hashtag> hashtagSet = null;
        if (Objects.nonNull(employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElse(null))) {
            hashtagSet = hashtagRepository.findAllByHidden(false);
        } else if (Objects.nonNull(adminRepository.findByUserId(authorizationService.getUUID(principal)).orElse(null))) {
            hashtagSet = hashtagRepository.findAll();
        }
        if (Objects.nonNull(hashtagSet)) {
            hashtagSet.sort(Comparator.comparingInt(Hashtag::getGlobalUsageCount).reversed());
            return ResponseEntity.ok(Objects.requireNonNull(hashtagSet).stream().map(hashtagMapper::toDto).collect(Collectors.toList()));
        }
        return ResponseEntity.ok(new ArrayList<>());
    }

    public ResponseEntity<?> listHashtagStatistic() {
        List<Hashtag> hashtagList = hashtagRepository.findAll();
        if (hashtagList != null) {
            Map<String, Integer> hashtagStatistic = new HashMap<>();

            for (Hashtag hashtag : hashtagList) {
                if (hashtag.getContent() == null) {
                    continue;
                } else {
                    hashtagStatistic.put(hashtag.getContent(), hashtag.getGlobalUsageCount());
                }
            }
            return ResponseEntity.ok(hashtagStatistic);
        } else {
            return ResponseEntity.status(404).body("There is no hashtags to display");
        }
    }
}
