package com.nodemessage.test.controller;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author wjsmc
 * @date 2022/8/6 19:51
 * @description
 **/
@TableName("file_info")
public class FileInfoEntity implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String md5;
    private Long start;
    private Long end;
    private Long toal;
    private String name;
    private Integer mark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public Long getToal() {
        return toal;
    }

    public void setToal(Long toal) {
        this.toal = toal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }
}
