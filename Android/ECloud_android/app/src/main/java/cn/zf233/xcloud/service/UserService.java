package cn.zf233.xcloud.service;

import java.util.List;

import cn.zf233.xcloud.common.BaseResponse;
import cn.zf233.xcloud.entity.File;
import cn.zf233.xcloud.entity.User;
import cn.zf233.xcloud.util.RequestUtil;

/**
 * Created by zf233 on 11/28/20
 */
public interface UserService {
    BaseResponse<User> login(RequestUtil requestUtil, User user);

    BaseResponse<List<File>> home(RequestUtil requestUtil, User user, Integer folderid);

    BaseResponse<List<File>> homeSort(RequestUtil requestUtil, User user, Integer folderid, Integer sortFlag);

    BaseResponse<List<File>> homeSearch(RequestUtil requestUtil, User user, Integer folderid, String searchString);

    BaseResponse<User> regist(RequestUtil requestUtil, User user, String code);

    BaseResponse<User> update(RequestUtil requestUtil, User user);
}
