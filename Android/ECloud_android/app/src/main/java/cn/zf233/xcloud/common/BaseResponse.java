package cn.zf233.xcloud.common;

import android.util.Log;

import java.util.List;

import cn.zf233.xcloud.entity.AbsolutePath;

/**
 * Created by zf233 on 12/1/20
 */
public class BaseResponse<T> {
    private int code;
    private String msg;
    private List<AbsolutePath> absolutePath;
    private T data;
    private String description;

    public boolean isSuccess() {
//        Log.d("===", String.valueOf(this.code));
//        System.out.println(ResponseCodeENUM.SUCCESS.getCode());
        return this.code == (int)ResponseCodeENUM.SUCCESS.getCode();
    }

    public BaseResponse() {
    }

    public BaseResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BaseResponse(Integer code, String msg, T data, String description) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.description = description;
    }

    public Integer getStatus() {
        return code;
    }

    public void setStatus(Integer status) {
        this.code = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<AbsolutePath> getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(List<AbsolutePath> absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
