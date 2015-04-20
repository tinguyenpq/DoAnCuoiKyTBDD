package vn.tdt.androidcamera.tabhost;

import vn.tdt.androidcamera.R;
import vn.tdt.androidcamera.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class GalleryActivity extends Activity {
	private TabHost mTabHost;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);

		mTabHost = (TabHost) findViewById(R.id.tab_host_gallery);
		mTabHost.setup();

		// Tab for My album tab
		TabSpec myAlbumSpec = mTabHost.newTabSpec("Photos");
		// setting Title and Icon for the Tab
		// photospec.setIndicator("Photos",
		// getResources().getDrawable(R.drawable.icon_photos_tab));
		Intent myAlbumIntent = new Intent(this, MyAlbumTabActivity.class);
		myAlbumSpec.setContent(myAlbumIntent);

		// Tab for Other album tab
		TabSpec otherAlbumSpec = mTabHost.newTabSpec("Photos");
		// setting Title and Icon for the Tab
		// photospec.setIndicator("Photos",
		// getResources().getDrawable(R.drawable.icon_photos_tab));
		Intent otherAlbumIntent = new Intent(this, OtherAlbumTabActivity.class);
		myAlbumSpec.setContent(otherAlbumIntent);

		// Adding all TabSpec to TabHost
		mTabHost.addTab(myAlbumSpec); // Adding My album tab
		mTabHost.addTab(otherAlbumSpec); // Adding Other album tab
	}

}
