package cn.yunzhisheng.autotest;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.yunzhisheng.autotest.dao.AutoTester;
import cn.yunzhisheng.autotest.dao.AutoTester.AutoTesterListener;
import cn.yunzhisheng.autotest.http.HttpClient;
import cn.yunzhisheng.autotest.utils.IOUtil;
import cn.yunzhisheng.autotest.value.Constant;

public class MainActivity extends Activity {

	private Button btn_start;
	private EditText et_show_msg;
	private AutoTester mAutoTester;

	//
	public final static String TEST = "123一二三四五六七八九十周吴郑王张三李四王五七abc";

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 1:
					et_show_msg.append((String) msg.obj);
					et_show_msg.append("\n");
					break;
				case 0:

					break;
				default:
					break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		// 生成任务配置文件夹
		// generateConfigFolder();
		// 获取任务配置文件
		// ConfigUtil.getInstance().getTaskConfig();
		initListener();
		initParams();
	}

	private void initParams() {
		mAutoTester = new AutoTester(this);
		mAutoTester.setTRListener(new MyListener());

	}

	private void start() {
		if (mAutoTester != null) {
			mAutoTester.start();
		}
	}

	// 获取测试结果的监听器
	class MyListener implements AutoTesterListener {

		@Override
		public void getTestTask(String result) {
			mHandler.obtainMessage(1, result).sendToTarget();
			Log.i("yi", "getTestTask： " + result);
		}

		@Override
		public void getConfigFile(String result) {
		}

		@Override
		public void getMeterials(String result) {
			mHandler.obtainMessage(1, result).sendToTarget();
			Log.i("yi", "getMeterials： " + result);
		}

		@Override
		public void initConfigInfo(String result) {
		}

		@Override
		public void getMeterial(String result) {
			mHandler.obtainMessage(1, result).sendToTarget();
			Log.i("yi", "getMeterial： " + result);
		}

		@Override
		public void startRecognizer(String result) {
			mHandler.obtainMessage(1, result).sendToTarget();
			Log.i("yi", "startRecognizer： " + result);
		}

		@Override
		public void beingRecognizer(String result) {
			mHandler.obtainMessage(1, result).sendToTarget();
			Log.i("yi", "beingRecognizer： " + result);
		}

		@Override
		public void stopRecognizer(String result) {
		}

		@Override
		public void finishRecognizer(String result) {
			mHandler.obtainMessage(1, result).sendToTarget();
			Log.i("yi", "finishRecognizer： " + result);
		}

		@Override
		public void allFinishRecognizer(String result) {
			mHandler.obtainMessage(1, result).sendToTarget();
			Log.i("yi", "allFinishRecognizer: " + result);
		}

		@Override
		public void getSendData(String result) {
			mHandler.obtainMessage(1, result).sendToTarget();
		}

		@Override
		public void getSendResult(String result) {
			mHandler.obtainMessage(1, result).sendToTarget();
		}
	}

	private void initView() {
		btn_start = (Button) findViewById(R.id.btn_start);
		et_show_msg = (EditText) findViewById(R.id.et_show_msg);
	}

	private void initListener() {
		btn_start.setOnClickListener(MyButtonListener);
	}

	private View.OnClickListener MyButtonListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
				case R.id.btn_start:
					 start();
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								String fileName = "badmethod.txt";
								Log.i("yi", "长度：" + TEST.getBytes(Constant.ENCODE).length);
								HttpClient.getHttpClient().doPostHttpUpload(
										"http://10.10.20.206:280/http_data/task_1/", IOUtil.String2InputStream(TEST),
										fileName);
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					});
//					.start();
					break;
				default:
					break;
			}

		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mAutoTester != null) {
			mAutoTester.stopAutoTest();
			mAutoTester = null;
		}

	}

	// 生成配置文件夹
	private void generateConfigFolder() {
		try {
			File configFolder = new File(AutoTestApplication.getAppResBasePath() + File.separator
					+ Constant.APP_RES_CONFIGFILE_FOLDER);
			if (!configFolder.exists()) {
				configFolder.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
