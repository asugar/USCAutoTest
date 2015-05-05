package cn.yunzhisheng.autotest;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.yunzhisheng.autotest.dao.AutoTester;
import cn.yunzhisheng.autotest.dao.AutoTester.AutoTesterListener;
import cn.yunzhisheng.autotest.utils.ConfigUtil;
import cn.yunzhisheng.autotest.value.Constant;

public class MainActivity extends Activity {

	private Button btn_start;
	private EditText et_show_msg;
	private AutoTester mAutoTester;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		// 生成任务配置文件夹
		generateConfigFolder();
		// 生成结果文件夹
		generateResultFolder();
		// 获取任务配置文件
		ConfigUtil.getInstance().getTaskConfig();
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
		public void getError(String error) {
			et_show_msg.append(error);
			et_show_msg.append("\n");
		}

		@Override
		public void getTestTask(String result) {
			et_show_msg.append(result);
			et_show_msg.append("\n");
			Log.i("yi", "getTestTask： " + result);
		}

		@Override
		public void getConfigFile(String result) {
		}

		@Override
		public void getMeterials(String result) {
			et_show_msg.append(result);
			et_show_msg.append("\n");
			Log.i("yi", "getMeterials： " + result);
		}

		@Override
		public void initConfigInfo(String result) {
		}

		@Override
		public void getMeterial(String result) {
			et_show_msg.append(result);
			et_show_msg.append("\n");
			Log.i("yi", "getMeterial： " + result);
		}

		@Override
		public void startRecognizer(String result) {
			et_show_msg.append(result);
			et_show_msg.append("\n");
			Log.i("yi", "startRecognizer： " + result);
		}

		@Override
		public void beingRecognizer(String result) {
			et_show_msg.append(result);
			et_show_msg.append("\n");
			Log.i("yi", "beingRecognizer： " + result);
		}

		@Override
		public void stopRecognizer(String result) {
		}

		@Override
		public void finishRecognizer(String result) {
			et_show_msg.append(result);
			et_show_msg.append("\n");
			Log.i("yi", "finishRecognizer： " + result);
		}

		@Override
		public void allFinishRecognizer(String result) {
			et_show_msg.append(result);
			et_show_msg.append("\n");
			Log.i("yi", "allFinishRecognizer: " + result);
		}

		@Override
		public void getSendData(String result) {
			et_show_msg.append(result);
			et_show_msg.append("\n");
		}

		@Override
		public void getSendResult(String result) {
			et_show_msg.append(result);
			et_show_msg.append("\n");
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

	// 生成测试结果文件夹
	private void generateResultFolder() {
		try {
			File configFolder = new File(AutoTestApplication.getAppResBasePath() + File.separator
					+ Constant.APP_RES_RESULT_FOLDER);
			if (!configFolder.exists()) {
				configFolder.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
