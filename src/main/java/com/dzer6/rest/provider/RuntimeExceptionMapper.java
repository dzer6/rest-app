package com.dzer6.rest.provider;

import com.dzer6.rest.exception.ServiceException;
import com.dzer6.rest.dto.ErrorMessage;
import com.google.common.base.Optional;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.io.IOException;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

/**
 * @author Andrew Panfilov
 */
@Slf4j
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(final RuntimeException exception) {
        if (exception instanceof ServiceException) {
            final ServiceException se = (ServiceException) exception;

            return handleServiceException(se);
        } else if (exception instanceof WebApplicationException) {
            final WebApplicationException wae = (WebApplicationException) exception;

            return handleWebApplicationException(wae);
        }

        log.error("Internal error", exception);
        return getResponse(exception);
    }


    protected Response handleServiceException(final ServiceException se) {
        final String message = se.getMessage();
        final Optional<Exception> cause = se.getExceptionCause();

        if (cause.isPresent()) {
            final Exception exception = cause.get();

            log.error(message, exception);

            if (exception instanceof IOException) {
                return getResponse(se);
            } else if (exception instanceof IllegalArgumentException) {
                return getResponse(se, BAD_REQUEST);
            } else if (exception instanceof IndexOutOfBoundsException) {
                return getResponse(se, BAD_REQUEST);
            }
        } else {
            log.error(message);
        }

        return getResponse(se);
    }

    protected Response handleWebApplicationException(final WebApplicationException wae) {
        log.error("Application error", wae);

        final Response.Status status = Response.Status.fromStatusCode(wae.getResponse().getStatus());

        return status == null ? getResponse(wae) :
                                getResponse(status);
    }

    protected Response getResponse(final Exception exception, final Response.Status status) {
        return Response.status(status)
                       .entity(getMessage(exception))
                       .build();
    }

    protected Response getResponse(final Response.Status status) {
        return Response.status(status)
                       .entity(new ErrorMessage(status.getReasonPhrase()))
                       .build();
    }

    protected Response getResponse(final Exception exception) {
        return Response.serverError()
                       .entity(getMessage(exception))
                       .build();
    }

    protected ErrorMessage getMessage(final Exception exception) {
        final String message = exception.getMessage();
        return new ErrorMessage(message == null ? "Internal error. Submit us a bug please: https://github.com/dzer6/rest-app/issues" :
                                                  message);
    }
}
