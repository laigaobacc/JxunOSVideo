package com.zsh.jxunosvideo.bean;

import cn.bmob.newim.bean.BmobIMConversation;

public class ConversationEvent {
    BmobIMConversation conversation;

    public ConversationEvent(BmobIMConversation conversation) {
        this.conversation = conversation;
    }

    public BmobIMConversation getConversation() {
        return conversation;
    }

    public void setConversation(BmobIMConversation conversation) {
        this.conversation = conversation;
    }
}
