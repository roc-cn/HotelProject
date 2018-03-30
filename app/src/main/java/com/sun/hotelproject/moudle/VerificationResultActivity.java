package com.sun.hotelproject.moudle;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseActivity;
import com.sun.hotelproject.entity.Draw;
import com.sun.hotelproject.entity.FaceRecognition;
import com.sun.hotelproject.entity.LayoutHouse;
import com.sun.hotelproject.entity.SeqNo;
import com.sun.hotelproject.utils.CommonSharedPreferences;
import com.sun.hotelproject.utils.CustomProgressDialog;
import com.sun.hotelproject.utils.DataTime;
import com.sun.hotelproject.utils.HttpUrl;
import com.sun.hotelproject.utils.JsonCallBack;
import com.sun.hotelproject.utils.Router;
import com.sun.hotelproject.utils.Tip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import K720_Package.K720_Serial;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author sun
 * 时间：2017年12月19日
 * TODO：验证结果页面
 */
public class VerificationResultActivity extends BaseActivity {

    @BindView(R.id.linear_1)LinearLayout linear_1;
    @BindView(R.id.linear_2)LinearLayout linear_2;
    @BindView(R.id.retry)Button retry;
    @BindView(R.id.wx)Button wx;
    @BindView(R.id.zfb)Button zfb;

    String orderId;
    private String name;
    private String id_CardNo;
    private String path;
    private LayoutHouse house;
    private static byte MacAddr = 0;
    Dialog dialog;
    private Bitmap orc_bitmap;//拍照和相册获取图片的Bitmap
    private static final String TAG = "VerificationResultActiv";
    private String filePath;//图片路径
    private String total_fee;//支付金额
    String paytype;
    String k;
    String orderid;
    String msgs;

    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Draw draw= (Draw) msg.obj;
            if (msg.what == 1){

                if (draw!=null){
                    Intent intent =new Intent(VerificationResultActivity.this,PaymentActivity.class);
                    intent.putExtra("url",draw.getCodeimgurl());
                    intent.putExtra("paytype",paytype);
                    intent.putExtra("house", house);
                    intent.putExtra("orderId",orderId);
                    intent.putExtra("name",name);
                    intent.putExtra("k","1");
                    startActivity(intent);
                    finish();
                }
            }if (msg.what == 2){
                Intent intent =new Intent(VerificationResultActivity.this,PaymentActivity.class);
                intent.putExtra("url",draw.getCodeimgurl());
                intent.putExtra("k","2");
                intent.putExtra("orderId",orderId);
                intent.putExtra("paytype",paytype);
                intent.putExtra("msgs",msgs);
                startActivity(intent);
                finish();

            }
        }
    };

    @Override
    protected int layoutID() {
        return R.layout.activity_verification_result;
    }

    @Override
    protected void initData() {
        k=getIntent().getStringExtra("k");
        if (k.equals("1")){
        house= (LayoutHouse) getIntent().getSerializableExtra("house");
        name=getIntent().getStringExtra("name");
        id_CardNo=getIntent().getStringExtra("id_CardNo");
      //  path=getIntent().getStringExtra("path");
      //  displayImage(path);
        Log.e(TAG, "initData: "+name+"\n"+id_CardNo );
        Log.e(TAG, "initData: house"+house );
        }else {
            orderId =getIntent().getStringExtra("orderid");
            msgs =getIntent().getStringExtra("msgs");
        }
        linear_1.setVisibility(View.VISIBLE);

        // Log.e(TAG, "initData: list-->"+house.toString());
        //get();
    }
    @OnClick({R.id.retry,R.id.wx,R.id.zfb})
    void  OnClick(View v){
        switch (v.getId()){
            case R.id.retry :
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.wx:
                paytype="1";
             //   getPost(paytype);
                break;
            case R.id.zfb:
                paytype="2";
               // getPost(paytype);
                break;
        }
    }





    /**
     * 生成流水号
     */
    public void get(){
        dialog= CustomProgressDialog.createLoadingDialog(this,"身份信息比对中....");
        dialog.show();
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
        OkGo.<FaceRecognition>post(HttpUrl.FACERECOQNITION)
                .tag(this)
                .retryCount(3)//超时重连次数
                .cacheTime(3000)//缓存过期时间
                .params("name",name)
                .params("creid_no", id_CardNo)
                .params("account",account)
                .params("type",8)
                .params("seq_no",seq_no)
                .params("photo_check_live",1)
                .isMultipart(true)//强制使用multipart/form-data 表单上传
                .params("image_fn", new File(path))
                .execute(new JsonCallBack<FaceRecognition>(FaceRecognition.class) {
                    @Override
                    public void onSuccess(Response<FaceRecognition> response) {
                        super.onSuccess(response);
                        Log.d(TAG, "onSuccess() called with: response = [" + response.body() + "]");

                        if (response.body().getRescode().equals("00") && response.body().getRetcode().equals("0")){
                            Tip.show(VerificationResultActivity.this,"比对成功 得分："+response.body().getScore(),true);
                            dialog.dismiss();
                            //handler.sendEmptyMessage(1);
                            linear_1.setVisibility(View.VISIBLE);
                            linear_2.setVisibility(View.GONE);
                            // handler.sendEmptyMessage(2);
                            //Connect();
                        }else {
                            linear_2.setVisibility(View.VISIBLE);
                            linear_1.setVisibility(View.GONE);
                            Tip.show(VerificationResultActivity.this,response.body().getRetmsg(),false);
                            dialog.dismiss();
                        }
                    }
                });
    }

    /**
     * 拍完照和从相册获取玩图片都要执行的方法(根据图片路径显示图片)
     */
    private void displayImage(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {
            //orc_bitmap = BitmapFactory.decodeFile(imagePath);//获取图片
            orc_bitmap = comp(BitmapFactory.decodeFile(imagePath)); //压缩图片
            ImgUpdateDirection(imagePath);//显示图片,并且判断图片显示的方向,如果不正就放正
        } else {
            Toast.makeText(VerificationResultActivity.this, "图片获取失败", Toast.LENGTH_LONG).show();
        }
    }
    //比例压缩
    private Bitmap comp(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 70, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 1920f;//这里设置高度为800f
        float ww = 1080f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return bitmap;//压缩好比例大小后再进行质量压缩
    }


    //改变拍完照后图片方向不正的问题
    private void ImgUpdateDirection(String filepath) {
        int digree = 0;//图片旋转的角度
        //根据图片的URI获取图片的绝对路径
        Log.i("tag", ">>>>>>>>>>>>>开始");
        //String filepath = ImgUriDoString.getRealFilePath(getApplicationContext(), uri);
        Log.i("tag", "》》》》》》》》》》》》》》》" + filepath);
        //根据图片的filepath获取到一个ExifInterface的对象
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
            Log.i("tag", "exif》》》》》》》》》》》》》》》" + exif);
            if (exif != null) {

                // 读取图片中相机方向信息
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                // 计算旋转角度
                switch (ori) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        digree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        digree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        digree = 270;
                        break;
                    default:
                        digree =0;
                        break;

                }
            }
            //如果图片不为0
            if (digree != 0) {
                // 旋转图片
                Matrix m = new Matrix();
                m.postRotate(digree);
                orc_bitmap = Bitmap.createBitmap(orc_bitmap, 0, 0, orc_bitmap.getWidth(),
                        orc_bitmap.getHeight(), m, true);
            }
            if (orc_bitmap != null) {
             //  img.setImageBitmap(orc_bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
            exif = null;
        }
    }
}
