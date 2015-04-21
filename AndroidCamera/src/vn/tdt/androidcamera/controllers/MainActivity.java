package vn.tdt.androidcamera.controllers;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import vn.tdt.androidcamera.R;
import vn.tdt.androidcamera.R.id;
import vn.tdt.androidcamera.R.layout;
import vn.tdt.androidcamera.models.SharedPreferencesModels;
import vn.tdt.androidcamera.tabactionbar.GalleryActivity;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements SurfaceHolder.Callback {

	public final static String LASTEST_PHOTO = "lastest_photo";
	public final static String DEFAULT_PATH = "/mnt/sdcard/TdtCamera";

	private ImageView mImageGallery;
	private ImageView mImageSetting;
	private ImageView imgViewCapture;
	private ImageView imgViewEffect;
	private Context mContext;
	HorizontalScrollView horizontalScrollViewListEffect;

	ImageView imgViewEffect1;
	ImageView imgViewEffect2;
	ImageView imgViewEffect3;
	ImageView imgViewEffect4;
	ImageView imgViewEffect5;
	ImageView imgViewEffect6;
	ImageView imgViewEffect7;
	ImageView imgViewEffect8;
	ImageView imgViewEffect9;

	Camera camera;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	boolean previewing = false;
	LayoutInflater controlInflater = null;
	String lastestPhotoTaken;

	// To save or get data from xml file
	SharedPreferencesModels prm;

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_surface);

		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		// getWindow().setFormat(PixelFormat.UNKNOWN);
		surfaceView = (SurfaceView) findViewById(R.id.cameraPreview);
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

		// preferences for put and get data from xml file
		prm = new SharedPreferencesModels(mContext);

		horizontalScrollViewListEffect = (HorizontalScrollView) findViewById(R.id.horizontalScrollViewListEffect);
		imgViewEffect1 = (ImageView) findViewById(R.id.ivEff1);
		imgViewEffect2 = (ImageView) findViewById(R.id.ivEff2);
		imgViewEffect3 = (ImageView) findViewById(R.id.ivEff3);
		imgViewEffect4 = (ImageView) findViewById(R.id.ivEff4);
		imgViewEffect5 = (ImageView) findViewById(R.id.ivEff5);
		imgViewEffect6 = (ImageView) findViewById(R.id.ivEff6);
		imgViewEffect7 = (ImageView) findViewById(R.id.ivEff7);
		imgViewEffect8 = (ImageView) findViewById(R.id.ivEff8);
		imgViewEffect9 = (ImageView) findViewById(R.id.ivEff9);

		mImageGallery.setOnClickListener(mGlobal_OnClickListener);
		mImageSetting.setOnClickListener(mGlobal_OnClickListener);
		imgViewCapture.setOnClickListener(mGlobal_OnClickListener);
		imgViewEffect.setOnClickListener(mGlobal_OnClickListener);
		horizontalScrollViewListEffect
				.setOnClickListener(mGlobal_OnClickListener);
		surfaceView.setOnClickListener(mGlobal_OnClickListener);

		imgViewEffect1.setOnClickListener(mGlobal_OnClickListener);
		imgViewEffect2.setOnClickListener(mGlobal_OnClickListener);
		imgViewEffect3.setOnClickListener(mGlobal_OnClickListener);
		imgViewEffect4.setOnClickListener(mGlobal_OnClickListener);
		imgViewEffect5.setOnClickListener(mGlobal_OnClickListener);
		imgViewEffect6.setOnClickListener(mGlobal_OnClickListener);
		imgViewEffect7.setOnClickListener(mGlobal_OnClickListener);
		imgViewEffect8.setOnClickListener(mGlobal_OnClickListener);
		imgViewEffect9.setOnClickListener(mGlobal_OnClickListener);

		lastestPhotoTaken = prm.getStringValue(LASTEST_PHOTO);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		if (previewing) {
			camera.stopFaceDetection();
			camera.stopPreview();
			previewing = false;
		}

		if (camera != null) {
			try {
				camera.setPreviewDisplay(surfaceHolder);
				camera.startPreview();
				// Ultilities.toastShow(getApplicationContext(), camera
				// .getParameters().getMaxNumDetectedFaces() + "",
				// Gravity.CENTER);
				// camera.startFaceDetection();
				previewing = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		try {
			camera = Camera.open();
			camera.setFaceDetectionListener(faceDetectionListener);
			camera.setDisplayOrientation(90);
			// Camera.Parameters params = camera.getParameters();
			// surfaceView.getLayoutParams().width =
			// params.getPreviewSize().height;
			// surfaceView.getLayoutParams().height =
			// params.getPreviewSize().width;
			// int picH = params.getPictureSize().height;
			// int picW = params.getPictureSize().width;
			// int preH = params.getPreviewSize().height;
			// int preW = params.getPreviewSize().width;
			// float scale = ((float) (picH * preW)) / ((float) (picW * preH));
			// params.setPictureSize(picW, picH);
		} catch (RuntimeException e) {
			Ultilities.toastShow(getApplicationContext(), "Camera not open!",
					Gravity.CENTER);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		camera.stopFaceDetection();
		camera.stopPreview();
		camera.release();
		camera = null;
		previewing = false;
	}

	FaceDetectionListener faceDetectionListener = new FaceDetectionListener() {
		@Override
		public void onFaceDetection(Face[] faces, Camera camera) {

			if (faces.length == 0) {
				Ultilities.toastShow(getApplicationContext(),
						" no faces detected", Gravity.CENTER);
			} else {
				// prompt.setText(String.valueOf(faces.length) +
				// " Face Detected :) ");
				// Ultilities.toastShow(getApplicationContext(), faces.length
				// + " faces detected", Gravity.CENTER);
			}

		}
	};

	AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback() {

		@Override
		public void onAutoFocus(boolean arg0, Camera arg1) {
			// TODO Auto-generated method stub
		}
	};

	PictureCallback myPictureCallback_RAW = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1) {
			// TODO Auto-generated method stub

		}
	};

	final OnClickListener mGlobal_OnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == mImageGallery.getId()) {
				Intent intent = new Intent(mContext, GalleryActivity.class);
				startActivity(intent);

			}
			if (v.getId() == mImageSetting.getId()) {
				Toast.makeText(getApplicationContext(), "Setting ",
						Toast.LENGTH_SHORT).show();
			}
			if (v.getId() == imgViewCapture.getId()) {

				camera.takePicture(null, null, new PictureCallback() {

					@Override
					public void onPictureTaken(byte[] data, Camera cam) {

						BitmapFactory.Options option = new Options();
						option.inSampleSize = 0;
						option.inPreferQualityOverSpeed = true;
						option.inSampleSize = 0;
						option.inScaled = true;
						
						Bitmap b = BitmapFactory.decodeByteArray(data, 0,
								data.length, option);
						
						String fileName = Ultilities.getFileName(1);
						String path = Ultilities.pathToSave(1);
						//Ultilities.takePictureHandler(b, fileName, path);

						// save lastest photo to file
						prm.saveStringValue(LASTEST_PHOTO, fileName);
						Ultilities.toastShow(mContext,
								prm.getStringValue(LASTEST_PHOTO)
										+ " has saved " + path, Gravity.CENTER);

						// set screenshot photo was taken recently to ImageView
						mImageGallery.setImageBitmap(Bitmap.createScaledBitmap(
								b, 64, 64, false));
						
						Intent intentPhotoTaken = new Intent(mContext,OptionAfterShutterActivity.class);
						Bundle bundle=new Bundle();
						 bundle.putByteArray("image", BitmapHandler.convertBitMapToByteArray(b));
						 bundle.putString("path",path);
						 bundle.putString("fileName", fileName);
						 
						 //Đưa Bundle vào Intent
						 intentPhotoTaken.putExtra("PhotoTakenPackage", bundle);
						 startActivity(intentPhotoTaken);
						 finish();

						//refeshCamera();
					}
				});
			}
			if (v.getId() == imgViewEffect.getId()) {
				Toast.makeText(getApplicationContext(), "Effect",
						Toast.LENGTH_SHORT).show();

				mImageGallery.setVisibility(View.GONE);
				imgViewCapture.setVisibility(View.GONE);
				imgViewEffect.setVisibility(View.GONE);
				horizontalScrollViewListEffect.setVisibility(View.VISIBLE);

			}
			if (v.getId() == surfaceView.getId()) {
				if (horizontalScrollViewListEffect.getVisibility() == View.VISIBLE) {
					horizontalScrollViewListEffect.setVisibility(View.GONE);
					mImageGallery.setVisibility(View.VISIBLE);
					imgViewCapture.setVisibility(View.VISIBLE);
					imgViewEffect.setVisibility(View.VISIBLE);
				}
			}

			if (v.getId() == imgViewEffect1.getId()) {
				prm.saveIntValue("current_effect", 5);
			}
			if (v.getId() == imgViewEffect2.getId()) {
				Ultilities.toastShow(mContext,
						prm.getIntValue("current_effect") + "", Gravity.CENTER);
			}

		}
	};

	public void refeshCamera() {
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

}
