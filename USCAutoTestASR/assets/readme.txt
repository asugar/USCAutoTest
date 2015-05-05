1.测试任务请求路径：http://10.10.11.104:8081/pullTask.action?userName=lpf&os=iOS&scheme=common&version=a
2.测试任务请求结果
{
	"config_uri":"http://10.10.20.206:280/http_data/task_1/",
	"data_uri":"http://10.10.20.206:280/http_data/task_1/",
	"msg":"",
	"result_uri":"http://10.10.20.206:280/http_data/task_1/",
	"task_id":"8"
}

cn.yunzhisheng.autotest.bean -- 实体类包
 --BaseBean -- 实体类基类
 --MsgBean -- 请求后返回消息实体类（可以复用）
 --TaskConfigBean -- 测试任务实体类（可以复用）两个url地址
 --TestConfigBean -- 测试配置实体类（可以复用）
 --TestTaskBean -- 测试任务单实体类（可以复用）五个数据
 
 cn.yunzhisheng.autotest.dao -- 业务处理包
 --AutoTester -- 自动测试者类
 --AutoTestBusiness -- 自动测试相关业务封装类
 --AutoTestRecognizer -- 封装识别类
 
 cn.yunzhisheng.autotest.http
 --HttpClient -- 封装http请求类（可以复用）
 
错误代号：
1 --- openConnect失败，请检查网络
2 --- initParams失败，请检查参数
3 --- conn.getInputStream()时，获取的输入流为空
4 --- inputStream2String时，str==null，io操作出错
5 --- 请求结果失败code
6 --- 抛异常了