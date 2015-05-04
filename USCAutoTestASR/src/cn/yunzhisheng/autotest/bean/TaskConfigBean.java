package cn.yunzhisheng.autotest.bean;

/**
 * @author 11 任务单的配置信息实体类
 */
public class TaskConfigBean {

	private String task_uri;
	private String task_result_uri;

	public TaskConfigBean() {

	}

	public TaskConfigBean(String uri, String result_uri) {
		this.task_uri = uri;
		this.task_result_uri = result_uri;
	}

	public String getTask_uri() {
		return task_uri;
	}

	public void setTask_uri(String task_uri) {
		this.task_uri = task_uri;
	}

	public String getTask_result_uri() {
		return task_result_uri;
	}

	public void setTask_result_uri(String task_result_uri) {
		this.task_result_uri = task_result_uri;
	}

}
