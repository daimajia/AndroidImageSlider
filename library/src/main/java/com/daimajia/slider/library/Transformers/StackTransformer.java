package com.daimajia.slider.library.Transformers;

import android.support.v4.view.ViewCompat;
import android.view.View;


public class StackTransformer extends BaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        ViewCompat.setTranslationX(view, position < 0 ? 0f : -view.getWidth() * position);
    }

}
