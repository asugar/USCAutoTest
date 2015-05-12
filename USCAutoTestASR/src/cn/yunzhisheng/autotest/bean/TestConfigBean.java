package cn.yunzhisheng.autotest.bean;

/**
 * @author 11 测试配置信息实体类
 */
public class TestConfigBean {

	// "appKey":"h36gapxahhurprhksabjp6sqhja3ig7npbv5lfa7"，
	// "secret":"4e66e9979027fa1c69c7b478b846877a",
	// "type":"online_tts|offline_tts|online_asr|offline_asr|wakeup",
	// "online_asr":
	private String appKey;
	private String secret;
	private String type;
	private OnlineASRConfigBean online_asr;

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public OnlineASRConfigBean getOnline_asr() {
		return online_asr;
	}

	public void setOnline_asr(OnlineASRConfigBean online_asr) {
		this.online_asr = online_asr;
	}

}
