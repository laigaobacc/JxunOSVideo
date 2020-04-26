package com.zsh.jxunosvideo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zsh.jxunosvideo.R;
import com.zsh.jxunosvideo.activity.LessonActivity;
import com.zsh.jxunosvideo.activity.VideoActivity;
import com.zsh.jxunosvideo.bean.Course;
import com.zsh.jxunosvideo.bean.Lesson;

import java.util.List;

public class LessonAdapter extends BaseAdapter {
    private Context context;
    private List<Lesson> lessons;

    public LessonAdapter(Context context, List<Lesson> lessons) {
        this.context = context;
        this.lessons = lessons;
    }

    @Override
    public int getCount() {
        return lessons.size();

    }

    @Override
    public Object getItem(int position) {
        return lessons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Lesson lesson=lessons.get(position);
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=layoutInflater.inflate(R.layout.item_lesson,null);

        TextView textView =convertView.findViewById(R.id.item_lesson);
        textView.setText(lesson.getLessonname());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, VideoActivity.class);
                intent.putExtra("lesson",lesson);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
