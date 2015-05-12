package cn.yunzhisheng.autotest.dao;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import cn.yunzhisheng.common.USCError;
import cn.yunzhisheng.nlu.basic.USCSpeechUnderstander;
import cn.yunzhisheng.nlu.basic.USCSpeechUnderstanderListener;
import cn.yunzhisheng.understander.USCUnderstanderResult;

public class OnlineASRRecognizer {

	private USCSpeechUnderstander mRecognizer;
	private OnLineAsrCallback callback;
	int OPTION_SET_ASR_RECORDING_DATA = 102;

	public OnlineASRRecognizer(Context context, String appKey, String secret) {
		mRecognizer = new USCSpeechUnderstander(context, appKey, secret);
		mRecognizer.setOption(OPTION_SET_ASR_RECORDING_DATA, false);
		// mRecognizer.getParams().h(false);
		setListener();
	}

	// 注册监听
	public void setCallback(OnLineAsrCallback callback) {
		this.callback = callback;
	}

	public void start(InputStream is) {
		asrInputStream(is);
	}

	/**
	 * "engine":"general", "frontTime":4000, "backTime":1000, "rate":11,
	 * "language":"USCSpeechUnderstander.LANGUAGE_CHINESE", "farFiled":false,
	 * "scene":"场景(暂不知怎么用)", "address":"服务器地址(未使用)"
	 */

	// 设置识别领域
	public void setEngine(String engine) {
		if (mRecognizer != null) {
			mRecognizer.setEngine(engine);
		}
	}

	// 设置vad前后超时
	public void setVADTimeout(int frontTime, int backTime) {
		if (mRecognizer != null) {
			mRecognizer.setVADTimeout(frontTime, backTime);
		}
	}

	// 设置识别语音采样率 16000/8000
	public void setSampleRate(int rate) {
		if (mRecognizer != null) {
			mRecognizer.setSampleRate(rate);
		}
	}

	// 设置在线识别带宽
	public void setRate(int rate) {
		if (mRecognizer != null) {
			mRecognizer.setAsrBandwidth(rate);
		}
	}

	// 设置语言
	public void setLanguage(String language) {
		if (mRecognizer != null) {
			mRecognizer.setLanguage(language);
		}
	}

	// 设置声学模型
	public void setFarFiled(String farFiled) {
		if (mRecognizer != null) {
			mRecognizer.setVoiceField(farFiled);
		}
	}

	// 停止
	public void stop() {
		if (mRecognizer != null) {
			mRecognizer.stop();
		}
	}

	// 设置语义场景
	public void setScenario(String scene) {
		if (mRecognizer != null) {
			mRecognizer.setNluScenario(scene);
		}
	}

	// 设置语义服务器 识别服务器地址
	public void setServer(String address) {
		if (mRecognizer != null) {
			mRecognizer.setOption(USCSpeechUnderstander.OPT_SET_SERVER_ADDRESS, address);
		}
	}
	

	// 识别语音流的方法
	private void asrInputStream(InputStream is) {
		mRecognizer.start();
		try {
			// 30ms
			byte[] buffer = new byte[9600];
			while (true) {
				int read = is.read(buffer, 0, buffer.length);
				if (read < 0) {
					if (callback != null) {
						callback.onLastTime(System.currentTimeMillis());
					}
					break;
				}
				try {
					mRecognizer.writePcmData(buffer, 0, read);
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		mRecognizer.stop();
		if (is != null) {
			is = null;
		}
		// try {
		// if (is != null) {
		// is.close();
		// is = null;
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

	}

	private void setListener() {
		if (mRecognizer != null) {
			mRecognizer.setListener(new USCSpeechUnderstanderListener() {

				@Override
				public void onVADTimeout() {

				}

				@Override
				public void onUpdateVolume(int arg0) {
					if (callback != null) {
						callback.onVolume(arg0);
					}
				}

				@Override
				public void onUnderstanderResult(USCUnderstanderResult arg0) {
					if (callback != null && arg0 != null) {
						callback.onNluResult(arg0.getStringResult());
					}
				}

				@Override
				public void onSpeechStart() {

				}

				@Override
				public void onRecordingStop() {

				}

				@Override
				public void onRecordingStart() {

				}

				@Override
				public void onRecognizerResult(String arg0, boolean arg1) {
					if (callback != null) {
						callback.onAsrResult(arg0, arg1);
					}
				}

				@Override
				public void onEnd(USCError arg0) {
					if (callback != null) {
						callback.onEnd(arg0);
					}
				}
			});
		}
	}

	public interface OnLineAsrCallback {

		public void onAsrResult(String result, boolean isLast);

		public void onNluResult(String result);

		public void onVolume(int volume);

		public void onEnd(USCError error);

		public void onLastTime(long time);
	}
}
