package com.zsh.jxunosvideo.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.zsh.jxunosvideo.R;
import com.zsh.jxunosvideo.adapter.CourseAdapter;
import com.zsh.jxunosvideo.bean.Course;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainVideoFragment extends Fragment {
    private ListView mlistView;
    //主页的数据
    private List<Course> curses=new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


            View v=inflater.inflate(R.layout.fragment_main_video, null, false);
            initView(v);
            return v;
    }

    private void initView(View v) {
        mlistView=v.findViewById(R.id.course_list);
        //数据请求
        requestCurses();
    }

    private void requestCurses() {
        BmobQuery<Course> query=new BmobQuery();
        query.order("updatedAt")
                .findObjects(new FindListener<Course>() {
            @Override
            public void done(List<Course> list, BmobException e) {
                if (e==null)
                {
                    curses=list;
                    Log.e("TAG",curses.toString());
                    mlistView.setAdapter(new CourseAdapter((ArrayList<Course>) curses,getContext()));

                }else {
                    //获取失败
                    Toast.makeText(getContext(),"数据获取失败"+e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
