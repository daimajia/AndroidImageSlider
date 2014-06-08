#Android Image Slider
 
This is an amazing image slider in Android platform. I decide to open source this because there is really not an attractive,and convenient slide widget in Android.
 
You can easily load image from net,drawable or file. And there are many kind of amazing animations you can choose. :-D
 
##Demo
 
![](http://ww3.sinaimg.cn/mw690/610dc034jw1egzor66ojdg20950fknpe.gif)

[Download Apk](http://jmp.sh/K3mBLCy)
 
##Usage

[Wiki](https://github.com/daimajia/AndroidImageSlider/wiki) is under construction. :-D

Step1: add dependencies in build.gradle.

```groovy
	dependencies {
	   compile 'com.daimajia.slider:library:1.0@aar'
	}
```

Step2: add `storage` and `internet` permission.

```xml
<uses-permission android:name="android.permission.INTERNET" /> 
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

Step3: add layout in your xml.
 
```java
<com.daimajia.slider.library.SliderLayout
        android:id="@+id/slider"
        android:layout_width="match_parent"
        android:layout_height="200dp"
/>
```        
 
There are some default indicators, while,if you want to custom your own indicator:
 
```java
<com.daimajia.slider.library.Indicators.PagerIndicator
        android:id="@+id/custom_indicator"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:gravity="center"
        />
```
 
====
 
##Some Details
 
###SliderLayout
 
`SliderLayout` is a compound layout, contains:
 
- `PagerIndicator`		:	actually extends from LinearLayout),
- `InfiniteViewPager`  	: 	a wrapper of `ViewPager`
- `BaseTransformer`		: 	a slider transformer instance, this can be used to implement effects transform.
- `BaseAnimationInterface`: user custom animation.
 
####Properties:
 
- Indicator
	- `indicator_visibility` : visibile | invisible
	- `auto_cycle` : true | false
	- `pager_animation`: Default | Accordion | Background2Foreground | CubeIn | DepthPage | Fade | FlipHorizontal | FlipPage | Foreground2Background | RotateDown | RotateUp | Stack | Tablet | ZoomIn | ZoomOutSlide | ZoomOut 
	- `pager_animation_span` : the slider animation time length. integer, milliseconds
 
 
###PagerIndicator
 
PagerIndicator is extended from LinearLayout. It is bind with a adapter, detect the adapter item count by `adapter.getCount()` , and then add `ImageView` into the LinearLayout.
 
There are two kind indicators, `selected` status and `unselected` status.
 
- Both properties:
        
   	- `shape` : oval(default) | rect , the shape of indicator (if you set your custom drawable, then this will not work).
    - `visibility` visibile | invisible
 
- Selected indicator's properties:
	- `selected_drawable` : if you set this drawable, then the below settings will not work.
	- `selected_color`
	- `selected_width`
	- `selected_height`
	- `selected_padding_left`
	- `selected_padding_right`
	- `selected_padding_top`
	- `selected_padding_bottom`
 
- Unselected indicator properties:
	- `unselected_drawable` : if you set this drawable, then the below settings will not work.
	- `unselected_color`	
	- `unselected_width`
	- `unselected_height`
	- `unselected_padding_left`
	- `unselected_padding_right`
	- `unselected_padding_top`
	- `unselected_padding_bottom`
 
###BaseTransformer
 
A [PageTransformer]((http://stuff.mit.edu/afs/sipb/project/android/docs/reference/android/support/v4/view/ViewPager.PageTransformer.html) ) is invoked whenever a visible/attached page is scrolled. You can add slide animation by `extends BaseTransformer`. And add your animation effect into `onTransform()` method. I supply a lot of [examples](./library/src/main/java/com/daimajia/slider/library/Transformers), if you are not so clear on `PageTransformer`, you can learn from them, and try to make your own.
 
###BaseAnimationInterface
 
If you need to manipulate your slide view（for example: add an animation to a target view, when slide animation is finished）, to make it simple, I made an interface to help you to concentrate on the view that are animating. You can see [the example](./library/src/main/java/com/daimajia/slider/library/Animations/DescriptionAnimation.java) if you want to make your own.
 
 
##About me
 
A student in China mainland. I love Google, love Android, love everything that interesting. If you get any problems when using this library or you have an internship opportunity, please feel relax to [email me](mailto:daimajia@gmail.com). :-D