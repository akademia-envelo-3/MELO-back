package pl.envelo.melo.domain.comment;

public class CommentConst {
    public static final String INVALID_CONTENT_LENGTH = "Commentary must contain from 1 to 2000 chars";
    public static final String COMMENT_EMPTY = "Comment is empty. You must add CONTENT, ATTACHMENT or both together.";
    public static final String ILLEGAL_ATTACHMENT_FORMAT = "Illegal format of attachment. WTF ARE U DOING?";
    public static final int MIN_CONTENT_LENGTH = 1;
    public static final int MAX_CONTENT_LENGTH = 2000;
}
