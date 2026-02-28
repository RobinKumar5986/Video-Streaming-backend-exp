package com.kgjr.videoStreaming.Controllers;


import com.kgjr.videoStreaming.entaties.Videos;
import com.kgjr.videoStreaming.payload.CustomMessage;
import com.kgjr.videoStreaming.services.VideoService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    //upload video
    @PostMapping("/")
    public ResponseEntity<?> create(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description
    ) {
        Videos video = new Videos();
        video.setTitle(title);
        video.setDescription(description);

        Videos savedVideo = videoService.save(video, file);
        CustomMessage msg = new CustomMessage();

        if (savedVideo != null) {
            msg.setSuccess(true);
            msg.setMessage("Video Uploaded Successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVideo);
        } else {
            msg.setSuccess(false);
            msg.setMessage("Video Upload Failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
        }
    }

    // stream video:

    //1. Sending the entire video to the frontend
    @GetMapping("/stream/{videoId}")
    public  ResponseEntity<Resource> stream(
            @PathVariable("videoId") String videoId
    ) {
        Videos video = videoService.getVideosById(videoId);
        String contentType = video.getContentType();
        String filePath = video.getFilePath();
        if(contentType == null){
            contentType = "application/octet-stream";
        }
        Resource resource = new FileSystemResource(filePath);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }


    //func to get all the videos
    @GetMapping("/stream/all")
    public List<Videos> getAllVideos() {
        return videoService.getAllVideos();
    }
}
