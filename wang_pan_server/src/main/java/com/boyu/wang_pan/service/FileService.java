package com.boyu.wang_pan.service;

import com.boyu.wang_pan.common.BaseResponse;
import com.boyu.wang_pan.model.domain.File;
import com.baomidou.mybatisplus.extension.service.IService;
import com.boyu.wang_pan.model.domain.User;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
* @author 余悸
* @description 针对表【file】的数据库操作Service
* @createDate 2023-07-18 15:56:13
*/
public interface FileService extends IService<File> {

    /**
     * 获取用户Home页面文件
     * @param newUser 用户
     * @param parentId 父id(目录id)
     * @return
     */
    BaseResponse home(User newUser, Integer parentId);

    /**
     *
     * @param newUser
     * @param parentId
     * @param filename 文件名
     * @param classify 文件种类
     * @return
     */
    BaseResponse homeSearch(User newUser, Integer parentId, String filename, Integer classify);

    /**
     *
     * @param newUser
     * @param parentId
     * @param sortFlag 排序种类（时间、文件名、大小）
     * @param sortType 排序方式（倒序，逆序）
     * @return
     */
    BaseResponse homeSort(User newUser, Integer parentId, Integer sortFlag, Integer sortType);

    /**
     * 上传文件到OSS
     * @param id 用户Id
     * @param uploadFileInfos 上传的文件
     * @param parentid 目录id
     * @return 返回结果
     */
    BaseResponse saveFile(Long id, List<File> uploadFileInfos, Long parentid);

    void removeDBWildFile();


    /**
     * 通过用户Id和文件名来删除文件
     * @param id 用户Id
     * @param fileName 文件名
     */
    void removeFileByName(Integer id, String fileName);

    /**
     * 判断文件容量是否可存
     * @param id 用户id
     * @param length 文件大小
     * @return 是否
     */
    boolean checkCapacity(Long id, int length);

    /**
     * 创建文件夹
     * @param user 用户
     * @param parentId 目录id
     * @param folderName 文件夹名字
     * @return 返回是否成功
     */
    BaseResponse createFolder(User user, Integer parentId, String folderName);

    /**
     * 下载文件
     * @param id 用户id
     * @param fileId 文件id
     * @return 结果
     */
    BaseResponse fileDownload(Integer id, Integer fileId, HttpServletResponse response) throws UnsupportedEncodingException;

    /**
     * 删除文件
     * @param userId 用户id
     * @param fileId  文件id
     * @return 结果
     */
    BaseResponse deleteFile(Integer userId, Integer fileId);
}
