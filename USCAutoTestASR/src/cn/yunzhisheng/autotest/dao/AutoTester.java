package cn.yunzhisheng.autotest.dao;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import cn.yunzhisheng.autotest.dao.AutoTestRecognizer.OnLineAsrCallback;
import cn.yunzhisheng.autotest.utils.IOUtil;
import cn.yunzhisheng.autotest.value.Constant;
import cn.yunzhisheng.common.USCError;

public class AutoTester {

	// 自动测试者监听器
	private AutoTesterListener mAutoTesterListener;
	private Context mContext;
	// 自动测试业务类
	private AutoTestBusiness mAutoTestBusiness;

	// 自动测试语音识别类
	private AutoTestRecognizer recognizer;
	private Object asrUSCLock = new Object();

	private StringBuffer testResult = new StringBuffer();
	public static StringBuffer sbResult = new StringBuffer();

	public AutoTester(Context mContext) {
		this.mContext = mContext;
		initParams();
	}

	// 把MainActivity中的handler移到里面来
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 1:// 获取测试任务 + 解析
					mAutoTesterListener.getTestTask((String) msg.obj);
					if (Constant.task != null) {
						new GetConfigThread().start();
					}
					break;
				case 2:// 下载配置文件 + 解析
					mAutoTesterListener.getConfigFile((String) msg.obj);
					// if(Constant.t)
					new GetMeterialsThread().start();
					break;
				case 3:// 获取素材列表 + 验证完整性
					mAutoTesterListener.getMeterials((String) msg.obj);
					if (Constant.materials.size() > 0) {
						initConfigData();

						startAutoTest();
					}
					break;
				case 4:// 下载单个素材 + 验证完整性

					break;
				case 5:// 初始化测试配置

					break;
				case 6:// 进行测试 + 每个结果反馈给接口 + 保存每个测试结果

					break;
				case 7:// 上传测试结果

					break;
				case 8:// 上报测试结果（根据一开始给的地址）

					break;
				default:
					break;
			}
		};
	};

	// 开启自动测试方法
	public void start() {
		new GetTestTaskThread().start();
	}

	// 初始化相关数据
	private void initParams() {
		// recognizer
		mAutoTestBusiness = new AutoTestBusiness(mContext);
	}

	// 获取测试任务线程
	private class GetTestTaskThread extends Thread {
		@Override
		public void run() {
			// 1.获取测试任务单，无需返回值
			String result = mAutoTestBusiness.getTestTask(Constant.TASK_URI);
			mHandler.obtainMessage(1, result).sendToTarget();
		}
	}

	// 获取测试配置文件
	private class GetConfigThread extends Thread {
		@Override
		public void run() {
			// 2.获取配置文件
			mHandler.obtainMessage(2, "获取配置文件情况").sendToTarget();
		}
	}

	// 获取素材列表
	private class GetMeterialsThread extends Thread {
		@Override
		public void run() {
			// 3.获取素材列表
			String result = mAutoTestBusiness.getTestMaterials();
			mHandler.obtainMessage(3, result).sendToTarget();
		}
	}

	// 停止自动测试
	public void stopAutoTest() {
		if (recognizer != null) {
			recognizer.stop();
			recognizer.setCallback(null);
			recognizer = null;
		}

		if (mAutoTestBusiness != null) {
			mAutoTestBusiness.stopAutoTest();
			mAutoTestBusiness = null;
		}

		if (mHandler != null) {
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}

	}

	// 初始化测试配置信息
	private void initConfigData() {
		recognizer = new AutoTestRecognizer(mContext, Constant.appKey, Constant.secret);
		recognizer.setCallback(new OnLineAsrCallback() {
			@Override
			public void onVolume(int volume) {
			}

			@Override
			public void onNluResult(String result) {
			}

			@Override
			public void onLastTime(long time) {
			}

			@Override
			public void onEnd(USCError error) {
				Log.i("yi", " error: " + error);
				testResult.append("\n");
				sbResult.append("\n");
				synchronized (asrUSCLock) {
					asrUSCLock.notify();
					mAutoTesterListener.finishRecognizer(testResult.toString());
					testResult.delete(0, testResult.length());
				}
			}

			@Override
			public void onAsrResult(String result, boolean isLast) {
				mAutoTesterListener.beingRecognizer(result);
				testResult.append(result);
				sbResult.append(result);
				Log.i("yi", "result: " + result + " isLast: " + isLast);
			}
		});
	}

	// 开始自动测试
	private void startAutoTest() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (Constant.materials != null && Constant.materials.size() <= 0) {
					Log.e("yi", "没有找到可识别的语音文件.....");
					return;
				}
				// 遍历hashmap
				Iterator<Entry<String, String>> iter = Constant.materials.entrySet().iterator();
				Constant.materials.size();
				int count = 0;
				while (iter.hasNext()) {
					Map.Entry<String, String> entry = (Entry<String, String>) iter.next();
					count++;
					String name = (String) entry.getKey();
					String uri = (String) entry.getValue();

					testResult.append("第 " + count + " 个文件").append(name).append(":");
					sbResult.append("第 " + count + " 个文件 ").append(name.trim()).append(" : ");
					InputStream is = mAutoTestBusiness.getTestMaterialData(uri);
					if (is == null) {
						Log.e("yi", "is == null");
						continue;
					}

					mAutoTesterListener.getTestTask("获取文件：" + name + "成功");

					try {
						Thread.sleep(100);
					} catch (Exception e) {
						e.printStackTrace();
					}

					mAutoTesterListener.startRecognizer("开始第" + count + "个文件识别");

					recognizer.start(is);

					synchronized (asrUSCLock) {
						try {
							asrUSCLock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					mAutoTestBusiness.closeConnect();
				}

				// data的数据也是需要计算获取的吧
				Log.i("yi", "语音识别结束了: \n" + testResult.toString());
				mAutoTesterListener.allFinishRecognizer("语音识别全部结束了");
				// 上传数据
				String sendDataResult = mAutoTestBusiness.sendData(Constant.task.getResult_uri(),
						IOUtil.String2InputStream(sbResult.toString()), "result.txt");
				if (sendDataResult != null) {
					mAutoTesterListener.getSendData(sendDataResult);
				}

				// 上报数据
				String sendResult = mAutoTestBusiness.sendResult(Constant.TASK_RESULT_URI, "结果");
				if (sendResult != null) {
					mAutoTesterListener.getSendResult(sendResult);
				}
			}
		}).start();
	}

	// 注册接口
	public void setTRListener(AutoTesterListener listener) {
		this.mAutoTesterListener = listener;
	}

	// 定义接口
	public interface AutoTesterListener {
		// 进度查询
		// public void getProcessState(int state);

		// 获取测试任务单
		public void getTestTask(String result);

		// 获取配置文件
		public void getConfigFile(String result);

		// 获取测试素材列表materials
		public void getMeterials(String result);

		// 初始化配置信息
		public void initConfigInfo(String result);

		// 下载测试素材成功
		public void getMeterial(String result);

		// 单次开始识别
		public void startRecognizer(String result);

		// 单次正在识别
		public void beingRecognizer(String result);

		// 单次识别结束
		public void finishRecognizer(String result);

		// 停止识别
		public void stopRecognizer(String result);

		// 整个识别结束
		public void allFinishRecognizer(String result);

		// 获取上传数据结果
		public void getSendData(String result);

		// 获取上报数据结果
		public void getSendResult(String result);

	}

}
