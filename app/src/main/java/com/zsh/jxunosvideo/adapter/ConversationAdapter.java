package com.zsh.jxunosvideo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zsh.jxunosvideo.R;
import com.zsh.jxunosvideo.activity.UIChatActivity;
import com.zsh.jxunosvideo.utils.MyUtils;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {
    private List<BmobIMConversation> conversations;
    private Context context;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView touxiangIMV;
        TextView nameTV;
        TextView messagefTv;
        TextView timeTv;
        TextView circleMinTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            touxiangIMV=itemView.findViewById(R.id.conversation_touxiang);
            nameTV=itemView.findViewById(R.id.conversation_name);
            messagefTv=itemView.findViewById(R.id.new_message_first);
            timeTv=itemView.findViewById(R.id.time_tv);
            circleMinTV=itemView.findViewById(R.id.circle_min_tv);
        }
    }

    public ConversationAdapter(List<BmobIMConversation> conversations,Context context){
        this.conversations=conversations;
        this.context=context;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final BmobIMConversation bmobIMConversation=conversations.get(position);
        Glide.with(context).load(bmobIMConversation.getConversationIcon()).into(holder.touxiangIMV);
        holder.nameTV.setText(bmobIMConversation.getConversationTitle());
        holder.messagefTv.setText(bmobIMConversation.getMessages().get(0).getContent());
        holder.timeTv.setText(MyUtils.longToDate(bmobIMConversation.getUpdateTime()));

        //TODO 会话：4.3、查询指定会话下的未读消息数
        long unReadCount = BmobIM.getInstance().getUnReadCount(bmobIMConversation.getConversationId());
        if (unReadCount>99)
        {
            holder.circleMinTV.setText("99");
        }else {
            holder.circleMinTV.setText(String.valueOf(unReadCount));
        }
        if(unReadCount==0)
        {
            //holder.circleMinTV.setText("");
            holder.circleMinTV.setBackgroundColor(Color.argb(0, 0, 255, 0));//背景透明度
            holder.circleMinTV.setTextColor(Color.argb(0, 0, 255, 0));  //文字透明度
        }


        //添加点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, UIChatActivity.class);
                intent.putExtra("conversation",bmobIMConversation);
                //TODO 消息：5.4、更新此会话的所有消息为已读状态
                bmobIMConversation.updateLocalCache();
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

}
