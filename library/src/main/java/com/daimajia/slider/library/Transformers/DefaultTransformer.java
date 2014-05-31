package com.daimajia.slider.library.Transformers;

import android.view.View;

public class DefaultTransformer extends BaseTransformer {

	@Override
	protected void onTransform(View view, float position) {
	}

	@Override
	public boolean isPagingEnabled() {
		return true;
	}

}
