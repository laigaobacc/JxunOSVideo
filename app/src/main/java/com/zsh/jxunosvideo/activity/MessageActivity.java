package com.zsh.jxunosvideo.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zsh.jxunosvideo.R;
import com.zsh.jxunosvideo.bean.ConversationEvent;
import com.zsh.jxunosvideo.bean.User;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;

public class MessageActivity extends AppCompatActivity {
    private User toUser;
    private Button btn;
    private EditText editText;
    private BmobIMConversation conversationEntrance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initData();
        initView();
    }

    private void initView() {
        toUser = (User) getIntent().getSerializableExtra("toUser");
        btn = findViewById(R.id.send_message_btn);
        editText = findViewById(R.id.send_message_edt);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tomessage = editText.getText().toString();

                sendMessage(tomessage);
            }
        });
    }

    public void sendMessage(String s) {
//        //TODO 会话：4.1、创建一个常态会话入口，好友聊天，陌生人聊天
        BmobIMUserInfo info = new BmobIMUserInfo(toUser.getObjectId(), toUser.getUsername(), toUser.getAvatar());
        //info.setUserId(toUser.getObjectId());
        BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, null);
        //TODO 消息：5.1、根据会话入口获取消息管理，在聊天页面以及发送添加好友和同意添加好友请求时使用 
        BmobIMConversation messageManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);
        //TODO 发送消息：6.1、发送文本消息
        BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setContent(s);
        messageManager.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                //发送消息
                Toast.makeText(MessageActivity.this, "消息发送成功", Toast.LENGTH_SHORT).show();
                Log.e("发送", "消息发送成功");
            }
        });
        //通知消息列表更新
        EventBus.getDefault().post(new ConversationEvent(conversationEntrance));
    }

    private void initData() {
        toUser = (User) getIntent().getSerializableExtra("toUser");
    }
}
