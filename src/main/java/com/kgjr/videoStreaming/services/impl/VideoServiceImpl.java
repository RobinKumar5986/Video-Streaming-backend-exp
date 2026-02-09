package com.kgjr.videoStreaming.services.impl;

import com.kgjr.videoStreaming.entaties.Videos;
import com.kgjr.videoStreaming.services.VideoService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {
    @Override
    public Videos save(Videos video, MultipartFile file) {
        return null;
    }

    @Override
    public Videos getVideosById(String videoId) {
        return null;
    }

    @Override
    public Videos getVideoByTitle(String title) {
        return null;
    }

    @Override
    public List<Videos> getAllVideos() {
        return List.of();
    }
}
