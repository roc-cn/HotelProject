package com.sun.hotelproject.moudle;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.sun.hotelproject.R;
import com.sun.hotelproject.entity.FaceRecognition;
import com.sun.hotelproject.entity.SeqNo;
import com.sun.hotelproject.utils.CustomProgressDialog;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.HttpUrl;
import com.sun.hotelproject.utils.JsonCallBack;
import com.sun.hotelproject.utils.PlaySound;
import com.sun.hotelproject.utils.Tip;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";
   TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Button bt1=findViewById(R.id.bt1);
        Button bt2=findViewById(R.id.bt2);
        Button bt3=findViewById(R.id.bt3);
        Button bt4=findViewById(R.id.bt4);


        tv1 =findViewById(R.id.tv1);


        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.play(R.raw.raw_fail_read,getApplicationContext());
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.play(R.raw.raw_success_read,getApplicationContext());
            }
        });

    }


    /**
     * 生成流水号
     */
    public void get(){
        tv1.setText("正在识别......");
//        dialog= CustomProgressDialog.createLoadingDialog(this,"身份信息比对中....");
//        dialog.show();
        OkGo.<SeqNo>get(HttpUrl.SEQNO)
                .tag(this)
                .execute(new JsonCallBack<SeqNo>(SeqNo.class) {
                    @Override
                    public void onSuccess(Response<SeqNo> response) {
                        super.onSuccess(response);
                        Log.d(TAG, "onSuccess() called with: response = [" + response.body().toString() + "]");
                        if (response.body().getRescode().equals("00") && response.body().getRetcode().equals("0")){
                            Post(response.body().getSeq_no(),response.body().getAccount());
                        }
                    }
                });
    }

    /**
     * 人脸识别
     * @param seq_no 流水号
     * @param account 账号
     */
    public void Post(String seq_no,String account){
      /*  name=et1.getText().toString();
        id_cardNo=et2.getText().toString();
*/
        String url2= Environment.getExternalStorageDirectory().getAbsolutePath()+"/Pictures/1523433783229.jpg";
        OkGo.<FaceRecognition>post(HttpUrl.FACERECOQNITION)
                .tag(this)
                .retryCount(3)//超时重连次数
                .cacheTime(3000)//缓存过期时间
                .params("name","孙串")
                .params("creid_no", "429004199202192753")
                .params("account",account)
                .params("type",8)
                .params("seq_no",seq_no)
                .params("photo_check_live",0) //0防翻拍，1关闭防翻拍
                .isMultipart(true)//强制使用multipart/form-data 表单上传
                .params("image_fn", new File(url2))
                .execute(new JsonCallBack<FaceRecognition>(FaceRecognition.class) {
                    @Override
                    public void onSuccess(Response<FaceRecognition> response) {
                        super.onSuccess(response);
                        Log.d(TAG, "onSuccess() called with: response = [" + response.body() + "]");

                        if (response.body().getRescode().equals("00") && response.body().getRetcode().equals("0")){
                            tv1.setText("识别成功");
                            Tip.show(getApplicationContext(),"比对成功 得分："+response.body().getScore(),true);
//                            dialog.dismiss();
//                            //handler.sendEmptyMessage(1);
//                            Scanpay();
                            //Connect();
                        }else {
                            Tip.show(getApplicationContext(),response.body().getRetmsg(),false);
                           // dialog.dismiss();
                        }
                    }
                });
    }

}
