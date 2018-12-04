package com.daimajia.slider.library.Transformers;

import android.view.View;

/**
 * Created by realandylawton on 11/22/13.
 */
public class FadeTransformer extends BaseTransformer {

    @Override
    protected void onTransform(View view, float position) {

        // Page is not an immediate sibling, just make transparent
        if(position < -1 || position > 1) {
            view.setAlpha(0.6f);
        }
        // Page is sibling to left or right
        else if (position <= 0 || position <= 1) {

            // Calculate alpha.  Position is decimal in [-1,0] or [0,1]
            float alpha = (position <= 0) ? position + 1 : 1 - position;
            view.setAlpha(alpha);

        }
        // Page is active, make fully visible
        else if (position == 0) {
            view.setAlpha(1);
        }
    }

}