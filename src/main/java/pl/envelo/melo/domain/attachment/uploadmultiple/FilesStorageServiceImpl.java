package pl.envelo.melo.domain.attachment.uploadmultiple;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import pl.envelo.melo.domain.attachment.AttachmentConst;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {
    public FilesStorageServiceImpl(){
        this.deleteAll();
        this.init();
    }
    private final Path root = Paths.get("uploads");

    @Override
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException(AttachmentConst.INIT_FAILED);
        }
    }

    @Override
    public String save(MultipartFile file) {
        try {
            String newUniqueFilename = generateUniqueFileName(file);
            Files.copy(file.getInputStream(), this.root.resolve(newUniqueFilename));
            return newUniqueFilename;
        } catch (Exception e) {
            throw new RuntimeException(AttachmentConst.SAVE_FAILED + e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException(AttachmentConst.READ_FAILED);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException(AttachmentConst.LOAD_ALL_FILES_FAILED);
        }
    }

    @Override
    public String getUrlToFile(String filename) {
        String url_string = root.resolve(filename).toString();
        //return url_string;
        return MvcUriComponentsBuilder
                .fromMethodName(FilesController.class, "getFile", filename).build().toString();
    }


    @Override
    public String generateUniqueFileName(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String extemsion = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newUniqueFilename = UUID.randomUUID().toString() + extemsion;
        return newUniqueFilename;
    }
}