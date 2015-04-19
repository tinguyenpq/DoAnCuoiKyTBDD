package vn.tdt.androidcamera.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class BitmapHandler {

	public static void saveBitmapToJpg(String fileName, String path,
			Bitmap bitmap) {
		OutputStream outStream = null;
		File file = new File(path, fileName + ".JPG");
		try {
			outStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			Log.d(path.toString(),
					"==========================file location ============================");
			outStream.flush();
			outStream.close();
		} catch (Exception e) {
		}
	}

	public static Bitmap convertImageToBitmap(String path) {
		return BitmapFactory.decodeFile(path);
	}
}
