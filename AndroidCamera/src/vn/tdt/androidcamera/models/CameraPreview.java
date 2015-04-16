package vn.tdt.androidcamera.models;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback {
	private Camera mCamera;
	private SurfaceHolder surfaceHolder;
	boolean previewing = false;

	public CameraPreview(Context context, Camera camera) {
		super(context);
		this.mCamera = camera;
		this.surfaceHolder = this.getHolder();
		this.surfaceHolder.addCallback(this);
		this.surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera.setPreviewDisplay(surfaceHolder);
			mCamera.startPreview();
		} catch (IOException e) {
			// left blank for now
		}

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {


		if (previewing) {
			mCamera.stopPreview();
			previewing = false;

		}

		if (mCamera != null) {
			try {

				mCamera.setPreviewDisplay(surfaceHolder);
				mCamera.startPreview();
				previewing = true;

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
		previewing = false;

	}

}
