package com.daimajia.slider.library.Transformers;

import android.view.View;

import com.nineoldandroids.view.ViewHelper;

public class FlipHorizontalTransformer extends BaseTransformer {

	@Override
	protected void onTransform(View view, float position) {
		final float rotation = 180f * position;
        ViewHelper.setAlpha(view,rotation > 90f || rotation < -90f ? 0 : 1);
        ViewHelper.setPivotY(view,view.getHeight()*0.5f);
		ViewHelper.setPivotX(view,view.getWidth() * 0.5f);
		ViewHelper.setRotationY(view,rotation);
	}

}
