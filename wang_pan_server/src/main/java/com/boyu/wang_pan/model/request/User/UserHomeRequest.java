package com.boyu.wang_pan.model.request.User;

import com.boyu.wang_pan.model.domain.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserHomeRequest implements Serializable {

    private static final long serialVersionUID = 1231425435457575674L;

    private User user;

    private Integer parentId;
}
