package com.zsh.jxunosvideo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zsh.jxunosvideo.R;
import com.zsh.jxunosvideo.adapter.LessonAdapter;
import com.zsh.jxunosvideo.bean.Course;
import com.zsh.jxunosvideo.bean.Lesson;

import java.util.ArrayList;
import java.util.List;

public class LessonActivity extends AppCompatActivity {
    List<Lesson> list=new ArrayList<>();
    private ListView lessonlistView;
    private Course course;
    private ImageView back;
    private TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        course= (Course) getIntent().getSerializableExtra("coursename");
        list= (List<Lesson>) getIntent().getSerializableExtra("lessList");
        initView();
    }

    private void initView() {
        tv_title=findViewById(R.id.tv_title);
        back=findViewById(R.id.to_back);
        lessonlistView=findViewById(R.id.lesson_list);
        lessonlistView.setAdapter(new LessonAdapter(LessonActivity.this,list));
        tv_title.setText(course.getCoursename());
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
