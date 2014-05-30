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

    /**
     * clear self means unregister the dataset observer and remove all the child views(indicators).
     */
    public void clearSelf(){
        ((InfinitePagerAdapter)(mPager.getAdapter())).getRealAdapter().unregisterDataSetObserver(dataChangeObserver);
        removeAllViews();
    }

    public void setViewPager(ViewPagerEx pager){
        if(pager.getAdapter() == null){
            throw new IllegalStateException("Viewpager does not have adapter instance");
        }
        mPager = pager;
        mPager.setOnPageChangeListener(this);
        ((InfinitePagerAdapter)mPager.getAdapter()).getRealAdapter().registerDataSetObserver(dataChangeObserver);
    }

    public void setIndicatorDrawable(int selected, int unselected){
        mUserSetSelectedIndicatorResId = selected;
        mUserSetUnSelectedIndicatorResId = unselected;

        mSelectedDrawable = mContext.getResources().getDrawable(mUserSetSelectedIndicatorResId);
        mUnselectedDrawable = mContext.getResources().getDrawable(mUserSetUnSelectedIndicatorResId);

        resetDrawable();
    }

    private void resetDrawable(){
        for(View i : mIndicators){
            if(mPreviousSelectedIndicator!= null && mPreviousSelectedIndicator.equals(i)){
                ((ImageView)i).setImageDrawable(mSelectedDrawable);
            }
            else{
                ((ImageView)i).setImageDrawable(mUnselectedDrawable);
            }
        }
    }

    public void redraw(){
        mItemCount = getShouldDrawCount();
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

    private int getShouldDrawCount(){
        if(mPager.getAdapter() instanceof InfinitePagerAdapter){
            return ((InfinitePagerAdapter)mPager.getAdapter()).getRealCount();
        }else{
            return mPager.getAdapter().getCount();
        }
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
            redraw();
        }
    };

    private void setItemAsSelected(int position){
        if(mPreviousSelectedIndicator != null){
            mPreviousSelectedIndicator.setImageDrawable(mUnselectedDrawable);
        }
        ImageView currentSelected = (ImageView)getChildAt(position + 1);
        if(currentSelected != null){
            currentSelected.setImageDrawable(mSelectedDrawable);
            mPreviousSelectedIndicator = currentSelected;
        }
        mPreviousSelectedPosition = position;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(mItemCount == 0){
            return;
        }
        int n = position % mItemCount;
        setItemAsSelected(n - 1);
    }

    @Override
    public void onPageSelected(int position) {

    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }

}
