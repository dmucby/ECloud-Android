package cn.zf233.xcloud.common;

/**
 * Created by zf233 on 11/28/20
 */
public enum RequestURL {

    LOGIN_URL(0, "http://10.0.2.2:8089/xcloud/user/login"),
    HOME_URL(1, "http://10.0.2.2:8089/xcloud/user/home"),
    HOME_SEARCH_URL(7, "http://10.0.2.2:8089/xcloud/user/homeSearch"),
    HOME_SORT_URL(8, "http://10.0.2.2:8089/xcloud/user/homeSort"),
//    HOME_URL(1, "http://localhost:8089/xcloud/user/home"),
    REGIST_URL(2, "http://10.0.2.2:8089/xcloud/user/regist"),
    UPDATE_URL(3, "http://10.0.2.2:8089/xcloud/user/update"),
    DOWNLOAD_URL(4, "http://10.0.2.2:8089/xcloud/file/download"),
    REMOVE_FILE_URL(5, "http://10.0.2.2:8089/xcloud/file/delete"),
    UPLOAD_FILE_URL(6, "http://10.0.2.2:8089/xcloud/file/upload"),
    CREATE_FOLDER(6, "http://10.0.2.2:8089/xcloud/file/createfolder");

    private final Integer code;
    private final String desc;

    RequestURL(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

