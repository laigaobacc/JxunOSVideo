package com.zsh.jxunosvideo.bean;

import cn.bmob.newim.bean.BmobIMMessage;

public class NewMessageEntity {
    public BmobIMMessage msg;

    public NewMessageEntity(BmobIMMessage msg) {
        this.msg = msg;
    }

    public BmobIMMessage getMsg() {
        return msg;
    }

    public void setMsg(BmobIMMessage msg) {
        this.msg = msg;
    }
}
