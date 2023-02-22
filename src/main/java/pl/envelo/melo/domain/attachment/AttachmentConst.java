package pl.envelo.melo.domain.attachment;

import java.util.List;

public class AttachmentConst {
    public static final String INVALID_NAME = "name of attachment can't be blank";
    public static final String INVALID_URL = "Wrong format of attachment URL";
    public static final String INVALID_TYPE = "temporary validation. attachmentType can't be null";
    public static final String INVALID_FORMAT = "Illegal format of attachment. WTF ARE U DOING? TURBO ERROR!";
    public static final String INVALID_PHOTO_FORMAT = "Illegal format of event Photo!";
    public static final String UPLOAD_SUCCESS = "Uploaded the files successfully: ";
    public static final String UPLOAD_FAILED = "Fail to upload files!";
    public static final String INIT_FAILED = "Could not initialize folder for upload!";
    public static final String SAVE_FAILED = "Could not store the file. Error: ";
    public static final String READ_FAILED = "Could not read the file!";
    public static final String LOAD_ALL_FILES_FAILED = "Could not load the files!";
    public static final String FILE_TOO_LARGE = "One or more files are too large!";
    public static final List ALLOWED_PHOTO_FORMATS = List.of("png", "jpg", "jpeg");
    public static final List ALLOWED_VIDEO_FORMATS = List.of("mp4");
    public static final List ALLOWED_DOCUMENT_FORMATS = List.of("pdf", "doc", "docx", "txt", "odt");
    public static final int MAX_ATTACHMENT_NAME_LENGTH = 255;
}
