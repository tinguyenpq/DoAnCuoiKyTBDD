package vn.tdt.androidcamera;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback {
	private Camera mCamera;
	private SurfaceHolder mSurfaceHolder;
	private boolean mPreviewing = false;

	public CameraPreview(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mCamera = Camera.open();

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (mPreviewing == true) {
			mCamera.stopPreview();
			mPreviewing = false;
		}

		if (mCamera != null) {
			try {
				mCamera.setPreviewDisplay(mSurfaceHolder);
				mCamera.startPreview();
				mPreviewing = true;
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
		mPreviewing = false;

	}

}
