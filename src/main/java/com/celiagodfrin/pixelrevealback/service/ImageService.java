package com.celiagodfrin.pixelrevealback.service;

import com.celiagodfrin.pixelrevealback.dao.ImageDao;
import com.celiagodfrin.pixelrevealback.model.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageDao imageDao;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public Long uploadImage(MultipartFile file) throws IOException {
        String storagePath = UUID.randomUUID() + ".jpg";

        Path targetPath = Path.of(uploadDir, storagePath);
        file.transferTo(targetPath);

        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setStoragePath(storagePath);
        image.setUploadedAt(LocalDateTime.now());
        image.setFormat("jpg");

        Image savedImage = imageDao.save(image);
        return savedImage.getId();
    }

}
