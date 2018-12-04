package com.daimajia.slider.library.Transformers;

import android.view.View;

public class StackTransformer extends BaseTransformer {

	@Override
	protected void onTransform(View view, float position) {
		view.setTranslationX(position < 0 ? 0f : -view.getWidth() * position);
	}

}
