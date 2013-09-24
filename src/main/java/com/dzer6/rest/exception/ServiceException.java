package com.dzer6.rest.exception;

import com.google.common.base.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Andrew Panfilov
 */
@AllArgsConstructor
public class ServiceException extends RuntimeException {

    @Getter
    private final String message;

    @Getter
    private final Optional<Exception> exceptionCause;

}
