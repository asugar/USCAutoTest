package cn.yunzhisheng.autotest.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import android.text.TextUtils;
import cn.yunzhisheng.autotest.value.Constant;

/**
 * @author 11 io操作工具类
 */
public final class IOUtil {

	/**
	 * 将一个inputStream转化为一个String字符串
	 * 
	 * @param is
	 * @return
	 */
	public static String inputStream2String(InputStream is) {
		try {
			StringBuffer sb = new StringBuffer();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				sb.append(new String(buffer, 0, len));
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 讲一个String转化为InputStream
	 * 
	 * @param str
	 * @return
	 */
	public static InputStream String2InputStream(String str) {
		InputStream is = null;
		try {
			if (TextUtils.isEmpty(str)) {
				return is;
			}
			is = new ByteArrayInputStream(str.getBytes(Constant.ENCODE));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return is;
	}

}
