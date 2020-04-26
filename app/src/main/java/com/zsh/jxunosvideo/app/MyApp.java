package com.zsh.jxunosvideo.app;

import android.app.Application;
import android.content.SharedPreferences;

import com.zsh.jxunosvideo.bean.User;
import com.zsh.jxunosvideo.broadcast.DemoMessageHandler;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;

public class MyApp extends Application {
    private static String TAG = "MyApp";
    public static SharedPreferences sharePreferences;
    public static SharedPreferences.Editor edit ;
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化bomb
        Bmob.initialize(this, "b66688c65ea503fb72bf29e37d993299");
        //创建sharePreferences
        sharePreferences=getSharedPreferences("info",MODE_PRIVATE);
        edit=MyApp.sharePreferences.edit();
        //BmobIM
        //TODO 集成：1.8、初始化IM SDK，并d动态注册消息接收器
        if (getApplicationInfo().packageName.equals(getMyProcessName())){
            BmobIM.init(this);
            BmobIM.registerDefaultMessageHandler(new DemoMessageHandler());
        }



    }


    /**
     * 获取当前运行的进程名
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }
}
