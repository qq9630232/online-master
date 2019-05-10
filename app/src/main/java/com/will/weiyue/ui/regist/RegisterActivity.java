package com.will.weiyue.ui.regist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.will.weiyue.R;
import com.will.weiyue.common.Constant;
import com.will.weiyue.proxy.UserProxy;
import com.will.weiyue.ui.login.LoginActivity;
import com.will.weiyue.utils.BitmapUtil;
import com.will.weiyue.utils.EncryptUtil;
import com.will.weiyue.utils.FileUtil;
import com.will.weiyue.utils.TextUtil;
import com.will.weiyue.widget.DeletableEditText;
import com.will.weiyue.widget.ToastView;

import org.litepal.util.LogUtil;

/**
 * author   YeMing(yeming_1001@163.com)
 * Date:    2015-01-26 20:53
 * version: V1.0
 * Description:  注册
 */
public class RegisterActivity extends Activity implements UserProxy.ISignUpListener{

    private static final String TAG = "RegisterActivity" ;
    private DeletableEditText userNameInput;
    private DeletableEditText userPwdInput,reUserPwdInput;
    private Button registerButton;
    private TextView loginLink;
    private ImageView backgroundImage;
    private Uri background = null;
    private Context context ;
    private String name,password,repassword ;
    private UserProxy userProxy ;  // 用户操作代理

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.register_layout);

//        MyApp.getInstance().addActivity(this);
        context = this ;

        userProxy = new UserProxy(context) ;
        //background = (Uri)getIntent().getParcelableExtra("background");

        initView();
        initListener();
    }

    /**
     * 初始化控件
     */
    public void initView(){

        userNameInput = (DeletableEditText) findViewById(R.id.user_name);
        userPwdInput = (DeletableEditText) findViewById(R.id.user_pwd);
        reUserPwdInput = (DeletableEditText) findViewById(R.id.re_user_pwd);
        registerButton = (Button) findViewById(R.id.registerButton);
        loginLink = (TextView) findViewById(R.id.login_link);
        backgroundImage = (ImageView) findViewById(R.id.register_backgroundImage);






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
     * 注册监听
     */
    public void initListener(){
        loginLink.setOnClickListener(loginLinkClick);
        registerButton.setOnClickListener(registerButtonClick);
        userProxy.setOnSignUpListener(this);
    }

    /**
     * 注册
     */
    View.OnClickListener registerButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            name = userNameInput.getText().toString();
            password = userPwdInput.getText().toString();
            repassword = reUserPwdInput.getText().toString();
            if(name.isEmpty() && password.isEmpty() && repassword.isEmpty()){
                userNameInput.setShakeAnimation();
                userPwdInput.setShakeAnimation();
                reUserPwdInput.setShakeAnimation();
             //   Toast.makeText(context, "请输入注册信息", Toast.LENGTH_SHORT).show();
                ToastView.showToast(context, "请输入注册信息", Toast.LENGTH_SHORT);
                return ;
            }
            if (name.isEmpty()) {
                userNameInput.setShakeAnimation();
            //    Toast.makeText(context,"邮箱不能为空",Toast.LENGTH_SHORT).show();
                ToastView.showToast(context, "邮箱不能为空", Toast.LENGTH_SHORT);
                return;
            }
            if (password.isEmpty()) {
                userPwdInput.setShakeAnimation();
             //   Toast.makeText(context,"密码不能为空",Toast.LENGTH_SHORT).show();
                ToastView.showToast(context, "密码不能为空", Toast.LENGTH_SHORT);
                return;
            }
            if (repassword.isEmpty()) {
                reUserPwdInput.setShakeAnimation();
            //    Toast.makeText(context,"确认密码不能为空",Toast.LENGTH_SHORT).show();
                ToastView.showToast(context, "确认密码不能为空", Toast.LENGTH_SHORT);
                return;
            }
            if(!TextUtil.isEmail(name)){
                userNameInput.setShakeAnimation();
            //    Toast.makeText(context,"邮箱格式错误",Toast.LENGTH_SHORT).show();
                ToastView.showToast(context, "邮箱格式错误", Toast.LENGTH_SHORT);
                return;
            }
            if(!password.equals(repassword)){
                reUserPwdInput.setShakeAnimation();
             //   Toast.makeText(context,"密码不一致",Toast.LENGTH_SHORT).show();
                ToastView.showToast(context, "密码不一致", Toast.LENGTH_SHORT);
                return;
            }

            // 加密
            String SHA_password = EncryptUtil.SHA1(password.trim()) ;
            LogUtil.d(TAG,"-------------SHA_password="+SHA_password);
            userProxy.signUp(name.trim(),SHA_password);
            loginLink.setClickable(false);
            registerButton.setClickable(false);
        }
    } ;

    /**
     * 跳转登录
     */
    View.OnClickListener loginLinkClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent() ;
            intent.setClass(context,LoginActivity.class) ;
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    } ;

    @Override
    public void onSignUpSuccess() {
        LogUtil.d(TAG,"-------SignUpSuccess-------");
        loginLink.setClickable(true);
        registerButton.setClickable(true);
        Intent intent = new Intent() ;
        intent.setClass(context,LoginActivity.class) ;
        intent.putExtra("username",name.trim()) ;
        intent.putExtra("password",password.trim()) ;
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        setResult(Constant.REGISTER_RESULT_CODE,intent);
        finish();
    }

    @Override
    public void onSignUpFailure(int code, String msg) {
        LogUtil.d(TAG,"-------SignUpFailure-------code="+code);
        LogUtil.d(TAG,"-------SignUpFailure-------"+msg);
        loginLink.setClickable(true);
        registerButton.setClickable(true);
        if(code == 9016){
            //The network is not available,please check your network!
            ToastView.showToast(context,"请检查网络连接", Toast.LENGTH_SHORT);
        }if(code == 202){
            //  用户名已经存在
            ToastView.showToast(context,"用户名已经存在", Toast.LENGTH_SHORT);
        }if(code == 203){
            //  邮箱已经存在
            ToastView.showToast(context,"邮箱已经存在", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
