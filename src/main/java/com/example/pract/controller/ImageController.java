package com.example.pract.controller;

import com.example.pract.entity.Image;
import com.example.pract.entity.User;
import com.example.pract.repository.ImageRepository;
import com.example.pract.repository.UserRepository;
import com.example.pract.security.CustomUserDetails;
import com.example.pract.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class ImageController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/user/mediaForm")
    public String showUploadForm(Model model) {
        log.info("Handling request to Image Upload Form");
        return "userMediaForm";
    }

    @PostMapping("/user/uploadMedia")
    public String handleFileUpload(@RequestParam("image") MultipartFile file, Model model, Authentication authentication) {
        log.info("Handling request to upload image");
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select image");
            return "userMediaForm";
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        try {
            log.info("Uploading image");
            imageService.saveImage(file, user);
            log.info("File uploaded successfully");
            return "main";
        } catch (Exception e) {
            model.addAttribute("message", "Error uploading image");
            return "userMediaForm";
        }
    }

    @GetMapping("/user/images")
    public String showUserImages(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        List<Image> userImages = imageRepository.findByClientId(userId);
        model.addAttribute("images", userImages);
        return "myImages";
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Image not found"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(image.getData().length);
        return new ResponseEntity<>(image.getData(), headers, HttpStatus.OK);
    }
}