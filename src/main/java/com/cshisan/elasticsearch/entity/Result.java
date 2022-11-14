package com.cshisan.elasticsearch.entity;

import lombok.Data;

/**
 * @author CShisan
 */
@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;
}
