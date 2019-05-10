package com.will.weiyue.widget;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.will.weiyue.R;


/**
 * Created by Danfeng on 2018/6/6.
 */

public class CusServiceDialog extends DialogFragment {
    private Button mCancleBt;
    private Button mCallBt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;//return true 不往上传递则关闭不了，默认是可以取消，即return false
                } else {
                    return false;
                }
            }
        });
        View view = inflater.inflate(R.layout.customer_service_dialog, container, false);
        initView(view);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.fullScreenDialog);
    }

    private void initView(View view) {
        mCancleBt = view.findViewById(R.id.cancle_bt);
        mCallBt = view.findViewById(R.id.call_bt);
        mCancleBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mCallBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "010-5289614"));//跳转到拨号界面，同时传递电话号码
                startActivity(dialIntent);
            }
        });
    }
}
