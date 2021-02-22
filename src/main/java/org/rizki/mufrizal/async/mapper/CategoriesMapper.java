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
public class CategoriesMapper implements Serializable {

    @JsonProperty("href")
    public String href;

    @JsonProperty("items")
    public List<ItemCategoryMapper> items;

    @JsonProperty("limit")
    public Double limit;

    @JsonProperty("next")
    public String next;

    @JsonProperty("offset")
    public String offset;

    @JsonProperty("previous")
    public String previous;

    @JsonProperty("total")
    public Double total;

}
