package com.sun.hotelproject.moudle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;

import com.sun.hotelproject.entity.BannerModel;
import com.sun.hotelproject.utils.ActivityManager;
import com.sun.hotelproject.utils.Animutils;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.Tip;
import com.sun.hotelproject.utils.Utils;
import com.sun.hotelproject.view.MyVideoView;
import java.util.Timer;
import java.util.TimerTask;

import K720_Package.K720_Serial;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * @author  sun
 * 时间：2017/11/22
 * TODO:主界面
 */
@Route(path = "/hotel/main")
public class MainActivity extends BaseActivity {
    @BindView(R.id.invoice)Button invoice; //发票
    @BindView(R.id.check_in)Button check_in;//入住
    @BindView(R.id.check_out)Button check_out;//退房
    @BindView(R.id.renwal)Button renwal;//续住
    @BindView(R.id.reserve)Button reserve;//打印发票
    @BindView(R.id.play)ImageView play;//播放
    @BindView(R.id.toolBarTime)TextView toolBarTime;
    @BindView(R.id.toolBar_logo) ImageView toolBar_logo;
    @BindView(R.id.toolbarBack)Button toolbarBack;
    @BindView(R.id.title2) TextView title2;
    @BindView(R.id.img_wuka)ImageView img_wuka;
    @BindView(R.id.img_wangshang)ImageView wangshang;
    //@BindView(R.id.myVideo)MyVideoView myVideo;
    @BindView(R.id.img_xufang)ImageView xufang;
   // @BindView(R.id.viewpager)ViewPager mViewPager;
//    private ScheduledExecutorService scheduledExecutorService;
//    private NiceVideoPlayer niceVideoPlayer;
   // @BindView(R.id.anim_iv)ImageView iv;
   // private ViewPager viewPager;
//    float ivX,ivY;
//    private int oldPosition = 0;//记录上一次点的位置
//    private ArrayList<View> viewList;
//    private int currentItem; //当前页面
//    private int mCurrentTimer = 5;
//    private boolean flag ;
    @BindView(R.id.imgchange) ImageView imgchange;
    @BindView(R.id.videoView) MyVideoView videoView;
    private Timer timer = new Timer();
    private TimerTask task;
    private int flag  = 0;
    //定义切换的图片的数组id
    int imgids[] = new int[]{R.drawable.beijing, R.drawable.beijing1,
            R.drawable.beijing2};
    int imgstart = 0;
    boolean isTrue = false;

    public static byte MacAddr = 0;
    private static final String TAG = "MainActivity";
    String url= Environment.getExternalStorageDirectory().getAbsolutePath()+"/123.mp4";
//    String url2= Environment.getExternalStorageDirectory().getAbsolutePath()+"/beijing.png";
//    String url3= Environment.getExternalStorageDirectory().getAbsolutePath()+"/beijing1.png";
//    String url4= Environment.getExternalStorageDirectory().getAbsolutePath()+"/beijing2.png";
   // private int ids[] = new int[]{R.drawable.beijing,R.drawable.beijing1,R.drawable.beijing2,R.drawable.img_default2};
    // @BindView(R.id.niceVideoPlayer)NiceVideoPlayer niceVideoPlayer;

   // private List<BannerModel> list = new ArrayList<>();
    //private static final int UPTATE_VIEWPAGER = 0;
    //private BannerViewAdapter mAdapter;
//    private int autoCurrIndex = 0;//设置当前 第几个图片 被选中
//    private Timer timer;
//    private TimerTask timerTask;
//    private long period = 5000;//轮播图展示时长,默认5秒
//    private boolean isTrue =false ;

    //定时轮播图片，需要在主线程里面修改 UI
    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler() {
        @Override
        //重写handleMessage方法,根据msg中what的值判断是否执行后续操作
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Log.d("数据", String.valueOf(imgstart));
                imgchange.setImageResource(imgids[imgstart++]);
            }else if(msg.what == 1){
                imgstart = 0;
                flag = 0;
//                imgchange.setVisibility(View.VISIBLE);
//                videoView.setVisibility(View.GONE);
//                flag = 2;//首先要将这个标签换掉 不然会出现因为定时器的原因导致视频播放不全的问题。
//                Log.d("测试", String.valueOf(flag));
//                imgchange.setVisibility(View.GONE);
//                videoView.setVisibility(View.VISIBLE);
//                initData();//播放视频的方法

            }else{
                Log.d(TAG, "啥我也不干  空定时器"  );
            }
        }
    };


    @Override
    protected int layoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();

        start();
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics dm = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels;         // 屏幕宽度（像素）
//        int height = dm.heightPixels;       // 屏幕高度（像素）
//        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
//        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
//        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
//        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
//        int screenHeight = (int) (height / density);// 屏幕高度(dp)

        Point point=new Point();
        wm.getDefaultDisplay().getSize(point);
        int width = point.x;
        int height = point.y;

        Log.d("h_bl", "屏幕宽度（像素）：" + width);
        Log.d("h_bl", "屏幕高度（像素）：" + height);
//        Log.d("h_bl", "屏幕密度（0.75 / 1.0 / 1.5）：" + density);
//        Log.d("h_bl", "屏幕密度dpi（120 / 160 / 240）：" + densityDpi);
//        Log.d("h_bl", "屏幕宽度（dp）：" + screenWidth);
//        Log.d("h_bl", "屏幕高度（dp）：" + screenHeight);


//        //2、通过Resources获取
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        int heigth = dm.heightPixels;
//        int width = dm.widthPixels;
//        Log.e(TAG, "initView: 1"+"heigth---->"+heigth +"width--->"+width);
//
//        //3、获取屏幕的默认分辨率
//        Display display = getWindowManager().getDefaultDisplay();
//        width = display.getWidth();
//        heigth = display.getHeight();
//        Log.e(TAG, "initView: 2"+"heigth---->"+heigth +"width--->"+width);
//
//        DisplayMetrics dm2 = new DisplayMetrics();
//        heigth = dm.heightPixels;
//        width = dm.widthPixels;
//        Log.e(TAG, "initView: 3"+"heigth---->"+heigth +"width--->"+width);

    }



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initData() {
        isRuning = false;
        toolBarTime.setVisibility(View.VISIBLE);
        toolbarBack.setVisibility(View.GONE);
        handler.postDelayed(runnable,1000);
        ActivityManager.getInstance().addActivity(this);
        Connect();
//        if (!isTrue){
//            play.setBackgroundResource(R.drawable.btn_play);
//        }

    }

    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this,1000);
            toolBarTime.setText(DataTime.curenTime());
        }
    };

    /**选择播放图片还是播放视频*/
    public void start() {
        task = new TimerTask() {
            @Override
            public void run() {
                if (imgstart < imgids.length) {
                    Log.d(TAG, "length" +imgids.length );
                    Message msg = Message.obtain();
                    msg.what = 0;
                    myHandler.sendEmptyMessage(flag);
                    Log.d(TAG, "flag" +flag );
                } else {
                    if(flag == 2){
                        myHandler.sendEmptyMessage(flag);
                        //啥也不干
                    }else {
                        flag = 1;
                        Log.d("测试", String.valueOf(flag));
                        myHandler.sendEmptyMessage(flag);
                        //播放视频
                    }
                }
            }
        };
        //定时器开始执行
        timer.schedule(task,0,3000);

    }

    //播放视频
    public void initVideo() {

        //String uri = "android.resource://" + getPackageName() + "/" + R.raw.b;
        videoView.setVideoURI(Uri.parse(url));
        //开始播放
        videoView.start();

        //播放完成回调
        videoView.setOnCompletionListener(new MyPlayerOnCompletionListener());

        //防止出现视频播放错误的问题
        videoView.setOnErrorListener(videoErrorListener);

    }
    //防出现无法播放此视频窗口
    public MediaPlayer.OnErrorListener videoErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {

            return true;
        }
    };

    //回调方法
    private class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            /**再次启动图片的轮播,设置了imgstart为初始值*/
            /**多个视频可以在这进行切换，进行一次判断加入还有视频就播放，没有就走下面这一段*/
//            imgstart = 0;
//            flag = 0;
//            imgchange.setVisibility(View.VISIBLE);
//            videoView.setVisibility(View.GONE);
            videoView.start();
        }
    }
    @OnClick({R.id.check_in,R.id.check_out,R.id.invoice,R.id.renwal,R.id.reserve,R.id.play})
    void OnClick(View v){
        Intent intent =new Intent();
        switch (v.getId()){
            case R.id.check_in://入住 首先判断卡箱是否有卡
                if (Utils.isFastClick()) {
                      clean();
                        if (getStates().equals("30")) {
                            intent.setClass(MainActivity.this, LayoutHouseActivity.class);
                            intent.putExtra("k", "1");
                            startActivity(intent);
                        } else {
                            img_wuka.setVisibility(View.VISIBLE);
                            Animutils.alphaAnimation(img_wuka);
                        }
                }
                break;
            case R.id.check_out: //退房
                if (Utils.isFastClick()) {
                    clean();
                   // play.setBackgroundResource(R.drawable.btn_play);
                    intent.setClass(MainActivity.this, CheckOutActivity.class);
                    intent.putExtra("k", "3");
                    startActivity(intent);
                }
                break;
            case R.id.invoice: //打印发票
                if (Utils.isFastClick()) {
                    clean();
                  //  play.setBackgroundResource(R.drawable.btn_play);
                }
                break;
            case R.id.renwal: //续住
                if (Utils.isFastClick()) {
                    clean();
                    xufang.setVisibility(View.VISIBLE);
                    Animutils.alphaAnimation(xufang);
                }
                break;
            case R.id.reserve://预定
                if (Utils.isFastClick()){
                    clean();

                    intent.setClass(MainActivity.this,ChoiceActivity.class);
                    intent.putExtra("k","4");
                    startActivity(intent);
                }
                break;
            case R.id.play://播放视频
                if (Utils.isFastClick()){
                    isTrue =!isTrue;
                    if (isTrue) {
                        play.setBackgroundResource(R.drawable.play2);
                        flag = 2;
                        Log.d("测试", String.valueOf(flag));
                        imgchange.setVisibility(View.GONE);
                        videoView.setVisibility(View.VISIBLE);
                        initVideo();//播放视频的方法
                    }else {
                      clean();
                    }
                }
                break;

                default:
                    break;
        }
    }
    public void  clean(){
        isTrue = false;
        play.setBackgroundResource(R.drawable.btn_play);
        imgstart = 0;
        flag = 0;
        imgchange.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.GONE);
    }

    @OnTouch({R.id.check_in,R.id.check_out,R.id.invoice,R.id.renwal,R.id.reserve,R.id.play})
    boolean OnTouch(View v, MotionEvent event){

        switch (v.getId()){
            case R.id.check_in://入住 首先判断卡箱是否有卡
              if (event.getAction() == MotionEvent.ACTION_DOWN){
                check_in.getBackground().setAlpha(128);
              }else if (event.getAction() == MotionEvent.ACTION_UP){
                  check_in.getBackground().setAlpha(255);
              }
                break;
            case R.id.check_out: //退房
                if (event.getAction() == MotionEvent.ACTION_DOWN){

                    check_out.getBackground().setAlpha(128);
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    check_out.getBackground().setAlpha(255);
                }
                break;
            case R.id.invoice: //打印发票
                if (event.getAction() == MotionEvent.ACTION_DOWN){

                    invoice.getBackground().setAlpha(128);
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    invoice.getBackground().setAlpha(255);
                }
                break;
            case R.id.renwal: //续住
                if (event.getAction() == MotionEvent.ACTION_DOWN){

                    renwal.getBackground().setAlpha(128);
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    renwal.getBackground().setAlpha(255);
                }
                break;
            case R.id.reserve://预定
                if (event.getAction() == MotionEvent.ACTION_DOWN){

                    reserve.getBackground().setAlpha(128);
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    reserve.getBackground().setAlpha(255);
                }
                break;
            default:
                break;
        }
        return false;
    }


    /**
     *查询卡箱
     */
    private String  getStates(){
        int nRet;
        String state = null;
        byte[] StateInfo = new byte[4];
        String[] RecordInfo = new String[2];
        nRet = K720_Serial.K720_SensorQuery(MacAddr, StateInfo, RecordInfo);
        if (nRet == 0) {
            state = Integer.toHexString(StateInfo[3] & 0xFF).toUpperCase();
        }else {
            Tip.show(getApplicationContext(),"状态值查询失败!",false);
        }
        return state;
    }

    /**
     * 连接
     */
    private void Connect(){
        String strPort = "/dev/ttyS3";
        int re;
        byte i;
        String[] RecordInfo=new String[2];
        int Baudate = 9600;
        re = K720_Serial.K720_CommOpen(strPort);

        if(re==0)
        {
            for(i = 0; i < 16; i++)
            {
                re = K720_Serial.K720_AutoTestMac(i, RecordInfo);
                if(re == 0)
                {
                    MacAddr = i;
                    break;
                }
            }
            if(i == 16 && MacAddr == 0)
            {
                K720_Serial.K720_CommClose();
            }
        }
    }
    private void DisConnect(){
        int nRet;
        nRet = K720_Serial.K720_CommClose();
        if(nRet == 0)
        {
            MacAddr = 0;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else{
            Tip.show(this,"设备断开失败",false);
        }
        Tip.show(this,"设备断开失败，错误代码为："+K720_Serial.ErrorCode(nRet, 0),false);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if ( timer !=null){
            timer.cancel();
            timer =null;
            timer = new Timer();
            start();
        }
        timer =new Timer();
        start();
        handler.post(runnable);
        isRuning = false;


    }
    private  void  cancel(){
        if (task != null) {
        task.cancel();
        task = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancel();
        handler.removeCallbacks(runnable);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DisConnect();
        cancel();
       // NiceVideoPlayerManager.instance().pausNiceVideoPlayer();
       // NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
       handler.removeCallbacks(runnable);
//        if (timerTask != null) {
//            timerTask.cancel();
//            timerTask = null;
//        }
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
    }
//    public class BannerViewAdapter extends PagerAdapter {
//
//        private Context context;
//        private List<BannerModel> listBean;
//
//        public BannerViewAdapter(Activity context, List<BannerModel> list) {
//            this.context = context.getApplicationContext();
//            if (list == null || list.size() == 0) {
//                this.listBean = new ArrayList<>();
//            } else {
//                this.listBean = list;
//            }
//        }
//
//        @Override
//        public Object instantiateItem(final ViewGroup container, final int position) {
//            if (listBean.get(position).getUrlType() == 0) {//图片
//                final ImageView imageView = new ImageView(context);
//                Glide.with(context).load(listBean.get(position).getBannerUrl())
//                        .skipMemoryCache(true)
//                        .into(imageView);
//                container.addView(imageView);
//
//                return imageView;
//            }else{//视频
//                niceVideoPlayer =new NiceVideoPlayer(context);
//                niceVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK); // IjkPlayer or MediaPlayer
//                niceVideoPlayer.setUp(listBean.get(position).getBannerUrl(), null);
//                TxVideoPlayerController controller = new TxVideoPlayerController(context);
//                // controller.setTitle("CHIC");
//                controller.setLenght(48000);
//                niceVideoPlayer.setController(controller);
//                container.addView(niceVideoPlayer);
//                return niceVideoPlayer;
//            }
//
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView((View) object);
//        }
//
//        @Override
//        public int getCount() {
//            return listBean.size();
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == (View) object;
//        }
//    }

}
