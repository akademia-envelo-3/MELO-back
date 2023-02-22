package pl.envelo.melo.domain.event;

public class EventConst {
    public static final int MAX_DESCRIPTION_LENGTH = 4000;
    public static final int MAX_HASHTAG_COUNT = 100;
    public static final String INVALID_EVENT_TYPE = "Event type cant be null";
    public static final String INVALID_PERIODIC_TYPE = "Periodic type can not be null";
    public static final String EVENT_ATTACHMENT_COUNT_LIMIT_REACHED = "You can upload max 10 attachments to Your Event";
    public static final String COMMENT_ATTACHMENT_COUNT_LIMIT_REACHED = "You can upload max 10 attachments to Your comment";
    public static final String EVENT_NOT_FOUND = "Event with this ID does not exist";
}
