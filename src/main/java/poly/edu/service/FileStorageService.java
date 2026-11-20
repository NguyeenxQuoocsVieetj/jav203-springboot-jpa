package poly.edu.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "jpg", "jpeg", "png", "docx");

    private final Path root;

    public FileStorageService(@Value("${app.upload-dir}") String uploadDir) throws IOException {
        this.root = Path.of(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(root);
    }

    public String store(MultipartFile file) throws IOException {
        String extension = extensionOf(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Định dạng file không được hỗ trợ");
        }
        String storedName = UUID.randomUUID() + "." + extension;
        file.transferTo(root.resolve(storedName));
        return storedName;
    }

    public boolean isAllowed(MultipartFile file) {
        return ALLOWED_EXTENSIONS.contains(extensionOf(file.getOriginalFilename()));
    }

    private String extensionOf(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
    }
}
