package com.daimajia.slider.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;

import com.daimajia.slider.library.Animations.BaseAnimationInterface;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.Tricks.InfinitePagerAdapter;
import com.daimajia.slider.library.Tricks.InfiniteViewPager;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Transformers.BaseTransformer;
import com.daimajia.slider.library.Transformers.AccordionTransformer;
import com.daimajia.slider.library.Transformers.BackgroundToForegroundTransformer;
import com.daimajia.slider.library.Transformers.CubeInTransformer;
import com.daimajia.slider.library.Transformers.DefaultTransformer;
import com.daimajia.slider.library.Transformers.DepthPageTransformer;
import com.daimajia.slider.library.Transformers.FadeTransformer;
import com.daimajia.slider.library.Transformers.FlipHorizontalTransformer;
import com.daimajia.slider.library.Transformers.FlipPageViewTransformer;
import com.daimajia.slider.library.Transformers.ForegroundToBackgroundTransformer;
import com.daimajia.slider.library.Transformers.RotateDownTransformer;
import com.daimajia.slider.library.Transformers.RotateUpTransformer;
import com.daimajia.slider.library.Transformers.StackTransformer;
import com.daimajia.slider.library.Transformers.TabletTransformer;
import com.daimajia.slider.library.Transformers.ZoomInTransformer;
import com.daimajia.slider.library.Transformers.ZoomOutSlideTransformer;
import com.daimajia.slider.library.Transformers.ZoomOutTransformer;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.daimajia.slider.library.Tricks.FixedSpeedScroller;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

/**
 * SliderLayout is compound layout. This is combined with {@link com.daimajia.slider.library.Indicators.PagerIndicator}
 * and {@link com.daimajia.slider.library.Tricks.ViewPagerEx} .
 *
 * There is some properties you can set in XML:
 *
 * indicator_visibility
 *      visible
 *      invisible
 *
 * indicator_shape
 *      oval
 *      rect
 *
 * indicator_selected_color
 *
 * indicator_unselected_color
 *
 * indicator_selected_drawable
 *
 * indicator_unselected_drawable
 *
 * pager_animation
 *      Default
 *      Accordion
 *      Background2Foreground
 *      CubeIn
 *      DepthPage
 *      Fade
 *      FlipHorizontal
 *      FlipPage
 *      Foreground2Background
 *      RotateDown
 *      RotateUp
 *      Stack
 *      Tablet
 *      ZoomIn
 *      ZoomOutSlide
 *      ZoomOut
 *
 * pager_animation_span
 *
 *
 */
public class SliderLayout extends RelativeLayout{

    private Context mContext;
    /**
     * InfiniteViewPager is extended from ViewPagerEx. As the name says, it can scroll without bounder.
     */
    private InfiniteViewPager mViewPager;

    /**
     * InfiniteViewPager adapter.
     */
    private SliderAdapter mSliderAdapter;

    /**
     * {@link com.daimajia.slider.library.Tricks.ViewPagerEx} indicator.
     */
    private PagerIndicator mIndicator;

    /**
     * The indicator selected status drawable.
     */
    private int mSelectedDrawableId;

    /**
     * The indicator unselected status drawable.
     */
    private int mUnselectedDrawableId;

    /**
     * A timer and a TimerTask using to cycle the {@link com.daimajia.slider.library.Tricks.ViewPagerEx}.
     */
    private Timer mCycleTimer;
    private TimerTask mCycleTask;

    /**
     * For resuming the cycle, after user touch or click the {@link com.daimajia.slider.library.Tricks.ViewPagerEx}.
     */
    private Timer mResumingTimer;
    private TimerTask mResumingTask;

    /**
     * If {@link com.daimajia.slider.library.Tricks.ViewPagerEx} is Cycling
     */
    private boolean mCycling;

    /**
     * If auto recover after user touch the {@link com.daimajia.slider.library.Tricks.ViewPagerEx}
     */
    private boolean mAutoRecover;


    private int mTransformerId;

    /**
     * {@link com.daimajia.slider.library.Tricks.ViewPagerEx} transformer time span.
     */
    private int mTransformerSpan;

    /**
     * Visibility of {@link com.daimajia.slider.library.Indicators.PagerIndicator}
     */
    private IndicatorVisibility mIndicatorVisibility = IndicatorVisibility.Visible;

    /**
     * {@link com.daimajia.slider.library.Tricks.ViewPagerEx} 's transformer
     */
    private BaseTransformer mViewPagerTransformer;

    /**
     * @see com.daimajia.slider.library.Animations.BaseAnimationInterface
     */
    private BaseAnimationInterface mCustomAnimation;

    /**
     * indicator selected status color read from xml
     */
    private int mIndicatorSelectedColor;

    /**
     * indicator selected status color read from xml
     */
    private int mIndicatorUnSelectedColor;

    /**
     * {@link com.daimajia.slider.library.Indicators.PagerIndicator} shape, rect or oval.
     */
    private int mIndicatorShape;

    private enum Shape{
        Oval,Rectangle
    }

    public SliderLayout(Context context) {
        this(context,null);
    }

    public SliderLayout(Context context, AttributeSet attrs) {
        this(context,attrs,R.attr.imageSliderStyle);
    }

    public SliderLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.slider_layout, this, true);

        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs,R.styleable.SliderLayout,
                defStyle,0);

        mIndicatorShape = attributes.getInt(R.styleable.SliderLayout_indicator_shape,Shape.Oval.ordinal());

        mTransformerSpan = attributes.getInteger(R.styleable.SliderLayout_pager_animation_span,1100);
        mTransformerId = attributes.getInt(R.styleable.SliderLayout_pager_animation, Transformer.Default.ordinal());
        mUnselectedDrawableId = attributes.getResourceId(R.styleable.SliderLayout_indicator_selected_drawable, R.drawable.unselected_indicator);
        mSelectedDrawableId = attributes.getResourceId(R.styleable.SliderLayout_indicator_unselected_drawable,R.drawable.selected_indicator);

        mSliderAdapter = new SliderAdapter(mContext);

        mIndicatorSelectedColor = attributes.getColor(R.styleable.SliderLayout_indicator_selected_color, Color.WHITE);
        mIndicatorUnSelectedColor = attributes.getColor(R.styleable.SliderLayout_indicator_unselected_color,Color.argb(33,255,255,255));

        if(mSelectedDrawableId == R.drawable.selected_indicator){
            LayerDrawable selectedIndicatorDrawable = (LayerDrawable)context.getResources().getDrawable(mSelectedDrawableId);
            GradientDrawable shape = (GradientDrawable)selectedIndicatorDrawable.findDrawableByLayerId(R.id.shape);
            if(mIndicatorShape == Shape.Oval.ordinal()){
                shape.setShape(GradientDrawable.OVAL);
            }else{
                shape.setShape(GradientDrawable.RECTANGLE);
            }
            shape.setColor(mIndicatorSelectedColor);
        }
        if(mUnselectedDrawableId == R.drawable.unselected_indicator){
            LayerDrawable unselectedIndicatorDrawable = (LayerDrawable)context.getResources().getDrawable(mUnselectedDrawableId);
            GradientDrawable shape = (GradientDrawable) unselectedIndicatorDrawable.findDrawableByLayerId(R.id.shape);
            shape.setColor(mIndicatorUnSelectedColor);
            if(mIndicatorShape == Shape.Oval.ordinal()){
                shape.setShape(GradientDrawable.OVAL);
            }else{
                shape.setShape(GradientDrawable.RECTANGLE);
            }
            shape.setColor(mIndicatorUnSelectedColor);
        }

        PagerAdapter wrappedAdapter = new InfinitePagerAdapter(mSliderAdapter);

        mViewPager = (InfiniteViewPager)findViewById(R.id.daimajia_slider_viewpager);
        mViewPager.setAdapter(wrappedAdapter);
        setPagerIndicator((PagerIndicator)findViewById(R.id.default_center_bottom_indicator));

        int visibility = attributes.getInt(R.styleable.SliderLayout_indicator_visibility,0);
        if(visibility == 1){
            mIndicatorVisibility = IndicatorVisibility.Invisible;
            setIndicatorVisibility(mIndicatorVisibility);
        }

        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                     case MotionEvent.ACTION_UP:
                        recoverCycle();
                        break;
                }
                return false;
            }
        });

        attributes.recycle();

        setPresetTransformer(mTransformerId);
        setSliderTransformDuration(mTransformerSpan,null);
    }

    /**
     * set ViewPager indicator.
     * @param indicator
     */
    public void setPagerIndicator(PagerIndicator indicator){
        if(mIndicator != null){
            mSelectedDrawableId = mIndicator.getSelectedIndicatorResId();
            mUnselectedDrawableId = mIndicator.getUnSelectedIndicatorResId();
            mIndicator.clearSelf();
        }
        mIndicator = indicator;
        mIndicator.setViewPager(mViewPager);
        mIndicator.setIndicatorStyle(mSelectedDrawableId, mUnselectedDrawableId);

        setIndicatorVisibility(mIndicatorVisibility);

        mIndicator.redraw();
    }

    public <T extends BaseSliderView> void addSlider(T imageContent){
        mSliderAdapter.addSlider(imageContent);
    }

    public void startAutoCycle(){
        startAutoCycle(1000, 3400, true);
    }

    /**
     * start auto cycle.
     * @param delay delay time
     * @param period period time.
     * @param autoRecover
     */
    public void startAutoCycle(long delay,long period,boolean autoRecover){
        mCycleTimer = new Timer();
        mAutoRecover = autoRecover;
        mCycleTask = new TimerTask() {
            @Override
            public void run() {
                mh.sendEmptyMessage(0);
            }
        };
        mCycleTimer.schedule(mCycleTask,delay,period);
        mCycling = true;
    }

    /**
     * pause auto cycle.
     */
    private void pauseAutoCycle(){
        if(mCycling){
            mCycleTimer.cancel();
            mCycleTask.cancel();
            mCycling = false;
        }else{
            if(mResumingTimer != null && mResumingTask != null){
                recoverCycle();
            }
        }
    }

    /**
     * stop the auto circle
     */
    public void stopAutoCycle(){
        if(mCycleTask!=null){
            mCycleTask.cancel();
        }
        if(mCycleTimer!= null){
            mCycleTimer.cancel();
        }
        if(mResumingTimer!= null){
            mResumingTimer.cancel();
        }
        if(mResumingTask!=null){
            mResumingTask.cancel();
        }
    }

    /**
     * when paused cycle, this method can weak it up.
     */
    private void recoverCycle(){

        if(!mAutoRecover){
            return;
        }

        if(!mCycling){
            if(mResumingTask != null && mResumingTimer!= null){
                mResumingTimer.cancel();
                mResumingTask.cancel();
            }
            mResumingTimer = new Timer();
            mResumingTask = new TimerTask() {
                @Override
                public void run() {
                    startAutoCycle();
                }
            };
            mResumingTimer.schedule(mResumingTask,6000);
        }
    }

    private android.os.Handler mh = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mViewPager.nextItem();
        }
    };

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                pauseAutoCycle();
                break;
        }
        return false;
    }


    /**
     * set indicators selected and unselected style.
     * @param selectedDrawableRes
     * @param unselectedDrawableRes
     */
    public void setIndicatorStyle(int selectedDrawableRes,int unselectedDrawableRes){
        if(mIndicator != null)
            mIndicator.setIndicatorStyle(selectedDrawableRes, unselectedDrawableRes);
    }

    /**
     * set ViewPager transformer.
     * @param reverseDrawingOrder
     * @param transformer
     */
    public void setPagerTransformer(boolean reverseDrawingOrder,BaseTransformer transformer){
        mViewPagerTransformer = transformer;
        mViewPagerTransformer.setCustomAnimationInterface(mCustomAnimation);
        mViewPager.setPageTransformer(reverseDrawingOrder,mViewPagerTransformer);
    }

    /**
     * set the duration between two slider changes.
     * @param period
     * @param interpolator
     */
    public void setSliderTransformDuration(int period,Interpolator interpolator){
        try{
            Field mScroller = ViewPagerEx.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext(),interpolator, period);
            mScroller.set(mViewPager,scroller);
        }catch (Exception e){

        }
    }

    /**
     * preset transformers and their names
     */
    public enum Transformer{
        Default("Default"),
        Accordion("Accordion"),
        Background2Foreground("Background2Foreground"),
        CubeIn("CubeIn"),
        DepthPage("DepthPage"),
        Fade("Fade"),
        FlipHorizontal("FlipHorizontal"),
        FlipPage("FlipPage"),
        Foreground2Background("Foreground2Background"),
        RotateDown("RotateDown"),
        RotateUp("RotateUp"),
        Stack("Stack"),
        Tablet("Tablet"),
        ZoomIn("ZoomIn"),
        ZoomOutSlide("ZoomOutSlide"),
        ZoomOut("ZoomOut");

        private final String name;

        private Transformer(String s){
            name = s;
        }
        public String toString(){
            return name;
        }

        public boolean equals(String other){
            return (other == null)? false:name.equals(other);
        }
    };

    /**
     * set a preset viewpager transformer by id.
     * @param transformerId
     */
    public void setPresetTransformer(int transformerId){
        for(Transformer t : Transformer.values()){
            if(t.ordinal() == transformerId){
                setPresetTransformer(t);
                break;
            }
        }
    }

    /**
     * set preset PagerTransformer via the name of transforemer.
     * @param transformerName
     */
    public void setPresetTransformer(String transformerName){
        for(Transformer t : Transformer.values()){
            if(t.equals(transformerName)){
                setPresetTransformer(t);
                return;
            }
        }
    }

    /**
     * Inject your custom animation into PageTransformer, you can know more details in
     * {@link com.daimajia.slider.library.Animations.BaseAnimationInterface},
     * and you can see a example in {@link com.daimajia.slider.library.Animations.DescriptionAnimation}
     * @param animation
     */
    public void setCustomAnimation(BaseAnimationInterface animation){
        mCustomAnimation = animation;
        if(mViewPagerTransformer != null){
            mViewPagerTransformer.setCustomAnimationInterface(mCustomAnimation);
        }
    }

    /**
     * pretty much right? enjoy it. :-D
     *
     * @param ts
     */
    public void setPresetTransformer(Transformer ts){
        //
        // special thanks to https://github.com/ToxicBakery/ViewPagerTransforms
        //
        BaseTransformer t = null;
        switch (ts){
            case Default:
                t = new DefaultTransformer();
                break;
            case Accordion:
                t = new AccordionTransformer();
                break;
            case Background2Foreground:
                t = new BackgroundToForegroundTransformer();
                break;
            case CubeIn:
                t = new CubeInTransformer();
                break;
            case DepthPage:
                t = new DepthPageTransformer();
                break;
            case Fade:
                t = new FadeTransformer();
                break;
            case FlipHorizontal:
                t = new FlipHorizontalTransformer();
                break;
            case FlipPage:
                t = new FlipPageViewTransformer();
                break;
            case Foreground2Background:
                t = new ForegroundToBackgroundTransformer();
                break;
            case RotateDown:
                t = new RotateDownTransformer();
                break;
            case RotateUp:
                t = new RotateUpTransformer();
                break;
            case Stack:
                t = new StackTransformer();
                break;
            case Tablet:
                t = new TabletTransformer();
                break;
            case ZoomIn:
                t = new ZoomInTransformer();
                break;
            case ZoomOutSlide:
                t = new ZoomOutSlideTransformer();
                break;
            case ZoomOut:
                t = new ZoomOutTransformer();
                break;
        }
        setPagerTransformer(true,t);
    }

    public enum IndicatorVisibility{
        Visible,
        Invisible;
    };

    /**
     * Set the visibility of the indicators.
     * @param visibility
     */
    public void setIndicatorVisibility(IndicatorVisibility visibility){

        if(mIndicator == null){
            return;
        }

        if(visibility.equals(IndicatorVisibility.Visible)){
            mIndicator.setVisibility(VISIBLE);
        }else{
            mIndicator.setVisibility(INVISIBLE);
        }
    }
}
