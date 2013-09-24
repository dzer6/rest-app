package com.dzer6.rest.resources;

import com.dzer6.rest.dto.Input;
import com.dzer6.rest.dto.Result;
import com.yammer.dropwizard.jersey.params.IntParam;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import static com.dzer6.rest.resources.CalcResource.*;

/**
 * @author Andrew Panfilov
 */
public class CalcResourceTest {

    public static final String FIRST_FILE_PATH = "1";
    public static final String SECOND_FILE_PATH = "2";

    private List<Double> f1 = new LinkedList<>();

    private List<Double> f2 = new LinkedList<>();

    private final CalcResource resource = new CalcResource(FIRST_FILE_PATH, SECOND_FILE_PATH) {
        @Override
        protected double readValue(final int index, final String path) throws IOException {
            return FIRST_FILE_PATH.equals(path) ? f1.get(index) :
                                                  f2.get(index);
        }

        @Override
        protected void writeValue(final int index, final double value, final String path) throws IOException {
            if (FIRST_FILE_PATH.equals(path)) {
                f1.set(index, value);
            } else {
                f2.set(index, value);
            }
        }
    };

    @Before
    public void init() {
        f1 = new LinkedList<Double>() {{
            add(8.0);
            add(1.0);
            add(10.0);
            add(3.0);
            add(4.0);
        }};

        f2 = new LinkedList<Double>() {{
            add(100.0);
            add(1.0);
            add(10.0);
            add(3.0);
            add(4.0);
        }};
    }

    @Test /*   cond: f2[v1] > 10
             result: f2[v1] - 10 */
    public void testGetGreaterThan10() {
        final IntParam v1 = new IntParam("0");
        final Result result = resource.get(v1);

        assertNotNull(result);
        assertEquals(f2.get(v1.get()) - 10.0, result.getValue());
    }

    @Test /*   cond: f2[v1] < 10
             result: f2[v1] */
    public void testGetLessThan10() {
        final IntParam v1 = new IntParam("1");
        final Result result = resource.get(v1);

        assertNotNull(result);
        assertEquals(f2.get(v1.get()), result.getValue());
    }

    @Test /*   cond: f2[v1] == 10
             result: f2[v1] */
    public void testGetEqual10() {
        final IntParam v1 = new IntParam("2");
        final Result result = resource.get(v1);

        assertNotNull(result);
        assertEquals(f2.get(v1.get()), result.getValue());
    }

    @Test /*   cond: f1[v3] + v2 > 10
               then: f2[v4] = f1[v3] + v2
             result: 1 */
    public void testPostGreaterThan10() {
        final double v2 = 3.0;
           final int v3 = 0;
           final int v4 = 0;

        final Result result = resource.post(new Input(v2, v3, v4));

        assertNotNull(result);
        assertEquals(POST_COND_FALSE, result.getValue());

        assertEquals(f2.get(v4), f1.get(v3) + v2);
    }

    @Test /*   cond: f1[v3] + v2 < 10
               then: f2[v4] = f1[v3] + v2 + 10
             result: 0 */
    public void testPostLess10() {
        final double v2 = 3.0;
        final int v3 = 1;
        final int v4 = 1;

        final Result result = resource.post(new Input(v2, v3, v4));

        assertNotNull(result);
        assertEquals(POST_COND_TRUE, result.getValue());

        assertEquals(f2.get(v4), f1.get(v3) + v2 + 10.0);
    }

    @Test /*   cond: f1[v3] + v2 == 10
               then: f2[v4] = f1[v3] + v2
             result: 1 */
    public void testPostEqual10() {
        final double v2 = 3.0;
        final int v3 = 2;
        final int v4 = 3;

        final Result result = resource.post(new Input(v2, v3, v4));

        assertNotNull(result);
        assertEquals(POST_COND_FALSE, result.getValue());

        assertEquals(f2.get(v4), f1.get(v3) + v2);
    }

}
