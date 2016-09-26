package com.daimajia.slider.library.Transformers;

import android.os.Build;
import android.view.View;

import com.daimajia.slider.library.Tricks.ViewPagerEx;

public class FlipPageViewTransformer extends BaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        float percentage = 1 - Math.abs(position);
        if (Build.VERSION.SDK_INT >= 13) {
            view.setCameraDistance(12000);
        }
        setVisibility(view, position);
        setTranslation(view);
        setSize(view, position, percentage);
        setRotation(view, position, percentage);
    }

    private void setVisibility(View page, float position) {
        if (position < 0.5 && position > -0.5) {
            page.setVisibility(View.VISIBLE);
        } else {
            page.setVisibility(View.INVISIBLE);
        }
    }

    private void setTranslation(View view) {
        ViewPagerEx viewPager = (ViewPagerEx) view.getParent();
        float scroll = viewPager.getScrollX() - view.getLeft();
        View.TRANSLATION_X.set(view, scroll);
    }

    private void setSize(View view, float position, float percentage) {
        View.SCALE_X.set(view, (position != 0 && position != 1) ? percentage : 1);
        View.SCALE_Y.set(view, (position != 0 && position != 1) ? percentage : 1);
    }

    private void setRotation(View view, float position, float percentage) {
        View.ROTATION_Y.set(view, position > 0 ? -1 : 1 * 180 * (percentage + 1));
    }
}