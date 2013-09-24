package com.dzer6.rest.resources;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.dzer6.rest.exception.ServiceException;
import com.dzer6.rest.dto.Input;
import com.dzer6.rest.dto.Result;
import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.dropwizard.jersey.params.IntParam;
import com.yammer.metrics.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Andrew Panfilov
 */
@Slf4j
@Path("/")
@Produces(MediaType.APPLICATION_XML)
@AllArgsConstructor
public class CalcResource {

    public static final boolean DO_NOT_APPEND = false;

    public static final char COMMA = ',';

    public static final int POST_COND_TRUE = 0;
    public static final int POST_COND_FALSE = 1;

    private final String firstFilePath;

    private final String secondFilePath;

    @GET
    @Timed
    @Path("{v1}")
    @CacheControl(noCache = true)
    public synchronized Result get(final @PathParam("v1") IntParam v1) {
        log.debug("GET input = {}", v1);

        if (v1 == null || v1.get() == null || v1.get() < 0) {
            throw new ServiceException("Wrong GET input: v1 parameter",
                                       Optional.<Exception>of(new IllegalArgumentException()));
        }

        try {
            final double number = readValue(v1.get(), secondFilePath);
            log.debug("POST f2[v1] = {}", number);

            if (number > 10.0) {
                return new Result(number - 10.0);
            } else {
                return new Result(number);
            }
        } catch (FileNotFoundException e) {
            throw new ServiceException("Unable to find file", Optional.<Exception>of(e));
        } catch (IOException e) {
            throw new ServiceException("Unable to read file", Optional.<Exception>of(e));
        } catch (NumberFormatException e) {
            throw new ServiceException("Wrong read from file value", Optional.<Exception>of(e));
        } catch (IllegalStateException e) {
            throw new ServiceException("Wrong file state", Optional.<Exception>of(e));
        } catch (IndexOutOfBoundsException e) {
            throw new ServiceException("There are no more data in second file", Optional.<Exception>of(e));
        }
    }

    @POST
    @Timed
    @CacheControl(noCache = true)
    @Consumes(MediaType.APPLICATION_XML)
    public synchronized Result post(final @Valid Input input) {
        log.debug("POST input = {}", input);

        if (input == null) {
            throw new ServiceException("Wrong POST input",
                                       Optional.<Exception>of(new IllegalArgumentException()));
        }

        try {
            final double number = readValue(input.getV3(), firstFilePath);
            log.debug("POST f1[v3] = {}", number);

            final double sum = number + input.getV2();
            log.debug("POST f1[v3] + v2 = {}", sum);

            if (sum < 10.0) {
                writeValue(input.getV4(),
                           sum + 10.0,
                           secondFilePath);

                return new Result(POST_COND_TRUE);
            } else {
                writeValue(input.getV4(),
                           sum,
                           secondFilePath);

                return new Result(POST_COND_FALSE);
            }
        } catch (FileNotFoundException e) {
            throw new ServiceException("Unable to find file", Optional.<Exception>of(e));
        } catch (IOException e) {
            throw new ServiceException("Unable to read file", Optional.<Exception>of(e));
        } catch (NumberFormatException e) {
            throw new ServiceException("Wrong read from file value", Optional.<Exception>of(e));
        } catch (IllegalStateException e) {
            throw new ServiceException("Wrong file state", Optional.<Exception>of(e));
        } catch (IndexOutOfBoundsException e) {
            throw new ServiceException("There are no more data in second file", Optional.<Exception>of(e));
        }
    }

    @Timed
    protected void writeValue(final int index, final double value, final String path) throws IOException {
        log.debug("write value: index = {}, value = {}, path = {}", index, value, path);

        final String[] values = read(path);
        values[index] = Double.toString(value);

        final CSVWriter writer = new CSVWriter(new FileWriter(path, DO_NOT_APPEND), COMMA);

        try {
            writer.writeNext(values);
        } finally {
            writer.close();
        }
    }

    @Timed
    protected double readValue(final int index, final String path) throws IOException {
        log.debug("read value: index = {}, path = {}", index, path);

        final String[] strings = read(path);

        final String value = strings[index];
        log.debug("read value: value = {}", value);

        return Double.valueOf(value);
    }

    protected String[] read(final String path) throws IOException {
        final CSVReader reader = new CSVReader(new FileReader(path));

        try {
            final String[] numbers = reader.readNext();

            if (numbers == null) {
                throw new IllegalStateException("Wrong second file content");
            }

            return numbers;
        } finally {
            reader.close();
        }
    }
}
