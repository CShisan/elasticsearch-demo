package com.cshisan.elasticsearch.utils;

import com.cshisan.elasticsearch.entity.Result;

/**
 * @author CShisan
 */
public class ResultUtil {
    public static <T> Result<T> ok(T t) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("成功");
        result.setData(t);
        return result;
    }
}
