package com.sun.hotelproject.moudle;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.sun.hotelproject.R;
import com.sun.hotelproject.utils.DataTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author  sun
 * 时间 2018-1-9
 *弹出框 activity
 */
public class DialogmsgActivity extends Activity {
    Unbinder unbinder;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.ok)
    Button ok;
    private String msgs;
    String orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window;
        //得到窗口
        window=getWindow();
        //隐藏返回键等虚拟按键
        WindowManager.LayoutParams params=window.getAttributes();
        params.systemUiVisibility= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION| View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(params);
        //设置全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //保持屏幕 常亮
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏


        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dialogmsg);
        unbinder= ButterKnife.bind(this);
        msgs =getIntent().getStringExtra("msgs");
        tv.setText("卡号"+msgs+"\n"+"消费："+"\n"+"方便面，数量1，单价 4元"+"\n"+"毛巾,数量2，单价 8元"+"\n"+"共计：20元");
        orderId = DataTime.orderId();

    }

    @OnClick({R.id.ok})
    void OnClick(View v){
        Intent intent =new Intent();
        intent.putExtra("msg","Ok");
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
