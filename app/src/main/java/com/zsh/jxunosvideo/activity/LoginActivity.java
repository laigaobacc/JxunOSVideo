package com.zsh.jxunosvideo.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.zsh.jxunosvideo.R;
import com.zsh.jxunosvideo.app.MyApp;
import com.zsh.jxunosvideo.bean.User;

import java.io.InputStream;
import java.util.concurrent.locks.Lock;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends Activity {
    private EditText username;
    private EditText password;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intiView();

        auto();
    }


    public void intiView(){
        username=findViewById(R.id.user_name);
        password=findViewById(R.id.user_password);
    }
    //自动登陆
    public void auto(){
        if(!MyApp.sharePreferences.getString("username","").equals(""))
        {
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            //跳转以后销毁
            finish();
        }
    }
    public void login(View v){
        String name=username.getText().toString();
        String pwd=password.getText().toString();
        //向bomb验证
        User user=new User();
        user.setUsername(name);
        user.setPassword(pwd);

        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e==null)
                {
                    Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                    //将用户转为json缓存
                    Gson gson=new Gson();
                    String json=gson.toJson(user);
                    MyApp.edit.putString("user",json).commit();
                    MyApp.edit.putString("username",user.getUsername()).commit();
                    //跳转
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    //跳转以后销毁
                    finish();
                }else {
                   // Toast.makeText(LoginActivity.this,"用户或者密码错误",Toast.LENGTH_SHORT).show();
                    Toast.makeText(LoginActivity.this,"用户或者密码错误",Toast.LENGTH_SHORT).show();
                    Log.e("失败",e.toString());
                }

            }
        });

    }

}
