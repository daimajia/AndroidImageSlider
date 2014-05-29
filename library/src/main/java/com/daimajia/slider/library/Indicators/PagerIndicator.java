package com.daimajia.slider.library.Indicators;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daimajia.slider.library.Infinity.InfinitePagerAdapter;
import com.daimajia.slider.library.R;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;

/**
 * Created by daimajia on 14-5-27.
 */
public class PagerIndicator extends LinearLayout implements ViewPagerEx.OnPageChangeListener{

    private Context mContext;
    private ViewPagerEx mPager;
    private ImageView mPreviousSelectedIndicator;
    private int mPreviousSelectedPosition;

    private int mUserSetUnSelectedIndicatorResId;
    private int mUserSetSelectedIndicatorResId;

    private Drawable mSelectedDrawable;
    private Drawable mUnselectedDrawable;

    private int mItemCount = 0;
    private int mRealItemCount = 0;

    private ArrayList<ImageView> mIndicators = new ArrayList<ImageView>();

    public PagerIndicator(Context context) {
        this(context,null);
    }

    public PagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.indicator_layout,this,true);

        mSelectedDrawable = getResources().getDrawable(R.drawable.circle_selected_layer);
        mUnselectedDrawable = getResources().getDrawable(R.drawable.circle_common_layer);
    }

    public void setViewPager(ViewPagerEx pager){
        if(pager.getAdapter() == null){
            throw new IllegalStateException("Viewpager does not have adapter instance");
        }
        mPager = pager;
        mPager.setOnPageChangeListener(this);
        ((InfinitePagerAdapter)mPager.getAdapter()).getRealAdapter().registerDataSetObserver(dataChangeObserver);
    }

    public void setIndicator(int selected,int unselected){
        mUserSetSelectedIndicatorResId = selected;
        mUserSetUnSelectedIndicatorResId = unselected;

        mSelectedDrawable = mContext.getResources().getDrawable(mUserSetSelectedIndicatorResId);
        mUnselectedDrawable = mContext.getResources().getDrawable(mUserSetUnSelectedIndicatorResId);

        redraw();
    }

    private void redraw(){
        mPreviousSelectedIndicator = null;
        for(View i:mIndicators){
            removeView(i);
        }
        for(int i =0 ;i<mItemCount;i++){
            ImageView indicator = new ImageView(mContext);
            indicator.setImageDrawable(mUnselectedDrawable);
            addView(indicator);
            mIndicators.add(indicator);
        }
        setItemAsSelected(mPreviousSelectedPosition);
    }

    private DataSetObserver dataChangeObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            PagerAdapter adapter = mPager.getAdapter();
            int count = 0;
            if(adapter instanceof InfinitePagerAdapter){
                count = ((InfinitePagerAdapter)adapter).getRealCount();
            }else{
                count = adapter.getCount();
            }
            mRealItemCount = count;
            if(count > mItemCount){
                for(int i =0 ; i< count - mItemCount;i++){
                    ImageView indicator = new ImageView(mContext);
                    indicator.setImageDrawable(mUnselectedDrawable);
                    addView(indicator);
                    mIndicators.add(indicator);
                }
            }else if(count < mItemCount){
                for(int i = 0; i < mItemCount - count;i++){
                    removeView(mIndicators.get(0));
                    mIndicators.remove(0);
                }
            }
            mItemCount = count;
            mPager.setCurrentItem(mItemCount*20 + mPager.getCurrentItem());
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    };

    private void setItemAsSelected(int position){
        if(mPreviousSelectedIndicator != null){
            mPreviousSelectedIndicator.setImageDrawable(mUnselectedDrawable);
        }
        ImageView currentSelected = (ImageView)getChildAt(position + 1);
        currentSelected.setImageDrawable(mSelectedDrawable);
        mPreviousSelectedIndicator = currentSelected;
        mPreviousSelectedPosition = position;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(mRealItemCount == 0){
            return;
        }
        int n = position % mRealItemCount;
        setItemAsSelected(n);
    }

    @Override
    public void onPageSelected(int position) {

    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }

}
