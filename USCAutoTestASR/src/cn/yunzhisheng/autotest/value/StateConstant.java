package cn.yunzhisheng.autotest.value;

/**
 * @author 11
 * 状态常量
 */
public class StateConstant {
	// 成功
	public static int SUCCESS = 110;
	// openConnect失败
	public static int OPEN_CONNECT_ERROR = 111;
	// initParams失败
	public static int INIT_PARAMS_ERROR = 112;
	// checkResponseCode失败
	public static int CHECK_RESPONSE_CODE = 113;
	// 连接获取的输入流为空
	public static int CONNECT_GET_INPUTSTREAM_IS_NULL = 114;
	
	// IOUtil 获取的string为空
	public static int IOUTIL_GET_STRING_IS_NULL = 115;
	
	// closeConnect失败
	public static int CLOSE_CONNECT_ERROR = 116;
	
	// TRY CATCH 异常
	public static int EXCEPTION_ERROR = 117;
	
	//
	public static int CONNECT_GET_OUTPUTSTREAM_IS_NULL = 117;
}
