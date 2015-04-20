package vn.tdt.androidcamera.tabhost;

import vn.tdt.androidcamera.R;

import android.app.Activity;
import android.app.LocalActivityManager;
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

		mTabHost = (TabHost) findViewById(android.R.id.tabhost);

		mTabHost.setup();
		
		
		// Tab for My album tab
		TabSpec myAlbumSpec = mTabHost.newTabSpec("MyAlbum");
		// setting Title and Icon for the Tab
		myAlbumSpec.setIndicator("My Album",
				getResources().getDrawable(R.drawable.custom_tab_my_album));
		Intent myAlbumIntent = new Intent(this, MyAlbumTabActivity.class);
		myAlbumSpec.setContent(myAlbumIntent);

		// Tab for Other album tab
		TabSpec otherAlbumSpec = mTabHost.newTabSpec("OtherAlbum");
		// setting Title and Icon for the Tab
		myAlbumSpec.setIndicator("My Album",
				getResources().getDrawable(R.drawable.custom_tab_other_album));
		Intent otherAlbumIntent = new Intent(this, OtherAlbumTabActivity.class);
		myAlbumSpec.setContent(otherAlbumIntent);

		// Adding all TabSpec to TabHost
		mTabHost.addTab(myAlbumSpec); // Adding My album tab
		mTabHost.addTab(otherAlbumSpec); // Adding Other album tab

		// set Windows tab as defaualt (zero based)
		mTabHost.setCurrentTab(1);
	}

}
