package com.nodemessage.test.controller;


import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wjsmc
 * @date 2022/8/6 19:51
 * @description
 **/
@Mapper
public interface FileInfoDao {
    List<FileInfoEntity> get();
}
