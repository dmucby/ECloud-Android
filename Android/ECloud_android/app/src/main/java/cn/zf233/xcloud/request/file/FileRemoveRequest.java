package cn.zf233.xcloud.request.file;


import java.io.Serializable;

import cn.zf233.xcloud.entity.User;
import lombok.Data;

@Data
public class FileRemoveRequest implements Serializable {

    private static final long serialVersionUID = 1231425435457575674L;

    private Integer userId;

    private Integer fileId;

    private String username;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
