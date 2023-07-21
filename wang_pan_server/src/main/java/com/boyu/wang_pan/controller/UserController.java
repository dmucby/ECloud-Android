package com.boyu.wang_pan.controller;


import com.boyu.wang_pan.common.BaseResponse;
import com.boyu.wang_pan.common.ResultUtils;
import com.boyu.wang_pan.model.domain.User;
import com.boyu.wang_pan.model.request.User.*;
import com.boyu.wang_pan.service.FileService;
import com.boyu.wang_pan.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.boyu.wang_pan.common.ErrorCode.*;

/**
 * 用户接口
 */
@RequestMapping("/user")
@RestController // 返回Json
public class UserController {

    @Resource
    UserService userService;

    @Resource
    FileService fileService;


    @PostMapping("/regist")
    public BaseResponse userRegist(@RequestBody UserRegistRequest userRegistRequest) {
        if (userService.requestIsNull(userRegistRequest)){
            return ResultUtils.error(NULL_ERROR, "参数为空！");
        }

        // TODO:邀请码功能待开发
        User newUser = userRegistRequest.getUser();
        String userName = newUser.getUsername();
        String password = newUser.getPassword();
        String inviteCode = userRegistRequest.getCode();

        if(StringUtils.isAnyBlank(userName,password)){
            return ResultUtils.error(PARAMS_ERROR, "请正确书写请求参数");
        }

        return userService.regist(userName, password, inviteCode);
    }


    @PostMapping("/login")
    public BaseResponse userLogin(@RequestBody UserLoginRequest userLoginRequest) {
        if (userService.requestIsNull(userLoginRequest)){
            return ResultUtils.error(NULL_ERROR, "参数为空！");
        }

        User newUser = userLoginRequest.getUser();
        String userName = newUser.getUsername();
        String password = newUser.getPassword();

        if(StringUtils.isAnyBlank(userName,password)){
            return ResultUtils.error(PARAMS_ERROR, "请正确书写请求参数");
        }

       return userService.login(userName, password);
    }

    @PostMapping("/home")
    public BaseResponse userHome(@RequestBody UserHomeRequest userHomeRequest) {

        if (userService.requestIsNull(userHomeRequest)){
            return ResultUtils.error(NULL_ERROR, "参数为空！");
        }

        User newUser = userHomeRequest.getUser();
        Integer parentId = userHomeRequest.getParentId();

        String userName = newUser.getUsername();

        if(userService.checkLogin(userName)){
            return fileService.home(newUser, parentId);
        }else{
            return ResultUtils.error(NOT_LOGIN, "用户还未登录！");
        }
    }

    @PostMapping("/homeSort")
    public BaseResponse userHomeSort(@RequestBody UserHomeSortRequest userHomeSortRequest) {

        if (userService.requestIsNull(userHomeSortRequest)){
            return ResultUtils.error(NULL_ERROR, "参数为空！");
        }

        User newUser = userHomeSortRequest.getUser();
        Integer parentId = userHomeSortRequest.getParentId();
        Integer sortFlag = userHomeSortRequest.getSortFlag();
        Integer sortType = userHomeSortRequest.getSortType();

        String userName = newUser.getUsername();

        if(userService.checkLogin(userName)){
            return fileService.homeSort(newUser, parentId, sortFlag, sortType);
        }else{
            return ResultUtils.error(NOT_LOGIN, "用户还未登录！");
        }
    }

    @PostMapping("/homeSearch")
    public BaseResponse userHomeSearch(@RequestBody UserHomeSearchRequest userHomeSearchRequest) {

        if (userService.requestIsNull(userHomeSearchRequest)){
            return ResultUtils.error(NULL_ERROR, "参数为空！");
        }

        User newUser = userHomeSearchRequest.getUser();
        Integer parentId = userHomeSearchRequest.getParentId();
        String filename = userHomeSearchRequest.getFilename();
        Integer classify = userHomeSearchRequest.getClassify();

        String userName = newUser.getUsername();

        if(userService.checkLogin(userName)){
            return fileService.homeSearch(newUser, parentId, filename, classify);
        }else{
            return ResultUtils.error(NOT_LOGIN, "用户还未登录！");
        }
    }

}
