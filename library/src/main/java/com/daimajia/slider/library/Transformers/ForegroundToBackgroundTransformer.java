package com.daimajia.slider.library.Transformers;

import android.support.v4.view.ViewCompat;
import android.view.View;


public class ForegroundToBackgroundTransformer extends BaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        final float height = view.getHeight();
        final float width = view.getWidth();
        final float scale = min(position > 0 ? 1f : Math.abs(1f + position), 0.5f);

        ViewCompat.setScaleX(view, scale);
        ViewCompat.setScaleY(view, scale);
        ViewCompat.setPivotX(view, width * 0.5f);
        ViewCompat.setPivotY(view, height * 0.5f);
        ViewCompat.setTranslationX(view, position > 0 ? width * position : -width * position * 0.25f);
    }

    private static final float min(float val, float min) {
        return val < min ? min : val;
    }

}
