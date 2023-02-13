package pl.envelo.melo.domain.event;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.domain.attachment.Attachment;
import pl.envelo.melo.domain.attachment.AttachmentRepository;
import pl.envelo.melo.domain.attachment.AttachmentService;
import pl.envelo.melo.domain.attachment.dto.AttachmentDto;
import pl.envelo.melo.domain.category.CategoryRepository;
import pl.envelo.melo.domain.event.dto.NewEventDto;
import pl.envelo.melo.domain.hashtag.Hashtag;
import pl.envelo.melo.domain.hashtag.HashtagDto;
import pl.envelo.melo.domain.hashtag.HashtagRepository;
import pl.envelo.melo.domain.hashtag.HashtagService;
import pl.envelo.melo.domain.location.LocationService;
import pl.envelo.melo.domain.unit.Unit;
import pl.envelo.melo.domain.unit.UnitRepository;
import pl.envelo.melo.mappers.AttachmentMapper;
import pl.envelo.melo.mappers.HashtagMapper;
import pl.envelo.melo.mappers.LocationMapper;

import java.time.LocalDateTime;
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
    CategoryRepository categoryRepository;
    AttachmentRepository attachmentRepository;
    LocationService locationService;
    AttachmentService attachmentService;

    public void update(Event event, NewEventDto newEventDto) {
        updateName(event, newEventDto);
        updateDescription(event, newEventDto);
        updateDate(event, newEventDto);
        updateUnitsAndInvitedMembers(event, newEventDto);
        updatePeriodic(event, newEventDto);
        updateHashtags(event, newEventDto);
        updateMemberLimit(event, newEventDto);
        updateOrganizer(event, newEventDto);
        //updateAttachments(event, newEventDto);
        updateCategory(event, newEventDto);
        updateLocation(event, newEventDto);
        //updateMainPhoto(event, newEventDto);
    }

    public boolean updateName(Event event, NewEventDto newEventDto) {
        if (event.getName().toLowerCase().trim().equals(newEventDto.getName().toLowerCase().trim())) {
            return false;
        }
        event.setName(newEventDto.getName());
        return true;
    }

    public boolean updateDescription(Event event, NewEventDto newEventDto) {
        if (event.getDescription().equals(newEventDto.getDescription())) {//TODO Trimowanie?
            return false;
        }
        event.setDescription(newEventDto.getDescription());
        return true;
    }

    public Optional<?> updateDate(Event event, NewEventDto newEventDto) {
        Map<String, String> errors = new HashMap<>();
        if (event.getStartTime().compareTo(LocalDateTime.now()) <= 0) {
            errors.put("forbidden error", "You cannot edit archived event");
        }
        if (newEventDto.getStartTime() == null && newEventDto.getEndTime() != null) {
            if (event.getEndTime().equals(newEventDto.getEndTime())) {
                errors.put("endTime error", "New EndTime is the same as old.");
            } else if (event.getStartTime().compareTo(newEventDto.getEndTime()) >= 0) {
                errors.put("endTime error", "You must set endTime to be after startTime");
            } else {
                event.setEndTime(newEventDto.getEndTime());
            }
        } else if (newEventDto.getStartTime() != null && newEventDto.getEndTime() == null) {
            if (event.getStartTime().equals(newEventDto.getStartTime())) {
                errors.put("startTime error", "New StartTime is the same as old.");
            } else if (event.getEndTime().compareTo(newEventDto.getStartTime()) <= 0) {
                errors.put("startTime error", "You must set startTime to be before endTime");
            } else {
                event.setStartTime(newEventDto.getStartTime());
            }
        } else if (newEventDto.getStartTime() != null && newEventDto.getEndTime() != null) {
            if (event.getEndTime().equals(newEventDto.getEndTime())) {
                errors.put("endTime error", "New EndTime is the same as old.");
            }
            if (event.getStartTime().equals(newEventDto.getStartTime())) {
                errors.put("startTime error", "New StartTime is the same as old.");
            }
            if (newEventDto.getStartTime().compareTo(newEventDto.getEndTime()) >= 0) {
                errors.put("endTime error", "You must set endTime to be after startTime");
            } else {
                event.setEndTime(newEventDto.getEndTime());
                event.setStartTime(newEventDto.getStartTime());
            }
        }
        if (errors.isEmpty()) {
            return Optional.of(true);
        } else return Optional.of(errors);
    }

    public void updatePeriodic(Event event, NewEventDto newEventDto) {
        event.setPeriodicType(newEventDto.getPeriodicType());
    }

    @Transactional
    public void updateHashtags(Event event, NewEventDto newEventDto) {
        if (event.getHashtags() != null) {
            Set<String> currHashtags = event.getHashtags().stream().map(hashtagMapper::convertToString).collect(Collectors.toSet());
            Set<Hashtag> eventHashtags = new HashSet<>(event.getHashtags());
            newEventDto.getHashtags().forEach(e -> {
                if (!currHashtags.contains(e.getContent())) {
                    HashtagDto hashtag = new HashtagDto();
                    hashtag.setContent(e.getContent());
                    Hashtag insertHash = hashtagService.insertNewHashtag(hashtag);
                    event.getHashtags().add(insertHash);
                }
            });
            Set<String> currNewHashtags = newEventDto.getHashtags().stream().map(HashtagDto::getContent).collect(Collectors.toSet());
            eventHashtags.forEach(e -> {
                if (!currNewHashtags.contains(e.getContent())) {
                    event.getHashtags().remove(e);
                    hashtagService.decrementHashtagGlobalCount(e.getId());
                }
            });

        } else {
            newEventDto.getHashtags().forEach(e -> {
                HashtagDto hashtag = new HashtagDto();
                hashtag.setContent(e.getContent());
                Hashtag insertHash = hashtagService.insertNewHashtag(hashtag);
                event.getHashtags().add(insertHash);
            });
        }
    }

    public boolean updateMemberLimit(Event event, NewEventDto newEventDto) {
        if (newEventDto.getMemberLimit() > 1 && event.getMembers().size() <= newEventDto.getMemberLimit()) {
            event.setMemberLimit((long) newEventDto.getMemberLimit());
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

    public boolean updateCategory(Event event, NewEventDto newEventDto) {
        if (!newEventDto.getCategoryId().equals(event.getCategory().getId()))
            if (categoryRepository.existsById(newEventDto.getCategoryId()))
                if (!categoryRepository.getReferenceById(newEventDto.getCategoryId()).isHidden()) {
                    event.setCategory(categoryRepository.getReferenceById(newEventDto.getCategoryId()));
                    return true;
                }
        return false;
    }


    public void updateLocation(Event event, NewEventDto newEventDto) {
        if (newEventDto.getLocation() != null)
            event.setLocation(locationService.insertOrGetLocation(newEventDto.getLocation()));
        else
            event.setLocation(null);
    }


    /*void updateMainPhoto(Event event, NewEventDto newEventDto) {
        event.setMainPhoto(attachmentService.insertOrGetAttachment(newEventDto.getMainPhoto()));
    }*/
    public boolean updateTheme(Event event, NewEventDto newEventDto) {
        if (newEventDto.getTheme().equals(event.getTheme())) {
            return false;
        }
        event.setTheme(newEventDto.getTheme());
        return true;
    }
}
