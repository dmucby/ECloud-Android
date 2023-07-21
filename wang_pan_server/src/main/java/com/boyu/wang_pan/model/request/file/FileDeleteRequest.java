package com.boyu.wang_pan.model.request.file;


import com.boyu.wang_pan.model.domain.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class FileDeleteRequest implements Serializable {

    private static final long serialVersionUID = 1231425435457575674L;

    private Integer userId;

    private Integer fileId;

    private String username;
}
