package com.kgjr.videoStreaming.Controllers;


import com.kgjr.videoStreaming.entaties.Videos;
import com.kgjr.videoStreaming.payload.CustomMessage;
import com.kgjr.videoStreaming.services.VideoService;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping("/")
    public ResponseEntity<CustomMessage> create(
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
            return ResponseEntity.status(HttpStatus.CREATED).body(msg);
        } else {
            msg.setSuccess(false);
            msg.setMessage("Video Upload Failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
        }
    }
}
