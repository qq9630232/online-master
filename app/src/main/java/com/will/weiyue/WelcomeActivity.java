package com.will.weiyue;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.will.weiyue.bean.RequestBean;
import com.will.weiyue.bean.User;
import com.will.weiyue.component.ApplicationComponent;
import com.will.weiyue.ui.base.BaseActivity;
import com.will.weiyue.ui.login.LoginActivity;

import org.litepal.util.LogUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class WelcomeActivity extends BaseActivity  {
    private String url = "http://df0234.com:8081/?appId=";
    private String appId = "newab2019032803";
//    @BindView(R.id.gifImageView)
//    GifImageView gifImageView;
    @BindView(R.id.iv_ad)
    ImageView ivAd;
    @BindView(R.id.ll_bottom)
    RelativeLayout llBottom;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @BindView(R.id.fl_ad)
    FrameLayout flAd;
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private String apkUrl;


    @Override
    public int getContentLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {

    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
//        StatusBarUtil.setTranslucentForImageViewInFragment(WelcomeActivity.this, 0, null);

        //必应每日壁纸 来源于 https://www.dujin.org/fenxiang/jiaocheng/3618.html.
//        ImageLoaderUtil.LoadImage(this,getDrawable(R.mipmap.ic_launch), ivAd);
        Glide.with(this).load(R.mipmap.ic_launch).into(ivAd);

        mCompositeDisposable.add(countDown(3).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(@NonNull Disposable disposable) throws Exception {
                tvSkip.setText("跳过 4");
            }
        }).subscribeWith(new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer integer) {
                tvSkip.setText("跳过 " + (integer + 1));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
//                toMain();
//                next();
                load(url,appId);
            }
        }));
    }


    @Override
    protected void onDestroy() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
        super.onDestroy();
    }

    private void toMain() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
        Intent intent = new Intent();
        intent.setClass(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public Observable<Integer> countDown(int time) {
        if (time < 0) time = 0;
        final int countTime = time;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(@NonNull Long aLong) throws Exception {
                        return countTime - aLong.intValue();
                    }
                })
                .take(countTime + 1);
    }


    @OnClick(R.id.fl_ad)
    public void onViewClicked() {
//        toMain();
//        next();
        load(url,appId);

    }

    @Override
    public void initData() {
//        mPresenter.checkUpdate("1.0");
        if ( ContextCompat.checkSelfPermission(WelcomeActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                .PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(WelcomeActivity.this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager
                .PERMISSION_GRANTED) {
//            ElitaAiCameraActivity.start(activity, s);
        } else {
            //不具有获取权限，需要进行权限申请
            ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_PHONE_STATE}, 0);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0){
            if(grantResults!=null){
                if(grantResults.length>0) {
//                    startCameraAct(EncyclopediaActivity.this, CAMERA_INTENT_CODE);
                }
            }
        }
    }

    @Override
    public void onRetry() {

    }
    /**
     * 跳转
     */
    public void next() {
        Intent intent;
        User user = BmobUser.getCurrentUser( User.class);
        if (user == null) {
            intent = new Intent(this, LoginActivity.class);
        } else {
            LogUtil.d("zxz","----------"+user.getUsername());
            intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);
        this.finish();
    }


    public void load(String baseUrl,String appId){

        String url = baseUrl + appId;
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();


        Request request = new Request.Builder().url(url).get().build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
//                skipToMain();
                next();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                RequestBean requestBean = new Gson().fromJson(string, RequestBean.class);

                if(!TextUtils.isEmpty(string)){
                    String status = requestBean.getStatus();
                    if(status.equals("1")){

                        apkUrl = requestBean.getUrl();
                        Log.e("zxz",apkUrl);
                        WebActivity.startActivity(WelcomeActivity.this,apkUrl,appId);

                    }else {
                        next();
                    }
                }
            }
        });
    }


}
