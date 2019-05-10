package com.will.weiyue.ui.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.will.weiyue.MyApp;
import com.will.weiyue.R;
import com.will.weiyue.component.ApplicationComponent;
import com.will.weiyue.ui.base.BaseActivity;
import com.will.weiyue.utils.DataCleanManager;
import com.will.weiyue.widget.ToastView;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.setting_back_btn)
    TextView mSettingBackBtn;
    @BindView(R.id.setting_login_password_item)
    TextView mSettingLoginPasswordItem;
    @BindView(R.id.setting_about_us_item)
    TextView mSettingAboutUsItem;
    @BindView(R.id.log_out_btn)
    Button mLogOutBtn;
    @BindView(R.id.clear_cache)
    RelativeLayout mClearCache;
    @BindView(R.id.cache_size)
    TextView mCacheSize;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_setting);
//        ButterKnife.bind(this);
//    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {

    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void initData() {
        try {
            String totalCacheSize = DataCleanManager.getTotalCacheSize(this);
            if (!TextUtils.isEmpty(totalCacheSize)) {
                mCacheSize.setText(totalCacheSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRetry() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @OnClick({R.id.setting_back_btn, R.id.setting_login_password_item, R.id.setting_about_us_item, R.id.log_out_btn, R.id.clear_cache})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.setting_back_btn:
                finish();
                break;
            case R.id.setting_login_password_item:
                UpdatePwdActivity.launch(this);
                break;
            case R.id.setting_about_us_item:
                AboutUsActivity.launch(this);
                break;
            case R.id.log_out_btn:
                MyApp.getInstance().logout();
                break;
            case R.id.clear_cache:
                DataCleanManager.clearAllCache(SettingActivity.this);
                ToastView.showToast(SettingActivity.this,"清除成功", Toast.LENGTH_SHORT);
                mCacheSize.setText("0K");
                break;
        }
    }
}
