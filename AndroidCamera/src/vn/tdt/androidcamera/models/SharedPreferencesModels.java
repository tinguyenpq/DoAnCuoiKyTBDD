package vn.tdt.androidcamera.models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesModels extends Activity {

	public static String fileName = "data_preferences";
	static SharedPreferences data;
	static SharedPreferences.Editor edit;

	public SharedPreferencesModels(Context context) {
		data = context.getSharedPreferences(fileName, MODE_PRIVATE);
		edit = data.edit();
	}

	public static void saveIntValue(String key, int value) {
		edit.putInt(key, value);
		edit.commit();
	}

	public static void saveStringValue(String key, String value) {
		edit.putString(key, value);
		edit.commit();
	}
	
	public static String getStringValue(String key){
		return data.getString(key, "0");
	}
	public static int getIntValue(String key){
		return data.getInt(key, 0);
	}
}
