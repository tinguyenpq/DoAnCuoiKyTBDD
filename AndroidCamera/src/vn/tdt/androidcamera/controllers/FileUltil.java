package vn.tdt.androidcamera.controllers;

import java.io.File;
import java.text.SimpleDateFormat;

import android.os.Environment;

public class FileUltil {
	public static void renameFile(String oldName, String newName) {
		File dir = Environment.getExternalStorageDirectory();
		if (dir.exists()) {
			File from = new File(dir, oldName);
			File to = new File(dir, newName);
			if (from.exists())
				from.renameTo(to);
		}
	}

	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		boolean deleted = file.delete();
		return deleted;
	}

	public static boolean isFileExist(String path) {
		File f = new File(path);
		if (f.exists() && !f.isDirectory()) {
			return true;
		} else
			return false;
	}

	public static double fileSize(String filePath) {
		File file = new File(filePath);

		if (file.exists()) {
			double bytes = file.length();
			double kilobytes = (bytes / 1024);
			// double megabytes = (kilobytes / 1024);
			// double gigabytes = (megabytes / 1024);
			// double terabytes = (gigabytes / 1024);
			// double petabytes = (terabytes / 1024);
			// double exabytes = (petabytes / 1024);
			// double zettabytes = (exabytes / 1024);
			// double yottabytes = (zettabytes / 1024);
			return kilobytes;
		}
		return 0;
	}

	public static String lastModified(String filePath) {
		File file = new File(filePath);
		//System.out.println("Before Format : " + file.lastModified());
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		//System.out.println("After Format : " + sdf.format(file.lastModified()));
		return sdf.format(file.lastModified());
	}
	public static boolean isImage(String path){
		File file = new File (path);
		String[] okFileExtensions =  new String[] {"jpg", "png", "gif","jpeg"};
		for (String extension : okFileExtensions)
        { 
          if (file.getName().toLowerCase().endsWith(extension))
          {                
            return true; 
          } 
        } 
		return false;
	}
}
