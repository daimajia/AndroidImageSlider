package com.daimajia.slider.library.SliderTypes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.slider.library.R;

/**
 * a simple slider view to show images with background as blur of the original image so that images 
 *  with fitCenterCrop scale do not show empty space in imageview as white which obviously does not look good.
 *
 * just extend BaseSliderView, and implement getView() method.
 */
public class BlurBackgroundSliderView extends BaseSliderView{

    private Drawable backgroundDrawable;
   
    public BlurBackgroundSliderView(Context context, Drawable blurredBackground) {
        super(context);
        backgroundDrawable = blurredBackground;
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.render_type_default,null);
        ImageView target = (ImageView)v.findViewById(R.id.daimajia_slider_image);
        
        //setting blurredBackground as background of target imageView
        target.setBackground(backgroundDrawable);
        
        bindEventAndShow(v, target);
        return v;
    }
}
