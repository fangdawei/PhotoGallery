package com.david.photogallery;

import android.app.Application;
import com.david.photogallery.manager.FileManager;

/**
 * Created by david on 2017/4/16.
 */

public class PgApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    FileManager.init(this);
  }
}
