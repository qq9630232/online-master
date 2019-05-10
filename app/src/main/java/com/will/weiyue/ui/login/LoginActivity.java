package com.will.weiyue.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.will.weiyue.MainActivity;
import com.will.weiyue.R;
import com.will.weiyue.bean.User;
import com.will.weiyue.common.Constant;
import com.will.weiyue.proxy.UserProxy;
import com.will.weiyue.ui.regist.RegisterActivity;
import com.will.weiyue.utils.BitmapUtil;
import com.will.weiyue.utils.EncryptUtil;
import com.will.weiyue.utils.FileUtil;
import com.will.weiyue.widget.CircleImageView;
import com.will.weiyue.widget.DeletableEditText;
import com.will.weiyue.widget.ToastView;

import org.litepal.util.LogUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * author   YeMing(yeming_1001@163.com)
 * Date:    2015-01-20 22:42
 * version: V1.0
 * Description:    登陆
 */
public class LoginActivity extends Activity implements UserProxy.ILoginListener {

    private static final String TAG = "LoginActivity" ;
    final float radius = 8;
    final double scaleFactor = 10;
//    DisplayImageOptions options = new DisplayImageOptions.Builder()
//            .showImageForEmptyUri(R.drawable.user_icon_default_main)
//            .showImageOnFail(R.drawable.user_icon_default_main)
//            .resetViewBeforeLoading(true)
//            .cacheOnDisc(true)
//            .imageScaleType(ImageScaleType.EXACTLY)
//            .bitmapConfig(Bitmap.Config.RGB_565)
//            .considerExifParams(true)
//            .displayer(new FadeInBitmapDisplayer(300))
//            .build();
    private CircleImageView userIcon;
    private DeletableEditText userNameInput;
    private DeletableEditText userPwdInput;
    private Button loginButton;
    private TextView registerLink;
    private ImageView backgroundImage;
    private Uri background = null;
    private Context context ;
    private User user ;            // 根据账户名查询的用户
    private UserProxy userProxy ;  // 用户操作代理
    private String userNameStr = null ;
    private Animation animation; // 登录icon 旋转 动画
//    private YmApplication ymApplication ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.login_layout);

        context = this ;
//        ymApplication = YmApplication.getInstance() ;
//        ymApplication.addActivity(this);
        background = (Uri)getIntent().getParcelableExtra("background");

        userProxy = new UserProxy(context) ;

        initView();
        initListener();

    //    LoadStartBackground loadStartBackground = new LoadStartBackground(getApplicationContext()) ;
    //   loadStartBackground.updateStartBackground();
    }

    /**
     *   初始化控件
     */
    public void initView() {

        userIcon = (CircleImageView) findViewById(R.id.userIcon);
        userNameInput = (DeletableEditText) findViewById(R.id.user_name_input);
        userPwdInput = (DeletableEditText) findViewById(R.id.user_pwd_input);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerLink = (TextView) findViewById(R.id.register_link);
        backgroundImage = (ImageView) findViewById(R.id.login_backgroundImage);
        // 默认头像
        //ImageLoader.getInstance().displayImage(null,userIcon,options);

//        // 字体风格
//        userNameInput.setTypeface(YmApplication.chineseTypeface);
//        userPwdInput.setTypeface(YmApplication.chineseTypeface);
//        loginButton.setTypeface(YmApplication.chineseTypeface);
//        registerLink.setTypeface(YmApplication.chineseTypeface);

        animation = AnimationUtils.loadAnimation(context,R.anim.login_icon) ;
        LinearInterpolator lin = new LinearInterpolator(); // 匀速旋转
        animation.setInterpolator(lin);

//        if (background == null) {
//            StartBackground startBackground = new LoadStartBackground(this).getStartBackground();
//            File file = startBackground.getCacheFile(this);
//            if (file.exists()) {
//                background = Uri.fromFile(file);
//            }
//        }

        BitmapDrawable bitmapDrawable;
        if (background == null) {
            bitmapDrawable = BitmapUtil.createBlur(getResources());
        } else {
            String path = FileUtil.getFilePathByUri(background) ;
            bitmapDrawable = BitmapUtil.createBlur(path,getResources());
        }
        backgroundImage.setImageDrawable(bitmapDrawable);
    }

    /**
     *   监听器注册
     */
    public void initListener(){
        loginButton.setOnClickListener(loginButtonClick);
        userPwdInput.setOnFocusChangeListener(userPwdFocus);
        userProxy.setOnLoginListener(this);
        registerLink.setOnClickListener(registerLinkClick);
    }

    /**
     * 登录监听
     */
    View.OnClickListener loginButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = userNameInput.getText().toString();
            String password = userPwdInput.getText().toString();

            if(name.isEmpty() && password.isEmpty()){
                userNameInput.setShakeAnimation();
                userPwdInput.setShakeAnimation();
                //Toast.makeText(LoginActivity.this,"请输入账户信息",Toast.LENGTH_SHORT).show();
                ToastView.showToast(context,"请输入账户信息", Toast.LENGTH_SHORT);
                return ;
            }

            if (name.isEmpty()) {
                userNameInput.setShakeAnimation();
            //    Toast.makeText(LoginActivity.this,"邮箱不能为空",Toast.LENGTH_SHORT).show();
                  ToastView.showToast(context,"邮箱不能为空", Toast.LENGTH_SHORT);
                return;
            }

            if (password.isEmpty()) {
                userPwdInput.setShakeAnimation();
            //    Toast.makeText(LoginActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                ToastView.showToast(context,"密码不能为空", Toast.LENGTH_SHORT);
                return;
            }
            LogUtil.d(TAG,"-----name="+name+"====---pwd="+password);
            //  开始登录动画
            if(animation != null){
                userIcon.startAnimation(animation);
            }
            // 加密
            String SHA_password = EncryptUtil.SHA1(password.trim()) ;
            LogUtil.d(TAG,"-------------SHA_password="+SHA_password);
            userProxy.login(name,SHA_password);  // 登录
            //登录时设置不可点击
            registerLink.setClickable(false);
            loginButton.setClickable(false);
        }
    };

    /**
     * 焦点监听器
     */
    View.OnFocusChangeListener userPwdFocus = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                String nameStr = userNameInput.getText().toString() ;
                if (userNameInput.getText().length() == 0 || nameStr.isEmpty()){
                    LogUtil.d(TAG,"-----"+"userName is null!");
                    //userIcon.setBackgroundResource(R.drawable.icon_user_monkey);
                      userIcon.setImageResource(R.drawable.user_icon_default_main);
                }else{
                    if(user != null ){
                        LogUtil.d(TAG,"-----user is not null!");
                        if(nameStr.equals(user.getEmail().toString())){
                            LogUtil.d(TAG,"-----userName is not change!");
                            return ;
                        }
                        LogUtil.d(TAG,"-----userName is change!");
                    }
                    LogUtil.d(TAG,"-----user is null!");
                    //LogUtil.d(TAG,"-----start time="+ Calendar.getInstance().getTimeInMillis());
                    LogUtil.d(TAG,"-----nameStr="+nameStr);
                    BmobQuery<User> query = new BmobQuery<User>() ;
                    query.addWhereEqualTo("username",nameStr) ;
                    query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> users, BmobException e) {
                            if(e==null){
                                LogUtil.d(TAG,"-------onSuccess---------"+users.size());
                                if(users.size() == 0){
                                    user = null ;
                                    //userIcon.setBackgroundResource(R.drawable.icon_user_monkey);
                                    //ImageLoader.getInstance().displayImage(null,userIcon,options);
                                    userIcon.setImageResource(R.drawable.user_icon_default_main);
                                    return ;
                                }
                                user = users.get(0) ;
                                if(null == user.getAvatar()){
                                    //ImageLoader.getInstance().displayImage(null,userIcon,options);
                                    userIcon.setImageResource(R.drawable.user_icon_default_main);
                                    return ;
                                }
                                //    String url = user.getAvatar().getFileUrl(context) ;
//                                String url = user.getAvatar() ;
                            }


                        }
//
//                        @Override
//                        public void onSuccess(List<User> users) {
//
//                        }
//
//                        @Override
//                        public void onError(int i, String s) {
//                            LogUtil.d(TAG,"-------onError---------"+s);
//                            user = null ;
//                            //userIcon.setBackgroundResource(R.drawable.icon_user_monkey);
//                            //ImageLoader.getInstance().displayImage(null,userIcon,options);
//                            userIcon.setImageResource(R.drawable.user_icon_default_main);
//                        }
                    });
                }
            }
        }
    } ;

    /**
     * 跳转注册
     */
    View.OnClickListener registerLinkClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent() ;
            intent.setClass(context,RegisterActivity.class) ;
            /*if (background != null) {
                intent.putExtra("background", background);
            }*/
            //startActivity(intent);
            startActivityForResult(intent, Constant.REGISTER_REQUEST_CODE);
        }
    } ;

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            updateLoginButton();
        }
    } ;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.d(TAG,"-------resultCode="+resultCode);
        switch (resultCode){
            case Constant.REGISTER_RESULT_CODE:
                String name = data.getStringExtra("username") ;
                String password = data.getStringExtra("password") ;
                userNameInput.setText(name);
                userPwdInput.setText(password);
                break ;
        }
    }

    /**
     * 登录按钮状态
     */
    public void updateLoginButton(){
        if (userNameInput.getText().length() == 0) {
            loginButton.setEnabled(false);
            return;
        }
        if (userPwdInput.getText().length() == 0) {
            loginButton.setEnabled(false);
            return;
        }
        loginButton.setEnabled(true);
    }

    /**
     * 登录成功回调
     */
    @Override
    public void onLoginSuccess() {
        LogUtil.d(TAG,"------Login Success-----");
    //    updateUserLocation() ;
        registerLink.setClickable(false);
        loginButton.setClickable(false);
        if(animation != null && animation.hasStarted()){
           // animation.cancel();
            userIcon.clearAnimation();
        }
        Intent intent = new Intent() ;
        intent.setClass(context,MainActivity.class) ;
        startActivity(intent);
        finish();
    }

    /**
     * @param msg
     * 登录失败回调
     */
    @Override
    public void onLoginFailure(int code,String msg) {
        LogUtil.d(TAG,"------Login Failure-----code="+code);
        LogUtil.d(TAG,"------Login Failure-----"+msg);
        registerLink.setClickable(true);
        loginButton.setClickable(true);
        if(code == 101){ //username or password incorrect.
            ToastView.showToast(context,"用户名或密码错误", Toast.LENGTH_SHORT);
        }else if(code == 9016){
            //The network is not available,please check your network!
            ToastView.showToast(context,"请检查网络连接", Toast.LENGTH_SHORT);
        }
        if(animation != null && animation.hasStarted()){
            userIcon.clearAnimation();
        }
    }
}
