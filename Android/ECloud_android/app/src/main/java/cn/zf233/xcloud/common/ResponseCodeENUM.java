package cn.zf233.xcloud.common;

/**
 * 错误码
 */
public enum ResponseCodeENUM {

    SUCCESS(200, "ok", ""),
    PARAMS_ERROR(400, "请求参数错误", ""),
    NULL_ERROR(404, "请求数据为空", ""),
    NOT_LOGIN(401, "未登录", ""),
    NO_AUTH(40101, "无权限", ""),
    REPEAT_LOGIN(40102,"重复的登录请求",""),
    SYSTEM_ERROR(50000, "系统内部异常", ""),
    USER_LOCK(40103, "用户被锁定", "");

    private final int code;

    /**
     * 状态码信息
     */
    private final String message;

    /**
     * 状态码描述（详情）
     */
    private final String description;

    ResponseCodeENUM(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
