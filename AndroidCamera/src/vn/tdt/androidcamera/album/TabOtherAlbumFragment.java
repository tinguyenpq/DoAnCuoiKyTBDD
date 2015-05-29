package vn.tdt.androidcamera.album;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import vn.tdt.androidcamera.R;
import vn.tdt.androidcamera.controllers.BitmapHandler;
import vn.tdt.androidcamera.controllers.FileUltil;
import vn.tdt.androidcamera.controllers.Ultilities;
import vn.tdt.androidcamera.models.SharedPreferencesModels;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;

public class TabOtherAlbumFragment extends Fragment {

	private int count;
	private Bitmap[] thumbnails;
	private boolean[] thumbnailsselection;
	private String[] arrPath;
	private ImageAdapter imageAdapter;
	private Context context;
	GridView imagegrid;
	ArrayList<String> f = new ArrayList<String>();// list of file paths
	ArrayList<BitmapDrawable> fileToDrawable = new ArrayList<BitmapDrawable>();
	File[] listFile;
	String filePath = "";

	SharedPreferencesModels spm;
	final static String FILE_SELECTING = "FILE_SELECTING";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_gallery_my_album,
				container, false);

		this.context = getActivity();
		spm = new SharedPreferencesModels(context);
		// getActivity().getActionBar().hide();
		// getActivity().
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// getFromSdcard();
		f = getAllShownImagesPath(getActivity());
		imagegrid = (GridView) view.findViewById(R.id.PhoneImageGrid);
		imageAdapter = new ImageAdapter(getActivity(),
				R.layout.custom_gridview_myalbum, f);
		imagegrid.setAdapter(imageAdapter);

		for (int i = 0; i < f.size(); i++) {
			Bitmap bm = BitmapHandler.convertImageToBitmap2(f.get(i));
			fileToDrawable.add(new BitmapDrawable(getResources(), bm));
		}
		imagegrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				Intent intentViewAlbum = new Intent(context,
						ViewAlbumBySlide.class);
				context.startActivity(intentViewAlbum);

				Bundle bundle = new Bundle();

				bundle.putInt("position", pos);
				bundle.putString("filePath", filePath);
				bundle.putInt("ref",2);

				intentViewAlbum.putExtra("viewAlbum", bundle);
				startActivity(intentViewAlbum);
				getActivity().finish();
			}
		});
		registerForContextMenu(imagegrid);
		return view;
	}

	public void getFromSdcard() {
		filePath = android.os.Environment.getExternalStorageDirectory() + "";
		File file = new File(filePath, "TdtCamera");
		if (file.isDirectory()) {
			listFile = file.listFiles();
			Arrays.sort(listFile, new Comparator<Object>() {
				public int compare(Object o1, Object o2) {

					if (((File) o1).lastModified() > ((File) o2).lastModified()) {
						return -1;
					} else if (((File) o1).lastModified() < ((File) o2)
							.lastModified()) {
						return +1;
					} else {
						return 0;
					}
				}

			});
			for (int i = 0; i < listFile.length; i++) {
				// long lastModified = listFile[i].lastModified();
				f.add(listFile[i].getAbsolutePath());

			}
		}
	}

	public ArrayList<String> getAllShownImagesPath(Activity activity) {
		Uri uri;
		Cursor cursor;
		int column_index_data, column_index_folder_name;
		ArrayList<String> listOfAllImages = new ArrayList<String>();
		String absolutePathOfImage = null;
		uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		filePath=uri.toString();
		Log.d("=================filePAth222", filePath);
		
		String[] projection = { MediaColumns.DATA,
				MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

		cursor = activity.getContentResolver().query(uri, projection, null,
				null, null);

		column_index_data = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		column_index_folder_name = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
		while (cursor.moveToNext()) {
			absolutePathOfImage = cursor.getString(column_index_data);

			listOfAllImages.add(absolutePathOfImage);
		}
		return listOfAllImages;
	}

	AdapterView.AdapterContextMenuInfo info;

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {

		case R.id.Delete:
			// Ultilities.toastShow(context, "Delete " + info.position,
			// Gravity.CENTER);
			AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
			builder1.setMessage("Delete?");
			builder1.setCancelable(true);
			builder1.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							boolean deleted = FileUltil.deleteFile(f
									.get(info.position));
							f.remove(info.position);
							imageAdapter.notifyDataSetChanged();
							// imagegrid.invalidateViews();
							imagegrid.setAdapter(imageAdapter);
							// imagegrid.refreshDrawableState();
							dialog.cancel();

						}
					});
			builder1.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});

			AlertDialog alert11 = builder1.create();
			alert11.show();
			return true;
		case R.id.Open:
			return true;
		case R.id.Properties:
			return true;

		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Select The Action");
		// menu.add(0, v.getId(), 0, "Call");// groupId, itemId, order, title
		// menu.add(0, v.getId(), 0, "SMS");
		// getActivity().getMenuInflater().inflate(R.menu.file_option, menu);
		menu.add(menu.NONE, R.id.Open, Menu.NONE, "Open");
		menu.add(menu.NONE, R.id.Delete, Menu.NONE, "Delete");
		menu.add(menu.NONE, R.id.Properties, Menu.NONE, "Properties");

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d("=================onStartMyAlbum===============", "myalbum start");
	}

}
