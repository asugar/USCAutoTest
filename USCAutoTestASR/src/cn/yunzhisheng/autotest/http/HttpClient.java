package cn.yunzhisheng.autotest.http;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import android.util.Log;
import cn.yunzhisheng.autotest.business.AutoTester;
import cn.yunzhisheng.autotest.utils.IOUtil;
import cn.yunzhisheng.autotest.value.Constant;
import cn.yunzhisheng.autotest.value.ErrorValue;

/**
 * @author 11 基础网络操作 不支持多线程安全问题
 */
public class HttpClient {

	private static HttpClient mHttpClient = null;
	private InputStream mInputStream;
	private OutputStream mOutputStream;
	private HttpURLConnection mHttpURLConnection;

	public static HttpClient getHttpClient() {
		if (mHttpClient == null) {
			mHttpClient = new HttpClient();
		}
		return mHttpClient;
	}

	private HttpClient() {
	}

	public static String[] types = new String[] { "GET", "POST" };

	/**
	 * 打开连接
	 * 
	 * @param uri
	 * @return
	 */
	private synchronized Boolean openConnect(String uri) {
		try {
			URL url = new URL(uri);
			mHttpURLConnection = (HttpURLConnection) url.openConnection();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 初始化连接参数
	 * 
	 * @param conn
	 * @param type
	 * @param fileName
	 * @return
	 */
	private synchronized boolean initParams(int type) {
		try {
			mHttpURLConnection.setConnectTimeout(5000);
			mHttpURLConnection.setReadTimeout(5000);
			switch (type) {
				case 0:// get
					mHttpURLConnection.setRequestMethod(types[type]);
					break;
				case 1:// post
					mHttpURLConnection.setRequestMethod(types[type]);
					mHttpURLConnection.setDoOutput(true);
					break;
			}
			return true;
		} catch (ProtocolException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 检测返回code
	 * 
	 * @param code
	 * @return
	 */
	private synchronized boolean checkResponseCode(int code) {
		if (code == HttpURLConnection.HTTP_OK) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * get 下载（主要用的是这个）
	 * 
	 * @param uri
	 * @return
	 */
	public String doGetHttpDownload(String uri) {
		try {
			Boolean isOpenConnect = openConnect(uri);
			if (!isOpenConnect) {
				return ErrorValue.OPEN_CONNECT_ERROR;
			}

			Boolean isInistParams = initParams(0);
			if (!isInistParams) {
				return ErrorValue.INIT_PARAMS_ERROR;
			}

			int code = mHttpURLConnection.getResponseCode();
			if (checkResponseCode(code)) {
				mInputStream = mHttpURLConnection.getInputStream();
				if (mInputStream == null) {
					return ErrorValue.CONN_INPUTSTREAM_NULL_ERROR;
				}

				String str = IOUtil.inputStream2String(mInputStream);
				if (str == null) {
					return ErrorValue.INPUTSTREAM_2_STRING_NULL_ERROR;
				}
				closeConn();
				return str;
			} else {
				closeConn();
				return ErrorValue.CODE_ERROR;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ErrorValue.EXCEPTION_ERROR;
		}
	}

	/**
	 * get 上传
	 * 
	 * @param uri
	 * @return
	 */
	public String doGetHttpUpload(String uri) {
		try {
			Boolean isConnect = openConnect(uri);
			if (!isConnect) {
				return ErrorValue.OPEN_CONNECT_ERROR;
			}

			Boolean isInitSuccess = initParams(0);
			if (!isInitSuccess) {
				return ErrorValue.INIT_PARAMS_ERROR;
			}

			int code = mHttpURLConnection.getResponseCode();
			if (checkResponseCode(code)) {
				mInputStream = mHttpURLConnection.getInputStream();

				if (mInputStream == null) {
					return ErrorValue.CONN_INPUTSTREAM_NULL_ERROR;
				}

				String str = IOUtil.inputStream2String(mInputStream);
				if (str == null) {
					closeConn();
					return ErrorValue.INPUTSTREAM_2_STRING_NULL_ERROR;
				}
				closeConn();
				return str;
			} else {
				closeConn();
				return ErrorValue.CODE_ERROR;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ErrorValue.EXCEPTION_ERROR;
		}
	}

	/**
	 * get 下载 获取输入流 针对素材下载使用
	 * 
	 * @param uri
	 * @return
	 */
	public InputStream doGetHttpDownloadInputStream(String uri) {

		try {
			Boolean isConnect = openConnect(uri);
			if (!isConnect) {
				// 1
				Log.e("yi", "openConnect 时 conn==null");
				return null;
			}

			Boolean isInitSuccess = initParams(0);
			if (!isInitSuccess) {
				// 2
				Log.e("yi", "initParams 时 出错");
				return null;
			}

			int code = mHttpURLConnection.getResponseCode();
			if (checkResponseCode(code)) {
				mInputStream = mHttpURLConnection.getInputStream();
				if (mInputStream == null) {
					// 3
					Log.e("yi", "conn.getInputStream()时 is==null");
					return null;
				}
				return mInputStream;
			} else {
				// 5
				Log.e("yi", "请求结果失败：code = " + code);
				closeConn();
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			// 6
			return null;
		}
	}

	/**
	 * post上传
	 * 
	 * @param uri
	 * @param is
	 * @param fileName
	 */

	public static final String PREFIX = "--", LINEND = "\r\n";
	public static final String BOUNDARY = java.util.UUID.randomUUID().toString();

	public String doPostHttpUpload(String uri, InputStream is, String fileName) {
		try {

			Boolean isConnect = openConnect(uri);
			if (!isConnect) {
				return ErrorValue.OPEN_CONNECT_ERROR;
			}

			Boolean isInitSuccess = initParams(1);
			if (!isInitSuccess) {
				return ErrorValue.INIT_PARAMS_ERROR;
			}

			byte[] headerInfo = null;
			StringBuffer sb = new StringBuffer();
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\"file\"; filename=" + "\"" + fileName + "\"" + LINEND);
			sb.append("Content-Type: application/octet-stream" + LINEND);
			sb.append(LINEND);
			headerInfo = sb.toString().getBytes(Constant.ENCODE);
			byte[] end_data = (LINEND + PREFIX + BOUNDARY + PREFIX + LINEND).getBytes(Constant.ENCODE);
			mHttpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			mHttpURLConnection.setRequestProperty(
					"Content-Length",
					String.valueOf(headerInfo.length + AutoTester.sbResult.toString().getBytes(Constant.ENCODE).length
							+ end_data.length));

			mOutputStream = mHttpURLConnection.getOutputStream();

			if (headerInfo != null) {
				mOutputStream.write(headerInfo);
			}

			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				mOutputStream.write(buffer, 0, len);
			}

			is.close();
			is = null;
			mOutputStream.write(end_data);
			mOutputStream.flush();

			int code = mHttpURLConnection.getResponseCode();

			if (checkResponseCode(code)) {
				closeConn();
				return "结果文件上传成功";
			} else {
				closeConn();
				return ErrorValue.CODE_ERROR;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ErrorValue.EXCEPTION_ERROR;
		}

	}

	/**
	 * 关闭连接
	 * 
	 * @param is
	 * @param os
	 * @param conn
	 * @return
	 */
	public synchronized Boolean closeConn() {
		try {
			if (mInputStream != null) {
				mInputStream.close();
				mInputStream = null;
			}
			if (mOutputStream != null) {
				mOutputStream.close();
				mOutputStream = null;
			}
			if (mHttpURLConnection != null) {
				mHttpURLConnection.disconnect();
				mHttpURLConnection = null;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

}
