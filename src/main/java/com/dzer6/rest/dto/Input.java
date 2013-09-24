package com.dzer6.rest.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Andrew Panfilov
 */
@XmlRootElement
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Input {

    @NotNull
    @Getter
    @Setter
    private Double v2;

    @NotNull
    @Min(0)
    @Getter
    @Setter
    private Integer v3;

    @NotNull
    @Min(0)
    @Getter
    @Setter
    private Integer v4;

}
