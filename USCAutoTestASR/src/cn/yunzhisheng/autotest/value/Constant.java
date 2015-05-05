package cn.yunzhisheng.autotest.value;

import java.util.ArrayList;
import java.util.List;

import cn.yunzhisheng.autotest.bean.TaskConfigBean;
import cn.yunzhisheng.autotest.bean.TestTaskBean;

public class Constant {

	// 应用根目录
	public static final String APP_RES_PARENT_FOLDER = "AutoTest";
	// 配置文件夹
	public static final String APP_RES_CONFIGFILE_FOLDER = "Config";
	// 测试结果文件夹
	public static final String APP_RES_RESULT_FOLDER = "Result";

	// 测试任务配置文件 autotest.properties
	public static final String APP_RES_TASK_CONFIGFILE = "autotest.properties";

	// 测试任务
	public static TaskConfigBean taskConfig = null;

	// 编码格式utf-8
	public static final String ENCODE = "gbk";

	// 测试任务单
	public static TestTaskBean task = null;

	// 测试任务地址
	public final static String TASK_URI = "http://10.10.11.104:8081/pullTask.action?userName=lpf&os=Android&scheme=common&version=1.0";
	public final static String TASK_RESULT_URI = "http://10.10.11.104:8081/pushTaskResult.action";

	// 获取测试文件列表后半部uri
	public final static String MATERIAL_URI_PART = "?tpl=list&folders-filter=\\&recursive";

	// 存放素材列表的引用
	public static List<String> materials = new ArrayList<String>();
	// public static HashMap<String, String> materials = new HashMap<String,
	// String>();

	// 配置信息
	public static final String appKey = "h36gapxahhurprhksabjp6sqhja3ig7npbv5lfa7";
	public static final String secret = "4e66e9979027fa1c69c7b478b846877a";
}
