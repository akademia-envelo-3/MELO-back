package pl.envelo.melo.domain.poll;


public class PollConst {

    public static final int MIN_QUESTION_CHARACTER_LIMIT = 2;
    public static final int MAX_QUESTION_CHARACTER_LIMIT = 1000;
    public static final int OPTION_CHARACTER_LIMIT = 255;
    public static final int MIN_OPTION_COUNT = 2;
    public static final int MAX_OPTION_COUNT = 10;
    public static final String POLL_QUESTION_OUT_OF_RANGE = "Poll question must have between 2 and 1000 characters";
    public static final String POLL_OPTION_TOO_LONG = "One of the poll options is exceeding limit of 255 characters";
    public static final String POLL_OPTION_BLANK = "Poll option must not be blank";
    public static final String OUT_OF_OPTION_COUNT_BOUNDS = "Poll must have minimum of 2 options and maximum of 10 options";
    public static final String EVENT_AND_POLL_NOT_CORRELATED = "Event with id %d and poll with id %d are not correlated";
    public static final String EVENT_NOT_FOUND = "Event with id %d was not found";
    public static final String POLL_NOT_FOUND = "Poll with id %d was not found";
    public static final String POLL_OPTIONS_NOT_UNIQUE = "Poll options cannot be the same";


}
