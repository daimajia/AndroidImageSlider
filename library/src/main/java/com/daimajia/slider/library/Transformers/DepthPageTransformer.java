package com.daimajia.slider.library.Transformers;

import android.support.v4.view.ViewCompat;
import android.view.View;


public class DepthPageTransformer extends BaseTransformer {

    private static final float MIN_SCALE = 0.75f;

    @Override
    protected void onTransform(View view, float position) {
        if (position <= 0f) {
            ViewCompat.setTranslationX(view, 0f);
            ViewCompat.setScaleX(view, 1f);
            ViewCompat.setScaleY(view, 1f);
        } else if (position <= 1f) {
            final float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
            ViewCompat.setAlpha(view, 1 - position);
            ViewCompat.setPivotY(view, 0.5f * view.getHeight());
            ViewCompat.setTranslationX(view, view.getWidth() * -position);
            ViewCompat.setScaleX(view, scaleFactor);
            ViewCompat.setScaleY(view, scaleFactor);
        }
    }

    @Override
    protected boolean isPagingEnabled() {
        return true;
    }

}
