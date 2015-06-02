package vn.tdt.androidcamera.controllers;

import vn.tdt.androidcamera.R;
import vn.tdt.androidcamera.album.GalleryActivity;
import vn.tdt.androidcamera.constant.PathConstant;
import vn.tdt.androidcamera.fragment.ListEffectOptionMain;
import vn.tdt.androidcamera.fragment.MagicSkinFragment;
import vn.tdt.androidcamera.fragment.OptionsTitleFragment;
import vn.tdt.androidcamera.fragment.OtherFeatureFragment;
import vn.tdt.androidcamera.fragment.RotateFragment;
import vn.tdt.androidcamera.fragment.SaveOrDiscardEffectFragment;
import vn.tdt.androidcamera.fragment.SeekBarOptionFragment;
import vn.tdt.androidcamera.models.SharedPreferencesModels;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnDrawListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class PhotoEditorMain extends Activity {

	public final static String BRIGHTNESS = "Brightness";
	public final static String CONTRAST = "Contrast";
	public final static String DEFAULT = "Features";
	public final static String RED = "Red";
	public final static String BLUE = "Blue";
	public final static String GREEN = "Green";
	// boolean customTitleSupported;
	Context context;
	SharedPreferencesModels prm;
	Button btnCancelEditor;
	Button btnEditEditor;
	Button btnSaveEditor;

	ImageView ivPhotoViewer;

	ImageView ivOtherFeatured;

	ImageView ivNone;
	ImageView ivMagicSkin;
	ImageView ivMagicSkinNatural;
	ImageView ivMagicSkinBlue;
	ImageView ivMagicSkinWarm;
	ImageView ivMagicSkinBW;
	ImageView ivMagicSkinHot;
	ImageView ivContrast;
	ImageView ivBrightness;
	ImageView ivEffRed;
	ImageView ivEffBlue;
	ImageView ivEffGreen;
	ImageView ivRotate;
	ImageView ivRotateLeft;
	ImageView ivRotateRight;
	ImageView ivRotateWidth;
	ImageView ivRotateHeight;
	ImageView ivCheckEffect;
	ImageView ivUnCheckEffect;
	TextView tvNameOfFeature;
	SeekBar seekbarOption;

	Bitmap bmOriginPhoto; // origin photo
	Bitmap bmCurrentPhoto; // bitmap to save photo when it edited
	Bitmap bmNone;
	String filePathReceiver = "";
	Bitmap bitmapReceiver = null;
	int ref = 0;
	String fileName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if custom title is supported BEFORE setting the content view!
		// customTitleSupported =
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// customTitleBar();
		setContentView(R.layout.activity_photo_editor_main);

		context = getApplicationContext();
		prm = new SharedPreferencesModels(context);
		btnCancelEditor = (Button) findViewById(R.id.btnCancelEditor);
		btnSaveEditor = (Button) findViewById(R.id.btnSaveEditor);
		ivPhotoViewer = (ImageView) findViewById(R.id.ivPhotoViewer);

		ivOtherFeatured = (ImageView) findViewById(R.id.ivOtherFeatured);
		ivCheckEffect = (ImageView) findViewById(R.id.ivCheckEffect);
		ivUnCheckEffect = (ImageView) findViewById(R.id.ivUnCheckEffect);
		tvNameOfFeature = (TextView) findViewById(R.id.tvNameOfFeature);
		seekbarOption = (SeekBar) findViewById(R.id.seekbarOption);

		ivNone = (ImageView) findViewById(R.id.ivNone);
		ivMagicSkin = (ImageView) findViewById(R.id.ivMagicSkin);
		ivMagicSkinNatural = (ImageView) findViewById(R.id.ivMagicSkinNatural);
		ivMagicSkinBlue = (ImageView) findViewById(R.id.ivMagicSkinBlue);
		ivMagicSkinWarm = (ImageView) findViewById(R.id.ivMagicSkinWarm);
		ivMagicSkinBW = (ImageView) findViewById(R.id.ivMagicSkinBW);
		ivMagicSkinHot = (ImageView) findViewById(R.id.ivMagicSkinHot);
		ivContrast = (ImageView) findViewById(R.id.ivContrast);
		ivBrightness = (ImageView) findViewById(R.id.ivBrightness);
		ivEffRed = (ImageView) findViewById(R.id.ivEffRed);
		ivEffBlue = (ImageView) findViewById(R.id.ivEffBlue);
		ivEffGreen = (ImageView) findViewById(R.id.ivEffGreen);

		ivRotate = (ImageView) findViewById(R.id.ivRotate);
		ivRotateLeft = (ImageView) findViewById(R.id.ivRotateLeft);
		ivRotateRight = (ImageView) findViewById(R.id.ivRotateRight);
		ivRotateWidth = (ImageView) findViewById(R.id.ivRotateWidth);
		ivRotateHeight = (ImageView) findViewById(R.id.ivRotateHeight);
		// Onclick Handler
		// onClickHandlers();
		btnCancelEditor.setOnClickListener(onClickEditorHandler);
		btnSaveEditor.setOnClickListener(onClickEditorHandler);

		ivOtherFeatured.setOnClickListener(onClickEditorHandler);

		ivCheckEffect.setOnClickListener(onClickEditorHandler);
		ivUnCheckEffect.setOnClickListener(onClickEditorHandler);

		ivNone.setOnClickListener(onClickEditorHandler);
		ivMagicSkin.setOnClickListener(onClickEditorHandler);
		ivMagicSkinNatural.setOnClickListener(onClickEditorHandler);
		ivMagicSkinBlue.setOnClickListener(onClickEditorHandler);
		ivMagicSkinWarm.setOnClickListener(onClickEditorHandler);
		ivMagicSkinBW.setOnClickListener(onClickEditorHandler);
		ivMagicSkinHot.setOnClickListener(onClickEditorHandler);
		ivContrast.setOnClickListener(onClickEditorHandler);
		ivBrightness.setOnClickListener(onClickEditorHandler);
		ivEffRed.setOnClickListener(onClickEditorHandler);
		ivEffBlue.setOnClickListener(onClickEditorHandler);
		ivEffGreen.setOnClickListener(onClickEditorHandler);
		ivRotate.setOnClickListener(onClickEditorHandler);
		ivRotateLeft.setOnClickListener(onClickEditorHandler);
		ivRotateRight.setOnClickListener(onClickEditorHandler);
		ivRotateWidth.setOnClickListener(onClickEditorHandler);
		ivRotateHeight.setOnClickListener(onClickEditorHandler);

		seekbarOption.setMax(510);
		seekbarOption.setProgress(255);
		seekbarOption.setOnSeekBarChangeListener(onSeekBarChangeHandler);

		Intent callerIntent = getIntent();
		Bundle packageFromCaller = callerIntent.getBundleExtra("PhotoEditor");
		if (packageFromCaller != null) {
			ref = packageFromCaller.getInt("ref");
			if (ref == 1) {

				bitmapReceiver = BitmapHandler
						.convertByteArrayToBitmap(packageFromCaller
								.getByteArray("image"));
			} else {
				bitmapReceiver = BitmapHandler
						.convertImageToBitmap(packageFromCaller
								.getString("pathToEdit"));
			}

			fileName = packageFromCaller.getString("fileName");
			// Ultilities.toastShow(mContext, filePath + "", Gravity.CENTER);
		} else {
			finish();
		}

		// just for test, this bitmap received from intent before sent
		// bmOriginPhoto = BitmapFactory.decodeResource(getResources(),
		// R.drawable.girl);

		bmOriginPhoto = bitmapReceiver;
		bmCurrentPhoto = bmOriginPhoto;
		bmNone = bmOriginPhoto;
		ivPhotoViewer.setImageBitmap(bmOriginPhoto);

		// hide fragments when not use
		showHideMagicSkinOption(false);
		showHideSaveOrDiscardEffect(false);
		showHideOtherFeature(false);
		showHideSeekBarOption(false);
		showHideRotate(false);

		// draw text on imageviews
		drawTextOnImageViews2(ivNone, R.drawable.eft_none, "None", 50);
		drawTextOnImageViews2(ivMagicSkin, R.drawable.magic_skin2, "Skin", 50);
		drawTextOnImageViews2(ivOtherFeatured, R.drawable.light_color, "Light",
				50);
		drawTextOnImageViews(ivMagicSkinNatural, R.drawable.magic_skin_natural,
				"Natural", 60);
		drawTextOnImageViews(ivMagicSkinBlue, R.drawable.magic_skin_blue,
				"Blue", 50);
		drawTextOnImageViews(ivMagicSkinWarm, R.drawable.magic_skin_warm,
				"Warm", 60);
		drawTextOnImageViews(ivMagicSkinBW, R.drawable.magic_skin_bw, "B&W", 50);
		drawTextOnImageViews(ivMagicSkinHot, R.drawable.magic_skin_hot, "Hot",
				60);
	}

	OnSeekBarChangeListener onSeekBarChangeHandler = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// Ultilities.toastShow(context, "Done...", Gravity.CENTER);
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			Ultilities.toastShow(context, "please wait...", Gravity.CENTER);

		}

		@Override
		public void onProgressChanged(SeekBar sb, int arg1, boolean arg2) {
			int value = sb.getProgress();
			int real = value - 255;
			if (tvNameOfFeature.getText().equals(BRIGHTNESS)) {
				bmCurrentPhoto = PhotoEffectHandler.doBrightness(bmOriginPhoto,
						real);
			}
			if (tvNameOfFeature.getText().equals(CONTRAST)) {
				bmCurrentPhoto = PhotoEffectHandler.adjustedContrast(
						bmOriginPhoto, (double) real);
			}
			if (tvNameOfFeature.getText().equals(RED)) {
				bmCurrentPhoto = PhotoEffectHandler.createSepiaToningEffect(bmOriginPhoto, 1,real, 0, 0);
			}
			if (tvNameOfFeature.getText().equals(BLUE)) {
				bmCurrentPhoto = PhotoEffectHandler.createSepiaToningEffect(bmOriginPhoto, 1,0, 0, real);
			}
			if (tvNameOfFeature.getText().equals(GREEN)) {
				bmCurrentPhoto = PhotoEffectHandler.createSepiaToningEffect(bmOriginPhoto, 1,0,real, 0);
			}
			// setNameOfFeature(real + "");
			ivPhotoViewer.setImageBitmap(bmCurrentPhoto);

		}
	};
	OnClickListener onClickEditorHandler = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == btnCancelEditor.getId()) {
				// Ultilities.toastShow(getApplicationContext(),
				// "Back to photo viewer and do nothing", Gravity.CENTER);
				finish();
			}

			if (v.getId() == btnSaveEditor.getId()) {
				String fileName = Ultilities.getFileName(1);
				String path = Ultilities.pathToSave(1);
				BitmapHandler.saveBitmapToJpg(fileName, path, bmCurrentPhoto);
				// Ultilities
				// .toastShow(
				// getApplicationContext(),
				// "save file to default path with current effect and back to photo viewer",
				// Gravity.CENTER);
				if (ref == 1) {
					prm.saveStringValue(PathConstant.LASTEST_PHOTO, path + "/"
							+ fileName + ".jpg");
					Intent i = new Intent(PhotoEditorMain.this,
							MainActivity.class);
					startActivity(i);
					finish();
				}
				if (ref == 2) {
					Intent i = new Intent(PhotoEditorMain.this,
							GalleryActivity.class);
					startActivity(i);
					finish();
				}
			}

			if (v.getId() == ivCheckEffect.getId()) {
				bmOriginPhoto = bmCurrentPhoto;
				ivPhotoViewer.setImageBitmap(bmOriginPhoto);
				setBackToEditorMain();
			}
			if (v.getId() == ivUnCheckEffect.getId()) {
				ivPhotoViewer.setImageBitmap(bmOriginPhoto);
				setBackToEditorMain();
			}

			if (v.getId() == ivNone.getId()) {
				bmOriginPhoto = bmNone;
				ivPhotoViewer.setImageBitmap(bmOriginPhoto);
			}
			if (v.getId() == ivMagicSkin.getId()) {
				setNameOfFeature("Magic Skin");
				showHideOptionTitleFrgament(false);
				showHideEffectListFragment(false);
				showHideMagicSkinOption(true);
				showHideSaveOrDiscardEffect(true);
			}

			if (v.getId() == ivMagicSkinNatural.getId()) {
				bmCurrentPhoto = PhotoEffectHandler.doBrightness(bmOriginPhoto,
						40);
				ivPhotoViewer.setImageBitmap(bmCurrentPhoto);
			}
			if (v.getId() == ivMagicSkinBlue.getId()) {
				bmCurrentPhoto = PhotoEffectHandler.boost(bmOriginPhoto, 3,
						(float) 50 / 100);
				ivPhotoViewer.setImageBitmap(bmCurrentPhoto);
			}
			if (v.getId() == ivMagicSkinWarm.getId()) {
				Ultilities.toastShow(context, "please wait...", Gravity.CENTER);
				bmCurrentPhoto = PhotoEffectHandler.doGamma(bmOriginPhoto, 13,
						12, 16);
				bmCurrentPhoto = PhotoEffectHandler.applySaturationFilter(
						bmCurrentPhoto, 1.3);
				bmCurrentPhoto = PhotoEffectHandler.applyGaussianBlur(
						bmCurrentPhoto, (int) 13, (int) 1);
				ivPhotoViewer.setImageBitmap(bmCurrentPhoto);
			}
			if (v.getId() == ivMagicSkinBW.getId()) {
				bmCurrentPhoto = PhotoEffectHandler.doGreyscale(bmOriginPhoto);
				ivPhotoViewer.setImageBitmap(bmCurrentPhoto);
			}
			if (v.getId() == ivMagicSkinHot.getId()) {
				Ultilities.toastShow(context, "please wait...", Gravity.CENTER);
				bmCurrentPhoto = PhotoEffectHandler.applySaturationFilter(
						bmOriginPhoto, 1.6);
				bmCurrentPhoto = PhotoEffectHandler.applyGaussianBlur(
						bmCurrentPhoto, (int) 13, (int) 1);
				ivPhotoViewer.setImageBitmap(bmCurrentPhoto);
			}
			if (v.getId() == ivOtherFeatured.getId()) {
				setNameOfFeature("Features");
				seekbarOption.setProgress(255);
				showHideOptionTitleFrgament(false);
				showHideEffectListFragment(false);
				showHideOtherFeature(true);
				showHideSaveOrDiscardEffect(true);
			}
			if (v.getId() == ivContrast.getId()) {
				seekbarOption.setProgress(255);
				tvNameOfFeature.setText(CONTRAST);
				showHideSeekBarOption(true);
				showHideOptionTitleFrgament(false);
				showHideEffectListFragment(false);
				showHideOtherFeature(true);
				showHideSaveOrDiscardEffect(true);
			}
			if (v.getId() == ivBrightness.getId()) {
				seekbarOption.setProgress(255);
				tvNameOfFeature.setText(BRIGHTNESS);
				showHideSeekBarOption(true);
				showHideOptionTitleFrgament(false);
				showHideEffectListFragment(false);
				showHideOtherFeature(true);
				showHideSaveOrDiscardEffect(true);
			}
			if (v.getId() == ivEffRed.getId()) {
				seekbarOption.setProgress(255);
				tvNameOfFeature.setText(RED);
				showHideSeekBarOption(true);
				showHideOptionTitleFrgament(false);
				showHideEffectListFragment(false);
				showHideOtherFeature(true);
				showHideSaveOrDiscardEffect(true);
			}
			if (v.getId() == ivEffBlue.getId()) {
				seekbarOption.setProgress(255);
				tvNameOfFeature.setText(BLUE);
				showHideSeekBarOption(true);
				showHideOptionTitleFrgament(false);
				showHideEffectListFragment(false);
				showHideOtherFeature(true);
				showHideSaveOrDiscardEffect(true);
			}
			if (v.getId() == ivEffGreen.getId()) {
				seekbarOption.setProgress(255);
				tvNameOfFeature.setText(GREEN);
				showHideSeekBarOption(true);
				showHideOptionTitleFrgament(false);
				showHideEffectListFragment(false);
				showHideOtherFeature(true);
				showHideSaveOrDiscardEffect(true);
			}

			if (v.getId() == ivRotate.getId()) {

				showHideRotate(true);
				showHideSeekBarOption(false);
				showHideOptionTitleFrgament(false);
				showHideEffectListFragment(false);
				showHideOtherFeature(false);
				showHideSaveOrDiscardEffect(true);
			}

			if (v.getId() == ivRotateLeft.getId()) {
				bmCurrentPhoto = PhotoEffectHandler.rotate(bmCurrentPhoto, 270);
				ivPhotoViewer.setImageBitmap(bmCurrentPhoto);
			}
			if (v.getId() == ivRotateRight.getId()) {
				bmCurrentPhoto = PhotoEffectHandler.rotate(bmCurrentPhoto, 90);
				ivPhotoViewer.setImageBitmap(bmCurrentPhoto);
			}
			if (v.getId() == ivRotateWidth.getId()) {
				bmCurrentPhoto = PhotoEffectHandler.flip(bmCurrentPhoto, 2);
				ivPhotoViewer.setImageBitmap(bmCurrentPhoto);
			}
			if (v.getId() == ivRotateHeight.getId()) {
				bmCurrentPhoto = PhotoEffectHandler.flip(bmCurrentPhoto, 1);
				ivPhotoViewer.setImageBitmap(bmCurrentPhoto);
			}
		}
	};

	public void showHideOptionTitleFrgament(boolean isShow) {
		FragmentManager fm = getFragmentManager();
		OptionsTitleFragment fr = (OptionsTitleFragment) fm
				.findFragmentById(R.id.fragment_option_title);
		FragmentTransaction ft = fm.beginTransaction();
		if (!isShow) {
			ft.hide(fr).commit();
		} else {
			ft.show(fr).commit();
		}
	}

	public void showHideEffectListFragment(boolean isShow) {
		FragmentManager fm = getFragmentManager();
		ListEffectOptionMain frListEffect = (ListEffectOptionMain) fm
				.findFragmentById(R.id.fragment_effect_list);
		// FilterEffectFragment frFilterFragment = (FilterEffectFragment)
		// fm.findFragmentById(R.id.fragment_effect_list);

		FragmentTransaction ft = fm.beginTransaction();
		if (!isShow) {
			ft.hide(frListEffect).commit();
		} else {
			ft.show(frListEffect).commit();
		}
	}

	public void showHideMagicSkinOption(boolean isShow) {
		FragmentManager fm = getFragmentManager();
		MagicSkinFragment frFilterFragment = (MagicSkinFragment) fm
				.findFragmentById(R.id.fragment_filter_effect);

		FragmentTransaction ft = fm.beginTransaction();
		if (!isShow) {
			ft.hide(frFilterFragment).commit();
		} else {
			ft.show(frFilterFragment).commit();
		}
	}

	public void showHideSaveOrDiscardEffect(boolean isShow) {
		FragmentManager fm = getFragmentManager();
		SaveOrDiscardEffectFragment frSaveOrDiscard = (SaveOrDiscardEffectFragment) fm
				.findFragmentById(R.id.fragment_save_or_discard_effect);

		FragmentTransaction ft = fm.beginTransaction();
		if (!isShow) {
			ft.hide(frSaveOrDiscard).commit();
		} else {
			ft.show(frSaveOrDiscard).commit();
		}
	}

	public void showHideOtherFeature(boolean isShow) {
		FragmentManager fm = getFragmentManager();
		OtherFeatureFragment frFeature = (OtherFeatureFragment) fm
				.findFragmentById(R.id.fragment_other_feature);

		FragmentTransaction ft = fm.beginTransaction();
		if (!isShow) {
			ft.hide(frFeature).commit();
		} else {
			ft.show(frFeature).commit();
		}
	}

	public void showHideSeekBarOption(boolean isShow) {
		FragmentManager fm = getFragmentManager();
		SeekBarOptionFragment fr = (SeekBarOptionFragment) fm
				.findFragmentById(R.id.fragment_seekbar_option);

		FragmentTransaction ft = fm.beginTransaction();
		if (!isShow) {
			ft.hide(fr).commit();
		} else {
			ft.show(fr).commit();
		}
	}

	public void showHideRotate(boolean isShow) {
		FragmentManager fm = getFragmentManager();
		RotateFragment fr = (RotateFragment) fm
				.findFragmentById(R.id.fragment_rotate_effect);

		FragmentTransaction ft = fm.beginTransaction();
		if (!isShow) {
			ft.hide(fr).commit();
		} else {
			ft.show(fr).commit();
		}
	}

	public void setNameOfFeature(String name) {
		tvNameOfFeature.setText(name);
	}

	public void drawTextOnImageViews(ImageView imgView, int src, String text,
			int textSize) {
		Bitmap bm = BitmapFactory.decodeResource(getResources(), src);
		Config config = bm.getConfig();
		int width = bm.getWidth();
		int height = bm.getHeight();

		Bitmap newImage = Bitmap.createBitmap(width, height, config);

		Canvas c = new Canvas(newImage);
		c.drawBitmap(bm, 0, 0, null);

		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.FILL);
		paint.setTextSize(textSize);
		c.drawText(text, width / 4, 7 * height / 8, paint);
		imgView.setImageBitmap(newImage);
	}

	public void drawTextOnImageViews2(ImageView imgView, int src, String text,
			int textSize) {
		Bitmap bm = BitmapFactory.decodeResource(getResources(), src);
		Config config = bm.getConfig();
		int width = bm.getWidth();
		int height = bm.getHeight();

		Bitmap newImage = Bitmap.createBitmap(width, height, config);

		Canvas c = new Canvas(newImage);
		c.drawBitmap(bm, 0, 0, null);

		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.FILL);
		paint.setTextSize(textSize);
		c.drawText(text, width / 10, 3 * height / 5, paint);
		imgView.setImageBitmap(newImage);
	}

	public void setBackToEditorMain() {
		showHideOptionTitleFrgament(true);
		showHideEffectListFragment(true);
		showHideMagicSkinOption(false);
		showHideSeekBarOption(false);
		showHideRotate(false);
		showHideOtherFeature(false);
		showHideSaveOrDiscardEffect(false);
	}

}
