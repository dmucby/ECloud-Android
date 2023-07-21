package com.boyu.wang_pan.mapper;

import com.boyu.wang_pan.model.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 余悸
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-07-19 12:58:55
* @Entity com.boyu.wang_pan.model.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




