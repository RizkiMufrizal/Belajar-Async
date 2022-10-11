package org.rizki.mufrizal.async.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Calculate implements Serializable {
    private String name;

    private Integer angka1;

    private Integer angka2;

    private Integer total;
}