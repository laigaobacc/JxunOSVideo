package com.zsh.jxunosvideo.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.zsh.jxunosvideo.R;
import com.zsh.jxunosvideo.activity.MessageActivity;
import com.zsh.jxunosvideo.app.MyApp;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;


public class MainMeFragment extends Fragment {
    private Button btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_main_me, null, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        btn=v.findViewById(R.id.quit_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //断开消息用户的连接
                BmobIM.getInstance().disConnect();
                //退出账号
                MyApp.edit.clear().commit();
                getActivity().finish();

                Toast.makeText(getActivity(), "退出成功", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
