package com.daimajia.slider.library.Indicators;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daimajia.slider.library.R;
import com.daimajia.slider.library.Tricks.InfinitePagerAdapter;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;

/**
 * Pager Indicator.
 */
public class PagerIndicator extends LinearLayout implements ViewPagerEx.OnPageChangeListener{

    private Context mContext;

    /**
     * bind this Indicator with {@link com.daimajia.slider.library.Tricks.ViewPagerEx}
     */
    private ViewPagerEx mPager;

    /**
     * Variable to remember the previous selected indicator.
     */
    private ImageView mPreviousSelectedIndicator;

    /**
     * Previous selected indicator position.
     */
    private int mPreviousSelectedPosition;

    /**
     * Custom selected indicator style resource id.
     */
    private int mUserSetUnSelectedIndicatorResId;


    /**
     * Custom unselected indicator style resource id.
     */
    private int mUserSetSelectedIndicatorResId;

    private Drawable mSelectedDrawable;
    private Drawable mUnselectedDrawable;

    /**
     *This value is from {@link com.daimajia.slider.library.SliderAdapter} getRealCount() represent
     *
     * the indicator count that we should draw.
     */
    private int mItemCount = 0;

    private Shape mIndicatorShape = Shape.Oval;

    private IndicatorVisibility mVisibility = IndicatorVisibility.Visible;

    private int mDefaultSelectedColor;
    private int mDefaultUnSelectedColor;

    private float mDefaultSelectedWidth;
    private float mDefaultSelectedHeight;

    private float mDefaultUnSelectedWidth;
    private float mDefaultUnSelectedHeight;

    public enum IndicatorVisibility{
        Visible,
        Invisible;
    };

    private GradientDrawable mUnSelectedGradientDrawable;
    private GradientDrawable mSelectedGradientDrawable;

    private LayerDrawable mSelectedLayerDrawable;
    private LayerDrawable mUnSelectedLayerDrawable;

    private float mPadding_left;
    private float mPadding_right;
    private float mPadding_top;
    private float mPadding_bottom;

    private float mSelectedPadding_Left;
    private float mSelectedPadding_Right;
    private float mSelectedPadding_Top;
    private float mSelectedPadding_Bottom;

    private float mUnSelectedPadding_Left;
    private float mUnSelectedPadding_Right;
    private float mUnSelectedPadding_Top;
    private float mUnSelectedPadding_Bottom;

    /**
     * Put all the indicators into a ArrayList, so we can remove them easily.
     */
    private ArrayList<ImageView> mIndicators = new ArrayList<ImageView>();


    public PagerIndicator(Context context) {
        this(context,null);
    }

    public PagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        final TypedArray attributes = context.obtainStyledAttributes(attrs,R.styleable.PagerIndicator,0,0);

        int visibility = attributes.getInt(R.styleable.PagerIndicator_visibility,IndicatorVisibility.Visible.ordinal());

        for(IndicatorVisibility v : IndicatorVisibility.values()){
            if(v.ordinal() == visibility){
                mVisibility = v;
                break;
            }
        }

        int shape = attributes.getInt(R.styleable.PagerIndicator_shape, Shape.Oval.ordinal());
        for(Shape s: Shape.values()){
            if(s.ordinal() == shape){
                mIndicatorShape = s;
                break;
            }
        }

        mUserSetSelectedIndicatorResId = attributes.getResourceId(R.styleable.PagerIndicator_selected_drawable,
                0);
        mUserSetUnSelectedIndicatorResId = attributes.getResourceId(R.styleable.PagerIndicator_unselected_drawable,
                0);

        mDefaultSelectedColor = attributes.getColor(R.styleable.PagerIndicator_selected_color, Color.rgb(255, 255, 255));
        mDefaultUnSelectedColor = attributes.getColor(R.styleable.PagerIndicator_unselected_color, Color.argb(33,255,255,255));

        mDefaultSelectedWidth = attributes.getDimension(R.styleable.PagerIndicator_selected_width,(int)pxFromDp(6));
        mDefaultSelectedHeight = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_selected_height,(int)pxFromDp(6));

        mDefaultUnSelectedWidth = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_unselected_width,(int)pxFromDp(6));
        mDefaultUnSelectedHeight = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_unselected_height,(int)pxFromDp(6));

        mSelectedGradientDrawable = new GradientDrawable();
        mUnSelectedGradientDrawable = new GradientDrawable();

        mPadding_left = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_padding_left,(int)pxFromDp(3));
        mPadding_right = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_padding_right,(int)pxFromDp(3));
        mPadding_top = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_padding_top,(int)pxFromDp(0));
        mPadding_bottom = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_padding_bottom,(int)pxFromDp(0));

        mSelectedPadding_Left = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_selected_padding_left,(int)mPadding_left);
        mSelectedPadding_Right = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_selected_padding_right,(int)mPadding_right);
        mSelectedPadding_Top = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_selected_padding_top,(int)mPadding_top);
        mSelectedPadding_Bottom = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_selected_padding_bottom,(int)mPadding_bottom);

        mUnSelectedPadding_Left = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_unselected_padding_left,(int)mPadding_left);
        mUnSelectedPadding_Right = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_unselected_padding_right,(int)mPadding_right);
        mUnSelectedPadding_Top = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_unselected_padding_top,(int)mPadding_top);
        mUnSelectedPadding_Bottom = attributes.getDimensionPixelSize(R.styleable.PagerIndicator_unselected_padding_bottom,(int)mPadding_bottom);

        mSelectedLayerDrawable = new LayerDrawable(new Drawable[]{mSelectedGradientDrawable});
        mUnSelectedLayerDrawable = new LayerDrawable(new Drawable[]{mUnSelectedGradientDrawable});


        setIndicatorStyleResource(mUserSetSelectedIndicatorResId,mUserSetUnSelectedIndicatorResId);
        setDefaultIndicatorShape(mIndicatorShape);
        setDefaultSelectedIndicatorSize(mDefaultSelectedWidth,mDefaultSelectedHeight,Unit.Px);
        setDefaultUnselectedIndicatorSize(mDefaultUnSelectedWidth,mDefaultUnSelectedHeight,Unit.Px);
        setDefaultIndicatorColor(mDefaultSelectedColor, mDefaultUnSelectedColor);
        setIndicatorVisibility(mVisibility);
        setDefaultSelectedPadding(mSelectedPadding_Left,mSelectedPadding_Top,mSelectedPadding_Right,mSelectedPadding_Bottom,Unit.Px);
        setDefaultUnSelectedPadding(mUnSelectedPadding_Left,mUnSelectedPadding_Top,mUnSelectedPadding_Right,mUnSelectedPadding_Bottom,Unit.Px);
        attributes.recycle();
    }

    public enum Shape{
        Oval,Rectangle
    }

    public void setDefaultPadding(float left,float top, float right, float bottom,Unit unit){
        setDefaultSelectedPadding(left,top,right,bottom,unit);
        setDefaultUnSelectedPadding(left,top,right,bottom,unit);
    }

    public void setDefaultSelectedPadding(float left,float top, float right, float bottom,Unit unit){
        if(unit == Unit.DP){
            mSelectedLayerDrawable.setLayerInset(0,
                    (int)pxFromDp(left),(int)pxFromDp(top),
                    (int)pxFromDp(right),(int)pxFromDp(bottom));
        }else{
            mSelectedLayerDrawable.setLayerInset(0,
                    (int)left,(int)top,
                    (int)right,(int)bottom);

        }
    }

    public void setDefaultUnSelectedPadding(float left,float top, float right, float bottom, Unit unit){
        if(unit == Unit.DP){
            mUnSelectedLayerDrawable.setLayerInset(0,
                    (int)pxFromDp(left),(int)pxFromDp(top),
                    (int)pxFromDp(right),(int)pxFromDp(bottom));

        }else{
            mUnSelectedLayerDrawable.setLayerInset(0,
                    (int)left,(int)top,
                    (int)right,(int)bottom);

        }
    }

    /**
     * if you are using the default indicator, this method will help you to set the shape of
     * indicator, there are two kind of shapes you  can set, oval and rect.
     * @param shape
     */
    public void setDefaultIndicatorShape(Shape shape){
        if(mUserSetSelectedIndicatorResId == 0){
            if(shape == Shape.Oval){
                mSelectedGradientDrawable.setShape(GradientDrawable.OVAL);
            }else{
                mSelectedGradientDrawable.setShape(GradientDrawable.RECTANGLE);
            }
        }
        if(mUserSetUnSelectedIndicatorResId == 0){
            if(shape == Shape.Oval){
                mUnSelectedGradientDrawable.setShape(GradientDrawable.OVAL);
            }else{
                mUnSelectedGradientDrawable.setShape(GradientDrawable.RECTANGLE);
            }
        }
        resetDrawable();
    }


    /**
     * Set Indicator style.
     * @param selected page selected drawable
     * @param unselected page unselected drawable
     */
    public void setIndicatorStyleResource(int selected, int unselected){
        mUserSetSelectedIndicatorResId = selected;
        mUserSetUnSelectedIndicatorResId = unselected;
        if(selected == 0){
            mSelectedDrawable = mSelectedLayerDrawable;
        }else{
            mSelectedDrawable = mContext.getResources().getDrawable(mUserSetSelectedIndicatorResId);
        }
        if(unselected == 0){
            mUnselectedDrawable = mUnSelectedLayerDrawable;
        }else{
            mUnselectedDrawable = mContext.getResources().getDrawable(mUserSetUnSelectedIndicatorResId);
        }

        resetDrawable();
    }

    /**
     * if you are using the default indicator , this method will help you to set the selected status and
     * the unselected status color.
     * @param selectedColor
     * @param unselectedColor
     */
    public void setDefaultIndicatorColor(int selectedColor,int unselectedColor){
        if(mUserSetSelectedIndicatorResId == 0){
            mSelectedGradientDrawable.setColor(selectedColor);
        }
        if(mUserSetUnSelectedIndicatorResId == 0){
            mUnSelectedGradientDrawable.setColor(unselectedColor);
        }
        resetDrawable();
    }

    public enum Unit{
        DP,Px
    }

    public void setDefaultSelectedIndicatorSize(float width,float height,Unit unit){
        if(mUserSetSelectedIndicatorResId == 0){
            float w = width;
            float h = height;
            if(unit == Unit.DP){
                w = pxFromDp(width);
                h = pxFromDp(height);
            }
            mSelectedGradientDrawable.setSize((int) w, (int) h);
            resetDrawable();
        }
    }

    public void setDefaultUnselectedIndicatorSize(float width,float height,Unit unit){
        if(mUserSetUnSelectedIndicatorResId == 0){
            float w = width;
            float h = height;
            if(unit == Unit.DP){
                w = pxFromDp(width);
                h = pxFromDp(height);
            }
            mUnSelectedGradientDrawable.setSize((int) w, (int) h);
            resetDrawable();
        }
    }

    public void setDefaultIndicatorSize(float width, float height, Unit unit){
        setDefaultSelectedIndicatorSize(width,height,unit);
        setDefaultUnselectedIndicatorSize(width,height,unit);
    }

    private float dpFromPx(float px)
    {
        return px / this.getContext().getResources().getDisplayMetrics().density;
    }

    private float pxFromDp(float dp)
    {
        return dp * this.getContext().getResources().getDisplayMetrics().density;
    }

    /**
     * set the visibility of indicator.
     * @param visibility
     */
    public void setIndicatorVisibility(IndicatorVisibility visibility){
        if(visibility == IndicatorVisibility.Visible){
            setVisibility(View.VISIBLE);
        }else{
            setVisibility(View.INVISIBLE);
        }
        resetDrawable();
    }

    /**
     * clear self means unregister the dataset observer and remove all the child views(indicators).
     */
    public void destroySelf(){
        if(mPager == null || mPager.getAdapter() == null){
            return;
        }
        InfinitePagerAdapter wrapper = (InfinitePagerAdapter)mPager.getAdapter();
        PagerAdapter adapter = wrapper.getRealAdapter();
        if(adapter!=null){
            adapter.unregisterDataSetObserver(dataChangeObserver);
        }
        removeAllViews();
        ShapeDrawable shapeDrawable;

    }

    /**
     * bind indicator with viewpagerEx.
     * @param pager
     */
    public void setViewPager(ViewPagerEx pager){
        if(pager.getAdapter() == null){
            throw new IllegalStateException("Viewpager does not have adapter instance");
        }
        mPager = pager;
        mPager.setOnPageChangeListener(this);
        ((InfinitePagerAdapter)mPager.getAdapter()).getRealAdapter().registerDataSetObserver(dataChangeObserver);
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

    /**
     * redraw the indicators.
     */
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

    /**
     * since we used a adapter wrapper, so we can't getCount directly from wrapper.
     * @return
     */
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

    public IndicatorVisibility getIndicatorVisibility(){
        return mVisibility;
    }

    @Override
    public void onPageSelected(int position) {

    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public int getSelectedIndicatorResId(){
        return mUserSetSelectedIndicatorResId;
    }

    public int getUnSelectedIndicatorResId(){
        return mUserSetUnSelectedIndicatorResId;
    }

}
