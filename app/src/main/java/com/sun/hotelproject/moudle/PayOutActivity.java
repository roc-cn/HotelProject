package com.sun.hotelproject.moudle;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.utils.Router;
import com.sun.hotelproject.utils.Tip;

import K720_Package.K720_Serial;
import butterknife.BindView;
import butterknife.OnClick;

import static com.sun.hotelproject.moudle.MainActivity.MacAddr;

/**
 * @author  sun
 * 时间 2018-1-9
 *退房卡activity
 */

public class PayOutActivity extends BaseActivity {
    String url;

    @Override
    protected int layoutID() {
        return R.layout.activity_pay_out;
    }

    @Override
    protected void initData() {
    }






}
