package com.boyu.wang_pan.model.request.file;


import lombok.Data;
import com.boyu.wang_pan.model.domain.User;

import java.io.Serializable;

@Data
public class FileCreateFolderRequest implements Serializable {

    private static final long serialVersionUID = 1231425435457575674L;

    private User user;

    private Integer parentId;

    private String folderName;
}
