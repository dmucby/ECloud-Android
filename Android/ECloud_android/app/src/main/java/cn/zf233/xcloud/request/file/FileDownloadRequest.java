package cn.zf233.xcloud.request.file;


import java.io.Serializable;

import cn.zf233.xcloud.entity.User;
import lombok.Data;

@Data
public class FileDownloadRequest implements Serializable {

    private static final long serialVersionUID = 1231425435457575674L;

    /**
     * user_id
     */
    private Integer id;

    private Integer fileId;

    private String username;

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
}
