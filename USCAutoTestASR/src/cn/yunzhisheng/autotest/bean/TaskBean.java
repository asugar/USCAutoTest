package cn.yunzhisheng.autotest.bean;

/**
 * @author 11
 * 获取的任务实体类
 */
public class TaskBean extends BaseBean {

	private String config_uri;// 配置文件路径
	private String data_uri;// 数据文件路径
	private String msg;// 消息
	private String result_uri;// 返回结果路径
	private String task_id;// 测试任务id

	public TaskBean() {
	}

	public TaskBean(String config_uri, String data_uri, String msg, String result_uri, String task_id) {
		super();
		this.config_uri = config_uri;
		this.data_uri = data_uri;
		this.msg = msg;
		this.result_uri = result_uri;
		this.task_id = task_id;
	}

	@Override
	public String toString() {
		return "TestTask [config_uri=" + config_uri + ", data_uri=" + data_uri + ", msg=" + msg + ", result_uri="
				+ result_uri + ", task_id=" + task_id + "]";
	}

	public String getConfig_uri() {
		return config_uri;
	}

	public void setConfig_uri(String config_uri) {
		this.config_uri = config_uri;
	}

	public String getData_uri() {
		return data_uri;
	}

	public void setData_uri(String data_uri) {
		this.data_uri = data_uri;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getResult_uri() {
		return result_uri;
	}

	public void setResult_uri(String result_uri) {
		this.result_uri = result_uri;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

}
