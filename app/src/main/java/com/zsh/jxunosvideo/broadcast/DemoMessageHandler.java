package com.zsh.jxunosvideo.broadcast;

import android.util.Log;
import android.widget.Toast;

import com.zsh.jxunosvideo.activity.MainActivity;
import com.zsh.jxunosvideo.bean.ConversationEvent;
import com.zsh.jxunosvideo.bean.NewMessageEntity;
import com.zsh.jxunosvideo.bean.User;
import com.zsh.jxunosvideo.linster.UpdateCacheListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class DemoMessageHandler extends BmobIMMessageHandler {
    private final String TAG="接受消息的广播接收器";
    private UpdateCacheListener updateCacheListener;
    @Override
    public void onMessageReceive(final MessageEvent event) {
        Toast.makeText(MainActivity.mainactivitycontent, "有1条在线消息", Toast.LENGTH_SHORT).show();
        //在线消息
        //在线消息
        updateUserInfo(event, new UpdateCacheListener() {
            @Override
            public void done() {
                Log.e(TAG, "bindView:  在线消息： "+"都更新完毕");

                EventBus.getDefault().post(new ConversationEvent(event.getConversation()));
                EventBus.getDefault().post(new NewMessageEntity(event.getMessage()));
            }
        });
    }

    @Override
    public void onOfflineReceive(OfflineMessageEvent offlineMessageEvent) {

        //离线消息，每次connect的时候会查询离线消息，如果有，此方法会被调用
        Map<String, List<MessageEvent>> map = offlineMessageEvent.getEventMap();
        Toast.makeText(MainActivity.mainactivitycontent, "有" + map.size() + "个用户发来离线消息", Toast.LENGTH_SHORT).show();
        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
            final List<MessageEvent> list = entry.getValue();
            Log.e(TAG, "onOfflineReceive: "+"用户" + entry.getKey() + "发来" + list.size() + "条消息" );
            for (int i = 0; i < list.size(); i++) {
                final int finalI = i;
                updateUserInfo(list.get(i), new UpdateCacheListener() {
                    @Override
                    public void done() {
                        EventBus.getDefault().post(new ConversationEvent(list.get(finalI).getConversation()));
                    }
                });

            }
        }
    }
    /**
     * 更新对方用户资料和会话资料
     *
     * @param event
     */
    public void updateUserInfo(MessageEvent event, final UpdateCacheListener listener) {
        final BmobIMConversation conversation = event.getConversation();//获取会话
        final BmobIMUserInfo info = event.getFromUserInfo();//对方的聊天用户对象
        String title = conversation.getConversationTitle();
        //SDK内部将新会话的会话标题用objectId表示，因此需要比对用户名和私聊会话标题，后续会根据会话类型进行判断
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(title, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    conversation.setConversationIcon(user.getAvatar());
                    conversation.setConversationTitle(user.getUsername());
                    info.setName(user.getUsername());
                    info.setAvatar(user.getAvatar());
                    BmobIM.getInstance().updateUserInfo(info);
                    Log.e(TAG, "更新完毕");
                } else {
                    Log.e(TAG, "该用户获取不到");
                }
                listener.done();
            }
        });
    }
}
