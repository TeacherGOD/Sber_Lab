package com.sber_ii_lab.service;

import com.sber_ii_lab.config.FileValidator;
import com.sber_ii_lab.exception.DirectoryCreationException;
import com.sber_ii_lab.exception.FileDeleteException;
import com.sber_ii_lab.exception.FileStorageException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.sber_ii_lab.repository.NewsRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {
    private final Path rootLocation = Paths.get("uploads");
    private final FileValidator fileValidator;
    private final NewsRepository newsRepository;
    private final ScheduledExecutorService taskScheduler;

    @PostConstruct
    public void init() {
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
        } catch (IOException e) {
            throw new DirectoryCreationException("Ошибка создания директории", e);
        }
    }

    public String storeFile(MultipartFile file) {
        fileValidator.validate(file);
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), rootLocation.resolve(fileName));
            return fileName;
        } catch (IOException e) {
            throw new FileStorageException("Ошибка сохранения файла: " + e.getMessage());
        }
    }

    @Transactional
    public String updateFile(MultipartFile newFile, String oldFileName) {
        if (newFile != null && !newFile.isEmpty()) {
            // Удаляем старый файл если он больше не используется
            taskScheduler.schedule(()->{
                log.info("check file "+oldFileName+" to exist.");
                deleteFileIfUnused(oldFileName);
            },1, TimeUnit.MINUTES);

            return storeFile(newFile);
        }
        return oldFileName;
    }

    public void deleteFileIfUnused(String fileName) {
        long count = newsRepository.countByImageUrl(fileName);
        if (count == 0) {
            deleteFile(fileName);
        }
    }

    private void deleteFile(String fileName) {
        Path file = rootLocation.resolve(fileName);
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new FileDeleteException("Ошибка удаления файла", e);
        }
    }

    public void cleanupUnusedFiles(){
        log.info("Starting files cleanup...");
        try{
            List<String> allFiles = listAllFiles();
            allFiles.forEach(file -> {
                if (newsRepository.countByImageUrl(file) == 0) {
                    deleteFile(file);
                }
            });
            log.info("Tags files completed");
        }
        catch (IOException e){
            throw new FileStorageException("Ошибка при отчистке файлов");
        }
    }

    public List<String> listAllFiles() throws IOException {
        try (Stream<Path> paths = Files.walk(this.rootLocation)) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(path -> this.rootLocation.relativize(path).toString())
                    .toList();
        }
    }
}
