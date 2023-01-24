package pl.envelo.melo.domain.attachment;

import jakarta.activation.MimeType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import pl.envelo.melo.domain.attachment.dto.AttachmentDto;
import pl.envelo.melo.domain.attachment.uploadmultiple.FileInfo;
import pl.envelo.melo.domain.attachment.uploadmultiple.FilesController;
import pl.envelo.melo.domain.attachment.uploadmultiple.FilesStorageService;
import pl.envelo.melo.domain.attachment.uploadmultiple.ResponseMessage;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static pl.envelo.melo.domain.attachment.MimeTypes.*;


@RequiredArgsConstructor
@Service
public class AttachmentService {

    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    FilesStorageService storageService;


    public void transformAttachment(MultipartFile mainPhotoAttachment) {
        mainPhotoAttachment.getContentType();
        mainPhotoAttachment.getName();
    }

    /*
    public Attachment uploadMainPhotoAndConvertToAttachment(MultipartFile mainPhotoUploaded) {

        String uploadedFileName = mainPhotoUploaded.getOriginalFilename();
        System.out.println(uploadedFileName);

        ///Zapis na serwerze
        storageService.save(mainPhotoUploaded);

        ///Zwracam URI zasobu
        String UrlToFile = storageService.getUrlToFile(uploadedFileName);

        Attachment mainPhoto = new Attachment();

        /// Tworzę attachment z typem PHOTO.
        mainPhoto.setName(mainPhotoUploaded.getName());
        mainPhoto.setAttachmentUrl(UrlToFile);
        mainPhoto.setAttachmentType(AttachmentType.PHOTO);

        ///Zapis do repozytorium attachmentów
        Attachment mainPhotoFromDb = attachmentRepository.save(mainPhoto);

        return mainPhotoFromDb;
    }

     */
    public Attachment uploadFileAndSaveAsAttachment(MultipartFile attachment) {
        String uploadedFileName = attachment.getOriginalFilename();

        ///Zapis na serwerze
        String newUniqueFilename = storageService.save(attachment);

        ///Zwracam URI zasobu
        String UrlToFile = storageService.getUrlToFile(newUniqueFilename);

        Attachment attachmentToSave = new Attachment();

        /// Tworzę attachment z nazwą i URL zasobu
        attachmentToSave.setName(newUniqueFilename);
        attachmentToSave.setAttachmentUrl(UrlToFile);

        /// Waliduję i ustawiam typ załącznika
        AttachmentType attachmentType = validateAttachmentType(attachment);
        attachmentToSave.setAttachmentType(attachmentType);

        ///Zapis do repozytorium attachmentów
        Attachment attachmentFromDb = attachmentRepository.save(attachmentToSave);

        return attachmentFromDb;
    }
    public AttachmentType validateAttachmentType(MultipartFile uploadedAttachment) {

        /// W miarę czasu pokombinować z MIMETypes. Rozwiązanie tymczasowe.
        final List ALLOWED_PHOTO_FORMATS = List.of("png", "jpg", "jpeg");
        final List ALLOWED_VIDEO_FORMATS = List.of("mp4");
        final List ALLOWED_DOCUMENT_FORMATS = List.of("pdf", "doc", "docx", "txt", "odt");

        /// Pobieram absolutną nazwę pliku
        String fileName = uploadedAttachment.getOriginalFilename();

        /// Wykorzystując MimeTypes weryfikuję typ pliku.
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        /// Zwracam odpowiedni typ, bądź null jeśli żaden z nich nie pasuje.
        if (ALLOWED_VIDEO_FORMATS.contains(extension)) {
            return AttachmentType.VIDEO;
        } else if (ALLOWED_PHOTO_FORMATS.contains(extension)) {
            return AttachmentType.PHOTO;
        } else if (ALLOWED_DOCUMENT_FORMATS.contains(extension)) {
            return AttachmentType.DOCUMENT;
        }
        return null;
    }


/*

    public ResponseEntity<ResponseMessage> uploadFiles(MultipartFile[] files) {
        String message = "";
        try {
            List<String> fileNames = new ArrayList<>();

            Arrays.asList(files).stream().forEach(file -> {
                storageService.save(file);
                fileNames.add(file.getOriginalFilename());
            });

            message = "Uploaded the files successfully: " + fileNames;
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Fail to upload files!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();
            return new FileInfo(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    public ResponseEntity<Resource> getFile(String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
*/



    public ResponseEntity<AttachmentDto> insertNewAttachment(AttachmentDto attachmentDto) {
        return null;
    }

    public Attachment insertOrGetAttachment(AttachmentDto attachmentDto) {
        return null;
    }
}
