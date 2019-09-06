package com.daimajia.slider.library.SliderTypes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import com.daimajia.slider.library.R;

public class VideoSliderView extends BaseSliderView {

    public VideoSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.render_type_video,null);
        VideoView videoView = v.findViewById(R.id.daimajia_slider_video);
        ImageButton playIcon = v.findViewById(R.id.play_button);
        TextView description = v.findViewById(R.id.description);
        description.setText(getDescription());
        bindEventAndShow(v, videoView, playIcon);
        return v;
    }
}
