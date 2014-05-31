package com.daimajia.slider.library.Animations;

import android.view.View;

/**
 * This interface gives you chance to set your own animation.
 * You can view class DescriptionAnimation as an example.
 */
public interface BaseAnimationInterface {

    /**
     * When the current item prepare to start leaving the screen.
     * @param current
     */
    public void onPrepareCurrentItemLeaveScreen(View current);

    /**
     * The next item which will be shown in ViewPager/
     * @param next
     */
    public void onPrepareNextItemShowInScreen(View next);

    /**
     * Current item totally disappear from screen.
     * @param view
     */
    public void onCurrentItemDisappear(View view);

    /**
     * Next item totally show in screen.
     * @param view
     */
    public void onNextItemAppear(View view);
}
