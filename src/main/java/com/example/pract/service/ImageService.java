package com.example.pract.service;

import com.example.pract.controller.MainController;
import com.example.pract.entity.Image;
import com.example.pract.entity.User;
import com.example.pract.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    private final ImageRepository imageRepository;

    @Value("${server.upload.path}")
    private String uploadPath;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public void saveImage(MultipartFile file, User user) {
        log.info("Trying to save file");
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadPath + file.getOriginalFilename());
            Path uploadDir = path.getParent();
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            Files.write(path, bytes);
            log.info("File is successfully saved on the server");

            Image image = new Image();
            image.setName(file.getOriginalFilename());
            image.setFilePath(path.toString());
            image.setData(bytes);
            image.setUser(user);
            imageRepository.save(image);
            log.info("File is successfully saved in DB");
        } catch (IOException e) {
            log.error("Error saving file: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving file: " + e.getMessage());
        }
    }
}