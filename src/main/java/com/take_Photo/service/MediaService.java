package com.take_Photo.service;

import com.take_Photo.model.MediaFile;
import com.take_Photo.model.User;
import com.take_Photo.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

@Service
public class MediaService {
    @Autowired
    private MediaRepository mediaRepo;

    public void saveUserMedia(MultipartFile file, User user) throws IOException {
        String uploadDir = "uploads/user_" + user.getId();
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = uploadDir + File.separator + fileName;

        // Photo save on disk
        File dest = new File(filePath);
        file.transferTo(dest.getAbsoluteFile());

        System.out.println("PHOTO SAVED AT: " + dest.getAbsolutePath()); // Console mein check karein

        // Database entry with leading slash
        String dbUrl = "/uploads/user_" + user.getId() + "/" + fileName;

        MediaFile media = new MediaFile();
        media.setFileName(fileName);
        media.setFileUrl(dbUrl);
        media.setUser(user);
        media.setType(file.getContentType());
        mediaRepo.save(media);

        System.out.println("DATABASE ENTRY DONE FOR: " + dbUrl); // Console mein check karein
    }
    public void deletePhoto(Long id) {
        MediaFile media = mediaRepo.findById(id).orElseThrow();
        // 1. Disk se file delete karo
        File file = new File(media.getFileUrl().substring(1)); // Remove leading slash
        if(file.exists()) file.delete();

        // 2. DB se record delete karo
        mediaRepo.delete(media);
    }
}