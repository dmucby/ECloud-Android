package cn.zf233.xcloud.service.impl;

import com.google.gson.reflect.TypeToken;

import java.io.File;

import cn.zf233.xcloud.common.BaseResponse;
import cn.zf233.xcloud.common.RequestURL;
import cn.zf233.xcloud.entity.User;
import cn.zf233.xcloud.request.file.FileCreateFolderRequest;
import cn.zf233.xcloud.request.file.FileDownloadRequest;
import cn.zf233.xcloud.request.file.FileRemoveRequest;
import cn.zf233.xcloud.service.FileService;
import cn.zf233.xcloud.util.RequestUtil;


public class FileServiceImpl implements FileService {

    /**
     * 构造请求体再发出请求
     */

    @Override
    public File fileDownload(RequestUtil requestUtil, User currentUser, Integer id) {
        FileDownloadRequest fileDownloadRequest = new FileDownloadRequest();
        fileDownloadRequest.setId(currentUser.getId());
        fileDownloadRequest.setFileId(id);
        fileDownloadRequest.setUsername(currentUser.getUsername());
        return requestUtil.fileDownload(RequestURL.DOWNLOAD_URL.getDesc(), fileDownloadRequest, new TypeToken<BaseResponse<String>>() {
        });
    }

    @Override
    public BaseResponse uploadFile(RequestUtil requestUtil, User currentUser, File file, Integer parentId) {
        return null;
    }

    @Override
    public BaseResponse createFolder(RequestUtil requestUtil, User currentUser, String filename, Integer parentId) {
        FileCreateFolderRequest fileCreateFolderRequest = new FileCreateFolderRequest();
        fileCreateFolderRequest.setUser(currentUser);
        fileCreateFolderRequest.setFolderName(filename);
        fileCreateFolderRequest.setParentId(parentId);
        return requestUtil.createFolder(RequestURL.CREATE_FOLDER.getDesc(),fileCreateFolderRequest);
    }

    @Override
    public BaseResponse fileRemove(RequestUtil requestUtil, User currentUser, Integer id) {
        FileRemoveRequest fileRemoveRequest = new FileRemoveRequest();
        fileRemoveRequest.setFileId(id);
        fileRemoveRequest.setUserId(currentUser.getId());
        fileRemoveRequest.setUsername(currentUser.getUsername());
        return requestUtil.fileRemove(RequestURL.REMOVE_FILE_URL.getDesc(), fileRemoveRequest);
    }

}
