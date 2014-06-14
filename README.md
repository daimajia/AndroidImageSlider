#Android Image Slider [![Build Status](https://travis-ci.org/daimajia/AndroidImageSlider.svg)](https://travis-ci.org/daimajia/AndroidImageSlider)
 
This is an amazing image slider in Android platform. I decide to open source this because there is really not an attractive, and convenient slide widget in Android.
 
You can easily load image from net, drawable or file. And there are many kind of amazing animations you can choose. :-D
 
##Demo
 
![](http://ww3.sinaimg.cn/mw690/610dc034jw1egzor66ojdg20950fknpe.gif)

[Download Apk](http://jmp.sh/K3mBLCy)
 
##Usage

Step1:

If you are using Android Studio, just add a one line code to your project root `build.gradle`.

```groovy
dependencies{
    compile 'com.daimajia.slider:library:1.0.4@aar'
}
```


If you are using maven to manage your project, add below to your `pom.xml`

```xml
<dependency>
    <groupId>com.daimajia.slider</groupId>
    <artifactId>library</artifactId>
    <version>1.0.4</version>
    <type>apklib</type>
</dependency>
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
 
There are some default indicators, while, if you want to custom your own indicator:
 
```java
<com.daimajia.slider.library.Indicators.PagerIndicator
        android:id="@+id/custom_indicator"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:gravity="center"
        />
```

[Code example](https://github.com/daimajia/AndroidImageSlider/blob/master/demo%2Fsrc%2Fmain%2Fjava%2Fcom%2Fdaimajia%2Fslider%2Fdemo%2FMainActivity.java)
 
====
 
##Advanced usage

Please visit [Wiki](https://github.com/daimajia/AndroidImageSlider/wiki)
 
##About me
 
A student in China mainland. I love Google, love Android, love everything that is interesting. If you get any problems when using this library or you have an internship opportunity, please feel relax to [email me](mailto:daimajia@gmail.com). :-D
