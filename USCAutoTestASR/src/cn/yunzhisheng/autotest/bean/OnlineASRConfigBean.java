package cn.yunzhisheng.autotest.bean;

/**
 * @author 11 在线语音识别配置实体类
 * 
 */
public class OnlineASRConfigBean {

	// 设置识别参数:"general:通用识别","poi:地名识别","song:歌曲识别","movietv:影视名识别","medical:医药领域识别"
	private String engine;
	// vad说话前超时 开始不说话超时（2000ms-5000ms）
	private int frontTime;
	// vad说话后超时 停止说话vad超时(200ms-3000ms)
	private int backTime;
	// 在线识别带宽(AUTO/8K/16K)
	private int rate;
	// 设置识别语言(中文、英语、粤语)
	private String language;
	// 远讲模型
	private Boolean farFiled;
	// 场景
	private String scene;
	// 服务器地址
	private String address;

	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}

	public int getFrontTime() {
		return frontTime;
	}

	public void setFrontTime(int frontTime) {
		this.frontTime = frontTime;
	}

	public int getBackTime() {
		return backTime;
	}

	public void setBackTime(int backTime) {
		this.backTime = backTime;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Boolean getFarFiled() {
		return farFiled;
	}

	public void setFarFiled(Boolean farFiled) {
		this.farFiled = farFiled;
	}

	public String getScene() {
		return scene;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
