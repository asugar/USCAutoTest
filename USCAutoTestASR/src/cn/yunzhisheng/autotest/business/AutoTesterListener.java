package cn.yunzhisheng.autotest.business;

public interface AutoTesterListener {

	// 返回错误信息
	// 出错
	public int ERROR = 0;

	public void getError(String error);

	// 获取测试任务单
	// 获取测试任务
	public int GET_TEST_TASK = 1;

	public void getTestTask(String result);

	// 获取配置文件
	// 获取测试配置
	public int GET_CONFIG_FILE = 2;

	public void getConfigFile(String result);

	// 获取测试素材列表materials
	// 获取素材列表
	public int GET_MATERIALS = 3;

	public void getMaterials(String result);

	// 初始化配置信息
	// 初始化测试配置
	public int INIT_TEST_CONFIG_PARAMS = 4;

	public void initTestConfigParams(String result);

	// 开始执行测试
	public int START_TEST = 5;

	public void startTest(String result);

	// 进度提示
	// 进度查询
	public int PROCESS_STATE = 6;

	public void getProcessState(int state);

	// // 下载测试素材成功
	// public void getMeterial(String result);
	//
	// // 单次开始识别
	// public void startRecognizer(String result);
	//
	// // 单次正在识别
	// public void beingRecognizer(String result);
	//
	// // 单次识别结束
	// public void finishRecognizer(String result);
	//
	// // 停止识别
	// public void stopRecognizer(String result);

	// 整个识别结束
	// 整个测试结束
	public int ALL_TEST_FINISH = 7;

	public void allTestFinish(String result);

	// 获取上传数据结果
	// 上传结果数据
	public int UPLOAD_DATA = 8;

	public void getUploadData(String result);

	// 获取上报数据结果
	// 上报结果数据
	public int SEND_RESULT = 9;

	public void getSendResult(String result);

}
