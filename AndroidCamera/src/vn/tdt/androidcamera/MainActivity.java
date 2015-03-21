package vn.tdt.androidcamera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private ImageView mImageGallery;
	private ImageView mImageSetting;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext = getApplicationContext();

		mImageGallery = (ImageView) findViewById(R.id.image_gallery);
		mImageSetting = (ImageView) findViewById(R.id.image_setting);

		mImageGallery.setOnClickListener(mGlobal_OnClickListener);
		mImageSetting.setOnClickListener(mGlobal_OnClickListener);
	}
/*
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
	}*/

	final OnClickListener mGlobal_OnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == mImageGallery.getId()) {
				Intent intent = new Intent(mContext, PhotoGalleryActivity.class);
				startActivity(intent);

			} else if (v.getId() == mImageSetting.getId()) {

			}

		}
	};
}
