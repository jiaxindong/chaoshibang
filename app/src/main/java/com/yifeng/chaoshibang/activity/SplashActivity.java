package com.yifeng.chaoshibang.activity;

import java.io.File;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yifeng.chaoshibang.R;
import com.yifeng.chaoshibang.interfaces.OnProgressListener;
import com.yifeng.chaoshibang.model.UpdateInfo;
import com.yifeng.chaoshibang.model.UpdateInfoService;
import com.yifeng.chaoshibang.service.DownloadFileService;
import com.yifeng.chaoshibang.utils.LogUtil;

public class SplashActivity extends BaseActivity {
	private TextView tv_version;
	private LinearLayout ll;
	private ProgressDialog progressDialog;
	private UpdateInfo info;
	private static final String TAG = "SplashActivity";
	private DownloadFileService downloadFileService;

	ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			//返回一个DownloadFileService对象
			downloadFileService = ((DownloadFileService.DownloadBinder)service).getService();
			//注册回调接口来接收下载进度的变化
			downloadFileService.setOnProgressListener(new OnProgressListener() {
				@Override
				public void onProgressInitialize(int total) {
					progressDialog.setMax(total);
				}
				@Override
				public void onProgressUpdate(int progress) {
					progressDialog.setProgress(progress);
				}
				@Override
				public void onProgressComplete(File file) {
					progressDialog.dismiss();
					Intent intent = new Intent(SplashActivity.this, DownloadFileService.class);
					unbindService(conn);
					stopService(intent);
					//安装下载的文件
					install(file);
				}
			});
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		initUI();
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setMessage("正在下载...");
		progressDialog.setCancelable(false);

		//启动线程
		Runnable r = new UpdateHandler();
		Thread thread = new Thread(r);
		thread.start();
	}

	class UpdateHandler implements Runnable {
		@Override
		public void run() {
			Looper.prepare();
			if(isNeedUpdate(getVersion())) {
				showUpdateDialog();
			}
			Looper.loop();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 获取当前程序的版本号
	 */
	private String getVersion() {
		try {
			PackageManager packageManager = getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "版本号未知";
		}
	}

	private void showUpdateDialog() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
		builder.setTitle("版本升级");
		builder.setMessage(info.getDescription());
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setCancelable(false);

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
					progressDialog.show();
					//绑定DownloadFileService
					Intent intent = new Intent(SplashActivity.this, DownloadFileService.class);
					intent.putExtra("url", info.getUrl());
					startService(intent);
					bindService(intent, conn, Context.BIND_AUTO_CREATE);
				} else {
					Toast.makeText(SplashActivity.this, "SD卡不可用，请插入SD卡", Toast.LENGTH_SHORT).show();
					loadMainUI();
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				loadMainUI();
			}

		});
		builder.create().show();
	}

	private void loadMainUI() {
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 安装apk
	 * @param file 要安装的apk的目录
	 */
	private void install(File file) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		finish();
		startActivity(intent);
	}

	private boolean isNeedUpdate(String version) {
		UpdateInfoService updateInfoService = new UpdateInfoService(this);
		LogUtil.d("SplashActivity", "version = " + version);
		try {
			info = updateInfoService.getUpdateInfo(R.string.serverUrl);
			String v = info.getVersion();
			if(v.equals(version)) {
				Log.i(TAG, "当前版本：" + version);
				Log.i(TAG, "最新版本：" + v);
				loadMainUI();
				return false;
			} else {
				Log.i(TAG, "需要更新");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "获取更新信息异常，请稍后再试", Toast.LENGTH_SHORT).show();
			loadMainUI();
		}
		return false;
	}

	private void initUI() {
		tv_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_version.setText("版本号 " + getVersion());
		//Toast.makeText(this, getVersion(), Toast.LENGTH_SHORT).show();

		ll = (LinearLayout) findViewById(R.id.ll_splash_main);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		alphaAnimation.setDuration(2000);
		ll.startAnimation(alphaAnimation);
	}
}
