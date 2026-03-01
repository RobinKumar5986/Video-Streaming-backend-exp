package com.kgjr.videoStreaming.Controllers;


import com.kgjr.videoStreaming.entaties.Videos;
import com.kgjr.videoStreaming.payload.CustomMessage;
import com.kgjr.videoStreaming.services.VideoService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    //Stream videos in chunks of byte
    @GetMapping("/stream/range/{videoId}")
    public ResponseEntity<Resource> streamVideoInRange(
            @PathVariable("videoId") String videoId,
            @RequestHeader(value = "Range", required = false) String range){

        System.out.println(range);
        Videos  video = videoService.getVideosById(videoId);
        try {
            Path path = Paths.get(video.getFilePath());

            Resource resource = new FileSystemResource(path);
            String contentType = video.getContentType();
            if(contentType == null){
                contentType = "application/octet-stream";
            }
            long fileLength = path.toFile().length();
            if(range == null){ // return all the video is the range is null
                return ResponseEntity
                        .ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);

            }

            // Calculating the start and end range.
            long rangeStart;
            long rangeEnd;

            String[] allRange = range.replace("bytes=","").split("-");

            rangeStart = Long.parseLong( allRange[0] );
            if(range.length() > 1){
                rangeEnd = Long.parseLong( allRange[1]);
            }else{
                rangeEnd = fileLength - rangeStart;
            }
            if(rangeEnd > fileLength - 1) {
                rangeEnd  = fileLength - 1;
            }

            InputStream inputStream;
            try {
                inputStream = Files.newInputStream(path);
                inputStream.skip(rangeStart);
                long contentLength = rangeEnd - rangeStart + 1;
                HttpHeaders headers = new HttpHeaders();
                headers.add("content-range", "bytes " + rangeStart + "-"+rangeEnd + "/"+fileLength);
                headers.setContentLength(contentLength);

                return ResponseEntity
                        .status(HttpStatus.PARTIAL_CONTENT)
                        .headers(headers)
                        .contentType(MediaType.parseMediaType(contentType))
//                        .body(new InputStreamResource(inputStream));// sending the entire range of byte from the specific index
                        .body(new ResourceRegion(resource, rangeStart, contentLength).getResource()); //sending some specific range



            }catch (Exception exp) {
                exp.printStackTrace();
                return ResponseEntity.internalServerError().build();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
