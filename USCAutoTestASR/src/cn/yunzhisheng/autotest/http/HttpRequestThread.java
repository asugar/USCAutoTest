package cn.yunzhisheng.autotest.http;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.yunzhisheng.autotest.dao.AutoTester;
import cn.yunzhisheng.autotest.utils.IOUtil;
import cn.yunzhisheng.autotest.value.Constant;

/**
 * @author 11 基础网络操作 为了支持多线程安全问题
 */
public class HttpRequestThread extends Thread {

	public static final String PREFIX = "--", LINEND = "\r\n";
	public static final String BOUNDARY = java.util.UUID.randomUUID().toString();
	public String mUrl = null;
	private int mConnectTimeout = 3000;
	private int mReadTimeout = 3000;
	private String mRequestMethod = null;
	private String fileName;

	// public String doPostHttpUpload(String uri, InputStream is, String
	// fileName) {
	//
	// }

	@Override
	public void run() {

		HttpURLConnection mHttpURLConnection = null;
		OutputStream mOutputStream = null;
		InputStream mInputStream = null;
		String str = null;
		try {

			URL url = new URL(mUrl);
			mHttpURLConnection = (HttpURLConnection) url.openConnection();

			mHttpURLConnection.setConnectTimeout(mConnectTimeout);
			mHttpURLConnection.setReadTimeout(mReadTimeout);
			mHttpURLConnection.setRequestMethod(mRequestMethod);
			if (mRequestMethod.equals("POST")) {
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
						String.valueOf(headerInfo.length
								+ AutoTester.sbResult.toString().getBytes(Constant.ENCODE).length + end_data.length));
				mOutputStream = mHttpURLConnection.getOutputStream();

				if (headerInfo != null) {
					mOutputStream.write(headerInfo);
				}

				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = mInputStream.read(buffer)) != -1) {
					mOutputStream.write(buffer, 0, len);
				}
				// mOutputStream.write("123456789012345678901234567890123456789".getBytes(Constant.ENCODE),
				// 0, 39);
				// is.close();
				// is = null;
				mOutputStream.write(end_data);
				mOutputStream.flush();
				int code = mHttpURLConnection.getResponseCode();
				if (code == HttpURLConnection.HTTP_OK) {
					// 成功
				} else {
					// 失败
				}
			} else {
				int code = mHttpURLConnection.getResponseCode();

				if (code == HttpURLConnection.HTTP_OK) {
					mInputStream = mHttpURLConnection.getInputStream();
					if (mInputStream == null) {

					}
					str = IOUtil.inputStream2String(mInputStream);
					str = "结果文件上传成功";
					// 成功
				} else {
					// 失败
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (mInputStream != null) {
					mInputStream.close();
					mInputStream = null;
				}
				if (mOutputStream != null) {
					mOutputStream.close();
					mOutputStream = null;
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}

			if (mHttpURLConnection != null) {
				mHttpURLConnection.disconnect();
				mHttpURLConnection = null;
			}
		}

	}
}
