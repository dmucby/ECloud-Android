package cn.zf233.xcloud.request.file;


import java.io.Serializable;

import lombok.Data;

@Data
public class FileUploadRequest implements Serializable {

    private static final long serialVersionUID = 1231425435457575674L;

    private Integer id;

    private String username;

    private String password;

    private Integer fileId;


    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
