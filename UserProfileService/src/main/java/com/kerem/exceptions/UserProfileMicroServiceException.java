package com.kerem.exceptions;

import lombok.Getter;


@Getter


public class UserProfileMicroServiceException extends RuntimeException
{
    private ErrorType errorType;

    public UserProfileMicroServiceException(ErrorType errorType)
    {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public UserProfileMicroServiceException(ErrorType errorType, String customMessage)
    {
        super(customMessage);
        this.errorType = errorType;
    }
}
