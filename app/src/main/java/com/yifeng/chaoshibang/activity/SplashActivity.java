package com.yifeng.chaoshibang.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
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

//		if(isNeedUpdate(getVersion())) {
//			showUpdateDialog();
//		}
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
//					UpdateTask task = new UpdateTask(info.getUrl(), apkPath);
//					progressDialog.show();
//					new Thread(task).start();
					new DownloadFileTask().execute(info.getUrl(), apkPath);
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

		public Handler handler = new Handler() {
			@Override
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

		@Override
		public void run() {
			/* Class used to run a message loop for a thread. 
			 * Threads by default do not have a message loop associated with them; 
			 * to create one, call prepare() in the thread that is to run the loop, 
			 * and then loop() to have it process messages until the loop is stopped. 
			 */
			Looper.prepare();
			Message msg = new Message();
			if(isNeedUpdate(getVersion())) {
				msg.what = 1;
			}
			handler.sendMessage(msg);
			Looper.loop();
		}
	}

	private class DownloadFileTask extends AsyncTask<String, Integer, File> {

		public DownloadFileTask() {
			super();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.show();
		}

		@Override
		protected void onPostExecute(File file) {
			super.onPostExecute(file);
			progressDialog.dismiss();
			loadMainUI();
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
			startActivity(intent);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			progressDialog.setMax(values[1]);
			progressDialog.setProgress(values[0]);
		}

		@Override
		protected File doInBackground(String... params) {
			try {
				URL url = new URL(params[0]);
				HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
				httpURLConnection.setConnectTimeout(2000);
				httpURLConnection.setRequestMethod("GET");
				if(httpURLConnection.getResponseCode() == 200) {
					int total = httpURLConnection.getContentLength();

					InputStream is = httpURLConnection.getInputStream();
					File file = new File(params[1]);
					FileOutputStream fos = new FileOutputStream(file);
					byte[] buffer = new byte[1024];
					int len;
					int process = 0;
					while((len = is.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
						process += len;
						//progressDialog.setProgress(process);
						publishProgress(process, total);
					}
					fos.flush();
					fos.close();
					is.close();
					return file;
				}
			} catch (Exception e) {

			}
			return null;
		}
	}
}
