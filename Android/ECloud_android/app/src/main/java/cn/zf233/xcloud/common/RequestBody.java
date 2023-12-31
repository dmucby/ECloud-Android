package cn.zf233.xcloud.common;


import cn.zf233.xcloud.entity.User;
import cn.zf233.xcloud.entity.VersionPermission;

/**
 * Created by zf233 on 11/28/20
 */
public class RequestBody {
    private User user;
    private Integer sortFlag;
    private Integer sortType;
    private String filename;
    private String inviteCode;
    private Integer parentid;
    private VersionPermission versionPermission = new VersionPermission(1, RequestTypeENUM.VERSION_FAILURE.getDesc());

    public RequestBody() {
    }

    public RequestBody(User user, Integer sortFlag, Integer sortType, String filename, String inviteCode, Integer parentid, VersionPermission versionPermission) {
        this.user = user;
        this.sortFlag = sortFlag;
        this.sortType = sortType;
        this.filename = filename;
        this.inviteCode = inviteCode;
        this.parentid = parentid;
        this.versionPermission = versionPermission;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getSortFlag() {
        return sortFlag;
    }

    public void setSortFlag(Integer sortFlag) {
        this.sortFlag = sortFlag;
    }

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public VersionPermission getVersionPermission() {
        return versionPermission;
    }

    public void setVersionPermission(VersionPermission versionPermission) {
        this.versionPermission = versionPermission;
    }
}
