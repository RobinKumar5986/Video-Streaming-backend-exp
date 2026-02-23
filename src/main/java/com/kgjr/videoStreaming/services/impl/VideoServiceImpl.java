package com.kgjr.videoStreaming.services.impl;

import com.kgjr.videoStreaming.entaties.Videos;
import com.kgjr.videoStreaming.repo.VideoRepo;
import com.kgjr.videoStreaming.services.VideoService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
public class VideoServiceImpl implements VideoService {

    @Value("${files.video}") //check the .property file for getting the path
    String DIR;

    @Autowired
    private VideoRepo videoRepository;

    @PostConstruct
    public void init() {
        File file = new File(DIR);
        if(!file.exists()){
            try {
                file.mkdir();
                System.out.println("DIR Created Successfully");
            }catch (Exception e){
                System.out.println("DIR Creation error: " + e.getMessage());
            }
        }else{
            System.out.println("Folder already exists");
        }
    }

    @Override
    public Videos save(Videos video, MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            String contentType = file.getContentType();
            InputStream inputStream = file.getInputStream();


            //file path
            String cleanFileName = StringUtils.cleanPath(filename);

            //folder path
            String cleanFolder = StringUtils.cleanPath(DIR);

            //file + folder path (exact location of the file)
            Path path  = Paths.get(cleanFolder,cleanFileName);

            // copy file to the folder
            Files.copy(inputStream, path ,StandardCopyOption.REPLACE_EXISTING);

            // creating video metadata
            video.setContentType(contentType);
            video.setFilePath(path.toString());

            // metadata save
            return videoRepository.save(video);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Videos getVideosById(String videoId) {
        try {
           Optional<Videos> video = videoRepository.findById(videoId);
           return video.orElse(null);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
