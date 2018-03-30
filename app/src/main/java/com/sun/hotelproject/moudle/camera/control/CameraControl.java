package com.sun.hotelproject.moudle.camera.control;
/**
 * 相机控制
 * @author wangchen11
 */

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Area;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;


import com.sun.hotelproject.moudle.camera.tools.MyMath;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CameraControl implements SurfaceHolder.Callback,Camera.AutoFocusCallback{
	private final static String TAG="CameraControl";
	private Camera mCamera=null;
	private SurfaceHolder mSurfaceHolder=null;
	private int mCameraId=0;
	private CameraControlCallback mCallback=null;
	private MediaRecorder mMediaRecorder=null;
	private boolean mIsTakingVedio=false;

	public CameraControl() {
	}

	public CameraControl(CameraControlCallback callback)
	{
		setOnFocusCallback(callback);
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.i(TAG, "surfaceCreated");
		if(mCamera==null)
			openCamera();
		startPreView(holder);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
							   int height) {
		Log.i(TAG, "surfaceChanged");
		if(mCamera==null)
			openCamera();
		startPreView(holder);
		startPreView(holder);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i(TAG, "surfaceDestroyed");
		if(mCamera==null)
			return;
		mCamera.stopPreview();
		mCamera.release();
		stopTakeVedio();
		mCamera=null;
	}

	public void setOnFocusCallback(CameraControlCallback callback)
	{
		mCallback=callback;
	}

	/**
	 * 拍摄一张照片
	 *
	 * @param
	 * @throws SetParametersException 设置相机参数失败会出抛出此异常
	 */
	public void takePicture(PictureCallback pictureCallback,int degree) throws SetParametersException
	{
		if(mCamera==null)
			return;
		Parameters parameters=mCamera.getParameters();
		CameraInfo cameraInfo=new CameraInfo();
		Camera.getCameraInfo(mCameraId, cameraInfo);
		if(cameraInfo.facing==CameraInfo.CAMERA_FACING_BACK)
		{
			parameters.setRotation(degree);
		}
		else
		if(cameraInfo.facing==CameraInfo.CAMERA_FACING_FRONT)
		{
			parameters.setRotation((int) MyMath.doDegress(-degree));
		}
		try
		{
			mCamera.setParameters(parameters);
			mCamera.takePicture(null, null, pictureCallback);
		} catch (Exception e) {
			Log.w(TAG, "setParameters has Exception");
			throw new SetParametersException("setParameters error");
		}
	}

	@SuppressLint("SdCardPath")
	public void takeVedio(int degree)
	{
		if (mMediaRecorder == null) {
			mMediaRecorder = new MediaRecorder(); // Create MediaRecorder
		}
		try {
			List<Size> vedioSizes=mCamera.getParameters().getSupportedVideoSizes();
			Size vedioSize=vedioSizes.get(0);
			mCamera.unlock();
			CameraInfo cameraInfo=new CameraInfo();
			Camera.getCameraInfo(mCameraId, cameraInfo);
			if(cameraInfo.facing==CameraInfo.CAMERA_FACING_BACK)
			{
				mMediaRecorder.setOrientationHint(degree);
			}
			else
			if(cameraInfo.facing==CameraInfo.CAMERA_FACING_FRONT)
			{
				mMediaRecorder.setOrientationHint((int) MyMath.doDegress(-degree));
			}
			mMediaRecorder.setCamera(mCamera);
			mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
			mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			// Set output file format
			mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);

			// 这两项需要放在setOutputFormat之后
			mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);

			mMediaRecorder.setVideoSize(vedioSize.width, vedioSize.height);//1920*1080
			mMediaRecorder.setVideoFrameRate(20);
			mMediaRecorder.setVideoEncodingBitRate((int) (vedioSize.width*vedioSize.height*4));//比特率

			// Set output file path
			String path = Environment.getExternalStorageDirectory().toString();

			File dir = new File(path + "");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			path = dir + "/" + System.currentTimeMillis() + ".mp4";
			mMediaRecorder.setOutputFile(path);
			mMediaRecorder.prepare();
			mMediaRecorder.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			mCamera.lock();
		}
		mIsTakingVedio = true;
	}

	public void stopTakeVedio()
	{
		if (mIsTakingVedio&&mMediaRecorder != null) {
			try {
				mMediaRecorder.stop();
				mMediaRecorder.reset();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		mIsTakingVedio=false;
	}
	public boolean isTakingVedio()
	{
		return mIsTakingVedio;
	}

	public void focusOn(float x,float y)//x,y [0-1] 点击位置的比例
	{
		Parameters parameters;
		Rect rect;
		ArrayList< Area> focusAreas;
		if(mCamera==null)
			return;
		focusAreas=new ArrayList<Area>();
		parameters=mCamera.getParameters();
		rect=new Rect( 	(int)((x-0.5f)*2000-100),(int)((y-0.5f)*2000-100),
				(int)((x-0.5f)*2000+100),(int)((y-0.5f)*2000+100) );
		/*
		 * range from -1000 to 1000. (-1000, -1000) is the upper left point. (1000, 1000) is the lower right point. The width and height of focus areas cannot be 0 or negative.
		 * The weight must range from 1 to 1000.
		 */
		Log.i(TAG, "x:"+x+" y:"+y);
		focusAreas.add(legalizeFocusRect(new Area(rect,1000)));
		//System.out.println("focus on "+rect.top+":"+rect.bottom+" : "+rect.left+":"+rect.right+" max focus num:"+parameters.getMaxNumFocusAreas());
		parameters.setFocusAreas(focusAreas);
		parameters.setMeteringAreas(focusAreas);
		try {
			//parameters=mCamera.getParameters();
			parameters.setFocusMode(Parameters.FOCUS_MODE_AUTO);
			mCamera.cancelAutoFocus();
			mCamera.setParameters(parameters);
			mCamera.autoFocus(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reopenCamera()
	{
		openCamera();
		startPreView(mSurfaceHolder);
	}
	/**
	 * 切换摄像头
	 */
	public void changeCamera()
	{
		int numberOfCameras=0;
		numberOfCameras=getNumberOfCameras();
		mCameraId++;
		if(mCameraId>=numberOfCameras)
			mCameraId=0;
		openCamera();
		startPreView(mSurfaceHolder);
	}

	/**
	 * 获取正在使用的摄像头的id
	 */
	public int getCamerasId()
	{
		return mCameraId;
	}

	/**
	 * 设置要使用的摄像头的id
	 * @param id 摄像头id
	 */
	public void setCamerasId(int id)
	{
		mCameraId=id;
		openCamera();
		startPreView(mSurfaceHolder);
	}

	/**
	 * 获取相机个数
	 * @return 返回相机个数
	 */
	public int getNumberOfCameras()
	{
		return Camera.getNumberOfCameras();
	}

	/**
	 * 获取当前相机
	 * @return 返回当前的相机，可能返回null
	 */
	public Camera getCamera()
	{
		return mCamera;
	}

	public List<Size> getCameraSupportedPreviewSizes()
	{
		if(mCamera==null)
			return null;
		return mCamera.getParameters().getSupportedPreviewSizes();
	}

	/**
	 * 开启预览
	 *
	 */
	@SuppressLint("NewApi")
	private void openCamera()
	{
		if(mCamera!=null)
		{
			mCamera.stopPreview();
			mCamera.release();
			mCamera=null;
		}
		try
		{
			mCamera=Camera.open(mCameraId);
			Parameters parameters=mCamera.getParameters();
			parameters.setAntibanding(Parameters.ANTIBANDING_50HZ);
			mCamera.setDisplayOrientation(0);
			mCamera.setParameters(parameters);
			if(mCallback!=null)
				mCallback.onCameraChange(mCamera,mCameraId);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void startPreView(SurfaceHolder holder)
	{
		mSurfaceHolder=holder;
		if(mCamera==null||mSurfaceHolder==null)
			return;
		try
		{
			mCamera.setPreviewDisplay(mSurfaceHolder);
			mCamera.startPreview();//开始预览
			mCamera.autoFocus(this);//自动聚焦
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onAutoFocus(boolean success, Camera camera) {
		List<Area> areas=null;
		Parameters parameters;
		parameters=camera.getParameters();
		parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
	//	camera.setParameters(parameters);
		try
		{
			areas=camera.getParameters().getFocusAreas();
		} catch (Exception e)
		{
		}
		if(mCallback!=null)
		{
			mCallback.onFocus(success, areas);
		}
	}

	/**
	 * 使聚焦区域合法化
	 */
	private Area legalizeFocusRect(Area area)
	{
		if(area.rect.left>=1000)
			area.rect.left=999;
		if(area.rect.top>=1000)
			area.rect.top=999;
		if(area.rect.right>1000)
			area.rect.right=1000;
		if(area.rect.bottom>1000)
			area.rect.bottom=1000;

		if(area.rect.left<-1000)
			area.rect.left=-1000;
		if(area.rect.top<-1000)
			area.rect.top=-1000;
		if(area.rect.right<=-1000)
			area.rect.right=-999;
		if(area.rect.bottom<=-1000)
			area.rect.bottom=-999;

		if(area.rect.right<=area.rect.left)
			area.rect.right=area.rect.left+1;
		if(area.rect.bottom<=area.rect.top)
			area.rect.bottom=area.rect.top+1;

		if(area.weight<1)
			area.weight=1;
		if(area.weight>1000)
			area.weight=1000;
		return area;
	}
}