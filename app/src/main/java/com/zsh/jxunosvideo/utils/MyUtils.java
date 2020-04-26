package com.zsh.jxunosvideo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.zsh.jxunosvideo.app.MyApp;
import com.zsh.jxunosvideo.bean.DanmuEntity;
import com.zsh.jxunosvideo.bean.NetDanmu;
import com.zsh.jxunosvideo.bean.User;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;

public class MyUtils {

    //把弹幕转为网络弹幕
    public static NetDanmu DanmuToNetDanmu(DanmuEntity danmuEntity){
        NetDanmu netDanmu=new NetDanmu();
        netDanmu.setContent(danmuEntity.getContent());
        netDanmu.setShowTime(danmuEntity.getShowTime());
        netDanmu.setTextColor(danmuEntity.getTextColor());
        netDanmu.setTime(danmuEntity.getTime());
        netDanmu.setUser(danmuEntity.getUser());
        return netDanmu;
    }
    //把网络弹幕转为弹幕
    public static DanmuEntity NetDanmuToDanmu(NetDanmu netDanmu){
        DanmuEntity danmuEntitymu=new DanmuEntity();
        danmuEntitymu.setContent(netDanmu.getContent());
        danmuEntitymu.setShowTime(netDanmu.getShowTime());
        danmuEntitymu.setTextColor(netDanmu.getTextColor());
        danmuEntitymu.setTime(netDanmu.getTime());
        danmuEntitymu.setUser(netDanmu.getUser());
        return danmuEntitymu;
    }
    //多个网络弹幕转弹幕
    public static List<DanmuEntity> NetDanmusToDanmus(List<NetDanmu> list){
        List<DanmuEntity> danmuEntities=new ArrayList<>();
        for (NetDanmu netDanmu:list)
        {
            danmuEntities.add(NetDanmuToDanmu(netDanmu));
        }
        return danmuEntities;
    }
    //得到用户
    public  static User getUser() {
        Gson gson = new Gson();
        String json = MyApp.sharePreferences.getString("user", "");
        User user = gson.fromJson(json, User.class);
        return user;
    }
    //long类型时间转换
    public static String longToDate(long lo){
        Date date = new Date(lo);
       //SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss");
        return sd.format(date);
    }

}
