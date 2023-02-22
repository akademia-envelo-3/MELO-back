package pl.envelo.melo.domain.event;

public class EventConst {
    public static final int MAX_DESCRIPTION_LENGTH = 4000;
    public static final int MAX_HASHTAG_COUNT = 100;
    public static final String INVALID_EVENT_TYPE = "Event type cant be null";
    public static final String INVALID_PERIODIC_TYPE = "Periodic type can not be null";
    public static final String EVENT_ATTACHMENT_COUNT_LIMIT_REACHED = "You can upload max 10 attachments to Your Event";
    public static final String EVENT_MEMBERS_COUNT_LIMIT_REACHED = "Event is full";
    public static final String COMMENT_ATTACHMENT_COUNT_LIMIT_REACHED = "You can upload max 10 attachments to Your comment";
    public static final String EVENT_NOT_FOUND = "Event with this ID does not exist";
    public static final String UNAUTHORIZED_EMPLOYEE = "You are not the organizer of the event you do not have the authority to make changes";
    public static final String EMPLOYEE_NOT_IN_MEMBER_LIST = "Employee is not member of this event.";
    public static final String ALREADY_EVENT_ORGANIZER = "You are event organizer already";
    public static final String ALREADY_IN_MEMBER_LIST = "Employee already on list";
    public static final String EVENT_ORGANIZER_LEAVE_ATTEMPT = "Event organizer cant be remove from his event";
    public static final String PERSON_STILL_ON_LIST = "Person removed successful";
    public static final String GUEST_ADDITION_ATTEMPT = "Event is not external, guest can't be added.";
    public static final String FORBIDDEN_ACTION = "forbidden error";
    public static final String INVALID_START_TIME = "startTime error";
    public static final String INVALID_END_TIME = "endTime error";
    public static final String START_TIME_SAME_AS_OLD = "New StartTime is the same as old.";
    public static final String END_TIME_SAME_AS_OLD = "New EndTime is the same as old.";
    public static final String PAST_TIME = "You cannot set past date";
    public static final String ARCHIVED_EVENT_EDIT_ATTEMPT = "You cannot edit archived event";
    public static final String END_TIME_AFTER_START_TIME = "You must set endTime to be after startTime";
    public static final String START_TIME_BEFORE_END_TIME = "You must set startTime to be before endTime";
}
