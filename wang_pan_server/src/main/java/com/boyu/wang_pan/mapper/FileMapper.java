package com.boyu.wang_pan.mapper;

import com.boyu.wang_pan.model.domain.File;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 余悸
* @description 针对表【file】的数据库操作Mapper
* @createDate 2023-07-19 13:18:01
* @Entity com.boyu.wang_pan.model.domain.File
*/
@Mapper
public interface FileMapper extends BaseMapper<File> {

}




