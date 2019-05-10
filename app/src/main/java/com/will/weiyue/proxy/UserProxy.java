package com.will.weiyue.proxy;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.will.weiyue.MyApp;
import com.will.weiyue.bean.User;
import com.will.weiyue.common.Constant;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by SDC on 2019/5/8.
 */

public class UserProxy {

    public static final String TAG = "UserProxy";
    private Context mContext;
    private ISignUpListener signUpLister;
    private ILoginListener loginListener;
    private IResetPasswordListener resetPasswordListener;
    public interface ISignUpListener {
        void onSignUpSuccess();

        void onSignUpFailure(int code,String msg);
    }
    public void setOnSignsUpListener(ISignUpListener signUpLister) {
        this.signUpLister = signUpLister;
    }
    public interface ILoginListener {
        void onLoginSuccess();

        void onLoginFailure(int code,String msg);
    }
    public void setOnLoginListener(ILoginListener loginListener) {
        this.loginListener = loginListener;
    }
    public interface IResetPasswordListener {
        void onResetSuccess();

        void onResetFailure(String msg);
    }

    public UserProxy(Context context) {
        this.mContext = context;
    }
    /**
     * @param email
     * @param password 注册
     */
    public void signUp(String email, String password) {
        User user = new User();
        user.setPassword(password);
        user.setEmail(email);
        user.setUsername(email);
        user.setSex("男");
        user.setAge("18");
        user.setAvatarUrl("");
        user.setAvatar(null);
        user.setNickname(email);
        user.setSign("");
        user.signUp( new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (signUpLister != null) {
                    signUpLister.onSignUpSuccess();
                } else {
                    Log.i(TAG, "signup listener is null,you must set one!");
                }
                if (e == null) {
                    if (signUpLister != null) {
                        signUpLister.onSignUpSuccess();
                    } else {
                        Log.i(TAG, "signup listener is null,you must set one!");
                    }
                } else {
                    if (signUpLister != null) {
                        signUpLister.onSignUpFailure(e.getErrorCode(),e.getMessage().toString());
                    } else {
                        Log.i(TAG, "signup listener is null,you must set one!");
                    }
                }
            }
        });
    }

    public void setOnSignUpListener(ISignUpListener signUpLister) {
        this.signUpLister = signUpLister;
    }

    /**
     * @param email
     * @param password 登录
     */
    public void login(String email, String password) {
        //此处替换为你的用户名密码
        BmobUser.loginByAccount(email, password, new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    if (loginListener != null) {
                        loginListener.onLoginSuccess();
                    } else {
                        Log.i(TAG, "login listener is null,you must set one!");
                    }
                } else {
                    if (loginListener != null) {
                        loginListener.onLoginFailure(e.getErrorCode(),e.getMessage().toString());
                    } else {
                        Log.i(TAG, "login listener is null,you must set one!");
                    }

                }
            }
        });


    }

    /**
     * 更新性别
     * @param sex
     */
    private void updateSex(String sex){
        // User user = BmobUser.getCurrentUser(context, User.class);
        User newUser = new User() ;
        newUser.setSex(sex);
        final User user = BmobUser.getCurrentUser(User.class);
        user.setSex("男");
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i(TAG, "更新性别信息成功。");
                // 发送修改性别的广播
                Intent intent = new Intent() ;
                intent.setAction(Constant.USER_SEX_CHANGE) ;
                MyApp.getInstance().sendBroadcast(intent);
                } else {
                    Log.e("error", e.getMessage());
                }
            }
        });
    }
}
