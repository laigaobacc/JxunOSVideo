package com.zsh.jxunosvideo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zsh.jxunosvideo.R;
import com.zsh.jxunosvideo.adapter.MsgAdapter;
import com.zsh.jxunosvideo.bean.ConversationEvent;
import com.zsh.jxunosvideo.bean.Msg;
import com.zsh.jxunosvideo.bean.NewMessageEntity;
import com.zsh.jxunosvideo.bean.User;
import com.zsh.jxunosvideo.utils.MyUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.v3.exception.BmobException;

import static java.util.Collections.*;

public class UIChatActivity extends AppCompatActivity {
    private List<Msg> msgList = new ArrayList<>();
    private RecyclerView msgvView;
    private EditText input_text;
    private Button btn_send;
    private MsgAdapter msgAdapter;
    private User toUser;
    private ImageView to_back;
    //会话
    BmobIMConversation bmobIMConversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uichat);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    private void initData() {
        bmobIMConversation = (BmobIMConversation) getIntent().getSerializableExtra("conversation");
        getAllMessage();
    }

    //得到新的消息
    @Subscribe
    public void getNewMessage(NewMessageEntity entity) {
        BmobIMMessage message = entity.getMsg();
        String fromId = message.getFromId();
        Msg msg;
        int flag = 0;
        if (fromId.equals(MyUtils.getUser().getObjectId())) {
            flag = Msg.TYPE_SENT;
            msg = new Msg(MyUtils.getUser().getAvatar(), message.getContent(), flag,message.getCreateTime());
        } else {
            flag = Msg.TYPE_RECEIVED;
            msg = new Msg(message.getBmobIMUserInfo().getAvatar(), message.getContent(), flag,message.getCreateTime());
        }
        msgList.add(msg);
        // 当有新消息是，刷新ListView页面中的显示
        msgAdapter.notifyItemInserted(msgList.size() - 1);
        // 滚动页面到ListView的最后一行
        msgvView.scrollToPosition(msgList.size() - 1);
    }

    //得到指定会话的聊天记录
    private void getAllMessage() {
        List<BmobIMMessage> messages = bmobIMConversation.getMessages();
        User myuser = MyUtils.getUser();
        Log.e("UIChatActivity", myuser.getObjectId());

        for (BmobIMMessage bmobIMMessage : messages) {
            String fromId = bmobIMMessage.getFromId();
            if (fromId.equals(myuser.getObjectId())) {
                Msg msg = new Msg(MyUtils.getUser().getAvatar(),bmobIMMessage.getContent(), Msg.TYPE_SENT,bmobIMMessage.getCreateTime());
                msgList.add(msg);
            } else {
                Msg msg = new Msg(bmobIMConversation.getConversationIcon(),bmobIMMessage.getContent(), Msg.TYPE_RECEIVED,bmobIMMessage.getCreateTime());
                msgList.add(msg);
            }
            Collections.sort(msgList,new MyCmp());
            msgAdapter.notifyItemInserted(msgList.size() - 1);
            // 滚动页面到ListView的最后一行
            msgvView.scrollToPosition(msgList.size() - 1);
            Log.e("UIChatActivity:fromId:", fromId);
        }
    }
    //比较器
    class MyCmp implements Comparator<Msg> {
        @Override
        public int compare(Msg o1, Msg o2) {
            return (int) (o1.getTime()-o2.getTime());
        }
    }
    private void initView() {
        msgvView = findViewById(R.id.msg_recycle_view);
        input_text = findViewById(R.id.input_text);
        btn_send = findViewById(R.id.btn_send);
        to_back=findViewById(R.id.to_back);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgvView.setLayoutManager(layoutManager);
        msgAdapter = new MsgAdapter(msgList, this);
        msgvView.setAdapter(msgAdapter);
        to_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = input_text.getText().toString();
                if (!"".equals(input)) {
                    sendMessage(input);
                    input_text.setText("");
                }
            }
        });
    }

    public void sendMessage(String s) {
        //TODO 会话：4.1、创建一个常态会话入口，好友聊天，陌生人聊天
        //TODO 消息：5.1、根据会话入口获取消息管理，在聊天页面以及发送添加好友和同意添加好友请求时使用 
        BmobIMConversation messageManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), bmobIMConversation);
        //TODO 发送消息：6.1、发送文本消息
        BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setContent(s);
        messageManager.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                //发送消息
                Toast.makeText(UIChatActivity.this, "消息发送成功", Toast.LENGTH_SHORT).show();
                Log.e("发送", "消息发送成功");
                addBrush(bmobIMMessage);
            }
        });

    }
    //发送后本地更新聊天框
    void addBrush(BmobIMMessage bmobIMMessage) {

        msgList.add(new Msg(MyUtils.getUser().getAvatar(),bmobIMMessage.getContent(),Msg.TYPE_SENT,bmobIMMessage.getCreateTime()));
        // 当有新消息是，刷新ListView页面中的显示
        msgAdapter.notifyItemInserted(msgList.size() - 1);
        // 滚动页面到ListView的最后一行
        msgvView.scrollToPosition(msgList.size() - 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
