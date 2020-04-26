package com.zsh.jxunosvideo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zsh.jxunosvideo.R;
import com.zsh.jxunosvideo.activity.LessonActivity;
import com.zsh.jxunosvideo.bean.Course;
import com.zsh.jxunosvideo.bean.Lesson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class CourseAdapter extends BaseAdapter{
    private Context context;
    private List<Course> courses;
    public CourseAdapter(ArrayList<Course> curses, Context context){
        this.courses=curses;
        this.context=context;
    }
    @Override
    public int getCount() {
        return courses.size();
    }

    @Override
    public Object getItem(int position) {
        return courses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Course curse=courses.get(position);


        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=layoutInflater.inflate(R.layout.item_course,null);


        TextView textView =convertView.findViewById(R.id.item_curse);
        textView.setText(curse.getCoursename());

        //点击事件
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //查询该大课下的所有小课
                BmobQuery<Lesson> query=new BmobQuery();
                query.addWhereEqualTo("course",new BmobPointer(curse));
                query.findObjects(new FindListener<Lesson>() {
                    @Override
                    public void done(List<Lesson> list, BmobException e) {
                        if(e==null) {
                            //Toast.makeText(context, list.toString(), Toast.LENGTH_SHORT).show();
                            //跳转页面
                            Intent intent=new Intent(context,LessonActivity.class);

                            intent.putExtra("coursename",curse);
                            intent.putExtra("lessList",(Serializable) list);
                            context.startActivity(intent);
                        }else {
                            Toast.makeText(context, "数据获取失败"+e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
        return convertView;
    }




}
