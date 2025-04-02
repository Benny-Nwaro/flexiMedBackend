package com.example.flexiMed.utils.fileStorage;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadLocation;

    public FileStorageService() {
        // Dynamically resolve the project root and construct the uploads path
        String projectRoot = System.getProperty("user.dir");
        this.uploadLocation = Paths.get(projectRoot, "uploads").toAbsolutePath();

        try {
            if (!Files.exists(this.uploadLocation)) {
                Files.createDirectories(this.uploadLocation);
            }
            if (!Files.isWritable(this.uploadLocation)) {
                throw new RuntimeException("Upload directory is not writable.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create or validate upload directory.", e);
        }
    }

    public String saveFile(MultipartFile file, UUID userId) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file.");
        }

        String fileName = "user_" + userId + "_" + sanitizeFilename(file.getOriginalFilename());
        Path destinationFile = this.uploadLocation.resolve(Paths.get(fileName)).normalize().toAbsolutePath();

        if (!destinationFile.getParent().equals(this.uploadLocation)) {
            throw new IOException("Cannot store file outside current directory.");
        }

        Files.copy(file.getInputStream(), destinationFile);

        return "/uploads/" + fileName;
    }

    public String saveCourseFile(MultipartFile file, UUID courseId) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file.");
        }

        String fileName = "course_" + courseId + "_" + sanitizeFilename(file.getOriginalFilename());
        Path destinationFile = this.uploadLocation.resolve(Paths.get(fileName)).normalize().toAbsolutePath();

        if (!destinationFile.getParent().equals(this.uploadLocation)) {
            throw new IOException("Cannot store file outside current directory.");
        }

        Files.copy(file.getInputStream(), destinationFile);

        return "/uploads/" + fileName;
    }

    private String sanitizeFilename(String filename) {
        // Basic filename sanitization (replace or remove potentially dangerous characters)
        return filename.replaceAll("[^a-zA-Z0-9.\\-]", "_");
    }
}