package com.zsh.jxunosvideo.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class Lesson extends BmobObject implements Serializable {
    /**
     * 课程名
     */
    private String lessonname;

    /**
     *小课所在的大课
     */
    private Course course;

    /**
     * 视频链接
     * @return
     */
    private String videourl;

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getLessonname() {
        return lessonname;
    }

    public void setLessonname(String lessonname) {
        this.lessonname = lessonname;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "lessonname='" + lessonname + '\'' +
                ", course=" + course +
                '}';
    }
}
