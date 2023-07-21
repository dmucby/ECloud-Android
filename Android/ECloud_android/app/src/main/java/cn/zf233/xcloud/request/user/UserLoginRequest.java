package cn.zf233.xcloud.request.user;


import java.io.Serializable;

import cn.zf233.xcloud.entity.User;
import lombok.Data;

@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 1231425435457575674L;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
