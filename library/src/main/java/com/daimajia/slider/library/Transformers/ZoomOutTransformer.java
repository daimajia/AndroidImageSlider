package com.daimajia.slider.library.Transformers;

import android.view.View;

public class ZoomOutTransformer extends BaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        final float scale = 1f + Math.abs(position);
        View.SCALE_X.set(view, scale);
        View.SCALE_Y.set(view, scale);
        view.setPivotX(view.getWidth() * 0.5f);
        view.setPivotY(view.getWidth() * 0.5f);
        View.ALPHA.set(view, position < -1f || position > 1f ? 0f : 1f - (scale - 1f));
        if (position < -0.9) {
            //-0.9 to prevent a small bug
            View.TRANSLATION_X.set(view, view.getWidth() * position);
        }
    }

}