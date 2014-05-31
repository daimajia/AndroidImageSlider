package com.daimajia.slider.library.Animations;

import android.view.View;

import com.daimajia.slider.library.R;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by daimajia on 14-5-31.
 */
public class DescriptionAnimation implements BaseAnimationInterface {

    private View mDescriptionView;

    @Override
    public void onPrepareCurrentItemLeaveScreen(View current) {
        View descriptionLayout = current.findViewById(R.id.description_layout);
        if(descriptionLayout!=null){
            current.findViewById(R.id.description_layout).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * When next item is coming to show, let's hide the description layout.
     * @param next
     */
    @Override
    public void onPrepareNextItemShowInScreen(View next) {
        View descriptionLayout = next.findViewById(R.id.description_layout);
        if(descriptionLayout!=null){
            next.findViewById(R.id.description_layout).setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onCurrentItemDisappear(View view) {

    }

    /**
     * When next item show in ViewPager, let's make an animation to show the
     * description layout.
     * @param view
     */
    @Override
    public void onNextItemAppear(View view) {

        View descriptionLayout = view.findViewById(R.id.description_layout);
        float layoutY = ViewHelper.getY(descriptionLayout);
        if(descriptionLayout!=null){
            view.findViewById(R.id.description_layout).setVisibility(View.VISIBLE);
            ValueAnimator animator = ObjectAnimator.ofFloat(
                    descriptionLayout,"y",layoutY + descriptionLayout.getHeight(),
                    layoutY).setDuration(500);
            animator.start();
        }

    }
}
