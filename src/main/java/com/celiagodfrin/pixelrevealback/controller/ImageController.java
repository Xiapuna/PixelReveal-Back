package com.celiagodfrin.pixelrevealback.controller;

import com.celiagodfrin.pixelrevealback.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
@CrossOrigin
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public Long uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        return imageService.uploadImage(file);
    }

}
