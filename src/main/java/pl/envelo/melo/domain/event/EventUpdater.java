package pl.envelo.melo.domain.event;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.domain.attachment.Attachment;
import pl.envelo.melo.domain.attachment.AttachmentRepository;
import pl.envelo.melo.domain.attachment.AttachmentService;
import pl.envelo.melo.domain.attachment.AttachmentType;
import pl.envelo.melo.domain.category.CategoryRepository;
import pl.envelo.melo.domain.event.dto.NewEventDto;
import pl.envelo.melo.domain.hashtag.Hashtag;
import pl.envelo.melo.domain.hashtag.HashtagDto;
import pl.envelo.melo.domain.hashtag.HashtagRepository;
import pl.envelo.melo.domain.hashtag.HashtagService;
import pl.envelo.melo.domain.location.LocationService;
import pl.envelo.melo.domain.location.dto.LocationDto;
import pl.envelo.melo.domain.unit.UnitRepository;
import pl.envelo.melo.mappers.AttachmentMapper;
import pl.envelo.melo.mappers.HashtagMapper;
import pl.envelo.melo.mappers.LocationMapper;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static pl.envelo.melo.domain.event.EventConst.*;

@Component
@AllArgsConstructor
public class EventUpdater {
    HashtagService hashtagService;
    HashtagMapper hashtagMapper;
    HashtagRepository hashtagRepository;
    EmployeeRepository employeeRepository;
    UnitRepository unitRepository;

    AttachmentMapper attachmentMapper;
    LocationMapper locationMapper;
    CategoryRepository categoryRepository;
    AttachmentRepository attachmentRepository;
    LocationService locationService;
    AttachmentService attachmentService;


    public boolean updateName(Event event, String name) {
        if (event.getName().toLowerCase().trim().equals(name)) {
            return false;
        }
        event.setName(name);
        return true;
    }

    public boolean updateDescription(Event event, String description) {
        if (event.getDescription().equals(description.trim())) {
            return false;
        }
        event.setDescription(description.trim());
        return true;
    }

    public Optional<?> updateDate(Event event, Object startTimeObject, Object endTimeObject) {
        LocalDateTime startTime;
        LocalDateTime endTime;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (startTimeObject == null) {
            startTime = null;
        } else {
            try {
                startTime = LocalDateTime.parse(startTimeObject.toString().replace("T", " "), format);
            } catch (DateTimeException e) {
                return Optional.of(e);
            }

        }
        if (endTimeObject == null) {
            endTime = null;
        } else {
            try {
                endTime = LocalDateTime.parse(endTimeObject.toString().replace("T", " "), format);
            } catch (DateTimeException e) {
                return Optional.of(e);
            }
        }

        Map<String, String> errors = new HashMap<>();
        if (event.getStartTime().compareTo(LocalDateTime.now()) <= 0) {
            errors.put(FORBIDDEN_ACTION, ARCHIVED_EVENT_EDIT_ATTEMPT);
        }
        if (startTime == null && endTime != null) {
            if (endTime.compareTo(LocalDateTime.now()) <= 0) {
                errors.put(FORBIDDEN_ACTION, PAST_TIME);
            }
            if (event.getEndTime().equals(endTime)) {
                errors.put(INVALID_END_TIME, END_TIME_SAME_AS_OLD);
            } else if (event.getStartTime().compareTo(endTime) >= 0) {
                errors.put(INVALID_END_TIME, END_TIME_AFTER_START_TIME);
            } else {
                event.setEndTime(endTime);
            }
        } else if (startTime != null && endTime == null) {
            if (startTime.compareTo(LocalDateTime.now()) <= 0) {
                errors.put(FORBIDDEN_ACTION, PAST_TIME);
            }
            if (event.getStartTime().equals(startTime)) {
                errors.put(INVALID_START_TIME, START_TIME_SAME_AS_OLD);
            } else if (event.getEndTime().compareTo(startTime) <= 0) {
                errors.put(INVALID_START_TIME, START_TIME_BEFORE_END_TIME);
            } else {
                event.setStartTime(startTime);
            }
        } else if (startTime != null && endTime != null) {
            if (endTime.compareTo(LocalDateTime.now()) <= 0 || startTime.compareTo(LocalDateTime.now()) <= 0) {
                errors.put(FORBIDDEN_ACTION, PAST_TIME);
            }
            if (event.getEndTime().equals(endTime)) {
                errors.put(INVALID_END_TIME, END_TIME_SAME_AS_OLD);
            }
            if (event.getStartTime().equals(startTime)) {
                errors.put(INVALID_START_TIME, START_TIME_SAME_AS_OLD);
            }
            if (startTime.compareTo(endTime) >= 0) {
                errors.put(INVALID_END_TIME, END_TIME_AFTER_START_TIME);
            } else {
                event.setEndTime(endTime);
                event.setStartTime(startTime);
            }
        }
        if (errors.isEmpty()) {
            return Optional.of(true);
        } else return Optional.of(errors);
    }

    public boolean updatePeriodic(Event event, Object newPeriodicType) {
        PeriodicType periodicType = PeriodicType.valueOf(newPeriodicType.toString());
        if (event.getPeriodicType() != null && event.getPeriodicType().equals(periodicType)) {
            return false;
        } else {
            if (event.getPeriodicType() == null) {
                if (event.getUnit() != null && periodicType != PeriodicType.NONE) {
                    event.setPeriodicType(periodicType);
                    return true;
                } else if (periodicType == PeriodicType.NONE) {
                    event.setPeriodicType(periodicType);
                    return true;
                } else return false;
            } else if (event.getPeriodicType().equals(PeriodicType.NONE)) {
                if (event.getUnit() != null) {
                    event.setPeriodicType(periodicType);
                    //TODO wywołać metodę która "stworzy" cykilczność wydarzenia
                    return true;
                } else {
                    return false;
                }
            } else {
                event.setPeriodicType(periodicType);
                //TODO odpowiednio ustawić cykliczność sąsienich powiązanych eventów
                return true;
            }
        }
    }


    @Transactional
    public boolean addHashtags(Event event, Object objectHashtags) {
        Set<HashtagDto> hashtags;
        try {
            ArrayList<Object> objectArrayList = (ArrayList<Object>) objectHashtags;
            hashtags = new HashSet<>();
            for (Object o : objectArrayList) {
                HashtagDto hash = new HashtagDto();
                hash.setContent(String.valueOf(((Map<String, String>) o).get("content")).toLowerCase());
                hashtags.add(hash);
            }
            if (event.getHashtags() != null) {
                Set<String> currHashtags = event.getHashtags().stream().map(hashtagMapper::convertToString).collect(Collectors.toSet());
                for (HashtagDto e : hashtags) {
                    if (!currHashtags.contains(e.getContent())) {
                        HashtagDto hashtag = new HashtagDto();
                        hashtag.setContent(e.getContent());
                        Hashtag insertHash = hashtagService.insertNewHashtag(hashtag);
                        event.getHashtags().add(insertHash);
                    }
                }
            } else {
                for (HashtagDto e : hashtags) {
                    HashtagDto hashtag = new HashtagDto();
                    hashtag.setContent(e.getContent());
                    Hashtag insertHash = hashtagService.insertNewHashtag(hashtag);
                    event.getHashtags().add(insertHash);

                }
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Transactional
    public boolean removeHashtags(Event event, Object objectHashtags, Set<HashtagDto> hashtagsInNameAndDescription) {
        try {
            ArrayList<Object> objectArrayList = (ArrayList<Object>) objectHashtags;
            Set<HashtagDto> hashtags = new HashSet<>();
            for (Object o : objectArrayList) {
                HashtagDto hash = new HashtagDto();
                hash.setContent(String.valueOf(((Map<String, String>) o).get("content")).toLowerCase());
                hashtags.add(hash);
            }
            Set<String> currHashtags = event.getHashtags().stream().map(hashtagMapper::convertToString).collect(Collectors.toSet());
            for (HashtagDto e : hashtags) {
                if (currHashtags.contains(e.getContent())) {
                    Optional<Hashtag> hashtagOpt = hashtagRepository.findByContent(e.getContent());
                    if (hashtagOpt.isPresent() && !hashtagsInNameAndDescription.contains(hashtagMapper.toDto(hashtagOpt.get()))) {
                        Hashtag hashtag = hashtagOpt.get();
                        event.getHashtags().remove(hashtag);
                        if (!hashtagService.decrementHashtagGlobalCount(hashtag.getId())) {
                            return false;
                        }
                    }
                }
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        }

    }

    @Transactional
    public void updateHashtags(Event event, Set<HashtagDto> hashtagDtos, Set<HashtagDto> oldHashtagDtos) {
        if (oldHashtagDtos != null) {
            Set<String> currHashtags = oldHashtagDtos.stream().map(HashtagDto::getContent).collect(Collectors.toSet());
            Set<Hashtag> eventHashtags = new HashSet<>(event.getHashtags());
            hashtagDtos.forEach(e -> {
                if (!currHashtags.contains(e.getContent())) {
                    HashtagDto hashtag = new HashtagDto();
                    hashtag.setContent(e.getContent());
                    Hashtag insertHash = hashtagService.insertNewHashtag(hashtag);
                    event.getHashtags().add(insertHash);
                }
            });
            Set<String> currNewHashtags = hashtagDtos.stream().map(HashtagDto::getContent).collect(Collectors.toSet());
            eventHashtags.forEach(e -> {
                if (!currNewHashtags.contains(e.getContent())) {
                    event.getHashtags().remove(e);
                    hashtagService.decrementHashtagGlobalCount(e.getId());
                }
            });

        } else {
            hashtagDtos.forEach(e -> {
                HashtagDto hashtag = new HashtagDto();
                hashtag.setContent(e.getContent());
                Hashtag insertHash = hashtagService.insertNewHashtag(hashtag);
                event.getHashtags().add(insertHash);
            });
        }
    }

    public boolean updateMemberLimit(Event event, int newMemberLimit) {
        if (newMemberLimit > 1 && event.getMembers().size() <= newMemberLimit) {
            event.setMemberLimit((long) newMemberLimit);
            return true;
        } else
            return false;
    }

    public void updateOrganizer(Event event, NewEventDto newEventDto) {
        Employee organizer = employeeRepository.getReferenceById(newEventDto.getOrganizerId());
        event.setOrganizer(organizer);
        event.getMembers().add(organizer.getUser().getPerson());
    }

    public void updateUnitsAndInvitedMembers(Event event, NewEventDto newEventDto) {
        if (newEventDto.getInvitedMembers() != null) {
            for (Integer i : newEventDto.getInvitedMembers()) {
                event.getInvited().add(employeeRepository.getReferenceById(i));
            }
        }
        if (newEventDto.getUnitId() != null) {
            event.setUnit(unitRepository.getReferenceById(newEventDto.getUnitId()));

            for (Employee employee : event.getUnit().getMembers()) {
                event.getInvited().add(employee);
            }
        }
    }

    public boolean updateCategory(Event event, int categoryId) {
        if (categoryId != event.getCategory().getId())
            if (categoryRepository.existsById(categoryId))
                if (!categoryRepository.getReferenceById(categoryId).isHidden()) {
                    event.setCategory(categoryRepository.getReferenceById(categoryId));
                    return true;
                }
        return false;
    }


    public boolean updateLocation(Event event, Object newLocation) {
        Map<String, String> mapLocation = (Map<String, String>) newLocation;
        LocationDto location = new LocationDto();
        location.setStreetName(mapLocation.get("streetName"));
        location.setStreetNumber(mapLocation.get("streetNumber"));
        location.setApartmentNumber(mapLocation.get("apartmentNumber"));
        location.setPostalCode(mapLocation.get("postalCode"));
        location.setCity(mapLocation.get("city"));
        System.out.println(location.getCity());
        if (event.getLocation().equals(locationMapper.convert(location)))
            return false;
        else {
            event.setLocation(locationService.insertOrGetLocation(location));
            return true;
        }
    }

    public boolean updateTheme(Event event, Object newTheme) {
        Theme theme = Theme.valueOf(newTheme.toString());
        if (theme.equals(event.getTheme())) {
            return false;
        }
        event.setTheme(theme);
        return true;
    }

    @Transactional
    public Optional<?> addInvitedMembers(Event event, Object listOfMembers) {
        try {
            ArrayList<Integer> invitedMembers = (ArrayList<Integer>) listOfMembers;
            Set<Integer> eventInvitedMembers = event.getInvited().stream().map(Employee::getId).collect(Collectors.toSet());
            for (Integer id : invitedMembers) {
                if (employeeRepository.existsById(id)) {
                    if (!eventInvitedMembers.contains(id)) {
                        event.getInvited().add(employeeRepository.getReferenceById(id));
                        //TODO notification?
                    }
                }
                return Optional.of("Add to invited: Employee does not exist");
            }
            return Optional.of(true);
        } catch (ClassCastException e) {
            return Optional.of("Error: Employee cast exception");
        }
    }

    @Transactional
    public boolean removeInvitedMembers(Event event, Object listOfMembers) {
        try {

            ArrayList<Integer> invitedMembers = (ArrayList<Integer>) listOfMembers;
            Set<Integer> eventInvitedMembers = event.getInvited().stream().map(Employee::getId).collect(Collectors.toSet());
            for (Integer id : invitedMembers) {
                if (eventInvitedMembers.contains(id)) {
                    event.getInvited().remove(employeeRepository.getReferenceById(id));
                }
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public boolean removeCategory(Event event, int categoryId) {
        if (event.getCategory() == null) return false;
        if (categoryId == event.getCategory().getId()) {
            event.setCategory(null);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean removeAttachments(Event event, Object listAttachment) {
        try {
            ArrayList<String> attachments = (ArrayList<String>) listAttachment;
            Set<String> eventAttachments = event.getAttachments().stream().map(Attachment::getName).collect(Collectors.toSet());
            for (String i : attachments) {
                if (eventAttachments.contains(i)) {
                    event.getAttachments().remove(attachmentRepository.findByName(i));
                    attachmentRepository.delete(attachmentRepository.findByName(i));
                }
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Transactional
    public boolean addAttachments(Event event, MultipartFile[] additionalAttachments) {

        /// Wysyłam, przetwarzam kolejne załączniki i dodaję do eventu.
        for (MultipartFile multipartFile : additionalAttachments) {
            AttachmentType attachmentType = attachmentService.validateAttachmentType(multipartFile);
            if (Objects.isNull(attachmentType)) {
                return false;
            }
        }
        for (MultipartFile multipartFile : additionalAttachments) {
            Attachment attachmentFromServer = attachmentService.uploadFileAndSaveAsAttachment(multipartFile);
            if (attachmentFromServer == null) {
                return false;
            }
            if (Objects.isNull(event.getAttachments())) {
                event.setAttachments(new HashSet<>());
            }
            event.getAttachments().add(attachmentFromServer);
        }
        return true;
    }

    public boolean removeMainPhoto(Event event, Object mainPhoto) {
        if (event.getMainPhoto() == null)
            return false;
        try {
            String id = (String) mainPhoto;
            if (Objects.equals(event.getMainPhoto().getName(), id)) {
                event.setMainPhoto(null);
                attachmentRepository.delete(attachmentRepository.findByName(id));
                return true;
            }
            return false;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public boolean addMainPhoto(Event event, MultipartFile mainPhoto) {
        Attachment mainPhotoFromServer = attachmentService.uploadFileAndSaveAsAttachment(mainPhoto);
        if (mainPhotoFromServer == null) {
            return false;
        }
        if (mainPhotoFromServer.getAttachmentType() != AttachmentType.PHOTO) {
            return false;
        }
        event.setMainPhoto(mainPhotoFromServer);
        return true;
    }


}

