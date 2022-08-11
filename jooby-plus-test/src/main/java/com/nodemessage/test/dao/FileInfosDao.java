package com.nodemessage.test.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nodemessage.test.controller.FileInfoEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wjsmc
 * @date 2022/8/6 19:51
 * @description
 **/
@Mapper
public interface FileInfosDao extends BaseMapper<FileInfoEntity> {

}
