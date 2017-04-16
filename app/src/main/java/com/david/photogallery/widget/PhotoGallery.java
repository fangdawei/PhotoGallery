package com.david.photogallery.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.david.photogallery.R;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by david on 2017/4/14.
 */

public class PhotoGallery extends RelativeLayout {

  public static final String TAG = "PhotoGallery";

  private ViewPager viewPager;
  private Context context;
  private int pagerWidth;
  private int pagerHeigth;
  private int pagerMargin;
  private int photoMax;
  private List<File> photoList = new ArrayList<>();
  private OnPageChangedListener onPageChangedListener;
  private Map<Integer, ImageView> previewImages = new HashMap<>();
  private int lastScrolledPosition = -1;

  private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
    @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      if (position >= 0 && position < photoList.size()) {
        int pos = -1;
        if (positionOffset < 0.4) {
          pos = position;
        } else if (positionOffset > 0.6) {
          pos = position + 1;
        }
        if (pos >= 0 && pos < photoList.size() && pos != lastScrolledPosition && onPageChangedListener != null) {
          Log.d(TAG, "" + pos);
          onPageChangedListener.onPageScrolled(pos);
        }
        lastScrolledPosition = pos;
      }
    }

    @Override public void onPageSelected(int position) {
      if (onPageChangedListener != null) {
        onPageChangedListener.onPageChanged(position);
      }
    }

    @Override public void onPageScrollStateChanged(int state) {

    }
  };

  private PagerAdapter pagerAdapter = new PagerAdapter() {
    @Override public int getCount() {
      return photoList.size();
    }

    @Override public boolean isViewFromObject(View view, Object object) {
      return view == object;
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
      ImageView imageView = previewImages.get(position);
      if (imageView == null) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load(photoList.get(position)).into(imageView);
        previewImages.put(position, imageView);
      }
      container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
      return imageView;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView(previewImages.get(position));
    }
  };

  public PhotoGallery(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PhotoGallery);
    pagerWidth = typedArray.getDimensionPixelSize(R.styleable.PhotoGallery_photo_width, 100);
    pagerHeigth = typedArray.getDimensionPixelSize(R.styleable.PhotoGallery_photo_height, 200);
    pagerMargin = typedArray.getDimensionPixelSize(R.styleable.PhotoGallery_Photo_margin, 10);
    photoMax = typedArray.getInteger(R.styleable.PhotoGallery_Photo_max, 20);
    initViewPager();
  }

  public void setPhotoList(List<File> list) {
    photoList.clear();
    photoList.addAll(list);
    pagerAdapter.notifyDataSetChanged();
    if (onPageChangedListener != null) {
      onPageChangedListener.onPageChanged(0);
    }
  }

  public void setOnPageChangedListener(OnPageChangedListener listener) {
    onPageChangedListener = listener;
  }

  public void setCurrentPage(int position) {
    viewPager.setCurrentItem(position);
  }

  private void initViewPager() {
    viewPager = new ViewPager(context);
    addView(viewPager, pagerWidth, pagerHeigth);
    LayoutParams params = (LayoutParams) viewPager.getLayoutParams();
    params.addRule(CENTER_IN_PARENT);
    viewPager.setLayoutParams(params);
    viewPager.setPageMargin(pagerMargin);
    viewPager.setClipChildren(false);
    viewPager.setOffscreenPageLimit(photoMax);
    viewPager.addOnPageChangeListener(pageChangeListener);
    viewPager.setOverScrollMode(OVER_SCROLL_NEVER);

    this.setClipChildren(false);

    this.setOnTouchListener(new OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        return viewPager.onTouchEvent(event);
      }
    });

    viewPager.setAdapter(pagerAdapter);
  }

  public interface OnPageChangedListener {
    void onPageChanged(int position);

    void onPageScrolled(int position);
  }
}
