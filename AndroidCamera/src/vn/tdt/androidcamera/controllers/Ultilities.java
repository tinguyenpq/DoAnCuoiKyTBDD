package vn.tdt.androidcamera.controllers;

import java.io.File;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.Toast;

public class Ultilities {

	public static String getFileName(int fileNameType) {
		Calendar cal = Calendar.getInstance();
		int second = cal.get(Calendar.SECOND);
		int minute = cal.get(Calendar.MINUTE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		String name = "";
		if (day < 10) {
			name += "0" + day;
		} else {
			name += day;
		}
		if (month < 10) {
			name += "0" + month;
		} else {
			name += month;
		}
		name += year;
		if (hour < 10) {
			name += "0" + hour;
		} else {
			name += hour;
		}
		if (minute < 10) {
			name += "0" + minute;
		} else {
			name += minute;
		}
		if (second < 10) {
			name += "0" + second;
		} else {
			name += second;
		}
		return name;
	}
	
	public static String pathToSave(int option){
		File sdcard = Environment.getExternalStorageDirectory();
		File path = new File(sdcard.getAbsolutePath() + "/TdtCamera/");
		path.mkdir();
		return path.toString();
	}
	public static void takePictureHandler(Bitmap b,String fileName,String path) {
		BitmapHandler.saveBitmapToJpg(fileName,path, b);
	}
	
	public static void toastShow(Context c,String str,int gravity){
		Toast  t = Toast.makeText(c, str, Toast.LENGTH_SHORT);
		t.setGravity(gravity, 0,0);
		t.show();
	}
}
