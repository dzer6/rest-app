package com.dzer6.rest.resources;

import com.dzer6.rest.dto.Input;
import com.dzer6.rest.exception.ServiceException;
import com.yammer.dropwizard.jersey.params.IntParam;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;

/**
 * @author Andrew Panfilov
 */
public class CalcResourceIOTest {

    @Test(expected = ServiceException.class)
    public void testWrongFilesGet() {
        final CalcResource resource = new CalcResource("/abc", "/abc");
        resource.get(new IntParam("0"));
    }

    @Test(expected = ServiceException.class)
    public void testWrongFilesPost() {
        final CalcResource resource = new CalcResource("/abc", "/abc");
        resource.post(new Input(0.0, 1, 2));
    }

    @Test(expected = ServiceException.class)
    public void testOutOfBoundGet() {
        final CalcResource resource = new CalcResource("/src/test/resources/f1", "/src/test/resources/f1");
        resource.get(new IntParam("100"));
    }

    @Test(expected = ServiceException.class)
    public void testOutOfBoundPost() {
        final CalcResource resource = new CalcResource("/src/test/resources/f1", "/src/test/resources/f1");
        resource.post(new Input(0.0, 100, 200));
    }

    @Test(expected = WebApplicationException.class)
    public void testNotIntegerInputGet() {
        final CalcResource resource = new CalcResource("/src/test/resources/f1", "/src/test/resources/f1");
        resource.get(new IntParam("100m"));
    }

    @Test(expected = WebApplicationException.class)
    public void testNullInputGet() {
        final CalcResource resource = new CalcResource("/src/test/resources/f1", "/src/test/resources/f1");
        resource.get(new IntParam(null));
    }

    @Test(expected = ServiceException.class)
    public void testNullInputPost() {
        final CalcResource resource = new CalcResource("/src/test/resources/f1", "/src/test/resources/f1");
        resource.post(null);
    }
}
