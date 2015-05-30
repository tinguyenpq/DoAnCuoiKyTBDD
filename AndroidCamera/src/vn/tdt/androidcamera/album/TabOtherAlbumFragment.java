package vn.tdt.androidcamera.album;

import java.util.ArrayList;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

public class TabOtherAlbumFragment extends Fragment {
	private ImageAdapter imageAdapter2;
	private Context context;
	GridView gridView;
	ArrayList<String> files;// list of file paths
	ArrayList<BitmapDrawable> fileToDrawable = new ArrayList<BitmapDrawable>();
	// File[] listFile;
	String filePath = "";

	SharedPreferencesModels spm;
	final static String FILE_SELECTING = "FILE_SELECTING";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_gallery_other_album,
				container, false);
		files = new ArrayList<String>();
		this.context = container.getContext();
		spm = new SharedPreferencesModels(context);
	
		files = getAllShownImagesPath(getActivity());
//		Ultilities.toastShow(context, "size: "+files.size(), Gravity.CENTER);
//		for(int i=0;i<files.size();i++){
//			Log.d("sssssssssssss"+i,files.get(i));
//		}
		gridView = (GridView) view.findViewById(R.id.gridViewAllPhoto);
		imageAdapter2 = new ImageAdapter(getActivity(),
				R.layout.custom_gridview_myalbum, files);
		gridView.setAdapter(imageAdapter2);

		// for (int i = 0; i < files.size(); i++) {
		// Bitmap bm = BitmapHandler.convertImageToBitmap2(files.get(i));
		// fileToDrawable.add(new BitmapDrawable(getResources(), bm));
		// }
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// Ultilities.toastShow(context, pos + "", Gravity.CENTER);
				Intent intentViewAlbum = new Intent(context,
						ViewAlbumBySlide.class);
				context.startActivity(intentViewAlbum);

				Bundle bundle = new Bundle();

				bundle.putInt("position", pos);
				bundle.putString("filePath", filePath);
				bundle.putInt("ref", 2);

				intentViewAlbum.putExtra("viewAlbum", bundle);
				startActivity(intentViewAlbum);
				getActivity().finish();
			}
		});

		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View v,
					int pos, long arg3) {

				optionLongClick(pos);
				return false;
			}
		});
		// registerForContextMenu(gridView);
		return view;
	}

	public ArrayList<String> getAllShownImagesPath(Activity activity) {
		Uri uri;
		Cursor cursor;
		int column_index_data, column_index_folder_name;
		ArrayList<String> listOfAllImages = new ArrayList<String>();
		String absolutePathOfImage = null;
		uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		filePath = uri.toString();
		Log.d("=================filePAth222", filePath);

		String[] projection = { MediaColumns.DATA,
				MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

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

	
	public void optionLongClick(final int pos) {

		AlertDialog.Builder builderSingle = new AlertDialog.Builder(
				getActivity());
		// builderSingle.setIcon(R.drawable.ic_launcher);
		builderSingle.setTitle("Select One Name:-");
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.select_dialog_item);
		arrayAdapter.add("Open");
		arrayAdapter.add("Delete");
		arrayAdapter.add("Properties");
		builderSingle.setAdapter(arrayAdapter,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String strName = arrayAdapter.getItem(which);
						
						if (strName.equals("Delete")) {
							Ultilities.toastShow(context, files.get(pos),
									Gravity.CENTER);
							AlertDialog.Builder builder1 = new AlertDialog.Builder(
									context);
							builder1.setMessage("Delete?");
							builder1.setCancelable(true);
							builder1.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											boolean deleted = FileUltil
													.deleteFile(files.get(pos));
//											Ultilities.toastShow(context,
//													files.get(pos) + deleted,
//													Gravity.CENTER);

											files.remove(pos);
//											Ultilities.toastShow(context,
//													files.size() + "size",
//													Gravity.CENTER);
											imageAdapter2
													.notifyDataSetChanged();
											gridView.invalidateViews();
											gridView.setAdapter(imageAdapter2);

											dialog.cancel();

										}
									});
							builder1.setNegativeButton("No",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});

							AlertDialog alert11 = builder1.create();
							alert11.show();

						}
					}
				});
		builderSingle.show();
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
