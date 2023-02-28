package pl.envelo.melo.domain.attachment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.envelo.melo.domain.attachment.dto.AttachmentDto;
import pl.envelo.melo.domain.attachment.uploadmultiple.FilesStorageService;

import java.util.Objects;

import static pl.envelo.melo.domain.attachment.AttachmentConst.*;


@RequiredArgsConstructor
@Service
@Transactional
public class AttachmentService {

    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    FilesStorageService storageService;

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
    @Transactional
    public Attachment uploadFileAndSaveAsAttachment(MultipartFile attachment) {
        //String uploadedFileName = attachment.getOriginalFilename();
        AttachmentType attachmentType = validateAttachmentType(attachment);
        if (Objects.isNull(attachmentType)) {
            return null;
        }

        ///Zapis na serwerze
        String newUniqueFilename = storageService.save(attachment);

        ///Zwracam URI zasobu
        String UrlToFile = storageService.getUrlToFile(newUniqueFilename);

        Attachment attachmentToSave = new Attachment();

        /// Tworzę attachment z nazwą i URL zasobu
        attachmentToSave.setName(newUniqueFilename);
        attachmentToSave.setAttachmentUrl(UrlToFile);

        /// Waliduję i ustawiam typ załącznika
        AttachmentType attachmentTyp = validateAttachmentType(attachment);
        if (Objects.isNull(attachmentTyp)) {
            return null;
        }

        attachmentToSave.setAttachmentType(attachmentType);

        ///Zapis do repozytorium attachmentów
        Attachment attachmentFromDb = attachmentRepository.save(attachmentToSave);

        return attachmentFromDb;
    }

    public AttachmentType validateAttachmentType(MultipartFile uploadedAttachment) {

        /// W miarę czasu pokombinować z MIMETypes. Rozwiązanie tymczasowe.


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
