package com.zsh.jxunosvideo.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zsh.jxunosvideo.R;
import com.zsh.jxunosvideo.adapter.ConversationAdapter;
import com.zsh.jxunosvideo.bean.ConversationEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.bean.BmobIMConversation;


public class MainMessageFragment extends Fragment {
    private RecyclerView recyclerView;
    Map<String,BmobIMConversation> map=new HashMap<>();

    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what==111)
            {
                Log.e("handler:","一直运行");
                getAllConversations();
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_main_message, null, false);
        initView(v);
        initData();
        return v;
    }
    //在线单个添加会话
    @Subscribe
    public void addCv(ConversationEvent event){
        map.put(event.getConversation().getConversationTitle(),event.getConversation());
    }
    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = Message.obtain();
                    message.what = 111;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    private void initView(View v) {
        recyclerView=v.findViewById(R.id.recyler_conversation);

    }
    private void getAllConversations(){
        List<BmobIMConversation> bmobIMConversations=new ArrayList(map.values());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ConversationAdapter(bmobIMConversations,getActivity()));//设置适配器
    }



}
