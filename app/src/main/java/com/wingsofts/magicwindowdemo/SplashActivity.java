package com.wingsofts.magicwindowdemo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.zxinsight.MLink;
import com.zxinsight.MWConfiguration;
import com.zxinsight.MagicWindowSDK;
import com.zxinsight.mlink.MLinkCallback;
import com.zxinsight.mlink.MLinkIntentBuilder;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class SplashActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    //初始化SDK
    initSDK();

    //注册SDK
    registerLinks(this);
    Uri mLink = getIntent().getData();

    //如果从浏览器传来 则进行路由操作
    if(mLink != null){

      MLink.getInstance(this).router(this, mLink);
      finish();
    }else {
      //否则执行原本操作
      go2MainActivity();
    }
  }

  private void registerLinks(Context context) {
    MLink.getInstance(context).registerDefault(new MLinkCallback() {
      @Override
      public void execute(Map paramMap, Uri uri, Context context) {
        //默认的路由 如果没有匹配则转跳到 MainActivity 为你的首页
        MLinkIntentBuilder.buildIntent(paramMap, context, MainActivity.class);
      }
    });
    // testKey:  mLink 的 key, mLink的唯一标识,用于进行路由操作
    MLink.getInstance(context).register("testKey", new MLinkCallback() {
      public void execute(Map paramMap, Uri uri, Context context) {

        //!!!!!!!!注意 此处有坑,如果你的SplashActivity转跳有延迟，那么在此处转跳的延迟必须大于前者转跳时间

        Observable.timer(1050,TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(aVoid->{
              MLinkIntentBuilder.buildIntent(paramMap, context, DetailsActivity.class);

            });

      }
    });
  }

  private void initSDK() {
    MWConfiguration config = new MWConfiguration(this);
    //设置渠道，非必须（渠道推荐在AndroidManifest.xml内填写）
    config
        //开启Debug模式，显示Log，release时注意关闭
        .setDebugModel(true)
        //带有Fragment的页面。具体查看2.2.2
        . setPageTrackWithFragment(true)
        //设置分享方式，如果之前有集成sharesdk，可在此开启
        . setSharePlatform (MWConfiguration. ORIGINAL);
    MagicWindowSDK.initSDK(config);
  }

  private void go2MainActivity() {



    //延迟1秒转跳
    Observable.timer(1, TimeUnit.SECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(aLong -> {
          startActivity(new Intent(this,MainActivity.class));
          finish();
        });
  }
}
