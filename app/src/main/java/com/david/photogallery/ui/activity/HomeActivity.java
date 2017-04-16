package com.david.photogallery.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.david.photogallery.databinding.ActivityHomeBinding;
import com.david.photogallery.databinding.ToolbarHomeBinding;
import com.david.photogallery.manager.FileManager;
import com.david.photogallery.widget.PhotoGallery;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseBindingActivity<ActivityHomeBinding> {

  private List<File> photos = new ArrayList<>();

  public static void startActivity(Context context) {
    Intent intent = new Intent(context, HomeActivity.class);
    context.startActivity(intent);
  }

  @Override protected View createToolBar(Bundle savedInstanceState, ViewGroup container) {
    ToolbarHomeBinding binding = ToolbarHomeBinding.inflate(getLayoutInflater(), container, false);
    binding.title.setText("照片");
    return binding.getRoot();
  }

  @Override protected ActivityHomeBinding createViewDataBinding(Bundle savedInstanceState, ViewGroup container) {
    return ActivityHomeBinding.inflate(this.getLayoutInflater(), container, false);
  }

  @Override protected void preInit(Bundle savedInstanceState) {
    super.preInit(savedInstanceState);
  }

  @Override public void initView() {

  }

  @Override public void initListener() {
    mVDB.pgPhotos.setOnPageChangedListener(new PhotoGallery.OnPageChangedListener() {
      @Override public void onPageChanged(int position) {

      }

      @Override public void onPageScrolled(int position) {
        Glide.with(HomeActivity.this).load(photos.get(position)).into(mVDB.ivPhoto);
      }
    });
  }

  @Override public void initData() {
    File picDir = FileManager.getInstance().getPictureDir();
    File[] pics = picDir.listFiles();
    picDir.mkdirs();
    if (pics == null) {
      return;
    }
    for (File file : pics) {
      if (!file.isDirectory()) {
        photos.add(file);
      }
    }
    mVDB.pgPhotos.setPhotoList(photos);
  }
}
