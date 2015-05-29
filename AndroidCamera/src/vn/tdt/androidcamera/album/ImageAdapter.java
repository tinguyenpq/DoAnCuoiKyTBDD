package vn.tdt.androidcamera.album;

import java.util.ArrayList;

import vn.tdt.androidcamera.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	// private LayoutInflater mInflater;
	// private Context context;
	Activity context = null;
	ArrayList<String> f = new ArrayList<String>();
	int layoutId;

	public ImageAdapter(Activity context, int layoutId, ArrayList<String> f) {
		// this.context = context;
		// mInflater = (LayoutInflater) context
		// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// this.f = f;
		this.context = context;
		this.f = f;
		this.layoutId = layoutId;
	}

	public int getCount() {
		return f.size();
	}

	public Object getItem(int position) {
		return f.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("null")
	public View getView(int position, View convertView, ViewGroup parent) {
		// ViewHolder holder = null;
		ImageView imgView;
		convertView = new View(context);
		LayoutInflater layoutInflater = context.getLayoutInflater();
		convertView = layoutInflater.inflate(layoutId, null);
		// if (convertView == null) {
		// holder = new ViewHolder();
		// convertView = mInflater.inflate(R.layout.custom_gridview_myalbum,
		// null);
		// holder.imageview = (ImageView) convertView
		// .findViewById(R.id.thumbImage);
		//
		// convertView.setTag(holder);
		// Bitmap myBitmap = BitmapFactory.decodeFile(f.get(position));
		// if (myBitmap != null) {
		// holder.imageview.setImageBitmap(Bitmap.createScaledBitmap(
		// myBitmap, 75, 75, false));
		// }
		// } else {
		// holder = (ViewHolder) convertView.getTag();
		// }
		if (f.size() > 0) {
			imgView = (ImageView) convertView.findViewById(R.id.thumbImage);
			// convertView =
			// mInflater.inflate(R.layout.custom_gridview_myalbum,null);
			// holder.imageview = (ImageView) convertView
			// .findViewById(R.id.thumbImage);
			Bitmap myBitmap = BitmapFactory.decodeFile(f.get(position));
			if (myBitmap != null) {
				imgView.setImageBitmap(Bitmap.createScaledBitmap(myBitmap, 75,
						75, false));
			}
		}

		// holder.imageview.setImageBitmap(myBitmap);
		return convertView;
	}
}
