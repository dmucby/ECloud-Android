package com.boyu.wang_pan.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyu.wang_pan.common.BaseResponse;
import com.boyu.wang_pan.common.ResultUtils;
import com.boyu.wang_pan.mapper.FileMapper;
import com.boyu.wang_pan.mapper.UserMapper;
import com.boyu.wang_pan.model.domain.AbsolutePath;
import com.boyu.wang_pan.model.domain.File;
import com.boyu.wang_pan.model.domain.User;
import com.boyu.wang_pan.service.FileService;
import com.boyu.wang_pan.service.UserService;
import com.boyu.wang_pan.util.OSSUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.boyu.wang_pan.common.ErrorCode.*;

/**
* @author 余悸
* @description 针对表【file】的数据库操作Service实现
* @createDate 2023-07-18 15:56:13
*/
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File>
    implements FileService{

    @Resource
    private FileMapper fileMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    @Autowired
    private OSS ossClient;

    OSSUtil ossUtil = new OSSUtil();

    private List<AbsolutePath> getAbsolutePath(Integer user_id, Integer parentId) {
        List<AbsolutePath> absolutePath = new ArrayList<AbsolutePath>();
        absolutePath = findAbsolutePathByParentId(absolutePath, user_id, parentId);
        Collections.reverse(absolutePath);

        return absolutePath;
    }

    // 递归查询所在parentId所在路径
    private List<AbsolutePath> findAbsolutePathByParentId(List<AbsolutePath> absolutePath, Integer user_id, Integer parentId) {
        QueryWrapper<File> query = new QueryWrapper<File>();
        // 不断找根文件
        query.eq("id", parentId);
        query.eq("user_id", user_id);
        File fileOfParentId = fileMapper.selectOne(query);
        if (fileOfParentId != null) {
            AbsolutePath nodeOfFile = new AbsolutePath();
            nodeOfFile.setParentId(fileOfParentId.getId());
            nodeOfFile.setFolderName(fileOfParentId.getFileName());
            absolutePath.add(nodeOfFile);
            findAbsolutePathByParentId(absolutePath, user_id, fileOfParentId.getParentId());
        }

        return absolutePath;
    }

    @Override
    public BaseResponse home(User user, Integer parentId) {
        List<File> files;
        List<AbsolutePath> absolutePathList = new ArrayList<>();

        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        queryWrapper.eq("parent_id", parentId);

        // 直接显示 该用户该路径下的所有文件
        files = fileMapper.selectList(queryWrapper);
        // 查找当前的绝对路径
        if(parentId == -1){
            // 当前为根目录
            AbsolutePath nodeOfFile = new AbsolutePath();
            // 查找根目录的id和名字
            QueryWrapper<File> query = new QueryWrapper<File>();
            query.eq("user_id", user.getId());
            query.eq("parent_id", parentId);
            File fileOfParentId = fileMapper.selectOne(query);
            nodeOfFile.setParentId(fileOfParentId.getId());
            nodeOfFile.setFolderName(fileOfParentId.getFileName());
            if(nodeOfFile != null){
                absolutePathList.add(nodeOfFile);
            }
        }else{
            absolutePathList = getAbsolutePath(user.getId(), parentId);
        }
        return ResultUtils.success(files, absolutePathList);
    }

    @Override
    public BaseResponse homeSearch(User user, Integer parentId, String filename, Integer classify) {
        List<File> files;

        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        queryWrapper.eq("parent_id", parentId);

        // 按照用户id搜索和文件名模糊搜索
        if(StringUtils.isNotBlank(filename)){
            queryWrapper.like("file_name", filename);
            files = fileMapper.selectList(queryWrapper);
            return ResultUtils.success(files);
        }else if(classify > 0){
            // 按照用户id搜索和文件种类
            queryWrapper.eq("logo_id", classify);
            files = fileMapper.selectList(queryWrapper);
            return ResultUtils.success(files);
        }
        return ResultUtils.error(NULL_ERROR, "参数为空！");
    }

    @Override
    public BaseResponse homeSort(User user, Integer parentId, Integer sortFlag, Integer sortType) {
        List<File> files;

        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        queryWrapper.eq("parent_id", parentId);

        if(sortFlag == 1) {
            // id排序
            if (sortType == 1) { // 升序
                queryWrapper.orderByAsc("id");
            } else { // 降序
                queryWrapper.orderByDesc("id");
            }
            files = fileMapper.selectList(queryWrapper);
            return ResultUtils.success(files);
        }else if(sortFlag == 2){
            // 更新时间
            if (sortType == 1) { // 升序
                queryWrapper.orderByAsc("update_time");
            } else { // 降序
                queryWrapper.orderByDesc("update_time");
            }
            files = fileMapper.selectList(queryWrapper);
            return ResultUtils.success(files);
        }else if(sortFlag == 3){
            // 文件大小
            if (sortType == 1) { // 升序
                queryWrapper.orderByAsc("file_size");
            } else { // 降序
                queryWrapper.orderByDesc("file_size");
            }
            files = fileMapper.selectList(queryWrapper);
            return ResultUtils.success(files);
        }
        return ResultUtils.error(NULL_ERROR, "参数为空！");
    }

    @Override
    public BaseResponse saveFile(Long id, List<File> uploadFileInfos, Long parentid) {
        // 判断能否能存
        // user_id
        if(checkFilesCapacity(id, uploadFileInfos)){
            for (File file : uploadFileInfos) {
                ossUtil.delete(ossClient, Math.toIntExact(id), file.getFileName());
            }
            return ResultUtils.error(USER_CAPACITY_FULL, "上传失败(空间已满)");
        }

        QueryWrapper<File> queryWrapper = new QueryWrapper<File>();
        queryWrapper.eq("id", parentid);
        File parentFile = fileMapper.selectOne(queryWrapper);
        // 如果该父目录为根文件
        if(parentid == -1 || parentFile == null){
            return ResultUtils.error(PARAMS_ERROR, "请上传正确的目录");
        }

        for(File file : uploadFileInfos){
            file.setParentId(Math.toIntExact(parentid));
            fileMapper.insert(file);
        }

        // 上传完文件更新经验值
        userService.updateUserGrowthValueByPrimaryKe(id);

        return ResultUtils.success("文件上传成功");
    }

    private boolean checkFilesCapacity(Long id, List<File> uploadFileInfos) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("id", id);
        User user = userMapper.selectOne(queryWrapper);
        // 文件容量计算规则
        return user.getCapacity() + uploadFileInfos.size() > user.getLevel() * 10000;
    }

    @Override
    public void removeDBWildFile() {
        QueryWrapper<File> queryWrapper = new QueryWrapper<File>();
        queryWrapper.ne("id", -1);
        List<File> files = fileMapper.selectList(queryWrapper);

        for (File file : files) {
            // 如果是文件
            if (file.getFolder() == 0) {
                // 如果网盘中不存在
                if (!ossUtil.objectNameExists(ossClient, file.getFileName())) {
                    QueryWrapper<File> query = new QueryWrapper<File>();
                    query.eq("id", file.getId());
                    // 在数据库表中删除该文件
                    fileMapper.delete(queryWrapper);
                }
            }
        }
    }

    @Override
    public void removeFileByName(Integer id, String fileName) {
        QueryWrapper<File> queryWrapper = new QueryWrapper<File>();
        queryWrapper.eq("user_id", id);
        queryWrapper.eq("file_name", fileName);
        fileMapper.delete(queryWrapper);
    }

    public boolean checkCapacity(Long id, int length) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("id", id);
        User user = userMapper.selectOne(queryWrapper);
        // 文件容量计算规则
        return user.getCapacity() + length > user.getLevel() * 10000;
    }

    @Override
    public BaseResponse createFolder(User user, Integer parentId, String folderName) {
        if (StringUtils.isBlank(folderName) || parentId == -1) {
            return ResultUtils.error(PARAMS_ERROR, "请上传正确的文件夹名字");
        }

        File file = new File();

        file.setUserId(user.getId());
        file.setParentId(parentId);
        file.setFolder(1);
        file.setFileName(folderName);
        file.setRemark("文件夹");
        file.setUploadTime(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        file.setUpdateTime(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        file.setClassify("0");

        int insert = fileMapper.insert(file);
        if(insert > 0){
            return ResultUtils.success("文件夹创建成功！");
        }
        return ResultUtils.error(SYSTEM_ERROR, "文件夹创建失败！");
    }

    @Override
    public BaseResponse fileDownload(Integer id, Integer fileId, HttpServletResponse response) throws UnsupportedEncodingException {
        // 查找到文件名来从OSS上下载文件
        QueryWrapper<File> query = new QueryWrapper<File>();
        query.eq("user_id", id);
        query.eq("id", fileId);
        File file = fileMapper.selectOne(query);

        String filename = file.getFileName();
        String stream = OSSUtil.download(ossClient, id, filename);
        if(stream != null){
            updateDownloadCount(fileId);
            // 设置响应头信息
            response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
            return ResultUtils.success(stream);
        }else{
            return ResultUtils.error(PARAMS_ERROR, "下载失败！");
        }
    }

    @Override
    public BaseResponse deleteFile(Integer userId, Integer fileId) {
        QueryWrapper<File> query = new QueryWrapper<File>();
        query.eq("id", fileId);
        query.eq("user_id", userId);
        File file = fileMapper.selectOne(query);
        String filename = file.getFileName();

        if(ossUtil.objectNameExists(ossClient, filename)){
            ossUtil.delete(ossClient, userId, filename);
            fileMapper.delete(query);
            return ResultUtils.success("成功删除！");
        }
        return ResultUtils.error(SYSTEM_ERROR, "删除失败！");
    }

    private void updateDownloadCount(Integer fileId) {
        QueryWrapper<File> query = new QueryWrapper<File>();
        query.eq("id", fileId);
        File file = fileMapper.selectOne(query);
        Integer downloadCount = file.getDownloadCount();
        file.setDownloadCount(downloadCount + 1);
        fileMapper.update(file, query);
    }
}




