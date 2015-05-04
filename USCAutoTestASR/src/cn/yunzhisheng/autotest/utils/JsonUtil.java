package cn.yunzhisheng.autotest.utils;

import org.json.JSONObject;

import cn.yunzhisheng.autotest.bean.BaseBean;
import cn.yunzhisheng.autotest.bean.MsgBean;
import cn.yunzhisheng.autotest.bean.TaskConfigBean;
import cn.yunzhisheng.autotest.bean.TestTaskBean;

public final class JsonUtil {

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
		TestTaskBean task = new TestTaskBean();
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
