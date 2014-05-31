package com.daimajia.slider.library.RenderTypes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.R;

/**
 * Created by daimajia on 14-5-28.
 */
public class TextSliderView extends BaseSliderView{
    public TextSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.slider_layout_text,null);
        ImageView target = (ImageView)v.findViewById(R.id.daimajia_slider_image);
        TextView description = (TextView)v.findViewById(R.id.description);
        description.setText(getDescription());
        loadImage(target);
        bindClickEvent(v);
        v.setTag(getDescription());
        return v;
    }
}
