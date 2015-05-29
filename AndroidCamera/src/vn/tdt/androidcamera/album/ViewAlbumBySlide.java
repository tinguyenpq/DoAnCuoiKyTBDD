package vn.tdt.androidcamera.album;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import vn.tdt.androidcamera.R;
import vn.tdt.androidcamera.controllers.BitmapHandler;
import vn.tdt.androidcamera.controllers.FileUltil;
import vn.tdt.androidcamera.controllers.MainActivity;
import vn.tdt.androidcamera.controllers.PhotoEditorMain;
import vn.tdt.androidcamera.controllers.Ultilities;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class ViewAlbumBySlide extends Activity implements OnClickListener,
		OnGestureListener {
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private ViewFlipper mViewFlipper;
	private AnimationListener mAnimationListener;
	private Context mContext;

	ArrayList<String> f = new ArrayList<String>();
	File[] listFile;
	String filePath = "";
	int pos = -1;
	int ref = 1;

	ImageView ivCameraMain;
	ImageView ivBackMyPhoto;
	TextView tvPhotoPosition;
	TextView tvEditPhoto;
	TextView tvDeletePhoto;

	ArrayList<BitmapDrawable> fileToDrawable = new ArrayList<BitmapDrawable>();
	@SuppressWarnings("deprecation")
	private final GestureDetector detector = new GestureDetector(
			new SwipeGestureDetector());

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.view_my_album);
		mContext = this;
		ivCameraMain = (ImageView) findViewById(R.id.ivCameraMain);
		ivBackMyPhoto = (ImageView) findViewById(R.id.ivBackMyPhoto);
		tvPhotoPosition = (TextView) findViewById(R.id.tvPhotoPosition);
		tvEditPhoto = (TextView) findViewById(R.id.tvEditPhoto);
		tvDeletePhoto = (TextView) findViewById(R.id.tvDeletePhoto);

		Intent callerIntent = getIntent();
		Bundle packageFromCaller = callerIntent.getBundleExtra("viewAlbum");
		if (packageFromCaller != null) {
			ref = packageFromCaller.getInt("ref");
			Log.d("-------------------ref---------", ref + "");
			filePath = packageFromCaller.getString("filePath");
			// Ultilities.toastShow(mContext, filePath + "", Gravity.CENTER);
			pos = packageFromCaller.getInt("position");
		} else {
			finish();
		}
		if (ref == 1) {
			getFromSdcard(filePath);
		} else {
			f = getAllShownImagesPath(this);
		}

		for (int i = 0; i < f.size(); i++) {
			if (FileUltil.isImage(f.get(i))) {
				Log.d("--------------check file type", "ok");

			} else {
				// Log.d("--------------check file type", "not is image");
				f.remove(i);
			}
		}
		for (String ff : f) {
			// Bitmap bm = BitmapHandler.convertImageToBitmap(f.get(i));
			fileToDrawable.add(new BitmapDrawable(getResources(), BitmapHandler
					.convertImageToBitmap(ff)));
		}

		mViewFlipper = (ViewFlipper) this.findViewById(R.id.flipper);
		// Ultilities.toastShow(mContext, fileToDrawable.size()+"",
		// Gravity.CENTER);
		for (int i = 0; i < fileToDrawable.size(); i++) {
			// This will create dynamic image view and add them to ViewFlipper
			setFlipperImage(fileToDrawable.get(i));
		}
		mViewFlipper.setDisplayedChild(pos);
		setPhotoPosition(mViewFlipper.getDisplayedChild() + 1,
				fileToDrawable.size());
		mViewFlipper.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(final View view, final MotionEvent event) {
				detector.onTouchEvent(event);
				setPhotoPosition(mViewFlipper.getDisplayedChild() + 1,
						mViewFlipper.getChildCount());
				return true;
			}
		});

		ivCameraMain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent iMain = new Intent(ViewAlbumBySlide.this,
						MainActivity.class);
				startActivity(iMain);
				finish();
			}
		});
		ivBackMyPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(ViewAlbumBySlide.this,
						GalleryActivity.class);
				startActivity(i);
				finish();
				onDestroy();
			}
		});
		tvDeletePhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
				builder1.setMessage("Delete?");
				builder1.setCancelable(true);
				builder1.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								boolean deleted = FileUltil.deleteFile(f
										.get(mViewFlipper.getDisplayedChild()));
								if (!deleted) {
									Ultilities.toastShow(mContext,
											"Can not delete this photo",
											Gravity.CENTER);
								} else {

									f.remove(mViewFlipper.getDisplayedChild());
									fileToDrawable.remove(mViewFlipper
											.getDisplayedChild());
									mViewFlipper.removeViewAt(mViewFlipper
											.getDisplayedChild());
									if (mViewFlipper.getChildCount() > 0) {
										mViewFlipper.setDisplayedChild(0);
										setPhotoPosition(mViewFlipper
												.getDisplayedChild() + 1,
												mViewFlipper.getChildCount());

									} else {
										Intent i = new Intent(
												ViewAlbumBySlide.this,
												GalleryActivity.class);
										startActivity(i);
										finish();
									}
								}
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

			}
		});
		tvEditPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String pathToEdit = f.get(mViewFlipper.getDisplayedChild());
				File f = new File(pathToEdit);
				String fileName = f.getName().toString();
				//Ultilities.toastShow(mContext, f.getName(), Gravity.CENTER);
			
				Intent intentPhotoEditor = new Intent(mContext,
						PhotoEditorMain.class);
				startActivity(intentPhotoEditor);
				Bundle bundle = new Bundle();
				bundle.putInt("ref", 2);// from view album
				bundle.putString("pathToEdit", pathToEdit);
				bundle.putString("fileName",fileName);
				
				intentPhotoEditor.putExtra("PhotoEditor", bundle);
				startActivity(intentPhotoEditor);
				//finish();
			}
		});

		// animation listener
		mAnimationListener = new Animation.AnimationListener() {
			public void onAnimationStart(Animation animation) {
				// animation started event
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				// TODO animation stopped event
			}
		};
	}

	class SwipeGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(
							mContext, R.anim.left_in));
					mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(
							mContext, R.anim.left_out));
					// controlling animation
					mViewFlipper.getInAnimation().setAnimationListener(
							mAnimationListener);
					mViewFlipper.showNext();
					return true;
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(
							mContext, R.anim.right_in));
					mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(
							mContext, R.anim.right_out));
					// controlling animation
					mViewFlipper.getInAnimation().setAnimationListener(
							mAnimationListener);
					mViewFlipper.showPrevious();
					return true;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void setFlipperImage(Drawable res) {
		Log.i("Set Filpper Called", res + "");
		ImageView image = new ImageView(getApplicationContext());
		image.setBackgroundDrawable(res);
		mViewFlipper.addView(image);
	}

	public void getFromSdcard(String filePath) {
		File file = new File(filePath, "TdtCamera");

		if (file.isDirectory()) {
			// File dir = new File(getFilesDir().toString());
			// listFile = dir.listFiles();
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
		// filePath = uri.toString();
		// Log.d("=================filePAth222", filePath);

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

	public void setPhotoPosition(int pos, int sum) {
		tvPhotoPosition.setText(pos + "/" + sum);
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(ViewAlbumBySlide.this, GalleryActivity.class);
		startActivity(i);
		finish();
		onDestroy();
	}
}
