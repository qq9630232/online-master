package com.will.weiyue.ui.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.will.weiyue.R;
import com.will.weiyue.component.ApplicationComponent;
import com.will.weiyue.ui.base.BaseActivity;
import com.will.weiyue.widget.ToastView;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.title_widget_back_iv)
    ImageView mTitleWidgetBackIv;
    @BindView(R.id.title_widget_title_tv)
    TextView mTitleWidgetTitleTv;
    @BindView(R.id.commont_title_left)
    RelativeLayout mCommontTitleLeft;
    @BindView(R.id.commont_title_left_rl)
    RelativeLayout mCommontTitleLeftRl;
    @BindView(R.id.version_btn)
    Button mVersionBtn;
    @BindView(R.id.check_for_update_tv)
    TextView mCheckForUpdateTv;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_about_us);
//        ButterKnife.bind(this);
//    }
    public static void launch(Activity activity){
        Intent intent = new Intent(activity, AboutUsActivity.class);
        activity.startActivity(intent);
    }
    @Override
    public int getContentLayout() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {

    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void initData() {
        mTitleWidgetTitleTv.setText("关于我们");
    }

    @Override
    public void onRetry() {

    }

    @OnClick({R.id.title_widget_back_iv, R.id.title_widget_title_tv, R.id.check_for_update_tv})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.title_widget_back_iv:
                finish();
                break;
            case R.id.title_widget_title_tv:
                finish();
                break;
            case R.id.check_for_update_tv:
                ToastView.showToast(this,"当前已经是最新版本!", Toast.LENGTH_SHORT);
                break;
        }
    }
}
