package com.zsh.jxunosvideo.bean;

import cn.bmob.v3.BmobObject;

public class NetDanmu extends BmobObject {
    public String content;
    public int textColor;
    public String time;
    public long showTime;
    public User user;
    public Lesson lesson;

    @Override
    public String toString() {
        return "NetDanmu{" +
                "content='" + content + '\'' +
                ", textColor=" + textColor +
                ", time='" + time + '\'' +
                ", showTime=" + showTime +
                ", user=" + user +
                ", lesson=" + lesson +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getShowTime() {
        return showTime;
    }

    public void setShowTime(long showTime) {
        this.showTime = showTime;
    }
}
