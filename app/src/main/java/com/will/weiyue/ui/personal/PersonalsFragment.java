package com.will.weiyue.ui.personal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.will.weiyue.R;
import com.will.weiyue.bean.User;
import com.will.weiyue.common.Constant;
import com.will.weiyue.component.ApplicationComponent;
import com.will.weiyue.ui.base.BaseFragment;
import com.will.weiyue.ui.setting.SettingActivity;
import com.will.weiyue.widget.CircleImageView;
import com.will.weiyue.widget.CusServiceDialog;

import org.litepal.util.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;


/**
 * desc: 个人页面
 * author: sdc .
 * date: 2019/5/8 .
 */
public class PersonalsFragment extends BaseFragment {

    private static final String TAG = "PersonalsFragment";
    @BindView(R.id.left_drawer_userIcon)
    CircleImageView mLeftDrawerUserIcon;
    @BindView(R.id.left_drawer_username)
    TextView mLeftDrawerUsername;
    @BindView(R.id.left_user_sign)
    TextView mLeftUserSign;
    @BindView(R.id.user_sex_icon)
    ImageView mUserSexIcon;

    @BindView(R.id.user_info_layout)
    RelativeLayout mUserInfoLayout;
//    @BindView(R.id.my_order_text)
//    TextView mMyOrderText;
//    @BindView(R.id.my_order_icon)
//    ImageView mMyOrderIcon;
//    @BindView(R.id.mine_order_item)
//    RelativeLayout mMineOrderItem;
    @BindView(R.id.my_doctor_text)
    TextView mMyDoctorText;
    @BindView(R.id.my_doctor_icon)
    ImageView mMyDoctorIcon;
    @BindView(R.id.mine_doctor_item)
    RelativeLayout mMineDoctorItem;
    @BindView(R.id.setting_text)
    TextView mSettingText;
    @BindView(R.id.setting_icon)
    ImageView mSettingIcon;
    @BindView(R.id.mine_setting_item)
    RelativeLayout mMineSettingItem;

    public static PersonalsFragment newInstance() {
        Bundle args = new Bundle();
        PersonalsFragment fragment = new PersonalsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.drawer_fmt_layout;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {

    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void initData() {
        if (BmobUser.isLogin()) {
            Log.e("zxz","已经登录");
            //注册广播
            IntentFilter filter = new IntentFilter() ;
            filter.addAction(Constant.USER_NICK_CHANGE);
            filter.addAction(Constant.USER_SIGN_CHANGE);
            filter.addAction(Constant.USER_AVATER_CHANGE);
            filter.addAction(Constant.USER_SEX_CHANGE);
            filter.addAction(Constant.USER_FANSNUM_CHANGE);
            filter.addAction(Constant.USER_FOCUSNUM_CHANGE);
            filter.addAction(Constant.USER_PAOPAONUM_CHANGE);
            filter.addAction(Constant.USER_LOCATION_CHANGE);
            getContext().registerReceiver(broadcastReceiver,filter) ;
            setViewData();
        } else {
            Log.e("zxz","尚未登录");
        }

    }

    @OnClick({R.id.left_drawer_userIcon,  R.id.mine_doctor_item, R.id.mine_setting_item})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.left_drawer_userIcon:
                Intent intent = new Intent(getContext(), PersonalActivity.class);
                startActivity(intent);
                break;

            case R.id.mine_doctor_item:
                CusServiceDialog cusServiceDialog = new CusServiceDialog();
                cusServiceDialog.show(getChildFragmentManager(),"");
                break;
            case R.id.mine_setting_item:
                Intent settingIntent = new Intent(getContext(), SettingActivity.class);
                startActivity(settingIntent);
                break;
        }
    }

    /**
     * 设置数据
     */
    public void setViewData(){

        User user = BmobUser.getCurrentUser(User.class);
        String avatarUrl = null;
        if(user != null){
            avatarUrl = user.getAvatarUrl();
            if(!TextUtils.isEmpty(avatarUrl)){
                Glide.with(getContext()).load(avatarUrl).apply(new RequestOptions().circleCrop()).into(mLeftDrawerUserIcon);
            }else {
                Glide.with(getContext()).load(R.drawable.user_icon_default_main).apply(new RequestOptions().circleCrop()).into(mLeftDrawerUserIcon);

            }
            mLeftDrawerUsername.setText(user.getNickname());
            String s = getResources().getString(R.string.user_sign)+" " ;
            if(TextUtils.isEmpty(user.getSign())){
                mLeftUserSign.setText(s);
            }else{
                mLeftUserSign.setText(s+user.getSign());
            }
            String sex = user.getSex();
            if(!TextUtils.isEmpty(sex)&&"女".equals(sex)){
                mUserSexIcon.setBackgroundResource(R.drawable.icon_sex_girl);
            }else{
                mUserSexIcon.setBackgroundResource(R.drawable.icon_sex_boy);
            }
        }
    }
    /**
     * 昵称或签名修改完成广播，更新页面数据
     */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction() ;
            LogUtil.d(TAG,"-----action drawer---"+action);
            // 更新签名
            if(action.equals(Constant.USER_SIGN_CHANGE)){
                User user = BmobUser.getCurrentUser(User.class) ;
                LogUtil.d(TAG,"-----action USER_SIGN_CHANGE---"+user.getSign());
                String s = getResources().getString(R.string.user_sign) +" " ;
                if(user.getSign() == null){
                    mLeftUserSign.setText("签名:");
                }else{
                    mLeftUserSign.setText(s+user.getSign());
                }
            }else if(action.equals(Constant.USER_NICK_CHANGE)){  //  更新昵称
                User user = BmobUser.getCurrentUser(User.class) ;
                LogUtil.d(TAG,"-----action USER_NICK_CHANGE---"+user.getNickname());
                mLeftDrawerUsername.setText(user.getNickname());
            }else if(action.equals(Constant.USER_AVATER_CHANGE)){  // 更新头像
                User user = BmobUser.getCurrentUser(User.class) ;
                Glide.with(getContext()).load(user.getAvatarUrl()).apply(new RequestOptions().circleCrop()).into(mLeftDrawerUserIcon);
            }else if(action.equals(Constant.USER_SEX_CHANGE)){    //  更新性别图标
                User user = BmobUser.getCurrentUser(User.class) ;
                if("女".equals(user.getSex())){
                    mUserSexIcon.setBackgroundResource(R.drawable.icon_sex_girl);
                }else{
                    mUserSexIcon.setBackgroundResource(R.drawable.icon_sex_boy);
                }
            }
        }
    } ;
}
