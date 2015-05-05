package cn.yunzhisheng.autotest.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import android.util.Log;

import cn.yunzhisheng.autotest.AutoTestApplication;
import cn.yunzhisheng.autotest.value.Constant;

public class ConfigUtil {

	private static ConfigUtil self = null;

	private ConfigUtil() {
	}

	public static ConfigUtil getInstance() {
		if (self == null) {
			self = new ConfigUtil();
		}
		return self;
	}

	public void getTaskConfig() {
		String basePath = AutoTestApplication.getAppResBasePath() + File.separator + Constant.APP_RES_CONFIGFILE_FOLDER;
		try {
			File configFile = new File(basePath + File.separator + Constant.APP_RES_TASK_CONFIGFILE);
			if(!configFile.exists()){
				Log.i("yi", "配置文件不存在");
				return;
			}
			InputStream is = new FileInputStream(configFile);
			String jsonString = IOUtil.inputStream2String(is);
			Constant.taskConfig = JsonUtil.jsonString2TaskConfigBean(jsonString);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 有效性验证缺失
	}
}
