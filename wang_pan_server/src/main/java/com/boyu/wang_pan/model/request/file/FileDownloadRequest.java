package com.boyu.wang_pan.model.request.file;


import java.io.Serializable;

import com.boyu.wang_pan.model.domain.User;
import lombok.Data;

@Data
public class FileDownloadRequest implements Serializable {

    private static final long serialVersionUID = 1231425435457575674L;

    private Integer id;

    private String username;

    private Integer fileId;
}
