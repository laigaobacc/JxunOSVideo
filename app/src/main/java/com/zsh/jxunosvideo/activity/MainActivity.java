package com.zsh.jxunosvideo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zsh.jxunosvideo.R;
import com.zsh.jxunosvideo.app.MyApp;
import com.zsh.jxunosvideo.bean.User;
import com.zsh.jxunosvideo.fragment.MainMeFragment;
import com.zsh.jxunosvideo.fragment.MainMessageFragment;
import com.zsh.jxunosvideo.fragment.MainVideoFragment;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.exception.BmobException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static Context mainactivitycontent;
    //下面的按钮
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;

    //三个fragment
    private MainVideoFragment mainVideoFragment = new MainVideoFragment();
    private MainMeFragment mainMeFragment = new MainMeFragment();
    private MainMessageFragment mainMessageFragment = new MainMessageFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initFragmentManger();
        //连接IM
        connectIM();
    }

    //连接IM
    private void connectIM() {
        Gson gson = new Gson();
        String json = MyApp.sharePreferences.getString("user", "");
        final User user = gson.fromJson(json, User.class);
                BmobIM.connect(user.getObjectId(), new ConnectListener() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            //连接成功
                            Log.e("连接:","用户连接成功");
                            //更新本地聊天用户
                            BmobIM.getInstance().updateUserInfo(new BmobIMUserInfo(user.getObjectId(),user.getUsername(),user.getAvatar()));

                        } else {
                            //连接失败
                            Log.e("连接:","用户连接失败");

                        }
                    }
                });
        }

        private void initFragmentManger() {
            //获取FragmentManger开启事务
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            //加入fragment
            transaction.add(R.id.fragment_list, mainVideoFragment);
            transaction.add(R.id.fragment_list, mainMeFragment).hide(mainMeFragment);
            transaction.add(R.id.fragment_list, mainMessageFragment).hide(mainMessageFragment);
            transaction.commit();
        }

        private void initView() {
            mainactivitycontent=MainActivity.this;
            radioButton1 = findViewById(R.id.video_fragment);
            radioButton2=findViewById(R.id.message_fragment);
            radioButton3=findViewById(R.id.me_fragment);
            radioButton1.setOnClickListener(this);
            radioButton2.setOnClickListener(this);
            radioButton3.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.video_fragment:
                    //获取FragmentManger开启事务,隐藏另外两个
                    this.getSupportFragmentManager().beginTransaction()
                            .show(mainVideoFragment)
                            .hide(mainMessageFragment)
                            .hide(mainMeFragment)
                            .commit();
                    break;
                case R.id.message_fragment:
                    //获取FragmentManger开启事务,隐藏另外两个
                    this.getSupportFragmentManager().beginTransaction()
                        .show(mainMessageFragment)
                        .hide(mainVideoFragment)
                        .hide(mainMeFragment)
                        .commit();
                break;
            case R.id.me_fragment:
                //获取FragmentManger开启事务,隐藏另外两个
                this.getSupportFragmentManager().beginTransaction()
                        .show(mainMeFragment)
                        .hide(mainVideoFragment)
                        .hide(mainMessageFragment)
                        .commit();
                break;
            default:
                break;
        }
    }
}
