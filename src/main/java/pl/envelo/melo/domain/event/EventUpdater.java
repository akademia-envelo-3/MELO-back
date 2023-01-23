package pl.envelo.melo.domain.event;

import lombok.AllArgsConstructor;
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

import java.util.Optional;
import java.util.Set;
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

    void updateDate(Event event, NewEventDto newEventDto) {
        event.setStartTime(newEventDto.getStartTime());
        event.setEndTime(newEventDto.getEndTime());
    }

    void updatePeriodic(Event event, NewEventDto newEventDto) {
        event.setPeriodicType(newEventDto.getPeriodicType());
    }

    void updateHashtags(Event event, NewEventDto newEventDto) {
        if (event.getHashtags() == null) {
            if (newEventDto.getHashtags() != null) {
                Set<String> currHashtags = event.getHashtags().stream().map(hashtagMapper::convertToString).collect(Collectors.toSet());
                newEventDto.getHashtags().forEach(e -> {
                    if (!currHashtags.contains(e)) {
                        Optional<Hashtag> hashtag = hashtagRepository.findByContent(e.getContent());
                        if (hashtag.isPresent()) {
                            hashtagService.incrementHashtagGlobalCount(hashtag.get().getId());
                            event.getHashtags().add(hashtag.get());
                        } else {
                            HashtagDto hashtagDto = new HashtagDto();
                            hashtagDto.setContent(e.getContent());
                            hashtagService.insertNewHashtag(hashtagDto);
                            event.getHashtags().add(hashtagRepository.findByContent(e.getContent()).get());
                        }
                    }
                });
                event.getHashtags().forEach(e -> {
                    if (!newEventDto.getHashtags().contains(e.getContent())) {
                        event.getHashtags().remove(e);
                        hashtagService.decrementHashtagGlobalCount(e.getId());
                    }
                });
            }
        } else {
            if (newEventDto.getHashtags() != null) {
                newEventDto.getHashtags().forEach(h -> {
                    Optional<Hashtag> hashtag = hashtagRepository.findByContent(h.getContent());
                    if (hashtag.isPresent()) {
                        hashtagService.incrementHashtagGlobalCount(hashtag.get().getId());
                        event.getHashtags().add(hashtag.get());
                    } else {
                        HashtagDto hashtagDto = new HashtagDto();
                        hashtagDto.setContent(h.getContent());
                        hashtagService.insertNewHashtag(hashtagDto);
                        event.getHashtags().add(hashtagRepository.findByContent(h.getContent()).get());
                    }
                });
            }
        }
    }

    void updateOrganizer(Event event, NewEventDto newEventDto) {
        Employee organizer = employeeRepository.getReferenceById(newEventDto.getOrganizerId());
        event.setOrganizer(organizer);
        event.getMembers().add(organizer.getUser().getPerson());
    }

    void updateUnitsAndInvitedMembers(Event event, NewEventDto newEventDto) {
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

    void updateCategory(Event event, NewEventDto newEventDto) {
        if (newEventDto.getCategoryId() != null) {
            if (categoryRepository.existsById(newEventDto.getCategoryId()))
                if (!categoryRepository.getReferenceById(newEventDto.getCategoryId()).isHidden())
                    event.setCategory(categoryRepository.getReferenceById(newEventDto.getCategoryId()));
                else
                    event.setCategory(null);
        } else
            event.setCategory(null);
    }

    void updateName(Event event, NewEventDto newEventDto) {
        event.setName(newEventDto.getName());
    }

    void updateDescription(Event event, NewEventDto newEventDto) {
        event.setDescription(newEventDto.getDescription());
    }

    void updateLocation(Event event, NewEventDto newEventDto) {
        event.setLocation(locationService.insertOrGetLocation(newEventDto.getLocation()));
    }

    void updateMemberLimit(Event event, NewEventDto newEventDto) {
        event.setMemberLimit((long) newEventDto.getMemberLimit());
    }

    /*void updateMainPhoto(Event event, NewEventDto newEventDto) {
        event.setMainPhoto(attachmentService.insertOrGetAttachment(newEventDto.getMainPhoto()));
    }*/
}
