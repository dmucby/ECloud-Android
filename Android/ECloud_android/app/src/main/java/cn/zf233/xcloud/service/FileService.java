package cn.zf233.xcloud.service;

import java.io.File;

import cn.zf233.xcloud.common.BaseResponse;
import cn.zf233.xcloud.entity.User;
import cn.zf233.xcloud.util.RequestUtil;

public interface FileService {


    /**
     * 文件下载
     * @param requestUtil 请求工具
     * @param currentUser 用户
     * @param id fileid
     * @return 下载的文件
     */
    File fileDownload(RequestUtil requestUtil, User currentUser, Integer id);


    /**
     * 文件上传
     * @param requestUtil 请求工具
     * @param currentUser 用户
     * @param file file
     * @param parentId 父目录id
     * @return 上传是否成功
     */
    BaseResponse uploadFile(RequestUtil requestUtil, User currentUser, File file, Integer parentId);

    /**
     * 创建文件夹
     * @param currentUser 用户
     * @param filename 文件夹名字
     * @param parentId 父目录id
     * @return 上传是否成功
     */
    BaseResponse createFolder(RequestUtil requestUtil, User currentUser, String filename, Integer parentId);

    /**
     * 删除文件
     * @param requestUtil 请求工具
     * @param currentUser 用户
     * @param id 父目录id
     * @return  删除是否成功
     */
    BaseResponse fileRemove(RequestUtil requestUtil, User currentUser, Integer id);
}
