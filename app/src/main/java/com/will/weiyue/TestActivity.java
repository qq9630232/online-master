package com.will.weiyue;

import android.os.Bundle;
import android.view.View;

import com.will.weiyue.component.ApplicationComponent;
import com.will.weiyue.ui.base.BaseActivity;

public class TestActivity extends BaseActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
//    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_test;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {

    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onRetry() {

    }
}
