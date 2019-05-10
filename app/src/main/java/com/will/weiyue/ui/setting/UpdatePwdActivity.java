package com.will.weiyue.ui.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.will.weiyue.R;
import com.will.weiyue.component.ApplicationComponent;
import com.will.weiyue.ui.base.BaseActivity;
import com.will.weiyue.widget.DeletableEditText;
import com.will.weiyue.widget.ToastView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class UpdatePwdActivity extends BaseActivity {
    @BindView(R.id.setting_back_btn)
    TextView mSettingBackBtn;
    @BindView(R.id.setting_login_old_password_et)
    DeletableEditText mSettingLoginOldPasswordEt;
    @BindView(R.id.setting_login_new_password_et)
    DeletableEditText mSettingLoginNewPasswordEt;
    @BindView(R.id.setting_login_password_next_btn)
    Button mSettingLoginPasswordNextBtn;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, UpdatePwdActivity.class);
        activity.startActivity(intent);
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_update_pwd);
//        ButterKnife.bind(this);
//    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_update_pwd;
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
    /**
     * 提供旧密码修改密码
     */
    private void updatePassword(String oldPwd,String newPwd){
        //TODO 此处替换为你的旧密码和新密码
        boolean login = BmobUser.isLogin();
        if(login){
            BmobUser.updateCurrentUserPassword(oldPwd, newPwd, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        ToastView.showToast(UpdatePwdActivity.this,"修改密码成功", Toast.LENGTH_SHORT);
                        finish();
                    } else if(e.getErrorCode()==210){
                        ToastView.showToast(UpdatePwdActivity.this,"原密码错误", Toast.LENGTH_SHORT);
                    }else {
                        ToastView.showToast(UpdatePwdActivity.this,"密码修改失败"+e.getMessage(), Toast.LENGTH_SHORT);
                    }
                }
            });
        }

    }
    @Override
    public void onRetry() {

    }

    @OnClick({R.id.setting_back_btn, R.id.setting_login_password_next_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.setting_back_btn:
                finish();
                break;
            case R.id.setting_login_password_next_btn:
                String pwd = mSettingLoginOldPasswordEt.getText().toString();
                String secondPwd = mSettingLoginNewPasswordEt.getText().toString();
                if(TextUtils.isEmpty(pwd)||TextUtils.isEmpty(secondPwd)){
                    ToastView.showToast(UpdatePwdActivity.this,"密码不能为空", Toast.LENGTH_SHORT);
                }else {
                    updatePassword(pwd,secondPwd);
                }
                break;
        }
    }
}
