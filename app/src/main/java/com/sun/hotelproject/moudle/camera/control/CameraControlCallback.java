package com.sun.hotelproject.moudle.camera.control;

import android.hardware.Camera;

import java.util.List;

public interface CameraControlCallback {
	void onFocus(boolean success, List<Camera.Area> areas);
	 void onCameraChange(Camera camera, int id);
}
