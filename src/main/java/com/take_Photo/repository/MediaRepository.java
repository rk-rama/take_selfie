package com.take_Photo.repository;


import com.take_Photo.model.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MediaRepository extends JpaRepository<MediaFile, Long> {
    List<MediaFile> findByUserId(Long userId);
}