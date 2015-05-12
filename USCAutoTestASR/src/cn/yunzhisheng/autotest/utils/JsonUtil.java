package cn.yunzhisheng.autotest.utils;

import org.json.JSONObject;

import cn.yunzhisheng.autotest.bean.BaseBean;
import cn.yunzhisheng.autotest.bean.MsgBean;
import cn.yunzhisheng.autotest.bean.OnlineASRConfigBean;
import cn.yunzhisheng.autotest.bean.TaskConfigBean;
import cn.yunzhisheng.autotest.bean.TestConfigBean;
import cn.yunzhisheng.autotest.bean.TaskBean;

public final class JsonUtil {

	/**
	 * 获取测试配置文件
	 * 
	 * @param jsonString
	 * @return
	 */
	public static TestConfigBean jsonString2TestConfigBean(String jsonString) {
		TestConfigBean testConfig = null;
		try {
			testConfig = new TestConfigBean();
			JSONObject json = new JSONObject(jsonString);
			testConfig.setType(json.getString("type"));
			testConfig.setAppKey(json.getString("appKey"));
			testConfig.setSecret(json.getString("secret"));
			JSONObject online_asr_json = json.getJSONObject("online_asr");
			OnlineASRConfigBean online = new OnlineASRConfigBean();
			online.setEngine(online_asr_json.getString("engine"));
			online.setFrontTime(online_asr_json.getInt("frontTime"));
			online.setBackTime(online_asr_json.getInt("backTime"));
			online.setRate(online_asr_json.getInt("rate"));
			online.setLanguage(online_asr_json.getString("language"));
			online.setFarFiled(online_asr_json.getBoolean("farFiled"));
			online.setAddress(online_asr_json.getString("address"));
			online.setScene(online_asr_json.getString("scene"));
			testConfig.setOnline_asr(online);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return testConfig;
	}

	/**
	 * 判断转化为那个实体类
	 * 
	 * @param jsonString
	 * @return
	 */
	public static BaseBean jsonString2Bean(String jsonString) {
		try {
			JSONObject json = new JSONObject(jsonString);
			json.getString("config_uri");
			return jsonString2TestTaskBean(jsonString);
		} catch (Exception e) {
			return jsonString2MsgBean(jsonString);
		}
	}

	/**
	 * 将一个jsonString转化为TestTaskBean实体类
	 * 
	 * @param jsonString
	 * @return
	 */
	public static BaseBean jsonString2TestTaskBean(String jsonString) {
		TaskBean task = new TaskBean();
		try {
			JSONObject json = new JSONObject(jsonString);
			task.setConfig_uri(json.getString("config_uri"));
			task.setData_uri(json.getString("data_uri"));
			task.setResult_uri(json.getString("result_uri"));
			task.setTask_id(json.getString("task_id"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return task;
	}

	/**
	 * 将一个jsonString转化为MsgBean实体类
	 * 
	 * @param jsonString
	 * @return
	 */
	public static BaseBean jsonString2MsgBean(String jsonString) {
		MsgBean msg = new MsgBean();
		try {
			JSONObject jo = new JSONObject(jsonString);
			msg.setMsg(jo.getString("msg"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	public static TaskConfigBean jsonString2TaskConfigBean(String jsonString) {
		TaskConfigBean config = new TaskConfigBean();
		try {
			JSONObject jo = new JSONObject(jsonString);
			config.setTask_uri(jo.getString("task_uri"));
			config.setTask_result_uri(jo.getString("task_result_uri"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;
	}
}
