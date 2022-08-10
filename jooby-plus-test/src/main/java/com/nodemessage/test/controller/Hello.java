package com.nodemessage.test.controller;

import com.github.pagehelper.PageHelper;
import com.nodemessage.jooby.mybatis.JoobyMybatis;
import io.jooby.annotations.GET;
import io.jooby.annotations.Path;

/**
 * @author wjsmc
 * @date 2022/8/8 23:29
 * @description
 **/
@Path("hello")
public class Hello {

    @GET("say")
    public String test() {
        PageHelper.startPage(1, 3);
        FileInfoDao mapper = JoobyMybatis.getMapper(FileInfoDao.class);
        System.out.println(mapper.get());
        return "ok";
    }

}
