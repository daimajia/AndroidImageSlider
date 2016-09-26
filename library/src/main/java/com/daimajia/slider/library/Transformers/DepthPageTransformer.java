package com.daimajia.slider.library.Transformers;

import android.view.View;

public class DepthPageTransformer extends BaseTransformer {

    private static final float MIN_SCALE = 0.75f;

    @Override
    protected void onTransform(View view, float position) {
        if (position <= 0f) {
            View.TRANSLATION_X.set(view, 0f);
            View.SCALE_X.set(view, 1f);
            View.SCALE_Y.set(view, 1f);
        } else if (position <= 1f) {
            final float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
            View.ALPHA.set(view, 1 - position);
            view.setPivotY(0.5f * view.getHeight());
            View.TRANSLATION_X.set(view, view.getWidth() * -position);
            View.SCALE_X.set(view, scaleFactor);
            View.SCALE_Y.set(view, scaleFactor);
        }
    }

    @Override
    protected boolean isPagingEnabled() {
        return true;
    }

}
