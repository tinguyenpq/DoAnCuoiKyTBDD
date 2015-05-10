package vn.tdt.androidcamera.controllers;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import vn.tdt.androidcamera.R;
import vn.tdt.androidcamera.R.id;
import vn.tdt.androidcamera.R.layout;
import vn.tdt.androidcamera.constant.PathConstant;
import vn.tdt.androidcamera.models.SharedPreferencesModels;
import vn.tdt.androidcamera.tabactionbar.GalleryActivity;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.FaceDetector;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements SurfaceHolder.Callback {

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

	DrawingView drawingView;
	Face[] detectedFaces;

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

		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

		getWindow().setFormat(PixelFormat.UNKNOWN);
		surfaceView = (SurfaceView) findViewById(R.id.cameraPreview);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		drawingView = new DrawingView(this);
		LayoutParams layoutParamsDrawing = new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		this.addContentView(drawingView, layoutParamsDrawing);

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

		lastestPhotoTaken = prm.getStringValue(PathConstant.LASTEST_PHOTO);

		// LinearLayout layoutBackground =
		// (LinearLayout)findViewById(R.id.background);
		// layoutBackground.setOnClickListener(new
		// LinearLayout.OnClickListener(){
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		//
		// imgViewCapture.setEnabled(false);
		// camera.autoFocus(myAutoFocusCallback);
		// }});
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
				camera.startFaceDetection();
				previewing = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// if (this.getResources().getConfiguration().orientation ==
		// Configuration.) {
		//
		// camera.setDisplayOrientation(90);
		//
		// }
	}

	@SuppressWarnings("deprecation")
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

			Camera.Parameters p = camera.getParameters();
			p.set("jpeg-quality", 100);
			p.setRotation(90);
			p.setPictureFormat(PixelFormat.JPEG);
			p.setPreviewSize(p.getPreviewSize().width,
					p.getPreviewSize().height);
			camera.setParameters(p);
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
				// prompt.setText(" No Face Detected! ");
				drawingView.setHaveFace(false);
			} else {
				// prompt.setText(String.valueOf(faces.length)
				// + " Face Detected :) ");
				drawingView.setHaveFace(true);
				detectedFaces = faces;
			}

			drawingView.invalidate();

		}
	};

	AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback() {

		@Override
		public void onAutoFocus(boolean arg0, Camera arg1) {
			// TODO Auto-generated method stub
			imgViewCapture.setEnabled(true);
		}
	};

	ShutterCallback myShutterCallback = new ShutterCallback() {

		@Override
		public void onShutter() {
			// TODO Auto-generated method stub

		}
	};

	PictureCallback myPictureCallback_RAW = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1) {
			// TODO Auto-generated method stub

		}
	};

	OnClickListener mGlobal_OnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == mImageGallery.getId()) {
				// Intent intent = new Intent(mContext, GalleryActivity.class);
				Intent intent = new Intent(mContext, PhotoEditorMain.class);
				startActivity(intent);

			}
			if (v.getId() == mImageSetting.getId()) {
				Toast.makeText(getApplicationContext(), "Setting ",
						Toast.LENGTH_SHORT).show();
			}
			if (v.getId() == imgViewCapture.getId()) {

				camera.takePicture(myShutterCallback, myPictureCallback_RAW,
						new PictureCallback() {

							@Override
							public void onPictureTaken(byte[] data, Camera cam) {

								BitmapFactory.Options option = new Options();
								option.inPreferredConfig = Bitmap.Config.ARGB_8888;
								option.inSampleSize = 0;
								option.inPreferQualityOverSpeed = true;
								option.inSampleSize = 0;
								option.inScaled = true;

								Bitmap b = BitmapFactory.decodeByteArray(data,
										0, data.length, option);

								// file name to save
								String fileName = Ultilities.getFileName(1);

								// path to store photo taken
								String path = Ultilities.pathToSave(1);

								// Ultilities.takePictureHandler(b, fileName,
								// path);

								// save lastest photo to file
								prm.saveStringValue(PathConstant.LASTEST_PHOTO,
										fileName);

								// Ultilities.toastShow(mContext,
								// prm.getStringValue(LASTEST_PHOTO)
								// + " has saved " + path, Gravity.CENTER);

								// set screenshot photo was taken recently to
								// ImageView
								mImageGallery.setImageBitmap(Bitmap
										.createScaledBitmap(b, 64, 64, false));

								Intent intentPhotoTaken = new Intent(mContext,
										OptionAfterShutterActivity.class);
								Bundle bundle = new Bundle();
								bundle.putByteArray("image", BitmapHandler
										.convertBitMapToByteArray(b));
								bundle.putString("path", path);
								bundle.putString("fileName", fileName);

								// Đưa Bundle vào Intent
								intentPhotoTaken.putExtra("PhotoTakenPackage",
										bundle);
								startActivity(intentPhotoTaken);
								finish();

								// refeshCamera();
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

	private class DrawingView extends View {

		boolean haveFace;
		Paint drawingPaint;
		private int width, height;
		private int NUMBER_OF_FACES = 6;
		private FaceDetector faceDetector;
		private int NUMBER_OF_FACE_DETECTED;
		private float eyeDistance;

		public DrawingView(Context context) {
			super(context);
			width = getWidth();
			height = getHeight();
//			width = getHeight();
//			height = getWidth();
			haveFace = false;
			drawingPaint = new Paint();
			drawingPaint.setColor(Color.GREEN);
			drawingPaint.setStyle(Paint.Style.STROKE);
			drawingPaint.setStrokeWidth(2);
		}

		public void setHaveFace(boolean h) {
			haveFace = h;
		}

		@Override
		protected void onDraw(Canvas canvas) {
//			if (haveFace) {
//
//				// Camera driver coordinates range from (-1000, -1000) to (1000,
//				// 1000).
//				// UI coordinates range from (0, 0) to (width, height).
//
//				 int vWidth =getWidth() ;
//				 int vHeight = getHeight();
//
//				for (int i = 0; i < detectedFaces.length; i++) {
//
//					int l = detectedFaces[i].rect.left;
//					int t = detectedFaces[i].rect.top;
//					int r = detectedFaces[i].rect.right;
//					int b = detectedFaces[i].rect.bottom;
//					
//					int left = (l + 1000) * vWidth / 2000;
//					int top = (t + 1000) * vHeight / 2000;
//					int right = (r + 1000) * vWidth / 2000;
//					int bottom = (b + 1000) * vHeight / 2000;
//					
//					canvas.drawRect(left, top, right, bottom, drawingPaint);
//				}
//			} else {
//				canvas.drawColor(Color.TRANSPARENT);
//			}
		}

	}

	public void drawSurfaceView(Canvas canvas) {

	}

}
