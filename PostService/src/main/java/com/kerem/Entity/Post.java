package com.kerem.Entity;

import com.kerem.Constant.Role;
import com.kerem.Constant.Status;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Document
public class Post {
    @MongoId
    String id;

    private String title;
    private String content;

    String userProfileId;

    String photoUrl;

    @Builder.Default
    private LocalDateTime publishedOn = LocalDateTime.now();


    private LocalDateTime updatedOn;

}
