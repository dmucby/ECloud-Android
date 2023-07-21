package com.boyu.wang_pan.controller;

import com.aliyun.oss.OSS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boyu.wang_pan.common.BaseResponse;
import com.boyu.wang_pan.common.ResultUtils;
import com.boyu.wang_pan.mapper.UserMapper;
import com.boyu.wang_pan.model.domain.File;
import com.boyu.wang_pan.model.domain.User;
import com.boyu.wang_pan.model.request.User.UserHomeSearchRequest;
import com.boyu.wang_pan.model.request.file.FileCreateFolderRequest;
import com.boyu.wang_pan.model.request.file.FileDeleteRequest;
import com.boyu.wang_pan.model.request.file.FileDownloadRequest;
import com.boyu.wang_pan.service.FileService;
import com.boyu.wang_pan.service.UserService;
import com.boyu.wang_pan.util.OSSUtil;
import com.boyu.wang_pan.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.boyu.wang_pan.Constant.FileConstant.DOMAIN_NAME;
import static com.boyu.wang_pan.common.ErrorCode.NOT_LOGIN;
import static com.boyu.wang_pan.common.ErrorCode.NULL_ERROR;
import static com.boyu.wang_pan.common.ErrorCode.PARAMS_ERROR;

@RequestMapping("/file")
@RestController // 返回Json
public class FileController {

    @Resource
    UserService userService;

    @Resource
    UserMapper userMapper;

    @Resource
    FileService fileService;

    @Resource
    RedisUtil redisUtil;

    OSSUtil ossUtil = new OSSUtil();

    @Autowired
    OSS ossClient;

    @Resource
    private ThreadPoolTaskExecutor taskExecutor;

    @PostMapping("/download")
    public BaseResponse fileDownload(@RequestBody FileDownloadRequest fileDownloadRequest, HttpServletResponse response) throws UnsupportedEncodingException {
        if (userService.requestIsNull(fileDownloadRequest)){
            return ResultUtils.error(NULL_ERROR, "参数为空！");
        }

        Integer id = fileDownloadRequest.getId();
        Integer fileId = fileDownloadRequest.getFileId();
        String userName = fileDownloadRequest.getUsername();

        if(id == 0 || fileId == 0){
            return ResultUtils.error(PARAMS_ERROR, "请正确书写请求参数");
        }

        if(userService.checkLogin(userName)){
            return fileService.fileDownload(id, fileId, response);
        }else{
            return ResultUtils.error(NOT_LOGIN, "用户还未登录！");
        }

    }

    @PostMapping("/upload")
    public BaseResponse fileUpload(
            @RequestParam("myFile") MultipartFile[] myFile,
            @RequestParam("id") Long id,
            @RequestParam(value = "parentid", required = false) Long parentid
    ) throws IOException {
        if (myFile.length == 0) {
            throw new IOException("上传文件为空");
        }
        if(fileService.checkCapacity(id, myFile.length)){
            throw new IOException("上传失败(空间已满)");
        }
        List<File> uploadFileInfos = getUploadFileInfosAndUploadFileToOSS(myFile, id);
        if (uploadFileInfos == null) {
            throw new IOException("移除命名不规范的文件后,上传列表为空。未成功上传任何文件!");
        }
        return fileService.saveFile(id, uploadFileInfos, parentid);
    }

    // 协调文件上传，文件先缓存到Redis，而后异步持久化至OSS
    private List<File> getUploadFileInfosAndUploadFileToOSS(MultipartFile[] myFile, Long id) {
        // 文件名合法的文件列表
        List<File> files = new ArrayList<>();
        // 顺序缓存至Redis的key 对应files顺序
        Queue<String> queue = new LinkedList<>();
        // 查询user
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("id", id);
        User user = userMapper.selectOne(queryWrapper);
        for (MultipartFile multipartFile : myFile) {
            String fileName = multipartFile.getOriginalFilename();
            if (StringUtils.isBlank(fileName)) {
                continue;
            }
            // 去掉空格
            fileName = fileName.replace(" ", "");
            if (checkUploadFileName(fileName)) {
                long fileSize = multipartFile.getSize();
                String fileType = getFileType(fileName);
                File file = assembleFile(user, fileName, fileSize, fileType);
                try {
                    String fileCacheKey = DOMAIN_NAME + "_" + UUID.randomUUID().toString();
                    redisUtil.setFileCache(fileCacheKey, multipartFile.getBytes());
                    queue.offer(fileCacheKey);
                    files.add(file);
                } catch (IOException e01) { // 尝试重新缓存
                    e01.printStackTrace();
                    try {
                        String fileCacheKey = DOMAIN_NAME + "_" + UUID.randomUUID().toString();
                        redisUtil.setFileCache(fileCacheKey, multipartFile.getBytes());
                        queue.offer(fileCacheKey);
                        files.add(file);
                    } catch (IOException e02) {
                        e02.printStackTrace();
                    }
                }
            }
        }

        // 文件名合法的文件列表不为空
        if (files.size() > 0) {
            // 异步持久操作
            taskExecutor.execute(() -> {
                List<String> fileNames = new ArrayList<>();
                for (File file : files) {
                    String fileCacheKey = queue.poll();
                    if (redisUtil.cacheKeyExists(fileCacheKey)) { // key存在 完整性校验
                        byte[] fileCache = redisUtil.getFileCache(fileCacheKey); // get
                        if (fileCache.length != file.getFileSize()) { // 不完整 get again
                            fileCache = redisUtil.getFileCache(fileCacheKey);
                        }
                        if (fileCache.length == file.getFileSize()) { // 完整-持久化
                            ossUtil.upload(ossClient, file.getFileName(),user.getId(), fileCache);
                        } else { // 依旧不完整-移除
                            fileNames.add(file.getFileName());
                        }
                        redisUtil.removeFileCache(fileCacheKey);
                    } else {
                        fileNames.add(file.getFileName());
                    }
                }

                if (fileNames.size() > 0) {
                    taskExecutor.execute(() -> {
                        for (String fileName : fileNames) {
                            fileService.removeFileByName(user.getId(), fileName);
                        }
                    }, 1000 * 60);
                }
                ossClient.shutdown();
            });
            return files;
        }
        return null;
    }

    // 组装上传文件对象
    private File assembleFile(User user, String fileName, long fileSize, String fileType) {
        File file = new File();

        file.setUserId(user.getId());
        file.setFolder(0);
        file.setFileName(fileName);
        file.setFileSize(fileSize);
        file.setFileType(fileType);
        file.setClassify(getClassify(fileName.substring(fileName.lastIndexOf("."))));
        file.setUploadTime(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        file.setUpdateTime(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());

        return file;
    }

    // 判断文件类型 1:文本类型   2:图像类型  3:视频类型  4:音乐类型  0:未知类型  -1:文件夹
    public String getClassify(String type) {
        if (".txt".equals(type) || ".doc".equals(type) || ".docx".equals(type)
                || ".wps".equals(type) || ".word".equals(type) || ".html".equals(type) || ".pdf".equals(type)) {
            return "1";
        }
        if (".bmp".equals(type) || ".gif".equals(type) || ".jpg".equals(type)
                || ".pic".equals(type) || ".png".equals(type) || ".jpeg".equals(type) || ".webp".equals(type)
                || ".svg".equals(type)) {
            return "2";
        }
        if (".avi".equals(type) || ".mov".equals(type) || ".qt".equals(type)
                || ".asf".equals(type) || ".rm".equals(type) || ".navi".equals(type) || ".wav".equals(type)
                || ".mp4".equals(type)) {
            return "3";
        }
        if (".mp3".equals(type) || ".wma".equals(type)) {
            return "4";
        }
        return "0";
    }

    // 检查上传文件名是否合法
    public Boolean checkUploadFileName(String fileName) {
        final String format = "[^\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w-_.,()（）《》]";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(fileName);
        return !matcher.find() && fileName.contains(".");
    }

    /**
     * 获取文件类型
     * @param fileName
     * @return
     */
    private static String getFileType(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }

    @PostMapping("/createfolder")
    public BaseResponse fileCreateFolder(@RequestBody FileCreateFolderRequest fileCreateFolderRequest) {

        if (userService.requestIsNull(fileCreateFolderRequest)){
            return ResultUtils.error(NULL_ERROR, "参数为空！");
        }

        User user = fileCreateFolderRequest.getUser();
        Integer parentId = fileCreateFolderRequest.getParentId();
        String folderName = fileCreateFolderRequest.getFolderName();

        String userName = user.getUsername();

        if(userService.checkLogin(userName)){
            return fileService.createFolder(user, parentId, folderName);
        }else{
            return ResultUtils.error(NOT_LOGIN, "用户还未登录！");
        }
    }

    @PostMapping("/delete")
    public BaseResponse fileDelete(@RequestBody FileDeleteRequest fileDeleteRequest) {
        if (userService.requestIsNull(fileDeleteRequest)){
            return ResultUtils.error(NULL_ERROR, "参数为空！");
        }

        Integer userId = fileDeleteRequest.getUserId();
        Integer fileId = fileDeleteRequest.getFileId();
        String userName = fileDeleteRequest.getUsername();

        if(userId == 0 || fileId == 0){
            return ResultUtils.error(PARAMS_ERROR, "请正确书写请求参数");
        }

        if(userService.checkLogin(userName)){
            return fileService.deleteFile(userId, fileId);
        }else{
            return ResultUtils.error(NOT_LOGIN, "用户还未登录！");
        }

    }

}
