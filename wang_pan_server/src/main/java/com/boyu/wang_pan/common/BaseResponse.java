package com.boyu.wang_pan.common;

import com.boyu.wang_pan.model.domain.AbsolutePath;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 通用返回类
 *
 * @param <T>
 * @author
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;

    private String msg;

    private String description;

    private List<AbsolutePath> absolutePath;

    public BaseResponse(int code, T data, String message, List<AbsolutePath> absolutePath) {
        this.code = code;
        this.data = data;
        this.msg = message;
        this.absolutePath = absolutePath;
    }

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.msg = message;
        this.description = description;
    }

//    public BaseResponse(int code, T data, String message, String description, String filename) {
//        this.code = code;
//        this.data = data;
//        this.msg = message;
//        this.description = description;
//        this.fileName = filename;
//    }

    public BaseResponse(int code, T data, String message) {
        this(code, data, message, "");
    }

    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }
}