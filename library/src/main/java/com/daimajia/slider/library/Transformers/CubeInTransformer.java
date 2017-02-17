package com.daimajia.slider.library.Transformers;

import android.support.v4.view.ViewCompat;
import android.view.View;


public class CubeInTransformer extends BaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        // Rotate the fragment on the left or right edge
        ViewCompat.setPivotX(view, position > 0 ? 0 : view.getWidth());
        ViewCompat.setPivotY(view, 0);
        ViewCompat.setRotation(view, -90f * position);
    }

    @Override
    public boolean isPagingEnabled() {
        return true;
    }

}
