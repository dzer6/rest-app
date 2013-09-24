package com.dzer6.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Andrew Panfilov
 */
public class CalcConfiguration extends Configuration {

    @Getter
    @NotEmpty
    @JsonProperty
    private String firstFilePath;

    @Getter
    @NotEmpty
    @JsonProperty
    private String secondFilePath;

}
