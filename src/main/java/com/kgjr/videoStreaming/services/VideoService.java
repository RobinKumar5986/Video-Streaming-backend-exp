package com.kgjr.videoStreaming.services;

import com.kgjr.videoStreaming.entaties.Videos;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {

    //save video
    Videos save(Videos video, MultipartFile file);

    //get video by id
    Videos getVideosById(String videoId);

    //get video by title
    Videos getVideoByTitle(String title);

    //get all video
    List<Videos> getAllVideos();

}
