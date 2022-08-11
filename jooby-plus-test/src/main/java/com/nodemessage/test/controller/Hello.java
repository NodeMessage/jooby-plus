package com.nodemessage.test.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nodemessage.jooby.mybatis.JoobyMybatisPlus;
import com.nodemessage.test.dao.FileInfosDao;
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
        FileInfosDao mapper = JoobyMybatisPlus.getMapper(FileInfosDao.class);
        System.out.println(mapper.selectList(new QueryWrapper<>()));
        return "ok";
    }

}
