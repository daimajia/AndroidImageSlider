package com.daimajia.slider.library.SliderTypes;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.VideoView;

import com.daimajia.slider.library.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;

/**
 * When you want to make your own slider view, you must extends from this class.
 * BaseSliderView provides some useful methods.
 * I provide two example: {@link DefaultSliderView} and
 * {@link TextSliderView}
 * if you want to show progressbar, you just need to set a progressbar id as @+id/loading_bar.
 */
public abstract class BaseSliderView {

    protected Context mContext;

    private Bundle mBundle;

    /**
     * Error place holder image.
     */
    private int mErrorPlaceHolderRes;

    /**
     * Empty imageView placeholder.
     */
    private int mEmptyPlaceHolderRes;

    private String mUrl;
    private File mFile;
    private int mRes;

    protected OnSliderClickListener mOnSliderClickListener;

    private boolean mErrorDisappear;

    private ImageLoadListener mLoadListener;

    private String mDescription;

    private Picasso mPicasso;

    /**
     * Scale type of the image.
     */
    private ScaleType mScaleType = ScaleType.Fit;

    private boolean videoLooping = true;
    private boolean videoIsPrepared = false;

    private boolean autoPlay = true;
    private int playButtonIcon = R.drawable.play_button;

    public enum ScaleType{
        CenterCrop, CenterInside, Fit, FitCenterCrop
    }

    protected BaseSliderView(Context context) {
        mContext = context;
    }

    /**
     * the placeholder image when loading image from url or file.
     * @param resId Image resource id
     * @return
     */
    public BaseSliderView empty(int resId){
        mEmptyPlaceHolderRes = resId;
        return this;
    }

    /**
     * determine whether remove the image which failed to download or load from file
     * @param disappear
     * @return
     */
    public BaseSliderView errorDisappear(boolean disappear){
        mErrorDisappear = disappear;
        return this;
    }

    /**
     * if you set errorDisappear false, this will set a error placeholder image.
     * @param resId image resource id
     * @return
     */
    public BaseSliderView error(int resId){
        mErrorPlaceHolderRes = resId;
        return this;
    }

    /**
     * the description of a slider image.
     * @param description
     * @return
     */
    public BaseSliderView description(String description){
        mDescription = description;
        return this;
    }

    /**
     * set a url as a image / video that preparing to load
     * @param url
     * @return
     */
    public BaseSliderView load(String url){
        if(mFile != null || mRes != 0){
            throw new IllegalStateException("Call multi load function," +
                    "you only have permission to call it once");
        }
        mUrl = url;
        return this;
    }

    /**
     * set a file as a image / video that will to load
     * @param file
     * @return
     */
    public BaseSliderView load(File file){
        if(mUrl != null || mRes != 0){
            throw new IllegalStateException("Call multi load function," +
                    "you only have permission to call it once");
        }
        mFile = file;
        return this;
    }

    /**
     * set a resource as a image / video that will to load
     * @param res
     * @return
     */
    public BaseSliderView load(int res){
        if(mUrl != null || mFile != null){
            throw new IllegalStateException("Call multi load function," +
                    "you only have permission to call it once");
        }
        mRes = res;
        return this;
    }

    /**
     * lets users add a bundle of additional information
     * @param bundle
     * @return
     */
    public BaseSliderView bundle(Bundle bundle){
        mBundle = bundle;
        return this;
    }

    public String getUrl(){
        return mUrl;
    }

    public boolean isErrorDisappear(){
        return mErrorDisappear;
    }

    public int getEmpty(){
        return mEmptyPlaceHolderRes;
    }

    public int getError(){
        return mErrorPlaceHolderRes;
    }

    public String getDescription(){
        return mDescription;
    }

    public Context getContext(){
        return mContext;
    }

    /**
     * set a slider image click listener
     * @param l
     * @return
     */
    public BaseSliderView setOnSliderClickListener(OnSliderClickListener l){
        mOnSliderClickListener = l;
        return this;
    }

    /**
     * When you want to implement your own slider view, please call this method in the end in `getView()` method
     * @param v the whole view
     * @param targetImageView where to place image
     */
    protected void bindEventAndShow(final View v, ImageView targetImageView){
        final BaseSliderView me = this;

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(mOnSliderClickListener != null){
                mOnSliderClickListener.onSliderClick(me);
            }
            }
        });

        if (targetImageView == null)
            return;

        if (mLoadListener != null) {
            mLoadListener.onStart(me);
        }

        Picasso p = (mPicasso != null) ? mPicasso : Picasso.with(mContext);
        RequestCreator rq = null;
        if(mUrl!=null){
            rq = p.load(mUrl);
        }else if(mFile != null){
            rq = p.load(mFile);
        }else if(mRes != 0){
            rq = p.load(mRes);
        }else{
            return;
        }

        if(rq == null){
            return;
        }

        if(getEmpty() != 0){
            rq.placeholder(getEmpty());
        }

        if(getError() != 0){
            rq.error(getError());
        }

        switch (mScaleType){
            case Fit:
                rq.fit();
                break;
            case CenterCrop:
                rq.fit().centerCrop();
                break;
            case CenterInside:
                rq.fit().centerInside();
                break;
        }

        rq.into(targetImageView,new Callback() {
            @Override
            public void onSuccess() {
                hideLoadingBar(v);

            }

            @Override
            public void onError() {
                if(mLoadListener != null){
                    mLoadListener.onEnd(false,me);
                }
                hideLoadingBar(v);
            }
        });
   }

    private void hideLoadingBar(View v) {
        if(v.findViewById(R.id.loading_bar) != null){
            v.findViewById(R.id.loading_bar).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * When you want to implement your own slider view, please call this method in the end in `getView()` method
     * @param v the whole view
     * @param videoView where to place video
     * @param playButton
     */
    protected void bindEventAndShow(final View v, VideoView videoView, ImageButton playButton){
        final BaseSliderView me = this;
        final VideoView finalVideoView = videoView;
        final ImageButton finalPlayButton = playButton;

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnSliderClickListener != null){
                    mOnSliderClickListener.onSliderClick(me);
                }
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoIsPrepared) {
                    finalVideoView.start();
                    finalPlayButton.setVisibility(View.GONE);
                }
            }
        });

        playButton.setBackgroundResource(playButtonIcon);

        if (videoView == null)
            return;

        if (mLoadListener != null) {
            mLoadListener.onStart(me);
        }

        Uri uri;

        if(mUrl!=null){
            uri = Uri.parse(mUrl);
        }else if(mFile != null){
            uri = Uri.parse(mFile.getAbsolutePath());
        }else if(mRes != 0){
            uri = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + mRes);
        }else{
            return;
        }

        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoIsPrepared = true;
                mp.setLooping(videoLooping);
                hideLoadingBar(v);

                if (autoPlay)
                    mp.start();
                else finalPlayButton.setVisibility(View.VISIBLE);

            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (!autoPlay) finalPlayButton.setVisibility(View.VISIBLE);
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if(mLoadListener != null){
                    mLoadListener.onEnd(false,me);
                }
                hideLoadingBar(v);
                return false;
            }
        });
    }

    public BaseSliderView setPlayButtonIcon(int playButtonIcon) {
        this.playButtonIcon = playButtonIcon;
        return this;
    }

    public int getPlayButtonIcon() {
        return playButtonIcon;
    }



    public BaseSliderView setScaleType(ScaleType type){
        mScaleType = type;
        return this;
    }

    public ScaleType getScaleType(){
        return mScaleType;
    }

    public BaseSliderView setVideoLooping(boolean looping){
        videoLooping = looping;
        return this;
    }

    public boolean getVideoLooping(){
        return videoLooping;
    }

    public boolean isAutoPlay() {
        return autoPlay;
    }

    public BaseSliderView setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
        return this;
    }

    /**
     * the extended class have to implement getView(), which is called by the adapter,
     * every extended class response to render their own view.
     * @return
     */
    public abstract View getView();

    /**
     * set a listener to get a message , if load error.
     * @param l
     */
    public void setOnImageLoadListener(ImageLoadListener l){
        mLoadListener = l;
    }

    public interface OnSliderClickListener {
        public void onSliderClick(BaseSliderView slider);
    }

    /**
     * when you have some extra information, please put it in this bundle.
     * @return
     */
    public Bundle getBundle(){
        return mBundle;
    }

    public interface ImageLoadListener{
        public void onStart(BaseSliderView target);
        public void onEnd(boolean result,BaseSliderView target);
    }

    /**
     * Get the last instance set via setPicasso(), or null if no user provided instance was set
     *
     * @return The current user-provided Picasso instance, or null if none
     */
    public Picasso getPicasso() {
        return mPicasso;
    }

    /**
     * Provide a Picasso instance to use when loading pictures, this is useful if you have a
     * particular HTTP cache you would like to share.
     *
     * @param picasso The Picasso instance to use, may be null to let the system use the default
     *                instance
     */
    public void setPicasso(Picasso picasso) {
        mPicasso = picasso;
    }
}
