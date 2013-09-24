package com.dzer6.rest;

import com.dzer6.rest.health.FileHealthCheck;
import com.dzer6.rest.provider.RuntimeExceptionMapper;
import com.dzer6.rest.resources.CalcResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import lombok.extern.slf4j.Slf4j;

/**
 * Hello world!
 *
 */
@Slf4j
public class CalcService extends Service<CalcConfiguration> {

    public static void main(final String[] args) throws Exception {
        new CalcService().run(args);
    }

    @Override
    public void initialize(final Bootstrap<CalcConfiguration> bootstrap) {
        bootstrap.setName("calc");
    }

    @Override
    public void run(final CalcConfiguration configuration, final Environment environment) throws Exception {
        final String firstFilePath = configuration.getFirstFilePath();
        final String secondFilePath = configuration.getSecondFilePath();

        environment.addResource(new CalcResource(firstFilePath, secondFilePath));
        environment.addProvider(RuntimeExceptionMapper.class);
        environment.addHealthCheck(new FileHealthCheck("first file", firstFilePath));
        environment.addHealthCheck(new FileHealthCheck("second file", secondFilePath));
    }
}
