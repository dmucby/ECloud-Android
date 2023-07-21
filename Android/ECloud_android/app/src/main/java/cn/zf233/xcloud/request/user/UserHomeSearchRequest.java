package cn.zf233.xcloud.request.user;


import java.io.Serializable;

import cn.zf233.xcloud.entity.User;
import lombok.Data;

@Data
public class UserHomeSearchRequest implements Serializable {

    private static final long serialVersionUID = 1231425435457575674L;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private Integer parentId;

    /**
     * 文件名搜索
     */
    private String filename;

    /**
     * 文件种类匹配
     */
    private Integer classify;

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getClassify() {
        return classify;
    }

    public void setClassify(Integer classify) {
        this.classify = classify;
    }
}
