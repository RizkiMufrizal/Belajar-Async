package org.rizki.mufrizal.async.mapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbumsMapper implements Serializable {

    @JsonProperty("href")
    private String href;

    @JsonProperty("items")
    private List<ItemMapper> items;

    @JsonProperty("limit")
    private Double limit;

    @JsonProperty("next")
    private String next;

    @JsonProperty("offset")
    private String offset;

    @JsonProperty("previous")
    private String previous;

    @JsonProperty("total")
    private Double total;

}