package vn.tdt.androidcamera.tabactionbar;

import vn.tdt.androidcamera.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class TabMyAlbumFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return (LinearLayout) inflater.inflate(
				R.layout.activity_gallery_my_album, container, false);
	}
}
