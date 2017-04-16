package com.david.photogallery.manager;

import android.content.Context;
import android.os.Environment;
import java.io.File;

/**
 * Created by david on 2017/3/29.
 */

public class FileManager {

  public static final String TAG = "FileManager";
  public static final long MAX_CACHE_SIZE = 1024 * 1024 * 64;//64M

  private static FileManager instance;
  private static Context context;
  private static String baseDirName;
  private static String pictureDirName = "picture";

  private File baseDir;
  private File sysCacheDir;
  private File pictureDir;

  public static void init(Context context) {
    FileManager.context = context;
    FileManager.baseDirName = "PhotoGallery";
  }

  private FileManager() {
    boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
    if (sdCardExist) {
      File sdDir = Environment.getExternalStorageDirectory(); //获取根目录
      baseDir = new File(sdDir, baseDirName);
      baseDir.mkdirs();
      pictureDir = new File(baseDir, pictureDirName);
      pictureDir.mkdirs();
    } else {
      File filesDir = context.getFilesDir();
      baseDir = new File(filesDir, baseDirName);
      baseDir.mkdirs();
      pictureDir = new File(baseDir, pictureDirName);
      pictureDir.mkdirs();
    }
    sysCacheDir = context.getCacheDir();
  }

  public static FileManager getInstance() {
    if (instance == null) {
      instance = new FileManager();
    }
    return instance;
  }

  public File getBaseDir() {
    return baseDir;
  }

  public File getSysCacheDir() {
    return sysCacheDir;
  }

  public File getPictureDir() {
    return pictureDir;
  }
}
