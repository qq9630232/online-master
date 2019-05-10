package com.will.weiyue.ui.personal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.will.weiyue.R;
import com.will.weiyue.bean.User;
import com.will.weiyue.common.Constant;
import com.will.weiyue.component.ApplicationComponent;
import com.will.weiyue.ui.base.BaseActivity;
import com.will.weiyue.utils.CustomDialog;
import com.will.weiyue.utils.FileUtil;
import com.will.weiyue.widget.DialogView;
import com.will.weiyue.widget.Rom;
import com.will.weiyue.widget.ToastView;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class PersonalActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "PersonalActivity";
    @BindView(R.id.user_icon_tips)
    TextView mUserIconTips;
    @BindView(R.id.user_icon_image)
    ImageView mUserIconImage;
    @BindView(R.id.user_icon)
    RelativeLayout mUserIcon;
    @BindView(R.id.user_nick_tips)
    TextView mUserNickTips;
    @BindView(R.id.user_nick_text)
    TextView mUserNickText;
    @BindView(R.id.user_nick)
    RelativeLayout mUserNick;
    @BindView(R.id.user_fans_tips)
    TextView mUserAgeTips;
    @BindView(R.id.user_fans_text)
    TextView mUserAgeText;
    @BindView(R.id.user_fans)
    RelativeLayout mUserFans;
    @BindView(R.id.sex_choice_tips)
    TextView mSexChoiceTips;
    @BindView(R.id.sex_choice_switch)
    CheckBox mSexChoiceSwitch;
    @BindView(R.id.sex_choice)
    RelativeLayout mSexChoice;
    @BindView(R.id.user_sign_tips)
    TextView mUserSignTips;
    @BindView(R.id.user_sign_text)
    TextView mUserSignText;
    @BindView(R.id.user_sign)
    RelativeLayout mUserSign;
    @BindView(R.id.user_set)
    Button mUserSet;
    private Dialog editDialog;
    private User user = null ;
    private Uri fileUri;
    private Uri fileCropUri;
    private Dialog loadDialog;
    private final int RESULT_REQUEST_PHOTO = 1005;
    private final int RESULT_REQUEST_PHOTO_CROP = 1006;
    @Override
    public int getContentLayout() {
        return R.layout.edit_userinfo_layout;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {

    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void initData() {
//        permission();
        loadDialog = DialogView.loadDialog(this, R.string.saveing) ;
        user = BmobUser.getCurrentUser(User.class);
        setData();
        mSexChoiceSwitch.setOnCheckedChangeListener(this);

    }

    private void setData() {
        String sign = user.getSign() ;
        String avatarUrl = null;
        if(user != null){
            /*if(user.getAvatar()!=null){
                avatarUrl = user.getAvatar().getFileUrl(this) ;
            }*/
            avatarUrl = user.getAvatarUrl() ;
            if(!TextUtils.isEmpty(avatarUrl)){
                Glide.with(this).load(avatarUrl).apply(new RequestOptions().circleCrop()).into(mUserIconImage);
            }else {
                Glide.with(this).load(R.drawable.user_icon_default_main).apply(new RequestOptions().circleCrop()).into(mUserIconImage);
            }
            mUserNickText.setText(user.getNickname());
            if(sign != null && !"".equals(sign)){
                mUserSignText.setText(sign);
            }
            if("男".equals(user.getSex())){
                mSexChoiceSwitch.setChecked(true);
            }else if("女".equals(user.getSex())){
                mSexChoiceSwitch.setChecked(false);
            }
            mUserAgeText.setText(user.getAge()+"");
        }
    }
    private void permission(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Rom.isVivo()) {
                showPicChoiceFormDialog();
            } else {
                if (ContextCompat.checkSelfPermission(PersonalActivity.this, Manifest.permission.CAMERA) == PackageManager
                        .PERMISSION_GRANTED && ContextCompat.checkSelfPermission(PersonalActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                        .PERMISSION_GRANTED) {
                    showPicChoiceFormDialog();
                } else {
                    //不具有获取权限，需要进行权限申请
                    ActivityCompat.requestPermissions(PersonalActivity.this, new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0){
            if(grantResults!=null){
                if(grantResults.length>0) {
                     showPicChoiceFormDialog();
                }
            }
        }
    }

    @Override
    public void onRetry() {

    }

    @OnClick({R.id.user_icon, R.id.user_nick, R.id.user_fans,  R.id.user_sign, R.id.user_set})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.user_icon:
                permission();
                break;
            case R.id.user_nick:
                showEditDialog("编辑昵称",0);
                break;
            case R.id.user_fans:
                showEditDialog("编辑年龄",1);
                break;
            case R.id.user_sign:
                showEditDialog("编辑签名",2);
                break;
            case R.id.user_set:
                finish();
                break;
        }
    }
    /**
     * 相机 或 图库 选择框
     */
    @SuppressLint("ResourceAsColor")
    private void showPicChoiceFormDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("更换头像")
                .setItems(R.array.camera_gallery, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            camera();
                        } else {
                            photo();
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        CustomDialog.dialogTitleLineColor(this, dialog);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case Constant.EDIT_SIGN_REQUEST_CODE:   //  从编辑签名页 返回
                    String signText = data.getStringExtra("sign") ;
                    if(signText == null){
                        mUserSignText.setText(user.getSign());
                    }else{
                        // 发送修改昵称的广播
                        Intent intent = new Intent() ;
                        intent.setAction(Constant.USER_SIGN_CHANGE) ;
                        sendBroadcast(intent);
                        mUserSignText.setText(signText);
                    }

                    break ;
                case RESULT_REQUEST_PHOTO:
                    if (data != null) {
                        fileUri = data.getData();
                    }
                    fileCropUri = FileUtil.getOutputMediaFileUri();
                    cropImageUri(fileUri, fileCropUri, 640, 640, RESULT_REQUEST_PHOTO_CROP);
                    break ;
                case RESULT_REQUEST_PHOTO_CROP:
                    loadDialog.show();
                    String filePath = FileUtil.getFilePathByUri(fileCropUri) ;
                    //  上传头像图片
                    pushPic(filePath);
                    break ;
                default:
                    break ;
            }
        }
    }
    /**
     * 上传头像
     * @param picPath
     */
    private void pushPic(String picPath){
        BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    String fileUrl = bmobFile.getFileUrl();
                    updatePic(fileUrl);
                }else{
                    ToastView.showToast(PersonalActivity.this,"上传文件失败:" + bmobFile.getFileUrl(),Toast.LENGTH_SHORT);
                    loadDialog.dismiss();

                }

            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }
    /**
     * 更新头像
     * @param url
     */
    private void updatePic(String url){
        User newUser = new User() ;
        newUser.setAvatarUrl(url);
        newUser.update(user.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i(TAG, "更新头像信息成功.");
                    // 发送修改头像的广播
                    Intent intent = new Intent() ;
                    intent.setAction(Constant.USER_AVATER_CHANGE) ;
                    sendBroadcast(intent);

                    user = BmobUser.getCurrentUser(User.class) ;
                    Glide.with(PersonalActivity.this).load(url).apply(new RequestOptions().circleCrop()).into(mUserIconImage);
                    ToastView.showToast(PersonalActivity.this,"上传头像成功:" + url,Toast.LENGTH_SHORT);
                }else{
                    ToastView.showToast(PersonalActivity.this,"上传头像失败!",Toast.LENGTH_SHORT);
                }

            }
        });
        loadDialog.dismiss();
    }

    /**
     * 编辑框
     */
    private void showEditDialog(String msg,int type){
        editDialog = new Dialog(this,R.style.mydialog) ;
        editDialog.setCanceledOnTouchOutside(false);
        View v = LayoutInflater.from(this).inflate(R.layout.edit_dialog_layout, null);
        editDialog.getWindow().setContentView(v);
        final EditText editNick = (EditText) v.findViewById(R.id.edit_nick);
        switch (type){
            case 0:
                editNick.setInputType(InputType.TYPE_CLASS_TEXT);
                editNick.setText(mUserNickText.getText());
                break;
            case 1:
                editNick.setInputType(InputType.TYPE_CLASS_NUMBER);
                editNick.setText(mUserAgeText.getText());
                break;
            case 2:
                editNick.setInputType(InputType.TYPE_CLASS_TEXT);
                editNick.setText(mUserSignText.getText());
                break;
        }
        TextView titleTip = (TextView) v.findViewById(R.id.titleTip);
        titleTip.setText(msg);
        Button setCancle = (Button) v.findViewById(R.id.set_cancle);
        Button setEnsure = (Button) v.findViewById(R.id.set_ensure);

        setCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //        softInput();
                editDialog.dismiss();

            }
        });
        setEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickText = editNick.getText().toString() ;
                switch (type){
                    case 0:
                        editNick.setInputType(InputType.TYPE_CLASS_TEXT);
                        // 更新昵称
                        updateNick(nickText);
                        break;
                    case 1:
                        editNick.setInputType(InputType.TYPE_CLASS_NUMBER);
                        // 更新年龄
                        updateAge(nickText);
                        break;
                    case 2:
                        editNick.setInputType(InputType.TYPE_CLASS_TEXT);
                        // 更新签名
                        updateSign(nickText);
                        break;
                }
            }
        });

        editDialog.getWindow().setGravity(Gravity.CENTER);
        editDialog.show();
        //设置自定义高度，在布局edit_dialog_layout中设置layout_width宽度无效。需使用下面代码设置dialog宽度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = editDialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth() - 50); //设置宽度
        editDialog.getWindow().setAttributes(lp);

    }
    /**
     * 更新nick
     * @param nick
     */
    private void updateNick(final String nick){
        // User user = BmobUser.getCurrentUser(context, User.class);
        if(user.getNickname().equals(nick)){
            editDialog.dismiss();
            return ;
        }
        User newUser = new User() ;
        newUser.setNickname(nick);
        newUser.update(user.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Intent intent = new Intent() ;
                    intent.setAction(Constant.USER_NICK_CHANGE) ;
                    sendBroadcast(intent);
                    mUserNickText.setText(nick);
                }else {
                    ToastView.showToast(PersonalActivity.this,"昵称更新失败", Toast.LENGTH_SHORT);
                }
            }
        });
        //    softInput();
        editDialog.dismiss();
    }
    /**
     * 更新年龄
     * @param nick
     */
    private void updateAge(final String  nick){
        User newUser = new User() ;
        newUser.setAge(nick);
        newUser.update(user.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Intent intent = new Intent() ;
                    intent.setAction(Constant.USER_AGE_CHANGE) ;
                    sendBroadcast(intent);
                    mUserAgeText.setText(nick);
                }else {
                    ToastView.showToast(PersonalActivity.this,"年龄更新失败", Toast.LENGTH_SHORT);
                }
            }
        });
        editDialog.dismiss();
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.sex_choice_switch:     //  性别
                loadDialog.show();
                if(isChecked){   //  m
                    updateSex("男");
                }else{           //  w
                    updateSex("女");
                }
                break ;
            default:
                break;
        }
    }
    /**
     * 更新性别
     * @param sex
     */
    private void updateSex(final String sex){
        User newUser = new User() ;
        newUser.setSex(sex);
        newUser.update(user.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Intent intent = new Intent() ;
                    intent.setAction(Constant.USER_SEX_CHANGE) ;
                    sendBroadcast(intent);
//                    muse.setText(sex);
                }else {
                    ToastView.showToast(PersonalActivity.this,"性别更新失败", Toast.LENGTH_SHORT);
                }
                if(loadDialog!=null){
                    loadDialog.dismiss();
                }
            }
        });

    }
    /**
     * 更新签名
     * @param nick
     */
    private void updateSign(final String nick){

        User newUser = new User() ;
        newUser.setSign(nick);
        newUser.update(user.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Intent intent = new Intent() ;
                    intent.setAction(Constant.USER_SIGN_CHANGE) ;
                    sendBroadcast(intent);
                    mUserSignText.setText(nick);
                }else {
                    ToastView.showToast(PersonalActivity.this,"签名更新失败", Toast.LENGTH_SHORT);
                }
            }
        });
        editDialog.dismiss();
    }

    /**
     * 相机
     */
    private void camera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = FileUtil.getOutputMediaFileUri();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, RESULT_REQUEST_PHOTO);
    }

    /**
     * 图库
     */
    private void photo() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_REQUEST_PHOTO);
    }

    /**
     * 处理图片
     * @param uri
     * @param outputUri
     * @param outputX
     * @param outputY
     * @param requestCode
     */
    private void cropImageUri(Uri uri, Uri outputUri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }
}
