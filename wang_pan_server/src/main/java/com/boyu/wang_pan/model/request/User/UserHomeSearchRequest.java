package com.boyu.wang_pan.model.request.User;

import com.boyu.wang_pan.model.domain.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserHomeSearchRequest implements Serializable {

    private static final long serialVersionUID = 1231425435457575674L;

    private User user;

    private Integer parentId;

    private String filename;

    private Integer classify;

}
