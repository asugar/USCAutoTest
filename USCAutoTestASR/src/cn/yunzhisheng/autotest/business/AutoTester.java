package cn.yunzhisheng.autotest.business;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import cn.yunzhisheng.autotest.bean.OnlineASRConfigBean;
import cn.yunzhisheng.autotest.dao.OnlineASRRecognizer;
import cn.yunzhisheng.autotest.dao.OnlineASRRecognizer.OnLineAsrCallback;
import cn.yunzhisheng.autotest.utils.IOUtil;
import cn.yunzhisheng.autotest.value.Constant;
import cn.yunzhisheng.autotest.value.ErrorValue;
import cn.yunzhisheng.common.USCError;

public class AutoTester {

	// 自动测试者监听器
	private AutoTesterListener mAutoTesterListener;
	private Context mContext;
	// 自动测试业务类
	private TestBusiness mAutoTestBusiness;

	// 自动测试语音识别类
	private OnlineASRRecognizer recognizer;
	private Object asrUSCLock = new Object();

	private StringBuffer testResult = new StringBuffer();
	public static StringBuffer sbResult = new StringBuffer();
	private ExecutorService mPool;
	// 记录线程池是否正在运行
	private Boolean isRunning = false;

	public AutoTester(Context mContext) {
		this.mContext = mContext;
	}

	// 把MainActivity中的handler移到里面来
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 1:// 获取测试任务 + 解析
					mAutoTesterListener.getTestTask((String) msg.obj);
					break;
				case 2:// 下载配置文件 + 解析
					mAutoTesterListener.getConfigFile((String) msg.obj);
					break;
				case 3:// 获取素材列表 + 验证完整性
					mAutoTesterListener.getMeterials((String) msg.obj);
					if (Constant.materials.size() > 0) {
						initConfigData();

						startAutoTest();
					}
					break;
				case 4:// 下载单个素材 + 验证完整性
					mAutoTesterListener.getMeterial((String) msg.obj);
					break;
				case 5:// 初始化测试配置

					break;
				case 6:// 进行测试 + 每个结果反馈给接口 + 保存每个测试结果

					break;
				case 7:// 上传测试结果
					mAutoTesterListener.allFinishRecognizer("语音识别全部结束了");
					mAutoTesterListener.getSendData((String) msg.obj);
					break;
				case 8:// 上报测试结果（根据一开始给的地址）
					mAutoTesterListener.getSendResult((String) msg.obj);
					break;
				case 0:
					String error = (String) msg.obj;
					if (error.equals(ErrorValue.OPEN_CONNECT_ERROR)) {
						mAutoTesterListener.getError("openConnect失败，请检查网络");
					} else if (error.equals(ErrorValue.INIT_PARAMS_ERROR)) {
						mAutoTesterListener.getError("initParams失败，请检查参数");
					} else if (error.equals(ErrorValue.CONN_INPUTSTREAM_NULL_ERROR)) {
						mAutoTesterListener.getError("conn.getInputStream()时，获取的输入流为空");
					} else if (error.equals(ErrorValue.INPUTSTREAM_2_STRING_NULL_ERROR)) {
						mAutoTesterListener.getError("inputStream2String时，str==null，io操作出错");
					} else if (error.equals(ErrorValue.CODE_ERROR)) {
						mAutoTesterListener.getError("请求结果失败code");
					} else if (error.equals(ErrorValue.EXCEPTION_ERROR)) {
						mAutoTesterListener.getError("抛异常了，检测url或者网络");
					} else if (error.equals(ErrorValue.DATA_URI_NULL_ERROR)) {
						mAutoTesterListener.getError("Constant.task.getData_uri() == null");
					} else if (error.equals(ErrorValue.NO_MATERIALS_ERROR)) {
						mAutoTesterListener.getError("没有找到可测试文件.....");
					} else {
						mAutoTesterListener.getError(error);
					}
					stopPool();
					Log.i("yi", "handleMessage is over");
					break;
				default:
					break;
			}
		};
	};

	// 开启自动测试方法，使用一个线程池把所有的操作一块执行了是不是更好？
	public void start() {
		initParams();
		Log.i("yi", "start is invoked");
		// 检测 Constant.task在MainActivity中已经做了
		if (!isRunning) {
			Log.i("yi", "start isRunning: " + isRunning);
			// 获取测试任务
			mPool.execute(new GetTestTaskThread());
			// 获取配置文件
			mPool.execute(new GetConfigThread());
			// 获取素材列表
			mPool.execute(new GetMeterialsThread());
			// mPool.shutdown();
			isRunning = true;
		}

	}

	// 初始化相关数据
	private void initParams() {

		// 初始化业务类
		if (mAutoTestBusiness == null) {
			mAutoTestBusiness = new TestBusiness(mContext);
		}

		// 初始化线程池
		if (mPool != null) {
			if (!mPool.isShutdown()) {
				mPool.shutdownNow();
			}
			mPool = null;
			isRunning = false;
		}
		mPool = Executors.newSingleThreadExecutor();
	}

	/**
	 * 获取测试任务线程 通用
	 * 
	 * @author 11
	 * 
	 */
	private class GetTestTaskThread extends Thread {

		@Override
		public void run() {
			try {
				Log.i("yi", "GetTestTaskThread");
				// 验证Constant.TASK_URI有效性
				if (Constant.TASK_URI == null) {
					stopPool();
					mHandler.obtainMessage(0, "TASK_URI无效，请核对！！！").sendToTarget();
				}

				// 1.获取测试任务单，无需返回值
				String result = mAutoTestBusiness.getTestTask(Constant.TASK_URI);
				// 对返回的结果进行处理
				if (result.length() == 1) {
					stopPool();
					Log.i("yi", "result.length() == 1");
					mHandler.obtainMessage(1, "获取任务失败！！！").sendToTarget();
					mHandler.obtainMessage(0, result).sendToTarget();
				} else {
					mHandler.obtainMessage(1, result).sendToTarget();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 获取测试配置文件 通用
	 * 
	 * @author 11
	 * 
	 */
	private class GetConfigThread extends Thread {
		@Override
		public void run() {
			try {
				Log.i("yi", "GetConfigThread");
				// 验证路径有效性
				if (Constant.task.getConfig_uri() == null) {
					stopPool();
					mHandler.obtainMessage(0, "Config_uri无效，请核对！！！").sendToTarget();
				}
				// 2.获取配置文件
				String result = mAutoTestBusiness.getConfigFile(Constant.task.getConfig_uri() + "test%20(3).config");
				if (result.length() == 1) {
					mHandler.obtainMessage(0, result).sendToTarget();
					mHandler.obtainMessage(2, "获取配置文件失败").sendToTarget();
				} else {
					mHandler.obtainMessage(2, result).sendToTarget();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 获取素材列表 通用
	 * 
	 * @author 11
	 * 
	 */
	private class GetMeterialsThread extends Thread {
		@Override
		public void run() {
			try {
				// 3.获取素材列表 使用的地址是从测试任务获取到的，
				// 地址有效性验证应该在这里做
				if (Constant.task.getData_uri() == null) {
					// 7
					// return "Constant.task.getData_uri() == null";
					stopPool();
					mHandler.obtainMessage(0, "7").sendToTarget();
				}
				Log.i("yi", "GetMeterialsThread");
				String result = mAutoTestBusiness.getTestMaterials();
				if (result.length() == 1) {
					stopPool();
					mHandler.obtainMessage(3, "获取素材列表失败！！！").sendToTarget();
					mHandler.obtainMessage(0, result).sendToTarget();
				} else {
					mHandler.obtainMessage(3, result).sendToTarget();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	// 停止自动测试
	public void stopAutoTest() {
		if (recognizer != null) {
			recognizer.stop();
			recognizer.setCallback(null);
			recognizer = null;
		}

		if (mPool != null) {
			mPool.shutdown();
			isRunning = false;
			mPool = null;
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

	// 停止线程池运行
	private void stopPool() {
		Log.i("yi", "stopPool shutdownNow");
		if (mPool != null) {
			if (!mPool.isShutdown()) {
				mPool.shutdownNow();
			}
			mPool = null;
			isRunning = false;
		}
	}

	private void pauseAutoTest() {
		Log.i("yi", "pauseAutoTest");
		if (recognizer != null) {
			recognizer.stop();
		}

		if (mPool != null) {
			mPool.shutdownNow();
			isRunning = false;
		}

		if (mAutoTestBusiness != null) {
			mAutoTestBusiness.stopAutoTest();
		}

		if (mHandler != null) {
			mHandler.removeCallbacksAndMessages(null);
		}
	}

	// 初始化测试配置信息
	private void initConfigData() {
		// 根据对应测试实例化对应参数
		String type = Constant.testConfig.getType();
		if ("online_asr".equals(type)) {
			OnlineASRConfigBean online_asr = Constant.testConfig.getOnline_asr();

			recognizer = new OnlineASRRecognizer(mContext, Constant.appKey, Constant.secret);

			recognizer.setEngine(online_asr.getEngine() == null ? "general" : online_asr.getEngine());
			recognizer.setVADTimeout(online_asr.getFrontTime() == 0 ? 4000 : online_asr.getFrontTime(),
					online_asr.getBackTime() == 0 ? 1000 : online_asr.getBackTime());
			recognizer.setRate(online_asr.getRate() == 0 ? 8000 : online_asr.getRate());
			recognizer.setLanguage(online_asr.getLanguage() == null ? "chinese" : online_asr.getLanguage());

			// 设置声学模型 字符串类型，需要修改配置文件
			// recognizer.setFarFiled(online_asr.getFarFiled() == null ? false :
			// online_asr.getFarFiled());
			// recognizer.setScenario(online_asr.getScene() == null ? "" :
			// online_asr.getScene());
			// recognizer.setServer(online_asr.getAddress() == null ? "" :
			// online_asr.getAddress());

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
					Log.i("yi", "--------- onEnd回调了 ");
					if (error != null) {
						Log.i("yi", " error: " + error);
						mAutoTesterListener.getError(error.toString());
					}
					testResult.append("\n");
					sbResult.append("\n");
					synchronized (asrUSCLock) {
						Log.i("yi", "--------- notify 这时候识别才结束了 ");
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
		} else if ("".equals(type)) {

		} else if ("".equals(type)) {

		} else if ("".equals(type)) {

		} else {
			// 有误
		}

		mAutoTesterListener.initConfigInfo("初始化测试参数成功");
	}

	// 开始自动测试
	private void startAutoTest() {
		// 获取列表有效性验证
		if (Constant.materials != null && Constant.materials.size() <= 0) {
			mHandler.obtainMessage(0, "8").sendToTarget();
			return;
		}
		// 遍历

		for (int i = 0; i < Constant.materials.size(); i++) {
			mPool.execute(new TestMaterialThread(i));
		}

		// 通用
		// data的数据也是需要计算获取的吧
		mPool.execute(new SendDataThread());
		mPool.execute(new SendResultThread());
		mPool.shutdown();
	}

	// 上报数据 通用
	private class SendResultThread extends Thread {
		@Override
		public void run() {
			try {
				// 上报数据
				String sendResult = mAutoTestBusiness.sendResult(Constant.TASK_RESULT_URI, "结果");
				if (sendResult.length() == 1) {
					mHandler.obtainMessage(8, "上报数据失败").sendToTarget();
					mHandler.obtainMessage(0, sendResult).sendToTarget();
				} else {
					mHandler.obtainMessage(8, sendResult).sendToTarget();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 数据上传线程 通用
	private class SendDataThread extends Thread {
		@Override
		public void run() {
			try {
				Log.i("yi", "语音识别结束了: \n" + testResult.toString());
				// mHandler.obtainMessage(7, "").sendToTarget();

				// 上传数据
				String sendDataResult = mAutoTestBusiness.sendData(Constant.task.getResult_uri(),
						IOUtil.String2InputStream(sbResult.toString()), "result.txt");
				if (sendDataResult.length() == 1) {
					mHandler.obtainMessage(7, "上传数据失败").sendToTarget();
					mHandler.obtainMessage(0, sendDataResult).sendToTarget();
				} else {
					mHandler.obtainMessage(7, sendDataResult).sendToTarget();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 素材测试线程
	private class TestMaterialThread extends Thread {

		private int i;

		public TestMaterialThread(int index) {
			i = index;
		}

		@Override
		public void run() {
			try {
				Log.i("yi", "---------第 " + i + " 个线程开始执行");
				String uri = Constant.materials.get(i);
				String[] spliter = uri.split("/");
				String name = spliter[5].trim();
				// 备用
				// String fileforder = spliter[4];

				testResult.append("第 " + i + " 个文件").append(name).append(":");
				sbResult.append("第 " + i + " 个文件 ").append(name).append(" : ");
				InputStream is = mAutoTestBusiness.getTestMaterialData(uri);
				if (is == null) {
					Log.e("yi", "is == null");
					return;
				}
				mHandler.obtainMessage(4, "获取 " + i + " 文件：" + name + "成功").sendToTarget();
				mHandler.obtainMessage(6, "开始第" + i + "个文件识别").sendToTarget();

				recognizer.start(is);

				synchronized (asrUSCLock) {
					try {
						Log.i("yi", "---------第 " + i + " 个线程开始等待 ");
						asrUSCLock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (is != null) {
					is = null;
				}
				mAutoTestBusiness.closeConnect();
				Log.i("yi", "---------第 " + i + " 个线程结束 ");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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

		// 返回错误信息
		public void getError(String error);

	}

}
