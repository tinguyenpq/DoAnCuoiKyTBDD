package vn.tdt.androidcamera.controllers;

import vn.tdt.androidcamera.R;
import vn.tdt.androidcamera.R.id;
import vn.tdt.androidcamera.R.layout;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class OptionAfterShutterActivity extends Activity {

	ImageView imgViewOptionAfterShutter;
	Button btnCancel;
	Button btnEdit;
	Button btnSave;

	Bitmap b;
	String fileName;
	String path;

	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		//Remove title bar 
//		 this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		    //Remove notification bar 
//		    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_option_after_shutter);
		 
	 
		imgViewOptionAfterShutter = (ImageView) findViewById(R.id.imgViewOptionAfterShutter);
		btnSave = (Button) findViewById(R.id.btnSave);
		btnEdit = (Button) findViewById(R.id.btnEdit);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		context = getApplicationContext();

		Intent callerIntent = getIntent();
		Bundle packageFromCaller = callerIntent
				.getBundleExtra("PhotoTakenPackage");
		if (packageFromCaller != null) {
			b = BitmapHandler.convertByteArrayToBitmap(packageFromCaller
					.getByteArray("image"));
			fileName = packageFromCaller.getString("fileName");
			path = packageFromCaller.getString("path");

			imgViewOptionAfterShutter.setImageBitmap(b);

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
				backMainActivity();
			}
			if (v.getId() == btnCancel.getId()) {
				backMainActivity();
			}
			if (v.getId() == btnEdit.getId()) {
				Intent i = new Intent(context,PhotoEditorMain.class);
				startActivity(i);
			}
		}

	};

	public void backMainActivity() {
		Intent i = new Intent(OptionAfterShutterActivity.this,
				MainActivity.class);
		startActivity(i);
		finish();
	}
}
