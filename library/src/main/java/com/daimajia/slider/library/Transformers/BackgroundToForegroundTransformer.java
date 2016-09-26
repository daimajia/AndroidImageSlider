package com.daimajia.slider.library.Transformers;

import android.view.View;

public class BackgroundToForegroundTransformer extends BaseTransformer {

    private static final float min(float val, float min) {
        return val < min ? min : val;
    }

    @Override
    protected void onTransform(View view, float position) {
        final float height = view.getHeight();
        final float width = view.getWidth();
        final float scale = min(position < 0 ? 1f : Math.abs(1f - position), 0.5f);

        View.SCALE_X.set(view, scale);
        View.SCALE_Y.set(view, scale);
        view.setPivotX(width * 0.5f);
        view.setPivotY(height * 0.5f);
        View.TRANSLATION_X.set(view, position < 0 ? width * position : -width * position * 0.25f);
    }

}
