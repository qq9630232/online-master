package com.will.weiyue;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;

import com.will.weiyue.component.ApplicationComponent;
import com.will.weiyue.ui.base.BaseActivity;
import com.will.weiyue.utils.download.IndexContract;
import com.will.weiyue.utils.download.IndexPresenter;
import com.will.weiyue.widget.ApkLaunchUtils;
import com.will.weiyue.widget.DownloadProgressDialog;

import java.io.File;

import static android.os.Process.killProcess;

public class WebActivity extends BaseActivity implements IndexContract.View {

    private DownloadProgressDialog progressDialog;
    private IndexPresenter mPresenter;

    @Override
    public int getContentLayout() {
        return R.layout.activity_web;
    }


    /**
     * 检查包是否存在
     *
     * @param packname
     * @return
     */
    private boolean checkPackInfo(String packname) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }
    private void initView() {
        String apkUrl = getIntent().getStringExtra("url");
        String app_id = getIntent().getStringExtra("app_id");
        ApkLaunchUtils.openPackage(WebActivity.this,"com.bxvip.app.dafa02");
        if (checkPackInfo("com.bxvip.app.dafa02")) {
        } else {
            //TODO  下载操作
            mPresenter.downApk(WebActivity.this,apkUrl);
        }
    }
    public static void startActivity(Context context, String url, String appId){
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("app_id",appId);
        context.startActivity(intent);
    }
    @Override
    public void initInjector(ApplicationComponent appComponent) {

    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void initData() {

        mPresenter = new IndexPresenter(WebActivity.this);

        progressDialog = new DownloadProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // 设置ProgressDialog 标题
        progressDialog.setTitle("下载提示");
        // 设置ProgressDialog 提示信息
        progressDialog.setMessage("当前下载进度:");
        // 设置ProgressDialog 标题图标
        //progressDialog.setIcon(R.drawable.a);
        // 设置ProgressDialog 进度条进度
        // 设置ProgressDialog 的进度条是否不明确
        progressDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        initView();

    }

    @Override
    public void onRetry() {

    }

    @Override
    public void showUpdate(String version) {

    }

    @Override
    public void showProgress(int progress) {
        Log.e("zxz","进度条"+progress);
        if(!progressDialog.isShowing()){
            progressDialog.show();

        }
        progressDialog.setProgress(progress);
    }

    @Override
    public void showFail(String msg) {

    }

    @Override
    public void showComplete(File file) {
        try {
            String authority = getApplicationContext().getPackageName() + ".fileProvider";
            Uri fileUri = FileProvider.getUriForFile(this, authority, file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //7.0以上需要添加临时读取权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
            } else {
                Uri uri = Uri.fromFile(file);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
            }

            startActivity(intent);

            //弹出安装窗口把原程序关闭。
            //避免安装完毕点击打开时没反应
            killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
