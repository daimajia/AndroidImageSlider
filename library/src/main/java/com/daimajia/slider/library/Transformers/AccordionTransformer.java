package com.daimajia.slider.library.Transformers;

/**
 * Created by daimajia on 14-5-29.
 */

import android.support.v4.view.ViewCompat;
import android.view.View;


public class AccordionTransformer extends BaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        ViewCompat.setPivotX(view, position < 0 ? 0 : view.getWidth());
        ViewCompat.setScaleX(view, position < 0 ? 1f + position : 1f - position);
    }

}