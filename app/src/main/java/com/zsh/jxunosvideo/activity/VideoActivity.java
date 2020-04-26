package com.zsh.jxunosvideo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Sampler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videoplayer.ijk.IjkPlayerFactory;
import com.dueeeke.videoplayer.player.VideoView;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.gson.Gson;
import com.orzangleli.xdanmuku.DanmuContainerView;
import com.orzangleli.xdanmuku.Model;
import com.zsh.jxunosvideo.R;
import com.zsh.jxunosvideo.adapter.DanmuAdapter;
import com.zsh.jxunosvideo.adapter.MyFragmentPagerAdapter;
import com.zsh.jxunosvideo.app.MyApp;
import com.zsh.jxunosvideo.bean.DanmuEntity;
import com.zsh.jxunosvideo.bean.Lesson;
import com.zsh.jxunosvideo.bean.NetDanmu;
import com.zsh.jxunosvideo.bean.User;
import com.zsh.jxunosvideo.fragment.PPtFragment;
import com.zsh.jxunosvideo.fragment.PdfFragment;
import com.zsh.jxunosvideo.utils.MyUtils;
import com.zsh.jxunosvideo.utils.MyVideoController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 视频播放界面
 */
public class VideoActivity extends AppCompatActivity {
    DanmuContainerView danmuContainerView;
    Button button;
    //输入框
    private EditText ed_txt;
    Random random;
    final int ICON_RESOURCES[] = {R.drawable.icon1, R.drawable.icon2, R.drawable.icon3, R.drawable.icon4, R.drawable.icon5};

    private long VIDEO_DURATION = 0;
    //当前进度
    private Long mCurrentProgress;

    List<Model> danmuEntities = new ArrayList<>();
    //视频的
    private VideoView videoView;


    //当前的Lesson
    Lesson lesson;

    private ViewPager viewPager;
    //偏移图片
    private View iv;//要偏移的图片
    private int index=0;//当前页卡
    private int imgleth;//图片宽度
    private int offset;//偏移量
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initData();
        initView();
        initData2();
    }
    //用来初始化Fragment的数据
    private void initData2() {
        //fragmemnt处理
        List<Fragment> list=new ArrayList<Fragment>(); //new一个List<Fragment>
        list.add(new PdfFragment());
        list.add(new PPtFragment());

        FragmentManager fm=getSupportFragmentManager();
        MyFragmentPagerAdapter mfpa=new MyFragmentPagerAdapter(fm, list); //new myFragmentPagerAdater记得带上两个参数
        viewPager.setAdapter(mfpa);
        viewPager.setCurrentItem(0);
    }

    private void initData() {
        lesson = (Lesson) getIntent().getSerializableExtra("lesson");
        //根据Lesson查弹幕
        getNetDanmus();

    }

    //从网络获取弹幕,并且转为弹幕
    private void getNetDanmus() {
        BmobQuery<NetDanmu> query = new BmobQuery<NetDanmu>();
        Lesson lesson = this.lesson;
        query.addWhereEqualTo("lesson", new BmobPointer(lesson));
        //希望同时查询该评论的发布者的信息，以及该帖子的作者的信息，这里用到上面`include`的并列对象查询和内嵌对象的查询
        query.include("user");
        query.findObjects(new FindListener<NetDanmu>() {
            @Override
            public void done(List<NetDanmu> list, BmobException e) {
                //
                danmuEntities.addAll(MyUtils.NetDanmusToDanmus(list));
                // Toast.makeText(VideoActivity.this,danmuEntities.toString(),Toast.LENGTH_SHORT).show();
                danmuContainerView.addDanmuIntoCachePool(danmuEntities);
            }
        });

    }

    private void initView() {
        //初始化ViewPager
        viewPager=findViewById(R.id.wengdang_vp);
        //初始化要偏移的图片(小红杠)
        iv=findViewById(R.id.iv_pianyi);
        //设置小红杠
        setRedView();
        //初始化播放器
        initBoFangQi();



        random = new Random();
        danmuContainerView = findViewById(R.id.danmuContainerView);
        button = findViewById(R.id.button);
        ed_txt=findViewById(R.id.edt_danmu);
        final DanmuAdapter danmuAdapter = new DanmuAdapter(VideoActivity.this);
        danmuContainerView.setAdapter(danmuAdapter);

        danmuContainerView.setSpeed(DanmuContainerView.NORMAL_SPEED);

        danmuContainerView.setGravity(DanmuContainerView.GRAVITY_FULL);

        //弹幕点击事件
        danmuContainerView.setOnItemClickListener(new DanmuContainerView.OnItemClickListener() {
            @Override
            public void onItemClick(Model model) {
                DanmuEntity danmuEntity = (DanmuEntity) model;
                User toUser = danmuEntity.getUser();
                creatDailog(toUser);
            }
        });
        //按钮点击事件
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DanmuEntity danmuEntity = new DanmuEntity();
                danmuEntity.setContent("弹幕" + ed_txt.getText());
                danmuEntity.setType(0);
                danmuEntity.setTime("23:20:11");
                danmuEntity.setShowTime((long) (videoView.getCurrentPosition()));
                danmuEntity.setUser(MyUtils.getUser());
                danmuEntities.add(danmuEntity);
                danmuContainerView.setSpeed(2);
                danmuContainerView.addDanmu(danmuEntity);
                //添加弹幕到服务器
                addToFWQ(danmuEntity);
                danmuContainerView.addDanmuIntoCachePool(danmuEntities);
            }

        });


    }

    private void setRedView() {

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //int one=offset*2+imgleth;//相邻页面的偏移量
                //评议动画
                //获取图片宽度
                imgleth= iv.getWidth();
                Log.e("图片宽度", String.valueOf(imgleth));
                //计算偏移量
                offset=imgleth;
                Animation anima=new TranslateAnimation(index*offset,position*offset,0,0);
                index=position; //当前页跟着变
                anima.setFillAfter(true); // 动画终止时停留在最后一帧，不然会回到没有执行前的状态
                anima.setDuration(200);// 动画持续时间0.2秒
               // iv.setAnimation(anima);
                iv.startAnimation(anima);// 是用ImageView来显示动画的
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void creatDailog(final User toUser) {
        videoView.pause();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("是否给该同学发送消息?")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Intent intent = new Intent(VideoActivity.this, MessageActivity.class);
                        intent.putExtra("toUser", toUser);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                })
                .show();
    }

    //添加弹幕到服务器
    private void addToFWQ(DanmuEntity danmuEntity) {
        NetDanmu netDanmu = MyUtils.DanmuToNetDanmu(danmuEntity);
        netDanmu.setLesson(lesson);
        Gson gson = new Gson();
        String json = MyApp.sharePreferences.getString("user", "");
        User user = gson.fromJson(json, User.class);

        netDanmu.setUser(user);
        netDanmu.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "弹幕发送成功");
                } else {
                    Log.i("bmob", "失败：" + e.getMessage());
                }
            }
        });
    }

    //初始化播放器
    private void initBoFangQi() {
        //视频播放
        videoView = findViewById(R.id.player);
        //使用IjkPlayer解码
        videoView.setPlayerFactory(IjkPlayerFactory.create());
        videoView.setUrl(lesson.getVideourl());
        MyVideoController standardVideoController = new MyVideoController(this);
        standardVideoController.addDefaultControlComponent(lesson.getLessonname(), false);
        videoView.setVideoController(standardVideoController);
        //设置总播放进度
        VIDEO_DURATION = videoView.getDuration();
        videoView.start();
        new Thread(new PlayThread()).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                danmuContainerView.onProgress(videoView.getCurrentPosition());
            }
        }
    };

    //监控进度条变化的线程
    private class PlayThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = Message.obtain();
                message.what = 1;
                message.obj = videoView.getCurrentPosition();
                handler.sendMessage(message);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.release();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!videoView.onBackPressed()) {
            super.onBackPressed();
        }
    }

    //下载pdf
    //http://video.ch9.ms/build/2011/slides/TOOL-532T_Sutter.pptx
    public void DownloadPdf(Context context, final String pdfurl) {

        final String cachurl = Environment.getExternalStorageDirectory() + File.separator + "jxunOS" + File.separator;
        //判断文件目录是否存在，不存在就创建
        File filedir = new File(cachurl);
        if (!filedir.exists()) {
            filedir.mkdirs();
        }
        final String pdfName = pdfurl.substring(pdfurl.lastIndexOf("/") + 1);//文件名称
        final File dest = new File(cachurl, pdfName);
        if (!dest.exists()) {
            try {
                dest.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e("VideoActivity路径:", cachurl + pdfName);

        Request request = new Request.Builder().url(pdfurl).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //下载失败

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //已经下载的长度
                int downloadSize = 0;
                //文件的总长度
                int contentLength = (int) response.body().contentLength();
                //开始下载
                InputStream bs = response.body().byteStream();//输入流
                OutputStream os = new FileOutputStream(dest);//输出流
                byte[] b = new byte[1024];
                int len = 0;
                while ((len = bs.read(b)) != -1) {
                    os.write(b, 0, len);
                    downloadSize += len;
                    Message message = Message.obtain();
                    message.what = 2;//更新进度
                    message.obj = downloadSize * 100 / contentLength;
                    handler.sendMessage(message);
                }
                os.close();
                bs.close();
                //加载完毕后
                Message message = Message.obtain();
                message.what = 3;
                message.obj = cachurl + "test.pdf";
                Log.e("VAD", cachurl + "test.pdf");
                handler.sendMessage(message);
            }
        });
    }
}
