package com.sun.hotelproject.moudle.camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Area;
import android.hardware.Camera.PictureCallback;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.sun.hotelproject.R;
import com.sun.hotelproject.base.BaseFragment;
import com.sun.hotelproject.entity.Affirmstay;
import com.sun.hotelproject.entity.GuestRoom;
import com.sun.hotelproject.entity.LayoutHouse;
import com.sun.hotelproject.entity.QueryCheckin;
import com.sun.hotelproject.moudle.PaymentActivity;

import com.sun.hotelproject.moudle.camera.control.CameraControl;
import com.sun.hotelproject.moudle.camera.control.CameraControlCallback;
import com.sun.hotelproject.moudle.camera.control.SetParametersException;
import com.sun.hotelproject.moudle.camera.tools.BitmapWork;
import com.sun.hotelproject.moudle.camera.tools.MyMath;

import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CameraFragment extends BaseFragment implements  SensorEventListener
						, PictureCallback ,CameraControlCallback
{
	private static final String TAG = "CameraFragment";
	@BindView(R.id.face) RelativeLayout mFaceLayout;
	@BindView(R.id.camera_preview)  SurfaceView mCameraPreView;
	//@BindView(R.id.time_tv)TextView time;
	//@BindView(R.id.speed_of_progress)ImageView speed_of_progress;
	private LayoutHouse house;
	private SurfaceHolder mHolder;
	private CameraControl mCameraControl=null;
	private Handler mHandler = new Handler();
	private int 		mOrientation=0;//方向
	private Sensor 		mSensor=null;
	private Uri 		mImagesUri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	private boolean mIsCamera=true;
	private int mCurrentTimer = 5;
	private String name;
	private String id_CardNo;
	private String birth;
	private boolean mIsSurfaceCreated = false;
	private boolean mIsTimerRunning = false;
	private GuestRoom.Bean gBean;
	private String locksign;
	private String k;
	private QueryCheckin.Bean b;
	private String querytype;
	private Affirmstay.Bean ab;
	private  boolean flag = false;
	@Override
	protected int layoutID() {
		return R.layout.fragment_camera;
	}

	@SuppressLint("HandlerLeak")
	@Override
	protected void initView() {
		super.initView();
		Log.e(TAG, "initView: " );
		//time.setTextColor(Color.RED);

	}

	@SuppressLint({ "InflateParams", "NewApi" })
	@Override
	protected void initData() {
		//speed_of_progress.setImageResource(R.drawable.home_five);
		Bundle bundle = this.getArguments();
		k = bundle.getString("k");
		assert k != null;
		switch (k) {
			case "1":
				name = bundle.getString("name");
				birth = bundle.getString("birth");
				id_CardNo = bundle.getString("id_CardNo");
				gBean = (GuestRoom.Bean) bundle.getSerializable("bean");
				locksign = bundle.getString("locksign");

				break;
			case "2":
				b = (QueryCheckin.Bean) bundle.getSerializable("bean");
				querytype = bundle.getString("querytype");
				ab = (Affirmstay.Bean) bundle.getSerializable("bean2");

				break;
			default:
				break;
		}
		requestNeedPermissions();

		mCameraControl=new CameraControl(this);
		mCameraPreView.getHolder().addCallback(mCameraControl);
		mCameraPreView.getHolder().addCallback(new Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
			}
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				//updatePicturePreView();
			}
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
									   int height) {
			}
		});
		openSensor();
		//mCameraControl.changeCamera();
			mHandler.postDelayed(timerRunnable,4*1000);
	}
	private Runnable timerRunnable = new Runnable() {
		@Override
		public void run() {
			mHandler.postDelayed(this,5*1000);
//			if (mCurrentTimer > 0) {
//				//time.setText(mCurrentTimer + "");
//
//				mCurrentTimer--;
//				mHandler.postDelayed(timerRunnable, 1000);
//			} else {
//				//time.setText("");
			Log.e(TAG, "run: "+345 );
				playSound();

				try {
					if(mIsCamera) {
						mCameraControl.takePicture(CameraFragment.this, (int) MyMath.doDegress(90 - MyMath.orientationToDegress(mOrientation)));
					}else {

					}
				} catch (SetParametersException e) {
					e.printStackTrace();
				}


			//	mIsTimerRunning = false;
			//	mCurrentTimer = 5;
			//}
		}
	};

	/**
	 *   播放系统拍照声音
	 */
	public void playSound() {
		Log.e(TAG, "playSound: "+123 );
		MediaPlayer mediaPlayer = null;
		AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
		int volume = audioManager.getStreamVolume( AudioManager.STREAM_NOTIFICATION);

		if (volume != 0) {
			mediaPlayer = MediaPlayer.create(getActivity(),
                    Uri.parse("file:///system/media/audio/ui/camera_click.ogg"));
			if (null != mediaPlayer) {
				mediaPlayer.start();
			}
		}
	}
	/**
	 * 改变方向并作动画效果
	 * @param orientation [0 , 7] 方向分别为 0上面朝下  1左上面朝下 2左面朝下 3左下面朝下 。。。依次类推
	 */
	private void changeOrientation(int orientation)
	{
		RotateAnimation rotateAnimationTakePicture;
		RotateAnimation rotateAnimationBase;
		RotateAnimation rotateAnimationPicture;
		RotateAnimation rotateAnimationFocus;
		RotateAnimation rotateAnimationSetting;
		float degress;
		float predegress;
		predegress= MyMath.orientationToDegress(mOrientation);
		predegress=MyMath.doDegress(predegress);
		mOrientation=orientation;
		degress= MyMath.orientationToDegress(mOrientation);
		degress=MyMath.doDegress(degress);
		if(Math.abs(predegress-degress)>180)
		{
			if(predegress>degress)
			{
				degress+=360;
			}
			else
			{
				predegress+=360;
			}
		}
		//Log.i(TAG, "ori"+orientation+"pre:"+predegress+" to:"+degress);
		rotateAnimationTakePicture=new RotateAnimation(predegress,degress,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		rotateAnimationTakePicture.setDuration(240);
		rotateAnimationTakePicture.setFillAfter(true);//停留在执行完的状态
		rotateAnimationBase=new RotateAnimation(predegress,degress,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		rotateAnimationBase.setDuration(240);
		rotateAnimationBase.setFillAfter(true);//停留在执行完的状态
		rotateAnimationPicture=new RotateAnimation(predegress,degress,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		rotateAnimationPicture.setDuration(240);
		rotateAnimationPicture.setFillAfter(true);//停留在执行完的状态
		rotateAnimationFocus=new RotateAnimation(predegress,degress,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		rotateAnimationFocus.setDuration(240);
		rotateAnimationFocus.setFillAfter(true);//停留在执行完的状态
		rotateAnimationSetting=new RotateAnimation(predegress,degress,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		rotateAnimationSetting.setDuration(240);
		rotateAnimationSetting.setFillAfter(true);//停留在执行完的状态
/*
	*//*	mButtonChangeCamera.clearAnimation();
		mButtonChangeCamera.setAnimation(rotateAnimationBase);

		mButtonMore.clearAnimation();
		mButtonMore.setAnimation(rotateAnimationBase);*//*

		rotateAnimationBase.startNow();

		mButtonTakePicture.clearAnimation();
		mButtonTakePicture.setAnimation(rotateAnimationTakePicture);
		rotateAnimationTakePicture.startNow();

		mButtonPicture.clearAnimation();
		mButtonPicture.setAnimation(rotateAnimationPicture);
		rotateAnimationPicture.startNow();*/
	}


	private void updatePicturePreView()
	{
//		try {
//			Cursor cursor=getActivity().getContentResolver().query(mImagesUri, null, null, null, null);
//			Log.i(TAG, ""+cursor.getColumnCount());
//			//_data : 1
//			if(cursor.getCount() > 0)
//			{
//				cursor.moveToLast();
//				try
//				{
//					String picturePath=null;
//					picturePath=cursor.getString(1);
//					if(picturePath!=null)
//					{
//						Log.i(TAG, ""+picturePath);
////						Bitmap resbmp=BitmapFactory.decodeResource(getResources(), R.drawable.picture);
////						Bitmap filebmp=BitmapFactory.decodeFile(picturePath);
////						Bitmap bmp= BitmapWork.getMainBitmap(filebmp, resbmp.getWidth(), resbmp.getHeight());
////						bmp=BitmapWork.roundBitmap(bmp,bmp.getWidth()*0.16f,bmp.getHeight()*0.16f,(bmp.getWidth()+bmp.getHeight())*0.04f,Color.GRAY);
//
//						//mButtonPicture.setImageBitmap(bmp);
//						Bundle bundle=new Bundle();
//						bundle.putParcelable("bitmap",bmp);
//
//						Message msg=new Message();
//						msg.setData(bundle);
//						msg.what=1;
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				cursor.close();
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
	}



	private void openSensor()
	{
		//用于管理重力感应设备
		SensorManager sensorMgr;
		sensorMgr = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
		assert sensorMgr != null;
		mSensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if(mSensor!=null)
			sensorMgr.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_GAME);
	}

	private void CloseSensor()
	{
		SensorManager sensorMgr;
		sensorMgr = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
		assert sensorMgr != null;
		sensorMgr.unregisterListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		int orientation;
		float x;
		float y;
		float z;
		x=event.values[SensorManager.DATA_X];
		y=event.values[SensorManager.DATA_Y];
		z=event.values[SensorManager.DATA_Z];
		//System.out.println(""+z);
		orientation=MyMath.getOrientation(MyMath.getAngle(x, y));
		if(
				(z>=8.0f||z<=-8.0f) ||
						(orientation==mOrientation) ||
						(orientation==1) ||
						(orientation==3) ||
						(orientation==5) ||
						(orientation==7)	)
			return;
		changeOrientation(orientation);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}


	public void onPictureTaken(byte[] data, Camera camera) {

		try {
			Uri imageUri = getActivity().getContentResolver().insert(mImagesUri, new ContentValues());
			assert imageUri != null;
			OutputStream os = getActivity().getContentResolver().openOutputStream(imageUri);

			//旋转角度，保证保存的图片方向是对的
			Matrix matrix = new Matrix();
			matrix.setRotate(-90);
			assert os != null;
			os.write(data);
			os.flush();
			os.close();

		} catch (Exception e) {
			Toast.makeText(getActivity(), "保存照片失败，可能是您禁止程序对储存空间的访问。", Toast.LENGTH_SHORT).show();
		}

		try {
			Cursor cursor = getActivity().getContentResolver().query(mImagesUri, null, null, null, null);
			//_data : 1
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToLast();
				try {
					String picturePath;
					picturePath = cursor.getString(1);
					if (picturePath != null){
						mHandler.removeCallbacks(timerRunnable);
						Log.e(TAG, "onPictureTaken: "+picturePath );
					}
					switch (k) {
						case "1": {
							Intent intent = new Intent(getActivity(), PaymentActivity.class);
							intent.putExtra("path", picturePath);
							intent.putExtra("name", name);
							intent.putExtra("birth", birth);
							intent.putExtra("id_CardNo", id_CardNo);
							intent.putExtra("bean", gBean);
							intent.putExtra("locksign", locksign);
							intent.putExtra("k", k);
							startActivity(intent);
							break;
						}
						case "2": {
							Intent intent = new Intent(getActivity(), PaymentActivity.class);
							intent.putExtra("path", picturePath);
							intent.putExtra("bean", b);
							intent.putExtra("querytype", querytype);
							intent.putExtra("bean2",ab);
							//	querytype =getIntent().getStringExtra("querytype");
							intent.putExtra("k", k);
							startActivity(intent);
							break;
						}
						default: {
							Intent intent = new Intent();
							intent.putExtra("path", picturePath);
							getActivity().setResult(Activity.RESULT_OK, intent);
							break;
						}
					}
					getActivity().finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
				cursor.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}


	@Override
	public void onResume() {
		super.onResume();
		Log.e(TAG, "onResume: " );
//		mIsTimerRunning = false;
//		mCurrentTimer = 5;
	//	mHandler.postDelayed(timerRunnable,5*1000);
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.e(TAG, "onPause: " );
		flag =true;
		mHandler.removeCallbacks(timerRunnable);
	}

	@Override
	public void onDestroy() {
		Log.e(TAG, "onDestroy: " );
		CloseSensor();
		mHandler.removeCallbacks(timerRunnable);
		super.onDestroy();
	}

	@Override
	public void onFocus(boolean success, List<Area> areas) {

	}


	@SuppressWarnings("unchecked")
	@Override
	public void onCameraChange(Camera camera,int id) {
	}

	private void requestNeedPermissions()
	{
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
		{
			ArrayList<String> permissions= new ArrayList<>();
			if(this.getActivity().checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
			{
				permissions.add(Manifest.permission.CAMERA);
			}
			if(this.getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
			{
				permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
			}
			if(this.getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
			{
				permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
			}
			if(permissions.size()>0)
			{
				String []strs=new String[permissions.size()];
				permissions.toArray(strs);
				this.requestPermissions(strs, 0);
			}
		}
		else
		{
		}
	}

	@SuppressLint("InlinedApi")
	@Override
	public void onRequestPermissionsResult(int requestCode,
										   @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		for(int i=0;i<permissions.length;i++)
		{
			if(grantResults[i]== PackageManager.PERMISSION_GRANTED)
			{//允许
				if(permissions[i].equals(Manifest.permission.CAMERA))
				{
					if(mCameraControl.getCamera()==null)
					{
						Log.i(TAG, "reopen ");
						mCameraControl.reopenCamera();
					}
				}
			}
			else
			{
				if(permissions[i].equals(Manifest.permission.CAMERA))
				{
					Toast.makeText(this.getContext(), "您已禁止程序调用摄像头权限，程序将无法拍照和录像。请尝试在->设置->权限管理 中允许调用摄像头.",Toast.LENGTH_LONG).show() ;
				}
				if(permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE))
				{
					Toast.makeText(this.getContext(), "您已禁止程序读取外部存储，程序将无法预览图片。请尝试在->设置->应用管理->本应用->权限 中打开存储功能.",Toast.LENGTH_LONG).show() ;
				}
				if(permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE))
				{
					Toast.makeText(this.getContext(), "您已禁止程序写入外部存储，程序将无法将照片和录像保存。请尝试在->设置->应用管理->本应用->权限 中打开存储功能.",Toast.LENGTH_LONG).show() ;
				}
			}
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		Log.e(TAG, "onCreateView: " );
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "onCreate: " );
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		Log.e(TAG, "onActivityCreated: " );
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		Log.e(TAG, "onStart: " );
		if (flag){
			mHandler.postDelayed(timerRunnable,5*1000);
		}
		super.onStart();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.e(TAG, "onDestroyView: " );
	}

	@Override
	public void onDetach() {
		Log.e(TAG, "onDetach: " );
		super.onDetach();
	}
}
