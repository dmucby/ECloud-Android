package cn.zf233.xcloud.request.user;


import java.io.Serializable;

import cn.zf233.xcloud.entity.User;
import lombok.Data;

@Data
public class UserRegistRequest implements Serializable {

    private static final long serialVersionUID = 1231425435457575674L;

    private User user;

    private String code;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
