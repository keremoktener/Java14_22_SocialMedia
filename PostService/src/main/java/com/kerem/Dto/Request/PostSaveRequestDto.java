package com.kerem.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostSaveRequestDto {
    String title;
    String content;
    String userProfileId;
    String photoUrl;
}
