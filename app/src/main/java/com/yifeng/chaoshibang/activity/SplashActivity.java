package com.yifeng.chaoshibang.activity;

import java.io.File;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yifeng.chaoshibang.R;
import com.yifeng.chaoshibang.model.DownloadTask;
import com.yifeng.chaoshibang.model.UpdateInfo;
import com.yifeng.chaoshibang.model.UpdateInfoService;

public class SplashActivity extends BaseActivity {
	private TextView tv_version;
	private LinearLayout ll;
	private ProgressDialog progressDialog;
	private UpdateInfo info;
	private String version;
	private static final String TAG = "SplashActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		initUI();
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setMessage("正在下载...");

		//启动线程
		Runnable r = new UpdateHandler();
		Thread thread = new Thread(r);  
		thread.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			return rootView;
		}
	}

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
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("升级提醒");
		builder.setMessage(info.getDescription());
		builder.setCancelable(false);

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					File dir = new File(Environment.getExternalStorageDirectory(), "/chaoshibang/update");
					if(!dir.exists()) {
						dir.mkdirs();
					}
					String apkPath = Environment.getExternalStorageDirectory() + "/chaoshibang/update/new.apk";
					UpdateTask task = new UpdateTask(info.getUrl(), apkPath);
					progressDialog.show();
					new Thread(task).start();
				} else {
					Toast.makeText(SplashActivity.this, "SD卡不可用，请插入SD卡", Toast.LENGTH_SHORT).show();
					loadMainUI();
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				loadMainUI();
			}

		});
		builder.create().show();
	}

	private void loadMainUI() {
		Intent intent = new Intent(this, MainActivity.class);
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

	/**
	 * 下载的线程
	 */
	class UpdateTask implements Runnable {
		private String path;
		private String filePath;

		public UpdateTask(String path, String filePath) {
			this.path = path;
			this.filePath = filePath;
		}

		@Override
		public void run() {
			try {
				File file = DownloadTask.getFile(path, filePath, progressDialog);
				progressDialog.dismiss();
				install(file);
			} catch (Exception e) {
				e.printStackTrace();
				progressDialog.dismiss();
				Toast.makeText(SplashActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
				loadMainUI();
			}
		}

	}

	private boolean isNeedUpdate(String version) {
		UpdateInfoService updateInfoService = new UpdateInfoService(this);
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

	/**
	 * 查询更新的线程
	 * 0.4以后不能在主线程处理网络相关
	 */
	private class UpdateHandler implements Runnable {
		Message msg = new Message();
		public Handler handler;

		@Override
		public void run() {
			/* Class used to run a message loop for a thread. 
			 * Threads by default do not have a message loop associated with them; 
			 * to create one, call prepare() in the thread that is to run the loop, 
			 * and then loop() to have it process messages until the loop is stopped. 
			 */
			Looper.prepare();
			
			if(isNeedUpdate(getVersion())) {
				msg.what = 1;
			}
			handler = new Handler() {
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					Log.i("SplashActivity", msg.what+"");
					switch (msg.what) {
					case 1:
						showUpdateDialog();
						break;
					}
				}
			};
			handler.sendMessage(msg);
			Looper.loop();
		}
	}
}
