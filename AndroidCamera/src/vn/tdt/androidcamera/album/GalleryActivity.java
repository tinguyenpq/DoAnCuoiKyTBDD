package vn.tdt.androidcamera.album;

import vn.tdt.androidcamera.R;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Window;
import android.widget.Toast;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import vn.tdt.androidcamera.album.TabMyAlbumFragment;
import vn.tdt.androidcamera.album.TabOtherAlbumFragment;
import vn.tdt.androidcamera.controllers.MainActivity;

public class GalleryActivity extends FragmentActivity implements TabListener {

	ViewPager pager;

	private final String tab1Lable = "My Album";
	private final String tab2Lable = "All Photo";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_gallery);
		pager = (ViewPager) findViewById(R.id.pager);
		FragmentManager fm = getSupportFragmentManager();
		pager.setAdapter(new MyAdapter(fm));
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		ActionBar actionBar = getActionBar();
		// actionBar.setTitle("");
		Tab tab1 = actionBar.newTab();
		tab1.setText(tab1Lable);
		tab1.setTabListener(this);
		actionBar.addTab(tab1);

		Tab tab2 = actionBar.newTab();
		tab2.setText(tab2Lable);
		tab2.setTabListener(this);
		actionBar.addTab(tab2);

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				getActionBar().setSelectedNavigationItem(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	class MyAdapter extends FragmentPagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = new TabMyAlbumFragment();
				break;
			case 1:
				fragment = new TabOtherAlbumFragment();
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (tab.getText().equals(tab1Lable)) {
			pager.setCurrentItem(0);
		} else {
			pager.setCurrentItem(1);
		}

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(GalleryActivity.this, MainActivity.class);
		startActivity(i);
		finish();
	}
}
