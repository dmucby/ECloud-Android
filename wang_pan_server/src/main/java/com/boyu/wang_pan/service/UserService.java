package com.boyu.wang_pan.service;

import com.boyu.wang_pan.common.BaseResponse;
import com.boyu.wang_pan.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 余悸
* @description 针对表【user】的数据库操作Service
* @createDate 2023-07-17 11:32:14
*/
public interface UserService extends IService<User> {

    /**
     * 判断请求参数是否为空
     * @param request 请求参数
     * @return 空 或者 不为空
     */
    boolean requestIsNull(Object request);

    /**
     * 用户注册
     * @param user 注册的用户
     */
    void regist(User user);

    /**
     * 带邀请码用户注册
     * @param userName 用户名
     * @param password 密码
     * @param inviteCode 邀请码
     * @return 是否成功
     */
    BaseResponse regist(String userName, String password, String inviteCode);


    /**
     *
     * @param userName 用户名
     * @param password 密码
     * @return 登录是否成功
     */
    BaseResponse login(String userName, String password);

    /**
     * 查看用户是否登录
     * @param userName 用户名
     * @return 是否在redis中
     */
    boolean checkLogin(String userName);

    /**
     * 更新用户等级
     */
    void refreshUserLevelTask();

    void removeUserInfoOfRegistFailTask();

    /**
     * 更新用户成长值
     * @param id 用户id
     */
    void updateUserGrowthValueByPrimaryKe(Long id);
}
