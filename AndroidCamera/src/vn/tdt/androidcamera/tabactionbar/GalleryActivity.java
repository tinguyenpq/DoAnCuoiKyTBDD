package vn.tdt.androidcamera.tabactionbar;

import vn.tdt.androidcamera.R;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class GalleryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_gallery);

		ActionBar actionBar = getActionBar();

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		String tabTitleMyAlbum = getResources().getString(
				R.string.tab_title_my_album);
		Tab tab = actionBar.newTab();
		tab.setText(tabTitleMyAlbum);
		tab.setIcon(R.drawable.custom_tab_my_album);
		TabListener<TabMyAlbumFragment> tabMyAlbum = new TabListener<TabMyAlbumFragment>(
				this, tabTitleMyAlbum, TabMyAlbumFragment.class);
		tab.setTabListener(tabMyAlbum);
		actionBar.addTab(tab);

		String tabTitleOtherAlbum = getResources().getString(
				R.string.tab_title_other_album);
		tab = actionBar.newTab();
		tab.setText(tabTitleOtherAlbum);
		tab.setIcon(R.drawable.custom_tab_other_album);
		TabListener<TabOtherAlbumFragment> tabOtherAlbum = new TabListener<TabOtherAlbumFragment>(
				this, tabTitleOtherAlbum, TabOtherAlbumFragment.class);
		tab.setTabListener(tabOtherAlbum);
		actionBar.addTab(tab);
		
		
	}

	private class TabListener<T extends Fragment> implements
			ActionBar.TabListener {
		private Fragment mFragment;
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;

		public TabListener(Activity activity, String tag, Class<T> clz) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// Check if the fragment is already initialized
			if (mFragment == null) {
				// If not, instantiate and add it to the activity
				mFragment = Fragment.instantiate(mActivity, mClass.getName());
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				// If it exists, simply attach it in order to show it
				ft.attach(mFragment);
			}

		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				// Detach the fragment, because another one is being attached
				ft.detach(mFragment);
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}
	}
}
