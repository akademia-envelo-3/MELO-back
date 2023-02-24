package pl.envelo.melo.domain.event;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.envelo.melo.authorization.AuthConst;
import pl.envelo.melo.authorization.AuthSucceded;
import pl.envelo.melo.authorization.AuthorizationService;
import pl.envelo.melo.authorization.MailConst;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.employee.EmployeeService;
import pl.envelo.melo.authorization.employee.dto.EmployeeDto;
import pl.envelo.melo.authorization.mailtoken.MailService;
import pl.envelo.melo.authorization.mailtoken.MailToken;
import pl.envelo.melo.authorization.mailtoken.MailTokenRepository;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.authorization.person.PersonRepository;
import pl.envelo.melo.authorization.person.dto.AddGuestToEventDto;
import pl.envelo.melo.domain.attachment.*;
import pl.envelo.melo.domain.category.CategoryConst;
import pl.envelo.melo.domain.attachment.Attachment;
import pl.envelo.melo.domain.attachment.AttachmentRepository;
import pl.envelo.melo.domain.attachment.AttachmentService;
import pl.envelo.melo.domain.attachment.AttachmentType;
import pl.envelo.melo.domain.category.Category;
import pl.envelo.melo.domain.category.CategoryRepository;
import pl.envelo.melo.domain.comment.CommentRepository;
import pl.envelo.melo.domain.event.dto.EventDetailsDto;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.domain.event.dto.NewEventDto;
import pl.envelo.melo.domain.event.utils.PagingHeaders;
import pl.envelo.melo.domain.event.utils.PagingResponse;
import pl.envelo.melo.domain.hashtag.Hashtag;
import pl.envelo.melo.domain.hashtag.HashtagDto;
import pl.envelo.melo.domain.hashtag.HashtagRepository;
import pl.envelo.melo.domain.hashtag.HashtagService;
import pl.envelo.melo.domain.location.LocationRepository;
import pl.envelo.melo.domain.location.LocationService;
import pl.envelo.melo.domain.notification.Notification;
import pl.envelo.melo.domain.notification.NotificationRepository;
import pl.envelo.melo.domain.notification.NotificationService;
import pl.envelo.melo.domain.notification.NotificationType;
import pl.envelo.melo.domain.notification.dto.EventNotificationDto;
import pl.envelo.melo.domain.poll.PollAnswerRepository;
import pl.envelo.melo.domain.poll.PollRepository;
import pl.envelo.melo.domain.poll.PollService;
import pl.envelo.melo.domain.poll.dto.PollToDisplayOnListDto;
import pl.envelo.melo.domain.unit.UnitConst;
import pl.envelo.melo.domain.unit.UnitRepository;
import pl.envelo.melo.domain.unit.Unit;
import pl.envelo.melo.exceptions.EmployeeNotFoundException;
import pl.envelo.melo.exceptions.EventNotFoundException;
import pl.envelo.melo.mappers.*;
import pl.envelo.melo.validators.EventValidator;
import pl.envelo.melo.validators.HashtagValidator;

import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventService {
    //Services
    private final HashtagService hashtagService;
    private final NotificationService notificationService;
    private final EmployeeService employeeService;
    private final LocationService locationService;
    private final PollService pollService;
    private AttachmentService attachmentService;
    private MailService mailService;
    //Repositories
    private final EventRepository eventRepository;
    private final EmployeeRepository employeeRepository;
    private final HashtagRepository hashtagRepository;
    private final CategoryRepository categoryRepository;
    private final AttachmentRepository attachmentRepository;
    private final LocationRepository locationRepository;
    private final UnitRepository unitRepository;
    private final PollRepository pollRepository;
    private final PollAnswerRepository pollAnswerRepository;
    private final CommentRepository commentRepository;
    private final PersonRepository personRepository;
    private final MailTokenRepository mailTokenRepository;
    private final NotificationRepository notificationRepository;
    //Mappers
    private EventDetailsMapper eventDetailsMapper;
    private final PollToDisplayOnListDtoMapper pollToDisplayOnListDtoMapper;
    private EventMapper eventMapper;
    private HashtagMapper hashtagMapper;
    private EventEditMapper eventEditMapper;
    private AttachmentMapper attachmentMapper;
    private EmployeeMapper employeeMapper;
    private AddGuestToEventMapper addGuestToEventMapper;
    //Other
    private EventValidator eventValidator;
    private HashtagValidator hashtagValidator;
    private EditEventNotificationHandler eventNotificationHandler;
    private EventUpdater eventUpdater;
    private AuthorizationService authorizationService;

    public ResponseEntity<?> getEvent(int id, Principal principal) {
        authorizationService.inflateUser(principal);
        Employee employee = employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElse(null);
        if (eventRepository.existsById(id)) {
            Event event = eventRepository.findById(id).get();
            EventDetailsDto eventDetailsDto = eventDetailsMapper.convert(event);
            if (Objects.nonNull(employee)) {
                if (eventDetailsDto.getPolls() != null) {
                    Set<PollToDisplayOnListDto> pollSet = new HashSet<>();
                    pollService.listAllPollsForEvent(id).getBody().stream()
                            .map(poll -> {
                                PollToDisplayOnListDto dto = pollToDisplayOnListDtoMapper.convert(poll);
                                dto.setFilled(pollService.employeeOnLists(poll, employee.getId()));
                                return dto;
                            })
                            .forEach(pollSet::add);
                    eventDetailsDto.setPolls(pollSet);
                }
            }
            return ResponseEntity.ok(eventDetailsDto);
        } else {
            return ResponseEntity.status(404).body(EventConst.EVENT_NOT_FOUND);
        }
    }

    ////////
    public PagingResponse get(Specification<Event> spec, HttpHeaders headers, Sort sort) {
        if (isRequestPaged(headers)) {
            return get(spec, buildPageRequest(headers, sort));
        } else {
            //List<Event> entities = get(spec, sort);
            List<Event> entities = get(spec, sort);

            /// Mapowanie na DTO.
            List<EventToDisplayOnListDto> entitiesMapped = entities.stream().map(eventMapper::convert).collect(Collectors.toList());
            return new PagingResponse((long) entitiesMapped.size(), 0L, 0L, 0L, 0L, entitiesMapped);
        }
    }

    private boolean isRequestPaged(HttpHeaders headers) {
        return headers.containsKey(PagingHeaders.PAGE_NUMBER.getName()) && headers.containsKey(PagingHeaders.PAGE_SIZE.getName());
    }

    private Pageable buildPageRequest(HttpHeaders headers, Sort sort) {
        int page = Integer.parseInt(Objects.requireNonNull(headers.get(PagingHeaders.PAGE_NUMBER.getName())).get(0));
        int size = Integer.parseInt(Objects.requireNonNull(headers.get(PagingHeaders.PAGE_SIZE.getName())).get(0));
        return PageRequest.of(page, size, sort);
    }

    /**
     * get elements using Criteria.
     *
     * @param spec     *
     * @param pageable pagination data
     * @return retrieve elements with pagination
     */
    public PagingResponse get(Specification<Event> spec, Pageable pageable) {
        Page<Event> page = eventRepository.findAll(spec, pageable);

        List<Event> content = page.getContent();

        ///Mapowanie na Dto!
        List<EventToDisplayOnListDto> contentMapped = content.stream().map(eventMapper::convert).collect(Collectors.toList());
        return new PagingResponse(page.getTotalElements(), (long) page.getNumber(), (long) page.getNumberOfElements(), pageable.getOffset(), (long) page.getTotalPages(), contentMapped);
    }

    /**
     * get elements using Criteria.
     *
     * @param spec *
     * @return elements
     */
    public List<Event> get(Specification<Event> spec, Sort sort) {
        return eventRepository.findAll(spec, sort);
    }

    public ResponseEntity<List<EventToDisplayOnListDto>> listAllEvents() {
        List<Event> result = eventRepository.findAllByStartTimeAfterAndType(LocalDateTime.now(), EventType.LIMITED_EXTERNAL);
        result.addAll(eventRepository.findAllByStartTimeAfterAndType(LocalDateTime.now(), EventType.UNLIMITED_EXTERNAL));
        result.addAll(eventRepository.findAllByStartTimeAfterAndType(LocalDateTime.now(), EventType.UNLIMITED_PUBLIC_INTERNAL));
        result.addAll(eventRepository.findAllByStartTimeAfterAndType(LocalDateTime.now(), EventType.LIMITED_PUBLIC_INTERNAL));
        result.sort(Comparator.comparing(Event::getStartTime));
        return ResponseEntity.ok(result.stream().map(eventMapper::convert).toList());
    }

    @Transactional
    public ResponseEntity<?> insertNewEvent(NewEventDto newEventDto, MultipartFile mainPhoto, MultipartFile[] additionalAttachments, Principal principal) {
        authorizationService.inflateUser(principal);
        Event event = eventMapper.newEvent(newEventDto);
        Employee employee = employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElseThrow(EmployeeNotFoundException::new);
        Optional<Category> category = Optional.empty();

        if (newEventDto.getCategoryId() != null) {
            category = categoryRepository.findById(newEventDto.getCategoryId());
        } else {
            event.setCategory(null);
        }

        if (category.isPresent() && !category.get().isHidden()) {
            event.setCategory(category.get());
        } else if (category.isPresent() && category.get().isHidden()) {
            return ResponseEntity.status(404).body(CategoryConst.CATEGORY_NOT_AVAILABLE_ANYMORE);
        } else if (!category.isPresent() && newEventDto.getCategoryId() != null) {
            return ResponseEntity.status(404).body(CategoryConst.CATEGORY_NOT_FOUND);
        }

        Map<String, String> validationResult = eventValidator.validateToCreateEvent(newEventDto);

        if (validationResult.size() != 0) {
            return ResponseEntity.badRequest().body(validationResult);
        }

        if (newEventDto.getLocation() != null) {
            event.setLocation(locationService.insertOrGetLocation(newEventDto.getLocation()));
        }

        event.setOrganizer(employee);
        Set<Person> members = new HashSet<>();
        members.add(employee.getUser().getPerson());
        event.setMembers(members);
        event.setStartTime(newEventDto.getStartTime());
        event.setEndTime(newEventDto.getEndTime());
        event.setType(newEventDto.getEventType());
        if (event.getType().name().startsWith("LIMITED"))
            event.setMemberLimit(newEventDto.getMemberLimit());
        else
            event.setMemberLimit(null);
        event.setPeriodicType(newEventDto.getPeriodicType());


        if (!Objects.isNull(additionalAttachments)) {
            /// Wysyłam, przetwarzam kolejne załączniki i dodaję do eventu.
            for (MultipartFile multipartFile : additionalAttachments) {
                AttachmentType attachmentType = attachmentService.validateAttachmentType(multipartFile);
                if (Objects.isNull(attachmentType)) {
                    return ResponseEntity.badRequest()
                            .body(AttachmentConst.INVALID_FORMAT);
                }
            }

            for (MultipartFile multipartFile : additionalAttachments) {
                Attachment attachmentFromServer = attachmentService.uploadFileAndSaveAsAttachment(multipartFile);
                if (attachmentFromServer == null) {
                    return ResponseEntity.badRequest()
                            .body(AttachmentConst.INVALID_FORMAT);
                }
                if (Objects.isNull(event.getAttachments())) {
                    event.setAttachments(new HashSet<>());
                }
                event.getAttachments().add(attachmentFromServer);
            }
        }
        /// Set Main Photo
        if (!Objects.isNull(mainPhoto)) {

            Attachment mainPhotoFromServer = attachmentService.uploadFileAndSaveAsAttachment(mainPhoto);
            if (mainPhotoFromServer == null) {
                return ResponseEntity.badRequest()
                        .body(AttachmentConst.INVALID_FORMAT);
            }
            if (mainPhotoFromServer.getAttachmentType() != AttachmentType.PHOTO) {
                return ResponseEntity.badRequest()
                        .body(AttachmentConst.INVALID_PHOTO_FORMAT);
            }
            event.setMainPhoto(mainPhotoFromServer);

        } else {
            event.setMainPhoto(null); //todo swap with attachmentMainPhoto method
        }

        Set<Hashtag> hashtags = new HashSet<>();

        Set<HashtagDto> hashtagDtoFromTitleAndDescription = findHashtagFromEvent(newEventDto.getName(), newEventDto.getDescription());
        if (newEventDto.getHashtags() != null) {
            Map<String, String> validationIsHidden = hashtagValidator.validateIsHidden(newEventDto.getHashtags());
            if (validationIsHidden.size() != 0) {
                return ResponseEntity.badRequest().body(validationIsHidden);
            }
            hashtagDtoFromTitleAndDescription.addAll(newEventDto.getHashtags());
        }

        Map<String, String> validationHashtagResults = hashtagValidator.validateHashtagFromForm(hashtagDtoFromTitleAndDescription);
        if (validationHashtagResults.size() != 0) {
            return ResponseEntity.badRequest().body(validationHashtagResults);
        }

        for (HashtagDto hashtagDto : hashtagDtoFromTitleAndDescription) {
            hashtagDto.setContent(hashtagDto.getContent().toLowerCase());
            hashtags.add(hashtagService.insertNewHashtag(hashtagDto));
        }

        Set<Employee> invitedCopyMembers = new HashSet<>();
        if (newEventDto.getInvitedMembers() != null) {
            Set<Integer> invitedMembers = newEventDto.getInvitedMembers();
            for (Integer invitedMember : invitedMembers) {
                Optional<Employee> employeeToInvite = employeeRepository.findById(invitedMember);
                employeeToInvite.ifPresent(invitedCopyMembers::add);
            }
        }

        Set<Employee> membersUnit;
        Optional<Unit> unit = Optional.empty();
        if (newEventDto.getUnitId() == null) {
            event.setUnit(null);
        } else {
            unit = unitRepository.findById(newEventDto.getUnitId());

            if (unit.isPresent()) {
                event.setUnit(unit.get());
                membersUnit = unit.get().getMembers();
                invitedCopyMembers.addAll(membersUnit);

                if (unit.get().getEventList() == null) {
                    List<Event> eventList = new ArrayList<>();
                    eventList.add(event);
                    unit.get().setEventList(eventList);
                } else {
                    unit.get().getEventList().add(event);
                }
            } else {
                return ResponseEntity.status(404).body(UnitConst.UNIT_NOT_AVAILABLE);
            }
        }
        event.setInvited(invitedCopyMembers);
        event.setHashtags(hashtags);
        eventRepository.save(event);
        employeeService.addToJoinedEvents(employee.getId(), event);
        employeeService.addToOwnedEvents(employee.getId(), event);
        sendEventInvitationNotification(event, NotificationType.INVITE);
        return ResponseEntity.created(URI.create("/v1/events/" + event.getId())).build();
    }

    public ResponseEntity<EmployeeDto> getEventOrganizer(int id) {
        return null;
    }

    private String eventDoesNotExist(int eventId) {
        return "Event with Id " + eventId + " does not exist";
    }

    public ResponseEntity<?> changeEventOrganizer(int eventId, int newOrganizerId, Principal principal) {
        authorizationService.inflateUser(principal);
        Employee employee = employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElseThrow(EmployeeNotFoundException::new);
        Event event = eventRepository.findById(eventId).orElse(null);
        if (Objects.isNull(event)) {
            return ResponseEntity.status(404).body(eventDoesNotExist(eventId));
        } else if (employee.getId() != event.getOrganizer().getId()) {
            return ResponseEntity.status(401).body(EventConst.UNAUTHORIZED_EMPLOYEE);
        } else if (newOrganizerId == event.getOrganizer().getId()) {
            return ResponseEntity.status(400).body(EventConst.ALREADY_EVENT_ORGANIZER);
        } else {
            Employee newOrganizer = employeeRepository.findById(newOrganizerId).orElseThrow(EmployeeNotFoundException::new);
            employeeService.removeFromOwnedEvents(event.getOrganizer().getId(), event);
            event.setOrganizer(newOrganizer);
            employeeService.addToOwnedEvents(newOrganizer.getId(), event);
            employeeService.addToJoinedEvents(newOrganizer.getId(), event);
            event
                    .getMembers()
                    .add(newOrganizer.getUser().getPerson());
            return changeEventOrganizerSuccessMessage(eventId, newOrganizer);
        }
    }

    private ResponseEntity<?> changeEventOrganizerSuccessMessage(int eventId, Employee newOrganizer) {
        return ResponseEntity.status(200).body("The organizer of the event with id "
                + eventId + " has been correctly changed to "
                + newOrganizer.getUser().getPerson().getFirstName() + " "
                + newOrganizer.getUser().getPerson().getLastName());
    }
    public ResponseEntity<?> updateEvent(int id, Map<String, Object> updates, Map<String, Object> adds, Map<String, Object> deletes, MultipartFile mainPhoto, MultipartFile[] additionalAttachments,Principal principal) {
        boolean general_change = false;
        authorizationService.inflateUser(principal);
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty())
            return ResponseEntity.badRequest().body(eventDoesNotExist(id));
        Event event = optionalEvent.get();
        if (event.getOrganizer().getId() != employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElseThrow(EmployeeNotFoundException::new).getId())
            return ResponseEntity.status(403).build();
        if (!Objects.isNull(updates)) {
            if (!Objects.isNull(updates.get("name"))) {
                Set<HashtagDto> oldHashtagDtos = findHashtagFromEvent(event.getName(), "");
                if (eventUpdater.updateName(event, updates.get("name").toString())) {
                    Set<HashtagDto> hashtagDtos = findHashtagFromEvent(updates.get("name").toString(), "");
                    if (hashtagDtos != null || !hashtagDtos.isEmpty())
                        eventUpdater.updateHashtags(event, hashtagDtos, oldHashtagDtos);
                    general_change = true;
                } else {
                    return ResponseEntity.status(404).body("Name is the same as was");
                }
            }
            if (!Objects.isNull(updates.get("description"))) {
                Set<HashtagDto> oldHashtagDtos = findHashtagFromEvent("", event.getDescription());
                if (eventUpdater.updateDescription(event, updates.get("description").toString())) {
                    Set<HashtagDto> hashtagDtos = findHashtagFromEvent("", updates.get("description").toString());
                    if (hashtagDtos != null || !hashtagDtos.isEmpty())
                        eventUpdater.updateHashtags(event, hashtagDtos, oldHashtagDtos);
                    general_change = true;
                } else {
                    return ResponseEntity.status(404).body("Description is the same as was");
                }
            }
            if (!Objects.isNull(updates.get("startTime")) || !Objects.isNull(updates.get("endTime"))) {
                Optional<?> updateDates = eventUpdater.updateDate(event, updates.get("startTime"), updates.get("endTime"));
                if (updateDates.get() instanceof Boolean) {
                    for (Person p : event.getMembers()) {
                        Optional<Employee> e = employeeRepository.findByUserPerson(p);//tą pętle można wyrzucić do insert notification
                        if (e.isPresent()) {
                            EventNotificationDto eventNotificationDto = new EventNotificationDto();
                            eventNotificationDto.setContent("Czas wydarzenia się zmienił na: " + event.getStartTime() + "-" + event.getEndTime());
                            eventNotificationDto.setEventId(event.getId());
                            eventNotificationDto.setType(NotificationType.EVENT_DATE_CHANGED);
                            notificationService.insertEventNotification(eventNotificationDto);
                        }
                    }
                } else if (updateDates.get() instanceof Map) {
                    return ResponseEntity.status(404).body(updateDates.get());
                }
            }
            if (!Objects.isNull(updates.get("periodicType"))) {
                if (eventUpdater.updatePeriodic(event, updates.get("periodicType"))) {
                    //TODO in eventUpdater add implementation for periodic event
                    general_change = true;
                } else {
                    return ResponseEntity.status(404).body("Periodic type of new event is the same as old");
                }
            }
            if (!Objects.isNull(updates.get("memberLimit"))) {
                if (event.getType().toString().startsWith("LIMITED")) {
                    if (eventUpdater.updateMemberLimit(event, Integer.parseInt(updates.get("memberLimit").toString()))) {
                        general_change = true;
                    } else {
                        return ResponseEntity.status(404).body("You can not set this member limit");
                    }
                } else return ResponseEntity.status(400).body("You can not change memberLimit for this type of event");
            }
            if (!Objects.isNull(updates.get("categoryId"))) {
                if (eventUpdater.updateCategory(event, Integer.parseInt(updates.get("categoryId").toString()))) {
                    general_change = true;
                } else {
                    return ResponseEntity.status(404).body("There is problem with changing category");//fixme
                }
            }
            if (!Objects.isNull(updates.get("location"))) {
                if (eventUpdater.updateLocation(event, updates.get("location"))) {
                    for (Person p : event.getMembers()) {
                        Optional<Employee> e = employeeRepository.findByUserPerson(p);//tą pętle można wyrzucić do insert notification
                        if (e.isPresent()) {
                            EventNotificationDto eventNotificationDto = new EventNotificationDto();
                            eventNotificationDto.setContent("Lokalizacja wydareznia się zmieniłą");
                            eventNotificationDto.setEventId(event.getId());
                            eventNotificationDto.setType(NotificationType.EVENT_LOCATION_CHANGED);
                            notificationService.insertEventNotification(eventNotificationDto);
                        }
                    }
                } else {
                    return ResponseEntity.status(404).body("Location of new event is the same as old");
                }
            }
            if (!Objects.isNull(updates.get("theme"))) {
                if (eventUpdater.updateTheme(event, updates.get("theme"))) {
                    //notification
                } else return ResponseEntity.status(404).body("Theme is the same as was");
            }
        }
        if (!Objects.isNull(adds)) {
            if (!Objects.isNull(adds.get("hashtags"))) {
                if (!eventUpdater.addHashtags(event, adds.get("hashtags")))
                    return ResponseEntity.status(404).body("Hashtags are in wrong format");
                general_change = true;
            }
            if (!Objects.isNull(adds.get("invitedMembers"))) {
                Optional<?> addMembers = eventUpdater.addInvitedMembers(event, adds.get("invitedMembers"));
                if (!(addMembers.get() instanceof Boolean)) {
                    return ResponseEntity.status(404).body(addMembers.get().toString());
                }
            }
        }
        if (!Objects.isNull(deletes)) {
            if (!Objects.isNull(deletes.get("hashtags"))) {
                if (!eventUpdater.removeHashtags(event, deletes.get("hashtags"),findHashtagFromEvent(event.getName(),event.getDescription()))) {
                    return ResponseEntity.status(404).body("Hashtags are in wrong format");
                }
                general_change = true;
            }
            if (!Objects.isNull(deletes.get("invitedMembers"))) {
                if (!eventUpdater.removeInvitedMembers(event, deletes.get("invitedMembers")))
                    return ResponseEntity.status(404).body("InvitedMembers are in wrong format");
            }
            if (!Objects.isNull(deletes.get("categoryId"))) {
                if (eventUpdater.removeCategory(event, Integer.parseInt(deletes.get("categoryId").toString()))) {
                    general_change = true;
                } else {
                    return ResponseEntity.status(404).body("There is problem with changing category");//fixme
                }
            }
            if (!Objects.isNull((deletes.get("attachments")))) {
                if (eventUpdater.removeAttachments(event, deletes.get("attachments"))) {
                    general_change = true;
                } else {
                    return ResponseEntity.status(404).body("Attachments was not remove correctly");
                }
            }
            if (!Objects.isNull((deletes.get("mainPhoto")))) {
                if (eventUpdater.removeMainPhoto(event, deletes.get("mainPhoto"))) {
                    general_change = true;
                } else {
                    return ResponseEntity.status(404).body("Main Photo was not remove correctly");
                }
            }
        }
        if (!Objects.isNull(additionalAttachments)) {
            if(event.getAttachments().size()+additionalAttachments.length>10){
                return ResponseEntity.status(404).body("You can upload max 10 attachments to Your Event");
            }
            if (eventUpdater.addAttachments(event, additionalAttachments)) {
                general_change = true;
            } else {
                return ResponseEntity.status(404).body("Attachments was not added correctly");
            }
        }
        if (!Objects.isNull(mainPhoto)) {
            if (eventUpdater.addMainPhoto(event, mainPhoto)) {
                general_change = true;
            } else {
                return ResponseEntity.status(404).body("Main Photo was not added correctly");
            }
        }
        if (general_change) {
            for (Person p : event.getMembers()) {
                Optional<Employee> e = employeeRepository.findByUserPerson(p);//tą pętle można wyrzucić do insert notification
                if (e.isPresent()) {
                    EventNotificationDto eventNotificationDto = new EventNotificationDto();
                    eventNotificationDto.setEventId(event.getId());
                    eventNotificationDto.setType(NotificationType.EVENT_UPDATED);
                    eventNotificationDto.setContent("Wydarzenie " + event.getName() + "został zmieniony");

                    eventNotificationDto.setEmployeeId(e.get().getId());
                    notificationService.insertEventNotification(eventNotificationDto);
                }
            }
        }
        return ResponseEntity.ok(eventDetailsMapper.convert(eventRepository.save(event)));
    }

    public ResponseEntity<?> editEventForm(int id, Principal principal) {
        if (!eventRepository.existsById(id))
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(eventDoesNotExist(id));
        Event event = eventRepository.getReferenceById(id);
        if (authorizationService.inflateUser(principal) instanceof AuthSucceded)
            if (event.getOrganizer().getId() != employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElseThrow(EmployeeNotFoundException::new).getId())
                return ResponseEntity.status(403).build();
        return ResponseEntity.ok(eventEditMapper.convert(event));
    }

    @Transactional
    public ResponseEntity<?> addEmployeeToEvent(int eventId, Principal principal) { //void?
        authorizationService.inflateUser(principal);
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isPresent()) {
            if (event.get().getType().toString().startsWith("LIMITED")) {
                if (event.get().getMembers().size() >= event.get().getMemberLimit().intValue()) {
                    return ResponseEntity.status(400).body(EventConst.EVENT_MEMBERS_COUNT_LIMIT_REACHED);
                }
            }
            Employee employee = employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElseThrow(EmployeeNotFoundException::new);
            if (employeeService.addToJoinedEvents(employee.getId(), event.get())) {
                Set<Person> eventMembers = event.get().getMembers();
                if (eventMembers == null) {
                    eventMembers = new HashSet<>();
                    eventMembers.add(employee.getUser().getPerson());
                    event.get().setMembers(eventMembers);
                } else {
                    event.get().getMembers().add(employee.getUser().getPerson());
                }
                return ResponseEntity.ok(eventMapper.convert(event.get()));
            } else {
                return ResponseEntity.status(400).body(EventConst.ALREADY_IN_MEMBER_LIST);
            }

        }
        return ResponseEntity.status(404).body(EventConst.EVENT_NOT_FOUND);
    }

    private ResponseEntity<?> employeeNotAMemberOfEvent(int id) {
        return ResponseEntity.status(404).body("Employee with Id " + id + " is not a member of this event");
    }

    private ResponseEntity<?> employeeLeaveSuccessful(int employeeId, int eventId) {
        return ResponseEntity.status(200).body("Successfully removed an employee with Id "
                + employeeId + " from the event with Id" + eventId);
    }

    public ResponseEntity<?> removeEmployeeFromEvent(int eventId, Principal principal) {

        authorizationService.inflateUser(principal);
        Optional<Event> event = eventRepository.findById(eventId);
        Employee employee = employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElseThrow(EmployeeNotFoundException::new);
        if (event.isEmpty()) {

            return ResponseEntity.status(404).body(eventDoesNotExist(eventId));

        }
        if (!eventRepository.findById(eventId).get()
                .getMembers()
                .contains(employee.getUser().getPerson())) {
            return employeeNotAMemberOfEvent(employee.getId());

        } else if (eventRepository.findById(eventId).get().getOrganizer().getId() == employee.getId()) {

            return ResponseEntity.status(403).body(EventConst.EVENT_ORGANIZER_LEAVE_ATTEMPT);

        } else {
            eventRepository.findById(eventId).get()
                    .getMembers()
                    .remove(employee
                            .getUser()
                            .getPerson());
            employeeService.removeFromJoinedEvents(employee.getId(), eventRepository.findById(eventId).get());
            return employeeLeaveSuccessful(employee.getId(), eventId);
        }
    }

    public ResponseEntity<?> toggleParticipation(UUID token) {
        Optional<MailToken> mailToken = mailTokenRepository.findById(token);
        if (!mailToken.isPresent()) {
            return ResponseEntity.status(404).body(AuthConst.EXPIRED_MAIL_TOKEN);
        }
        Event event = mailToken.get().getEvent();
        if (eventRepository.findById(event.getId()).isPresent()) {
            Person person = mailToken.get().getPerson();
            String email = person.getEmail();
            if (event.getMembers().contains(person)) {
                event.getMembers().remove(person);
                mailTokenRepository.delete(mailToken.get());
                if (event.getMembers().contains(person)) {
                    return ResponseEntity.status(400).body(EventConst.EVENT_ORGANIZER_LEAVE_ATTEMPT);
                }
                if (mailTokenRepository.existsById(mailToken.get().getToken())) {
                    return ResponseEntity.status(400).body(AuthConst.MAIL_TOKEN_STILL_EXISTS);
                }
                return ResponseEntity.ok(EventConst.PERSON_STILL_ON_LIST);
            }
            if (event.getType().toString().startsWith("LIMITED")) {
                if (event.getMembers().size() >= event.getMemberLimit().intValue()) {
                    return ResponseEntity.status(400).body(EventConst.EVENT_MEMBERS_COUNT_LIMIT_REACHED);
                }
            }
            event.getMembers().add(person);
            eventRepository.save(event);
            mailTokenRepository.delete(mailToken.get());
            return sendResignationTokenMail(event, person);
        }

        return ResponseEntity.status(404).body(EventConst.EVENT_NOT_FOUND);
    }

    public ResponseEntity<?> sendConfirmationMail(int eventId, AddGuestToEventDto addGuestToEventDto) {
        Person person = addGuestToEventMapper.toEntity(addGuestToEventDto);
        if (!eventRepository.findById(eventId).isPresent()) {
            return ResponseEntity.status(404).body(EventConst.EVENT_NOT_FOUND);
        }
        Event event = eventRepository.findById(eventId).get();

        Optional<List<MailToken>> mailTokenList = mailTokenRepository.findAllByEvent(event);
        if (mailTokenList.isPresent()) {
            Person finalPerson = person;
            if (mailTokenList.get().stream().anyMatch(mailToken -> Objects.equals(mailToken.getPerson().getEmail(), finalPerson.getEmail()))) {
                return ResponseEntity.status(400).body(AuthConst.MAIL_TOKEN_ALREADY_CREATED);
            }
        }
        if (!event.getType().toString().contains("EXTERNAL")) {
            return ResponseEntity.status(400).body(EventConst.GUEST_ADDITION_ATTEMPT);
        }
        if (event.getType().toString().startsWith("LIMITED")) {
            if (event.getMembers().size() >= event.getMemberLimit().intValue()) {
                return ResponseEntity.status(400).body(EventConst.EVENT_ATTACHMENT_COUNT_LIMIT_REACHED);
            }
        }
        personRepository.save(person);
        if (mailService.sendMailWithToken(person, event, true)) {
            return ResponseEntity.ok(MailConst.MAIL_SENT);
        }
        return ResponseEntity.status(404).body(MailConst.MAIL_NOT_SENT);
    }

    private Set<HashtagDto> findHashtagFromEvent(String eventName, String eventDescription) {
        Set<HashtagDto> hashtagSet = new HashSet<>();
        Optional<Hashtag> hashtag = Optional.empty();
        String text = eventName + " " + eventDescription;
        String[] textArray = text.split(" ");
        for (String s : textArray) {
            if (s.startsWith("#")) {
                s = s.replaceFirst("#", "");
                hashtag = hashtagRepository.findByContentIgnoreCase(s);
                if (hashtag.isPresent() && !hashtag.get().isHidden() || hashtag.isEmpty()) {
                    hashtagSet.add(new HashtagDto(s.toLowerCase()));
                }
            }
        }
        return hashtagSet;
    }

    public ResponseEntity<?> sendResignationTokenMail(Event event, Person person) {
        if (mailService.sendMailWithToken(person, event, false)) {
            return ResponseEntity.ok(MailConst.MAIL_SENT);
        }
        return ResponseEntity.status(404).body(MailConst.MAIL_NOT_SENT);
    }

    private void sendEventInvitationNotification(Event event, NotificationType notificationType) {
        EventNotificationDto eventNotificationDto = new EventNotificationDto();
        eventNotificationDto.setEventId(event.getId());
        eventNotificationDto.setType(notificationType);

        for (Employee employee : event.getInvited()) {
            System.out.println("Wysyłam powiadomienie " + notificationType + " do Employee id=" + employee.getId());
            eventNotificationDto.setEmployeeId(employee.getId());
            notificationService.insertEventNotification(eventNotificationDto);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteEvent(int eventId, Principal principal) {
        authorizationService.inflateUser(principal);
        Event event = eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);
        Employee employee = employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElseThrow(EmployeeNotFoundException::new);
        if(!event.getOrganizer().equals(employee)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only owner can delete event");
        }
        employee.getJoinedEvents().remove(event);
        employeeService.removeFromOwnedEvents(employee.getId(),event);
        if(event.getStartTime().compareTo(LocalDateTime.now())<=0){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Past events cannot be deleted");
        }
        for (Hashtag h: event.getHashtags()) {
            hashtagService.decrementHashtagGlobalCount(h.getId());
        }
        for(Person p: event.getMembers()){
            if(employeeRepository.findByUserPerson(p).isPresent()){
                employeeService.removeFromJoinedEvents(employeeRepository.findByUserPerson(p).get().getId(),event);
            }
        }
        Optional<List<MailToken>> tokens = mailTokenRepository.findAllByEvent(event);
        if(tokens.isPresent()){
            for (MailToken token: tokens.get()) {
                mailTokenRepository.delete(token);
            }
        }
        Optional<List<Notification>> notifications = notificationRepository.findAllByEvent(event);
        if(notifications.isPresent()){
            for (Notification notification: notifications.get()) {
                notification.setEvent(null);
            }
        }
        eventRepository.delete(event);
        if(eventRepository.existsById(eventId))
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Event still exist");
        //TODO Maybe notification
        return ResponseEntity.ok("Event was deleted");

    }
}

