package com.zsh.jxunosvideo.bean;

import java.io.Serializable;
import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

public class Course extends BmobObject implements Serializable {

    private String info;
    private String coursename;


    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String cursename) {
        this.coursename = cursename;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


}
