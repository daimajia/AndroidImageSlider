package com.daimajia.slider.library.Tricks;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * A {@link ViewPager} that allows pseudo-infinite paging with a wrap-around effect. Should be used with an {@link
 * InfinitePagerAdapter}.
 */
public class InfiniteViewPager extends ViewPagerEx {

    public InfiniteViewPager(Context context) {
        super(context);
    }

    public InfiniteViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(getAdapter() != null && getAdapter() instanceof InfinitePagerAdapter){
            InfinitePagerAdapter adapter = (InfinitePagerAdapter)getAdapter();
            if(adapter.getRealCount() == 1){
                return false;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }
}