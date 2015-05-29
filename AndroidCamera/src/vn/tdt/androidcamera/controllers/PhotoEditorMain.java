package vn.tdt.androidcamera.controllers;

import vn.tdt.androidcamera.R;
import vn.tdt.androidcamera.fragment.ListEffectOptionMain;
import vn.tdt.androidcamera.fragment.MagicSkinFragment;
import vn.tdt.androidcamera.fragment.OptionsTitleFragment;
import vn.tdt.androidcamera.fragment.OtherFeatureFragment;
import vn.tdt.androidcamera.fragment.SaveOrDiscardEffectFragment;
import vn.tdt.androidcamera.fragment.SeekBarOptionFragment;
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

	// boolean customTitleSupported;
	Context context;
	Button btnCancelEditor;
	Button btnEditEditor;
	Button btnSaveEditor;

	ImageView ivPhotoViewer;

	ImageView ivOtherFeatured;

	ImageView ivMagicSkin;
	ImageView ivMagicSkinNatural;
	ImageView ivMagicSkinBlue;
	ImageView ivMagicSkinWarm;
	ImageView ivMagicSkinBW;
	ImageView ivMagicSkinHot;
	ImageView ivRedEffect;

	ImageView ivCheckEffect;
	ImageView ivUnCheckEffect;
	TextView tvNameOfFeature;
	SeekBar seekbarOption;

	Bitmap bmOriginPhoto; // origin photo
	Bitmap bmCurrentPhoto; // bitmap to save photo when it edited
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
		btnCancelEditor = (Button) findViewById(R.id.btnCancelEditor);
		btnSaveEditor = (Button) findViewById(R.id.btnSaveEditor);
		ivPhotoViewer = (ImageView) findViewById(R.id.ivPhotoViewer);

		ivOtherFeatured = (ImageView) findViewById(R.id.ivOtherFeatured);
		ivCheckEffect = (ImageView) findViewById(R.id.ivCheckEffect);
		ivUnCheckEffect = (ImageView) findViewById(R.id.ivUnCheckEffect);
		tvNameOfFeature = (TextView) findViewById(R.id.tvNameOfFeature);
		seekbarOption = (SeekBar) findViewById(R.id.seekbarOption);

		ivMagicSkin = (ImageView) findViewById(R.id.ivMagicSkin);
		ivMagicSkinNatural = (ImageView) findViewById(R.id.ivMagicSkinNatural);
		ivMagicSkinBlue = (ImageView) findViewById(R.id.ivMagicSkinBlue);
		ivMagicSkinWarm = (ImageView) findViewById(R.id.ivMagicSkinWarm);
		ivMagicSkinBW = (ImageView) findViewById(R.id.ivMagicSkinBW);
		ivMagicSkinHot = (ImageView) findViewById(R.id.ivMagicSkinHot);
		ivRedEffect = (ImageView) findViewById(R.id.ivRedEffect);
		// Onclick Handler
		// onClickHandlers();
		btnCancelEditor.setOnClickListener(onClickEditorHandler);
		btnSaveEditor.setOnClickListener(onClickEditorHandler);

		ivOtherFeatured.setOnClickListener(onClickEditorHandler);

		ivCheckEffect.setOnClickListener(onClickEditorHandler);
		ivUnCheckEffect.setOnClickListener(onClickEditorHandler);

		ivMagicSkin.setOnClickListener(onClickEditorHandler);
		ivMagicSkinNatural.setOnClickListener(onClickEditorHandler);
		ivMagicSkinBlue.setOnClickListener(onClickEditorHandler);
		ivMagicSkinWarm.setOnClickListener(onClickEditorHandler);
		ivMagicSkinBW.setOnClickListener(onClickEditorHandler);
		ivMagicSkinHot.setOnClickListener(onClickEditorHandler);
		ivRedEffect.setOnClickListener(onClickEditorHandler);

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
		ivPhotoViewer.setImageBitmap(bmOriginPhoto);

		// hide fragments when not use
		showHideMagicSkinOption(false);
		showHideSaveOrDiscardEffect(false);
		showHideOtherFeature(false);
		showHideSeekBarOption(false);

		// draw text on imageviews
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
			Ultilities.toastShow(context, "Done...", Gravity.CENTER);
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			Ultilities.toastShow(context, "please wait...", Gravity.CENTER);

		}

		@Override
		public void onProgressChanged(SeekBar sb, int arg1, boolean arg2) {
			int value = sb.getProgress();
			int real = value - 255;
			setNameOfFeature(real + "");
			bmCurrentPhoto = PhotoEffectHandler.applySaturationFilter(
					bmOriginPhoto, real);
			ivPhotoViewer.setImageBitmap(bmCurrentPhoto);

		}
	};
	OnClickListener onClickEditorHandler = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == btnCancelEditor.getId()) {
				Ultilities.toastShow(getApplicationContext(),
						"Back to photo viewer and do nothing", Gravity.CENTER);
			}

			if (v.getId() == btnSaveEditor.getId()) {
				String fileName = Ultilities.getFileName(1);
				String path = Ultilities.pathToSave(1);
				BitmapHandler.saveBitmapToJpg(fileName, path, bmCurrentPhoto);
				Ultilities
						.toastShow(
								getApplicationContext(),
								"save file to default path with current effect and back to photo viewer",
								Gravity.CENTER);
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
				showHideOptionTitleFrgament(false);
				showHideEffectListFragment(false);
				showHideOtherFeature(true);
				showHideSaveOrDiscardEffect(true);
			}
			if (v.getId() == ivRedEffect.getId()) {
				showHideSeekBarOption(true);
				showHideOptionTitleFrgament(false);
				showHideEffectListFragment(false);
				showHideOtherFeature(false);
				showHideSaveOrDiscardEffect(true);
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

	public void setBackToEditorMain() {
		showHideOptionTitleFrgament(true);
		showHideEffectListFragment(true);
		showHideMagicSkinOption(false);
		showHideOtherFeature(false);
		showHideSaveOrDiscardEffect(false);
	}

}
