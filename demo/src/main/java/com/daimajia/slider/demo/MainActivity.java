package com.daimajia.slider.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.RenderTypes.BaseSliderView;
import com.daimajia.slider.library.RenderTypes.TextSliderView;
import com.daimajia.slider.library.SliderLayout;

import java.util.HashMap;


public class MainActivity extends ActionBarActivity implements BaseSliderView.OnSliderClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SliderLayout slider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal",R.drawable.hannibal);
        file_maps.put("Big Bang Theory",R.drawable.bigbang);
        file_maps.put("House of Cards",R.drawable.house);
        file_maps.put("Game of Thrones",R.drawable.game_of_thrones);

        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a sliderview
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .errorDisappear(true)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.getBundle()
                    .putString("extra",name);

            slider.addSlider(textSliderView);
        }
        slider.startCycle();
        slider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);

        ListView l = (ListView)findViewById(R.id.transformers);
        l.setAdapter(new TransformerAdapter(this));

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                slider.setPresetTransformer(((TextView)view).getText().toString());
                Toast.makeText(MainActivity.this,((TextView)view).getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

}
