package com.kerem.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessage
{
    private Integer code;
    private String message;
    @Builder.Default
    private LocalDateTime dateTime = LocalDateTime.now();


}
