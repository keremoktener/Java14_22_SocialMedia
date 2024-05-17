package com.kerem.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostFindAllResponseDto {
    Long id;
    String userProfileId;
    private String title;
    private String content;
    private LocalDateTime publishedOn;
}
