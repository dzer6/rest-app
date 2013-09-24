package com.dzer6.rest.health;

import com.yammer.metrics.core.HealthCheck;

import java.io.File;

/**
 * @author Andrew Panfilov
 */
public class FileHealthCheck extends HealthCheck {

    private final String path;

    public FileHealthCheck(final String name, final String path) {
        super(name);
        this.path = path;
    }

    @Override
    protected Result check() throws Exception {
        if (new File(path).exists()) {
            return Result.healthy();
        } else {
            return Result.unhealthy("Cannot find file " + new File(path).getAbsolutePath());
        }
    }
}
