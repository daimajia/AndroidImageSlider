package com.daimajia.slider.library.Transformers;

import android.view.View;

public class FlipHorizontalTransformer extends BaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        final float rotation = 180f * position;
        View.ALPHA.set(view, rotation > 90f || rotation < -90f ? 0 : 1f);
        view.setPivotY(view.getHeight() * 0.5f);
        view.setPivotX(view.getWidth() * 0.5f);
        View.ROTATION_Y.set(view, rotation);
    }

}
