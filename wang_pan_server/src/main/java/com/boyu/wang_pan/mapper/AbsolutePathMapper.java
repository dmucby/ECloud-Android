package com.boyu.wang_pan.mapper;

import com.boyu.wang_pan.model.domain.AbsolutePath;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 余悸
* @description 针对表【absolute_path(文件路径)】的数据库操作Mapper
* @createDate 2023-07-18 10:06:27
* @Entity com.boyu.wang_pan.model.domain.AbsolutePath
*/
@Mapper
public interface AbsolutePathMapper extends BaseMapper<AbsolutePath> {

}




