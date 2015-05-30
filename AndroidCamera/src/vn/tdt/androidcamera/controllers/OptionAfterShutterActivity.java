package vn.tdt.androidcamera.controllers;

import javax.crypto.spec.IvParameterSpec;

import vn.tdt.androidcamera.R;
import vn.tdt.androidcamera.album.ViewAlbumBySlide;
import vn.tdt.androidcamera.constant.PathConstant;
import vn.tdt.androidcamera.models.SharedPreferencesModels;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class OptionAfterShutterActivity extends Activity {

	ImageView imgViewOptionAfterShutter;
	Button btnCancel;
	Button btnEdit;
	Button btnSave;

	SharedPreferencesModels prm;
	Bitmap b;
	String fileName;
	String path;
	TextView tvNameOfPhoto;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		//Remove title bar 
		 this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		    //Remove notification bar 
//		    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_option_after_shutter);
		 
//		DisplayMetrics displaymetrics = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//		int height = displaymetrics.heightPixels;
//		int width = displaymetrics.widthPixels;
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		
		imgViewOptionAfterShutter = (ImageView) findViewById(R.id.imgViewOptionAfterShutter);
		btnSave = (Button) findViewById(R.id.btnSave);
		btnEdit = (Button) findViewById(R.id.btnEdit);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		tvNameOfPhoto = (TextView) findViewById(R.id.tvNameOfPhoto);
		context = getApplicationContext();
		prm = new SharedPreferencesModels(context);

		Intent callerIntent = getIntent();
		Bundle packageFromCaller = callerIntent
				.getBundleExtra("PhotoTakenPackage");
		if (packageFromCaller != null) {
			b = BitmapHandler.convertByteArrayToBitmap(packageFromCaller
					.getByteArray("image"));
			fileName = packageFromCaller.getString("fileName");
			path = packageFromCaller.getString("path");
			//imgViewOptionAfterShutter.setImageBitmap(b);
			imgViewOptionAfterShutter.setImageBitmap(Bitmap
					.createScaledBitmap(b, width+100, height, true));
			tvNameOfPhoto.setText(fileName+".JPG");
			btnSave.setOnClickListener(onClickHandler);
			btnEdit.setOnClickListener(onClickHandler);
			btnCancel.setOnClickListener(onClickHandler);
		}
	}

	public OnClickListener onClickHandler = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == btnSave.getId()) {
				Ultilities.takePictureHandler(b, fileName, path);
				prm.saveStringValue(PathConstant.LASTEST_PHOTO, path+"/"+fileName+".jpg");
				backMainActivity();
			}
			if (v.getId() == btnCancel.getId()) {
				backMainActivity();
			}
			if (v.getId() == btnEdit.getId()) {
//				Intent i = new Intent(context,PhotoEditorMain.class);
//				startActivity(i);
				Intent intentPhotoEditor = new Intent(context,
						PhotoEditorMain.class);
				startActivity(intentPhotoEditor);
				Bundle bundle = new Bundle();
				//bundle.putString("pathToEdit", pathToEdit);
				bundle.putInt("ref", 1);//from affter shutter
				bundle.putByteArray(
						"image",
						BitmapHandler
								.convertBitMapToByteArray(b));
				bundle.putString("fileName",fileName);
				intentPhotoEditor.putExtra("PhotoEditor", bundle);
				startActivity(intentPhotoEditor);
				//finish();
			}
		}

	};

	public void backMainActivity() {
		Intent i = new Intent(OptionAfterShutterActivity.this,
				MainActivity.class);
		startActivity(i);
		finish();
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(OptionAfterShutterActivity.this,
				MainActivity.class);
		startActivity(i);
		finish();
	}
}
