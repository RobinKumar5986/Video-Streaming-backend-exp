package com.kgjr.videoStreaming.entaties;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "yt_videos")
@Getter
@Setter
public class Videos {
    @Id
    private String videoId;
    private String title;
    private String description;
    private String contentType;
    private String filePath;

//    @ManyToOne
//    private Courses courses;

}
