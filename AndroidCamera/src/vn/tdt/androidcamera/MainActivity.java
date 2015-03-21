package vn.tdt.androidcamera;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements SurfaceHolder.Callback {

	private ImageView mImageGallery;
	private ImageView mImageSetting;
	private ImageView imgViewCapture;
	private ImageView imgViewEffect;
	private Context mContext;

	Camera camera;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	boolean previewing = false;
	LayoutInflater controlInflater = null;

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// getWindow().setFormat(PixelFormat.UNKNOWN);
		surfaceView = (SurfaceView) findViewById(R.id.camerapreview);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		// surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
		controlInflater = LayoutInflater.from(getBaseContext());
		View viewControl = controlInflater
				.inflate(R.layout.activity_main, null);
		@SuppressWarnings("deprecation")
		LayoutParams layoutParamsControl = new LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT);
		this.addContentView(viewControl, layoutParamsControl);

		mContext = getApplicationContext();

		mImageGallery = (ImageView) findViewById(R.id.image_gallery);
		mImageSetting = (ImageView) findViewById(R.id.image_setting);
		imgViewCapture = (ImageView) findViewById(R.id.imgViewCapture);
		imgViewEffect = (ImageView) findViewById(R.id.imgViewEffect);
		
		mImageGallery.setOnClickListener(mGlobal_OnClickListener);
		mImageSetting.setOnClickListener(mGlobal_OnClickListener);
		imgViewCapture.setOnClickListener(mGlobal_OnClickListener);
		imgViewEffect.setOnClickListener(mGlobal_OnClickListener);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		if (previewing) {
			camera.stopPreview();
			previewing = false;
		}

		if (camera != null) {
			try {
				camera.setPreviewDisplay(surfaceHolder);
				camera.startPreview();
				previewing = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		camera = Camera.open();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		camera.stopPreview();
		camera.release();
		camera = null;
		previewing = false;
	}

	final OnClickListener mGlobal_OnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == mImageGallery.getId()) {
				Intent intent = new Intent(mContext, PhotoGalleryActivity.class);
				startActivity(intent);

			}
			if (v.getId() == mImageSetting.getId()) {
				Toast.makeText(getApplicationContext(), "setting ", Toast.LENGTH_SHORT).show();
			}
			if(v.getId()== imgViewCapture.getId()){
				Toast.makeText(getApplicationContext(), "catured", Toast.LENGTH_SHORT).show();
			}
			if(v.getId()== imgViewEffect.getId()){
				Toast.makeText(getApplicationContext(), "effect", Toast.LENGTH_SHORT).show();
			}

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
