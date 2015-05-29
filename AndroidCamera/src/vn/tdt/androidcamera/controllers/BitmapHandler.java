package vn.tdt.androidcamera.controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
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
	public static Bitmap convertImageToBitmap2(String path) {
		Bitmap bitmap = null;
		File f = new File(path);
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inScaled = false;
		o.inJustDecodeBounds = true;
		FileInputStream stream1 = null;
		try {
			stream1 = new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Find the correct scale value. It should be the power of 2.
		final int REQUIRED_SIZE = 75; // This is the max size of the bitmap in
										// kilobytes
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		FileInputStream stream2 = null;
		try {
			stream2 = new FileInputStream(f);
			bitmap = BitmapFactory.decodeStream(stream2, null, o2);
			stream2.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;

		// return BitmapFactory.decodeFile(path);
	}

	public static byte[] convertBitMapToByteArray(Bitmap bmp) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
		return stream.toByteArray();
	}

	public static Bitmap convertByteArrayToBitmap(byte[] byteArray) {
		return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
	}

	public static Bitmap drawTextOnImageView(Context context, int src,
			String text) {
		Bitmap bm = BitmapFactory.decodeResource(context.getResources(), src);
		Config config = bm.getConfig();
		int width = bm.getWidth();
		int height = bm.getHeight();

		Bitmap newImage = Bitmap.createBitmap(width, height, config);

		Canvas c = new Canvas(newImage);
		c.drawBitmap(bm, 0, 0, null);

		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.FILL);
		paint.setTextSize(20);
		c.drawText(text, 0, 25, paint);
		return newImage;
	}
}
