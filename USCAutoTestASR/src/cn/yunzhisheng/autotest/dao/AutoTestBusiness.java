package cn.yunzhisheng.autotest.dao;

import java.io.InputStream;

import android.content.Context;
import android.util.Log;
import cn.yunzhisheng.autotest.bean.BaseBean;
import cn.yunzhisheng.autotest.bean.MsgBean;
import cn.yunzhisheng.autotest.bean.TestTaskBean;
import cn.yunzhisheng.autotest.http.HttpClient;
import cn.yunzhisheng.autotest.utils.JsonUtil;
import cn.yunzhisheng.autotest.value.Constant;

/**
 * @author 11 对自动测试的业务封装
 */
public class AutoTestBusiness {

	private HttpClient mHttpClient;
	private Context mContext;

	public AutoTestBusiness(Context c) {
		mContext = c;
		mHttpClient = HttpClient.getHttpClient();
	}

	/**
	 * 关闭所有连接
	 */
	protected Boolean closeConnect() {
		Boolean isSuccess = mHttpClient.closeConn();
		if (isSuccess) {
			// 关闭成功，
		} else {
			// 关闭失败
		}
		return isSuccess;
	}

	protected void stopAutoTest() {
		if (mHttpClient != null) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					mHttpClient.closeConn();
					mHttpClient = null;
				}
			}).start();

		}
	}

	/**
	 * 获取测试素材流
	 * 
	 * @param uri
	 * @return
	 */
	public InputStream getTestMaterialData(String uri) {
		InputStream is = mHttpClient.doGetHttpDownloadInputStream(uri);
		if (is == null) {

		} else {

		}
		return is;
	}

	/**
	 * get方式 ： 获取测试素材列表
	 * 
	 * @return
	 */
	public String getTestMaterials() {
		try {
			if (Constant.task.getData_uri() == null) {
				return "Constant.task.getData_uri() == null";
			}
			// 在此拼接uri的地址
			String str = mHttpClient.doGetHttpDownload(Constant.task.getData_uri() + Constant.MATERIAL_URI_PART);
			if (str == null) {
				return "inputStream2String时 str==null";
			}

			String[] subStrings = str.split("\n");
			for (int i = 0; i < subStrings.length; i++) {
				String song = subStrings[i];
				String[] spliter = song.split("/");
				String filename = spliter[5];
				String fileforder = spliter[4];
				Log.i("yi", fileforder + " : " + filename);
				Constant.materials.put(filename, song);
			}
			return "获取测试素材列表成功";
		} catch (Exception e) {
			e.printStackTrace();
			return "获取测试素材列表失败";
		}

	}

	/**
	 * get方式： 获取测试任务单
	 * 
	 * @param uri
	 * @return
	 */
	protected String getTestTask(String uri) {
		String result = null;
		try {
			String str = mHttpClient.doGetHttpDownload(uri);
			if (str == null) {
				result = "mHttpClient.doGetHttpDownload(uri)时，获取的str为空";
			}
			BaseBean base = JsonUtil.jsonString2Bean(str);
			if (base instanceof TestTaskBean) {
				Constant.task = (TestTaskBean) base;
				result = "获取测试任务单成功";
			} else {
				MsgBean m = (MsgBean) base;
				result = m.getMsg();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 获取配置文件:get方式
	public Boolean getConfigFile() {
		return false;
	}

	/**
	 * 上传结果：post方式,文件路径
	 * 
	 * @param uri
	 * @param inputStream
	 * @param fileName
	 * @return
	 */
	public String sendData(String uri, InputStream inputStream, String fileName) {
		return mHttpClient.doPostHttpUpload(uri, inputStream, fileName);
	}

	/**
	 * 上报结果:get方式
	 * 
	 * @param result_uri
	 * @param data
	 * @return
	 */
	public String sendResult(String result_uri, String data) {
		String result = null;
		try {
			String str = mHttpClient.doGetHttpUpload(result_uri + "?task_id=" + Constant.task.getTask_id()
					+ "&data_count=" + data);

			if (str == null) {
				Log.e("yi", "inputStream2String时 str==null");
				return null;
			}

			MsgBean msg = (MsgBean) JsonUtil.jsonString2MsgBean(str);
			result = msg.getMsg();
			Log.i("yi", "返回结果：" + msg.getMsg());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}
}
