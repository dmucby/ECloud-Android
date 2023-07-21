package cn.zf233.xcloud.service.impl;

import com.google.gson.reflect.TypeToken;

import java.util.List;

import cn.zf233.xcloud.common.RequestURL;
import cn.zf233.xcloud.common.ResponseCodeENUM;
import cn.zf233.xcloud.common.BaseResponse;
import cn.zf233.xcloud.entity.File;
import cn.zf233.xcloud.common.RequestBody;
import cn.zf233.xcloud.entity.User;
import cn.zf233.xcloud.request.user.UserHomeRequest;
import cn.zf233.xcloud.request.user.UserHomeSearchRequest;
import cn.zf233.xcloud.request.user.UserHomeSortRequest;
import cn.zf233.xcloud.request.user.UserLoginRequest;
import cn.zf233.xcloud.request.user.UserRegistRequest;
import cn.zf233.xcloud.service.UserService;
import cn.zf233.xcloud.util.RequestUtil;

/**
 * Created by zf233 on 11/29/20
 */
public class UserServiceImpl implements UserService {

    @Override
    public BaseResponse<User> login(RequestUtil requestUtil, User user) {
        UserLoginRequest body = new UserLoginRequest();
        body.setUser(user);
        String requestName = "UserLoginRequest";
        BaseResponse response = requestUtil.requestUserXCloudServer(RequestURL.LOGIN_URL.getDesc(), requestName, body, new TypeToken<BaseResponse<User>>() {
        });
        if (response != null) {
            if (response.getStatus() == ResponseCodeENUM.SUCCESS.getCode()) {
                return response;
            }
            response.setData(null);
        }
        return response;
    }

    @Override
    public BaseResponse<List<File>> home(RequestUtil requestUtil, User user, Integer parentid) {
        UserHomeRequest body = new UserHomeRequest();
        body.setUser(user);
        body.setParentId(parentid);
        String requestHomeName = "UserHomeRequest";

        return requestUtil.requestUserXCloudServer(RequestURL.HOME_URL.getDesc(), requestHomeName, body, new TypeToken<BaseResponse<List<File>>>() {
        });
    }

    @Override
    public BaseResponse<List<File>> homeSort(RequestUtil requestUtil, User user, Integer parentid, Integer sortFlag) {
        UserHomeSortRequest body = new UserHomeSortRequest();
        body.setUser(user);
        body.setParentId(parentid);
        body.setSortFlag(sortFlag);
        body.setSortType(1);
        String requestHomeName = "UserHomeSortRequest";
        if (sortFlag != null && !"".equals(sortFlag)) {
            body.setSortFlag(sortFlag);
            body.setSortType(0);
        }
        return requestUtil.requestUserXCloudServer(RequestURL.HOME_SORT_URL.getDesc(), requestHomeName, body, new TypeToken<BaseResponse<List<File>>>() {
        });
    }

    @Override
    public BaseResponse<List<File>> homeSearch(RequestUtil requestUtil, User user, Integer parentid, String searchString) {
        UserHomeSearchRequest body = new UserHomeSearchRequest();
        body.setUser(user);
        body.setFilename(searchString);
        body.setParentId(parentid);
//        body.setClassify();
        String requestHomeName = "UserHomeSearchRequest";
        return requestUtil.requestUserXCloudServer(RequestURL.HOME_SEARCH_URL.getDesc(), requestHomeName, body, new TypeToken<BaseResponse<List<File>>>() {
        });
    }


    @Override
    public BaseResponse<User> regist(RequestUtil requestUtil, User user, String code) {
        UserRegistRequest body = new UserRegistRequest();
        body.setCode(code);
        body.setUser(user);
        String requestName = "UserRegistRequest";
        BaseResponse<User> response = requestUtil.requestUserXCloudServer(RequestURL.REGIST_URL.getDesc(), requestName, body, new TypeToken<BaseResponse<User>>() {
        });
//        if (response != null) {
//            if (response.getStatus() == ResponseCodeENUM.SUCCESS.getCode()) {
//                response.getData().setPassword(user.getPassword());
//                return response;
//            }
//            response.setData(null);
//        }
        return response;
    }

    @Override
    public BaseResponse<User> update(RequestUtil requestUtil, User user) {
        RequestBody body = new RequestBody();
        body.setUser(user);
        BaseResponse<User> response = requestUtil.requestUserXCloudServer(RequestURL.UPDATE_URL.getDesc(), "test", body, new TypeToken<BaseResponse<User>>() {
        });
        if (response != null) {
            if (response.getStatus() == ResponseCodeENUM.SUCCESS.getCode()) {
                response.getData().setPassword(user.getPassword());
                return response;
            }
            response.setData(null);
        }
        return response;
    }
}
