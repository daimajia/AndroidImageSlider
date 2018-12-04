package com.daimajia.slider.library.Transformers;

import android.view.View;

public class CubeInTransformer extends BaseTransformer {

	@Override
	protected void onTransform(View view, float position) {
		// Rotate the fragment on the left or right edge
        view.setPivotX(position > 0 ? 0 : view.getWidth());
        view.setPivotY(0);
        view.setRotation(-90f * position);
	}

	@Override
	public boolean isPagingEnabled() {
		return true;
	}

}
