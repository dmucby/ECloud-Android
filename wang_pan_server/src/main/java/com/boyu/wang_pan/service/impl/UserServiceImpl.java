package com.boyu.wang_pan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyu.wang_pan.common.BaseResponse;
import com.boyu.wang_pan.common.ResultUtils;
import com.boyu.wang_pan.mapper.FileMapper;
import com.boyu.wang_pan.mapper.UserMapper;
import com.boyu.wang_pan.model.domain.File;
import com.boyu.wang_pan.model.domain.User;
import com.boyu.wang_pan.service.UserService;

//import com.boyu.wang_pan.util.RedisUtil;
import com.boyu.wang_pan.util.RedisUtil;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static com.boyu.wang_pan.Constant.FileConstant.FOLDER;
import static com.boyu.wang_pan.common.ErrorCode.*;
import static com.sun.javafx.font.FontResource.SALT;

/**
* @author 余悸
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-07-17 11:32:14
*/
@Service

public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public boolean requestIsNull(Object request) {
        return request == null;
    }

    @Override
    public void regist(User user) {

    }

    @Override
    public BaseResponse regist(String userName, String password, String inviteCode) {

//        if(checkIsRegistered(userName)){
//            return ResultUtils.error(SYSTEM_ERROR, "用户名户已被注册");
//        }

        User newUser = new User();

        String uuid = UUID.randomUUID().toString();
        String saltId = SALT + uuid;

        String newPassword = SALT + password;

        newUser.setUsername(userName);
        newUser.setNickname(userName);
        newUser.setPassword(newPassword);
        newUser.setRole(0); // 默认用户
        newUser.setLevel(1);
        newUser.setGrowthValue(0);
        newUser.setCapacity(0);
        newUser.setCreateTime(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        newUser.setUpdateTime(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());

        if (userMapper.insert(newUser) > 0) {
            // 记录当前的用户
//            redisUtil.set(saltId, uuid);
            // 为新用户初始化根目录
            File rootNode =  registRootNode(newUser);
            if(fileMapper.insert(rootNode) > 0){
                return ResultUtils.success(cleanUser(newUser));
            }else{
                userMapper.deleteById(newUser);
                return ResultUtils.error(SYSTEM_ERROR, "初始化跟文件失败");
            }

        }else{
            return ResultUtils.error(SYSTEM_ERROR, "系统异常");
        }

    }

    private boolean checkIsRegistered(String userName) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        wrapper.eq("username", userName);
        User user = userMapper.selectOne(wrapper);

        return user != null;
    }

    /**
     * 为注册用户创建根目录
     * @param newUser 新用户
     * @return 文件
     */
    private File registRootNode(User newUser) {
        File rootNode =new File();

        rootNode.setUserId(newUser.getId());
        rootNode.setLogoId(FOLDER);
        // -1 根节点
        rootNode.setParentId(-1);
        rootNode.setFolder(1);
        rootNode.setFileName("根文件夹");
        rootNode.setFileSize(0L);
        rootNode.setFileType("文件夹");
        rootNode.setRemark("根节点");
        rootNode.setDownloadCount(0);
        rootNode.setUploadTime(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        rootNode.setUpdateTime(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());

        return rootNode;
    }

    @Override
    public BaseResponse login(String userName, String password) {
        String newPassword = SALT + password;

        QueryWrapper<User> wrapper = new QueryWrapper<>();

        wrapper.eq("username",userName);
        wrapper.eq("password",newPassword);

        User user = userMapper.selectOne(wrapper);

        if(user != null){
            if(user.getRole() == 1){
                return ResultUtils.error(USER_LOCK, "用户被锁定");
            }
            String uuid = UUID.randomUUID().toString();
            redisUtil.set(userName, uuid);
            return ResultUtils.success(cleanUser(user));
        }else{
            return ResultUtils.error(SYSTEM_ERROR, "系统异常");
        }
    }

    @Override
    public boolean checkLogin(String userName) {
        return redisUtil.get(userName) != null;
    }

    private User cleanUser(User user) {
        User cleanUser = new User();

        cleanUser.setId(user.getId());
        cleanUser.setUsername(user.getUsername());
        cleanUser.setNickname(user.getNickname());
        cleanUser.setRole(user.getRole());
        cleanUser.setLevel(user.getLevel());
        cleanUser.setGrowthValue(user.getGrowthValue());
        cleanUser.setCapacity(user.getCapacity());

        return cleanUser;
    }


    // 检查用户细节
    private Boolean checkUserInfo(User user) {
        if (user == null) {
            return true;
        }

        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            return true;
        }

        return user.getUsername().trim().length() < 5 || user.getPassword().trim().length() < 5;
    }

    // 刷新用户等级
    @Override
    @Transactional
    public void refreshUserLevelTask() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.ne("id", -1);
        List<User> users = userMapper.selectList(queryWrapper);
        for (User user : users) {

            int userLevel = (user.getGrowthValue() - 1) / 100 + 1;
            if (userLevel != user.getLevel()) {
                user.setLevel(userLevel);
                user.setUpdateTime(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
                // 更新所有有变化的user
                updateByPrimaryKeySelective(user);
            }
        }
    }

    private void updateByPrimaryKeySelective(User user) {
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", user.getId())
                .set(user.getOpenId() != null, "user_id", user.getOpenId())
                .set( user.getUsername() != null, "username", user.getUsername())
                .set(user.getNickname() != null, "nickname", user.getNickname())
                .set(user.getPassword() != null, "password", user.getPassword())
                .set(user.getQuestion() != null, "question", user.getQuestion())
                .set(user.getAnswer() != null, "answer", user.getAnswer())
                .set(user.getRole() != null, "role", user.getRole())
                .set(user.getLevel() != null, "level", user.getLevel())
                .set(user.getGrowthValue() != null, "growth_value", user.getGrowthValue())
                .set(user.getCreateTime() != null, "create_time", user.getCreateTime())
                .set(user.getUpdateTime() != null, "update_time", user.getUpdateTime());
        userMapper.update(user, wrapper);
    }

    // 清除系统使用的redis缓存
    @Override
    @Transactional
    public void removeUserInfoOfRegistFailTask() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.ne("id", -1);
        List<User> users = userMapper.selectList(queryWrapper);
        for (User user : users) {

            if (user.getRole() == -1) {
                redisUtil.remove(user.getUsername());

                QueryWrapper<File> wrapper = new QueryWrapper<File>();
                wrapper.eq("user_id", user.getId());
                wrapper.eq("parent_id", -1);
                File file = fileMapper.selectOne(wrapper);
                if (file != null) {
                    fileMapper.deleteById(file.getId());
                }
                userMapper.deleteById(user.getId());
            }
        }
    }

    @Override
    public void updateUserGrowthValueByPrimaryKe(Long id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("id", id);
        User user = userMapper.selectOne(queryWrapper);
        // 上传一次文件经验值+1
        user.setGrowthValue(user.getGrowthValue() + 1);
        user.setUpdateTime(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        updateByPrimaryKeySelective(user);
    }

}




