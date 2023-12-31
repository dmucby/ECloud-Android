package cn.zf233.xcloud.entity;

/**
 * Created by zf233 on 11/28/20
 */
public class File {
    private Integer id;
    private Integer parentId;
    private Integer folder;
    private String fileName;
    private String fileType;
    private String fileSize;
    private String uploadTime;
    private String remark;
    private Integer downloadCount;
    private Integer logoID;

    public File() {
    }

    public File(Integer id, Integer parentId, Integer folder, String fileName, String fileType, String fileSize, String uploadTime, String remark, Integer downloadCount, Integer logoID) {
        this.id = id;
        this.parentId = parentId;
        this.folder = folder;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.uploadTime = uploadTime;
        this.remark = remark;
        this.downloadCount = downloadCount;
        this.logoID = logoID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getFolder() {
        return folder;
    }

    public void setFolder(Integer folder) {
        this.folder = folder;
    }

    public String getFilename() {
        return fileName;
    }

    public void setFilename(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Integer getLogoID() {
        return logoID;
    }

    public void setLogoID(Integer logoID) {
        this.logoID = logoID;
    }
}
