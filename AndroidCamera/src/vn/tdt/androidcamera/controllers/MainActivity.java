package vn.tdt.androidcamera.controllers;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import vn.tdt.androidcamera.R;
import vn.tdt.androidcamera.album.GalleryActivity;
import vn.tdt.androidcamera.constant.PathConstant;
import vn.tdt.androidcamera.models.SharedPreferencesModels;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.AudioManager;
import android.media.FaceDetector;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ZoomControls;

public class MainActivity extends Activity implements SurfaceHolder.Callback {

	private ImageView mImageGallery;
	private ImageView mImageSetting;
	private ImageView imgViewCapture;
	// private ImageView imgViewEffect;
	private ImageView ivFlash;
	private ImageView ivSwitchCamera;
	private Context mContext;
	SeekBar sbZoom;
	private ZoomControls zoomControls;

	public final static String ASK_TO_SAVE = "ask_save";
	public final static String SHUTTER_SOUND = "shutter_sound";
	public final static String TAP_TO_CAPTURE = "tap_cature";
	public final static String CAMERA_MODE = "camera_mode";

	Camera camera;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	boolean previewing = false;
	LayoutInflater controlInflater = null;
	Camera.Parameters params;
	int currentCameraId = 0;
	int numberOfCamera;
	private boolean isLighOn = false;

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

		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		getWindow().setFormat(PixelFormat.UNKNOWN);
		surfaceView = (SurfaceView) findViewById(R.id.cameraPreview);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		//
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
		// imgViewEffect = (ImageView) findViewById(R.id.imgViewEffect);
		ivFlash = (ImageView) findViewById(R.id.ivFlash);
		ivSwitchCamera = (ImageView) findViewById(R.id.ivSwitchCamera);
		sbZoom = (SeekBar) findViewById(R.id.sbZoom);
		// enableZoom();
		// preferences for put and get data from xml file
		prm = new SharedPreferencesModels(mContext);

		toggleFlashIcon();

		mImageGallery.setOnClickListener(mGlobal_OnClickListener);
		mImageSetting.setOnClickListener(mGlobal_OnClickListener);
		imgViewCapture.setOnClickListener(mGlobal_OnClickListener);
		// imgViewEffect.setOnClickListener(mGlobal_OnClickListener);
		ivFlash.setOnClickListener(mGlobal_OnClickListener);
		ivSwitchCamera.setOnClickListener(mGlobal_OnClickListener);
		surfaceView.setOnClickListener(mGlobal_OnClickListener);
		sbZoom.setOnSeekBarChangeListener(onSeekBarChangeHandler);

		lastestPhotoTaken = prm.getStringValue(PathConstant.LASTEST_PHOTO);
		// prm.saveIntValue(CAMERA_MODE, 0);

		// Ultilities.toastShow(mContext,
		// FileUltil.isFileExist(lastestPhotoTaken)+"", Gravity.CENTER);
		if (FileUltil.isFileExist(lastestPhotoTaken)) {
			mImageGallery.setImageBitmap(Bitmap.createScaledBitmap(
					BitmapHandler.convertImageToBitmap(lastestPhotoTaken), 64,
					64, false));
		} else {
			mImageGallery.setImageResource(R.drawable.image_gallery_normal_64);
		}

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
		Log.d("=======================change------------------", "change");
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
	}

	@SuppressWarnings("deprecation")
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			camera = Camera.open(prm.getIntValue(CAMERA_MODE));
			// Ultilities.toastShow(mContext, prm.getIntValue(CAMERA_MODE)
			// + "open", Gravity.CENTER);
			// camera.setFaceDetectionListener(faceDetectionListener);
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

			params = camera.getParameters();
			// params.setFlashMode(Parameters.FLASH_MODE_TORCH);
			params.set("jpeg-quality", 100);
			params.setRotation(90);
			params.setPictureFormat(PixelFormat.JPEG);
			sbZoom.setMax(params.getMaxZoom());
			params.setPreviewSize(params.getPreviewSize().width,
					params.getPreviewSize().height);
			// params.set("rotation", 90);
			camera.setParameters(params);

			camera.setPreviewDisplay(surfaceHolder);

		} catch (RuntimeException e) {
			Ultilities.toastShow(getApplicationContext(), "Camera not open!",
					Gravity.CENTER);
		}
		// camera.release();
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// AudioManager mgr = (AudioManager)
			// getSystemService(Context.AUDIO_SERVICE);
			// mgr.playSoundEffect(AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
			AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			mgr.setStreamMute(AudioManager.STREAM_SYSTEM, true);
		}
	};

	PictureCallback myPictureCallback_RAW = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1) {
			// TODO Auto-generated method stub

		}
	};

	OnSeekBarChangeListener onSeekBarChangeHandler = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (camera.getParameters().isZoomSupported()) {
				// Ultilities.toastShow(mContext, progress + "",
				// Gravity.CENTER);
				Parameters params = camera.getParameters();
				params.setZoom(progress);
				camera.setParameters(params);
			}

		}
	};
	OnClickListener mGlobal_OnClickListener = new OnClickListener() {
		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			if (v.getId() == mImageGallery.getId()) {
				Intent intent = new Intent(mContext, GalleryActivity.class);
				// Intent intent = new Intent(mContext, PhotoEditorMain.class);
				startActivity(intent);
				finish();

			}
			if (v.getId() == mImageSetting.getId()) {
				onClickSetting();
			}
			if (v.getId() == imgViewCapture.getId()) {

				if (prm.getIntValue(SHUTTER_SOUND) == 1) {
					shutterSoundOff();
				} else {
					shutterSoundOn();
				}
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

								String fullPath = path + "/" + fileName
										+ ".JPG";
								// Ultilities.toastShow(mContext, fullPath,
								// Gravity.CENTER);

								// set screenshot photo was taken recently to
								// ImageView
								mImageGallery.setImageBitmap(Bitmap
										.createScaledBitmap(b, 64, 64, false));
								if (prm.getIntValue(ASK_TO_SAVE) == 0) {

									Intent intentPhotoTaken = new Intent(
											mContext,
											OptionAfterShutterActivity.class);
									Bundle bundle = new Bundle();
									bundle.putByteArray("image", BitmapHandler
											.convertBitMapToByteArray(b));
									bundle.putString("path", path);
									bundle.putString("fileName", fileName);

									// Đưa Bundle vào Intent
									intentPhotoTaken.putExtra(
											"PhotoTakenPackage", bundle);
									startActivity(intentPhotoTaken);
									finish();
								} else {
									// save lastest photo to file
									prm.saveStringValue(PathConstant.LASTEST_PHOTO,
											fullPath);
									Ultilities.takePictureHandler(b, fileName,
											path);
									refeshCamera();
								}

							}
						});
			}
			// if (v.getId() == imgViewEffect.getId()) {
			// Toast.makeText(getApplicationContext(), "Effect",
			// Toast.LENGTH_SHORT).show();
			//
			// mImageGallery.setVisibility(View.GONE);
			// imgViewCapture.setVisibility(View.GONE);
			// imgViewEffect.setVisibility(View.GONE);
			// horizontalScrollViewListEffect.setVisibility(View.VISIBLE);
			//
			// }
			if (v.getId() == surfaceView.getId()) {
				try {
					if (prm.getIntValue(TAP_TO_CAPTURE) == 1) {
						if (prm.getIntValue(SHUTTER_SOUND) == 1) {
							shutterSoundOff();
						} else {
							shutterSoundOn();
						}
						camera.takePicture(myShutterCallback,
								myPictureCallback_RAW, new PictureCallback() {

									@Override
									public void onPictureTaken(byte[] data,
											Camera cam) {

										BitmapFactory.Options option = new Options();
										option.inPreferredConfig = Bitmap.Config.ARGB_8888;
										option.inSampleSize = 0;
										option.inPreferQualityOverSpeed = true;
										option.inSampleSize = 0;
										option.inScaled = true;
										Bitmap b = BitmapFactory
												.decodeByteArray(data, 0,
														data.length, option);
										String fileName = Ultilities
												.getFileName(1);
										String path = Ultilities.pathToSave(1);

										String fullPath = path + "/" + fileName
												+ ".JPG";
										prm.saveStringValue(
												PathConstant.LASTEST_PHOTO,
												fullPath);
										mImageGallery.setImageBitmap(Bitmap
												.createScaledBitmap(b, 64, 64,
														false));
										if (prm.getIntValue(ASK_TO_SAVE) == 0) {
											Intent intentPhotoTaken = new Intent(
													mContext,
													OptionAfterShutterActivity.class);
											Bundle bundle = new Bundle();
											bundle.putByteArray(
													"image",
													BitmapHandler
															.convertBitMapToByteArray(b));
											bundle.putString("path", path);
											bundle.putString("fileName",
													fileName);

											intentPhotoTaken
													.putExtra(
															"PhotoTakenPackage",
															bundle);
											startActivity(intentPhotoTaken);
											finish();
										} else {
											Ultilities.takePictureHandler(b,
													fileName, path);
											refeshCamera();
										}

									}
								});
					}
				} catch (Exception ex) {
					Log.d("---------------------Onclick SurfaceView--------------",
							ex.toString());
				}

			}

			if (v.getId() == ivFlash.getId()) {
				if (isLighOn) {
					// turn off flash
					turnOffFlash();
				} else {
					// turn on flash
					turnOnFlash();
				}
			}
			if (v.getId() == ivSwitchCamera.getId()) {
				openFrontFacingCamera();
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

	// Turning On flash
	public void turnOnFlash() {

		if (!isLighOn) {
			if (camera == null || params == null) {
				return;
			}
			// camera.release();
			params = camera.getParameters();
			params.setFlashMode(Parameters.FLASH_MODE_TORCH);
			camera.setParameters(params);
			camera.startPreview();
			previewing = true;
			isLighOn = true;

			// changing button/switch image
			toggleFlashIcon();
		}

	}

	// Turning Off flash
	private void turnOffFlash() {

		if (isLighOn) {
			if (camera == null || params == null) {
				return;
			}

			params = camera.getParameters();
			params.setFlashMode(Parameters.FLASH_MODE_OFF);
			camera.setParameters(params);
			camera.startPreview();
			isLighOn = false;

			// changing button/switch image
			toggleFlashIcon();
		}
	}

	private void toggleFlashIcon() {
		if (isLighOn) {
			ivFlash.setImageResource(R.drawable.flash_on);
		} else {
			ivFlash.setImageResource(R.drawable.flash_off);
		}
	}

	public void openFrontFacingCamera() {
		// numberOfCamera = Camera.getNumberOfCameras();
		if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
			currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
			prm.saveIntValue(CAMERA_MODE, currentCameraId);
			Log.d("--------camera id-----------", currentCameraId + "");
			
			Toast.makeText(getApplicationContext(), "BACK TO FRONT", 1000)
					.show();
			try {
				camera.release();
				camera = Camera.open(currentCameraId);
				// camera.setFaceDetectionListener(faceDetectionListener);
				camera.setDisplayOrientation(90);
				params = camera.getParameters();
				// params.setFlashMode(Parameters.FLASH_MODE_TORCH);
				params.set("jpeg-quality", 100);
				params.setRotation(90);
				params.setPictureFormat(PixelFormat.JPEG);
				params.setPreviewSize(params.getPreviewSize().width,
						params.getPreviewSize().height);
				camera.setParameters(params);
				camera.setPreviewDisplay(surfaceHolder);
				camera.startPreview();
				previewing = true;
			} catch (RuntimeException e) {
				Log.d("-----------front camera1-------", e.toString());
			} catch (IOException e) {
				Log.d("-----------front camera2-------", e.toString());
			}
		} else if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
			prm.saveIntValue(CAMERA_MODE, currentCameraId);
			Log.d("--------camera id-----------", currentCameraId + "");
			
			Toast.makeText(getApplicationContext(), "FRONT TO BACK",

			1000).show();
			try {
				camera.release();
				camera = Camera.open(currentCameraId);
				// camera.setFaceDetectionListener(faceDetectionListener);
				camera.setDisplayOrientation(90);
				params = camera.getParameters();
				// params.setFlashMode(Parameters.FLASH_MODE_TORCH);
				params.set("jpeg-quality", 100);
				params.setRotation(90);
				params.setPictureFormat(PixelFormat.JPEG);
				params.setPreviewSize(params.getPreviewSize().width,
						params.getPreviewSize().height);
				camera.setParameters(params);
				camera.setPreviewDisplay(surfaceHolder);
				camera.startPreview();
			} catch (RuntimeException e) {
				Log.d("-----------back camera1-------", e.toString());

			} catch (IOException e) {
				Log.d("-----------back camera2-------", e.toString());
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
			// width = getHeight();
			// height = getWidth();
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
			// if (haveFace) {
			//
			// // Camera driver coordinates range from (-1000, -1000) to (1000,
			// // 1000).
			// // UI coordinates range from (0, 0) to (width, height).
			//
			// int vWidth = getWidth();
			// int vHeight = getHeight();
			//
			// for (int i = 0; i < detectedFaces.length; i++) {
			//
			// int l = detectedFaces[i].rect.left;
			// int t = detectedFaces[i].rect.top;
			// int r = detectedFaces[i].rect.right;
			// int b = detectedFaces[i].rect.bottom;
			//
			// int left = (l + 1000) * vWidth / 2000;
			// int top = (t + 1000) * vHeight / 2000;
			// int right = (r + 1000) * vWidth / 2000;
			// int bottom = (b + 1000) * vHeight / 2000;
			//
			// canvas.drawRect(left, top, right, bottom, drawingPaint);
			// }
			// } else {
			// canvas.drawColor(Color.TRANSPARENT);
			// }
		}

	}

	public void drawSurfaceView(Canvas canvas) {

	}

	@Override
	public void onBackPressed() {
		finish();
	}

	public void zoomCamera(boolean zoomInOrOut) {
		if (camera != null) {
			Parameters parameter = camera.getParameters();

			if (parameter.isZoomSupported()) {
				int MAX_ZOOM = parameter.getMaxZoom();
				int currnetZoom = parameter.getZoom();
				if (zoomInOrOut && (currnetZoom < MAX_ZOOM && currnetZoom >= 0)) {
					parameter.setZoom(++currnetZoom);
				} else if (!zoomInOrOut
						&& (currnetZoom <= MAX_ZOOM && currnetZoom > 0)) {
					parameter.setZoom(--currnetZoom);
				}
			} else
				Toast.makeText(mContext, "Zoom Not Avaliable",
						Toast.LENGTH_LONG).show();

			camera.setParameters(parameter);
		}
	}

	public void onClickSetting() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		// builder.setTitle("Setting");
		// builder.setMessage("Are you sure you want to delete this entry?");
		LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
		View promptView = layoutInflater.inflate(R.layout.setting_dialog, null);
		builder.setView(promptView);
		Switch swAskSave = (Switch) promptView.findViewById(R.id.swAskSave);
		Switch swTapCapture = (Switch) promptView
				.findViewById(R.id.swTapCapture);
		Switch swShutterSound = (Switch) promptView
				.findViewById(R.id.swShutterSound);

		// 0 is default
		if (prm.getIntValue(TAP_TO_CAPTURE) == 0) {
			swTapCapture.setChecked(false);
		} else {
			swTapCapture.setChecked(true);
		}

		if (prm.getIntValue(ASK_TO_SAVE) == 0) {
			swAskSave.setChecked(true);
		} else {
			swAskSave.setChecked(false);
		}

		if (prm.getIntValue(SHUTTER_SOUND) == 0) {
			swShutterSound.setChecked(true);
		} else {
			swShutterSound.setChecked(false);
		}

		swAskSave.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {
					prm.saveIntValue(ASK_TO_SAVE, 0);
				} else {
					prm.saveIntValue(ASK_TO_SAVE, 1);
				}

			}
		});
		swTapCapture.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					prm.saveIntValue(TAP_TO_CAPTURE, 1);
				} else {
					prm.saveIntValue(TAP_TO_CAPTURE, 0);
				}
			}
		});
		swShutterSound
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean isChecked) {
						if (isChecked) {
							prm.saveIntValue(SHUTTER_SOUND, 0);
						} else {
							prm.saveIntValue(SHUTTER_SOUND, 1);
						}
					}
				});
		builder.show();
	}

	public void shutterSoundOn() {
		myShutterCallback = new ShutterCallback() {

			@Override
			public void onShutter() {

			}
		};
	}

	public void shutterSoundOff() {
		myShutterCallback = null;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		prm.saveIntValue(CAMERA_MODE, 0);
	}

}
