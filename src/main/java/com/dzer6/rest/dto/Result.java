package com.dzer6.rest.dto;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Andrew Panfilov
 */
@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Result<V extends Number> {

    @Getter
    @Setter
    private V value;

}
