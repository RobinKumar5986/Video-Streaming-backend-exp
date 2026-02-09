package com.kgjr.videoStreaming.repo;

import com.kgjr.videoStreaming.entaties.Videos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepo extends JpaRepository<Videos,String> {
    //video is the table type and the Strig in the type for the primary Key
    //this jpa provides the basic curd operations
    //we can add our custom methods here

    //we can also create a query methods

    Optional<Videos> findByTitle(String title);
}
