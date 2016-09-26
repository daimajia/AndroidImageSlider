package com.daimajia.slider.library.Transformers;

import android.view.View;

public class ZoomInTransformer extends BaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        final float scale = position < 0 ? position + 1f : Math.abs(1f - position);
        View.SCALE_X.set(view, scale);
        View.SCALE_Y.set(view, scale);
        view.setPivotX(view.getWidth() * 0.5f);
        view.setPivotY(view.getHeight() * 0.5f);
        View.ALPHA.set(view, position < -1f || position > 1f ? 0f : 1f - (scale - 1f));
    }

}
