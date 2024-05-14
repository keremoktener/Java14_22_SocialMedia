package com.kerem.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserProfileUpdateRequestDto {
    String email;
    String photo;
    String phone;
    String address;
    String about;
}
