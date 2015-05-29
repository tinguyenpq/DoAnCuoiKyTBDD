package vn.tdt.androidcamera.controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
