package pl.envelo.melo.domain.event;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.domain.attachment.AttachmentRepository;
import pl.envelo.melo.domain.attachment.AttachmentService;
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

//    public void update(Event event, NewEventDto newEventDto) {
//        updateName(event, newEventDto);
//        updateDescription(event, newEventDto);
//        updateDate(event, newEventDto);
//        updateUnitsAndInvitedMembers(event, newEventDto);
//        updatePeriodic(event, newEventDto);
//        updateHashtags(event, newEventDto);
//        updateMemberLimit(event, newEventDto);
//        updateOrganizer(event, newEventDto);
//        //updateAttachments(event, newEventDto);
//        updateCategory(event, newEventDto);
//        updateLocation(event, newEventDto);
//        //updateMainPhoto(event, newEventDto);
//    }

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
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSX");
        if (startTimeObject == null) {
            startTime = null;
        } else {
            try {
                startTime = LocalDateTime.parse(startTimeObject.toString().replace("T"," "), format);
            } catch (DateTimeException e) {
                return Optional.of(e);
            }

        }
        if (endTimeObject == null) {
            endTime = null;
        } else {
            try {
                endTime = LocalDateTime.parse(endTimeObject.toString().replace("T"," "), format);
            } catch (DateTimeException e) {
                return Optional.of(e);
            }
        }

        Map<String, String> errors = new HashMap<>();
        if (event.getStartTime().compareTo(LocalDateTime.now()) <= 0) {
            errors.put("forbidden error", "You cannot edit archived event");
        }
        if (startTime == null && endTime != null) {
            if (event.getEndTime().equals(endTime)) {
                errors.put("endTime error", "New EndTime is the same as old.");
            } else if (event.getStartTime().compareTo(endTime) >= 0) {
                errors.put("endTime error", "You must set endTime to be after startTime");
            } else {
                event.setEndTime(endTime);
            }
        } else if (startTime != null && endTime == null) {
            if (event.getStartTime().equals(startTime)) {
                errors.put("startTime error", "New StartTime is the same as old.");
            } else if (event.getEndTime().compareTo(startTime) <= 0) {
                errors.put("startTime error", "You must set startTime to be before endTime");
            } else {
                event.setStartTime(startTime);
            }
        } else if (startTime != null && endTime != null) {
            if (event.getEndTime().equals(endTime)) {
                errors.put("endTime error", "New EndTime is the same as old.");
            }
            if (event.getStartTime().equals(startTime)) {
                errors.put("startTime error", "New StartTime is the same as old.");
            }
            if (startTime.compareTo(endTime) >= 0) {
                errors.put("endTime error", "You must set endTime to be after startTime");
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
            if(event.getPeriodicType() == null) {
                if (event.getUnit() != null && periodicType!=PeriodicType.NONE) {
                    event.setPeriodicType(periodicType);
                    return true;
                } else if(periodicType==PeriodicType.NONE) {
                    event.setPeriodicType(periodicType);
                    return true;
                }
                else return false;
            }
            else if (event.getPeriodicType().equals(PeriodicType.NONE) ) {
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
                hash.setContent(String.valueOf(((Map<String, String>) o).get("content")));
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
    public boolean removeHashtags(Event event, Object objectHashtags) {
        try {
            ArrayList<Object> objectArrayList = (ArrayList<Object>) objectHashtags;
            Set<HashtagDto> hashtags = new HashSet<>();
            for (Object o : objectArrayList) {
                HashtagDto hash = new HashtagDto();
                hash.setContent(String.valueOf(((Map<String, String>) o).get("content")));
                hashtags.add(hash);
            }
            Set<String> currHashtags = event.getHashtags().stream().map(hashtagMapper::convertToString).collect(Collectors.toSet());
            for (HashtagDto e : hashtags) {
                if (currHashtags.contains(e.getContent())) {
                    event.getHashtags().remove(hashtagMapper.toEntity(e));
                    hashtagService.decrementHashtagGlobalCount(hashtagMapper.toEntity(e).getId());
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


   /* void updateAttachments(Event event, NewEventDto newEventDto) {
        if (newEventDto.getAttachments() == null) {
            if (event.getAttachments() != null || event.getAttachments().size() != 0) {
                event.getAttachments().clear();
            }
        }
        if (event.getAttachments() != null && newEventDto.getAttachments() != null) {
            Set<String> attachmentUrl = event.getAttachments().stream().map(Attachment::getAttachmentUrl).collect(Collectors.toSet());
            newEventDto.getAttachments().forEach(e -> {
                if (!attachmentUrl.contains(e.getAttachmentUrl())) {
                    Attachment attachment = attachmentMapper.toEntity(e);
                    event.getAttachments().add(attachmentRepository.save(attachment));
                }
            });
            attachmentUrl.forEach(e -> {
                Set<String> newAttachments = newEventDto.getAttachments().stream().map(AttachmentDto::getAttachmentUrl).collect(Collectors.toSet());
                if (!newAttachments.contains(e)) {
                    event.getAttachments().forEach(attachment -> {
                        if (attachment.getAttachmentUrl().equals(e)) {
                            event.getAttachments().remove(attachment);
                            attachmentRepository.deleteById(attachment.getId());
                        }
                    });
                }
            });
        }
    }*/

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


    /*void updateMainPhoto(Event event, NewEventDto newEventDto) {
        event.setMainPhoto(attachmentService.insertOrGetAttachment(newEventDto.getMainPhoto()));
    }*/
    public boolean updateTheme(Event event, Object newTheme) {
        Theme theme = Theme.valueOf(newTheme.toString());
        if (theme.equals(event.getTheme())) {
            return false;
        }
        event.setTheme(theme);
        return true;
    }

    @Transactional
    public boolean addInvitedMembers(Event event, Object listOfMembers) {
        try {
            ArrayList<Integer> invitedMembers = (ArrayList<Integer>) listOfMembers;
            Set<Integer> eventInvitedMembers = event.getInvited().stream().map(Employee::getId).collect(Collectors.toSet());
            for (Integer id : invitedMembers) {
                if (!eventInvitedMembers.contains(id)) {
                    event.getInvited().add(employeeRepository.getReferenceById(id));
                    //notification?
                }
            }
            return true;
        } catch (ClassCastException e) {
            return false;
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
        if (categoryId == event.getCategory().getId()) {
            event.setCategory(null);
            return true;
        }
        return false;
    }
}
