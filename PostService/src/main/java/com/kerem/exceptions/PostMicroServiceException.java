package com.kerem.exceptions;

import lombok.Getter;


@Getter


public class PostMicroServiceException extends RuntimeException
{
    private ErrorType errorType;

    public PostMicroServiceException(ErrorType errorType)
    {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public PostMicroServiceException(ErrorType errorType, String customMessage)
    {
        super(customMessage);
        this.errorType = errorType;
    }
}
