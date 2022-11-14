package com.cshisan.elasticsearch.entity;

import lombok.Data;

/**
 * @author CShisan
 */
@Data
public class SearchEntity {
    private Integer current;
    private Integer size;
    private String keyword;
}
