package vn.tdt.androidcamera.fragment;

import vn.tdt.androidcamera.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SaveOrDiscardEffectFragment  extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_save_or_discard_effect, container, false);
	}
}
