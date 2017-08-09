package com.toolproject.runtop.tool_logstatistics.statistics;

import com.alibaba.fastjson.JSONObject;

import cn.caifuqiao.request.ParameterTimelyManager;
import cn.caifuqiao.statistics.PublicParameter;
import cn.caifuqiao.tool.HelpString;

/**
 * 基本参数日志类
 * 
 * @author bazengliang
 *
 */
public class BaseLog {
	/** 用户登陆ID */
	private int faId;
	/**
	 * 地理位置信息 子类型location
	 * 
	 * IP IP地址
	 * 
	 * longitude 经度
	 * 
	 * latitude 维度
	 */
	private JSONObject location;

	/** 生成时间\时间戳\到毫秒 */
	private long createTime;
	/**
	 * 访问的网络连接类型
	 * 
	 * 1:wifi;2:2g;3:3g;4:4g;5:其它;
	 */
	private int networkType;

	// -------------------------------------------------------------------------------------------------------------------------

	/**
	 * 设备信息
	 * 
	 * deviceNo 设备号 string
	 * 
	 * phoneType 手机型号 string
	 * 
	 * phoneSystem 手机系统 string
	 * 
	 * phoneBrand 手机品牌 string
	 */
	private JSONObject device;

	/** app版本号或其它终端版本号 */
	private String version;
	/** 访问终端来源 */
	private int source = 1;
	/**
	 * 时间偏差\毫秒
	 * 
	 * 手机本地时间与标准时间的偏差 时间差=本地时间-服务器时间 服务器时间=本地时间-时间差
	 */
	private String offsetTime;
	/**
	 * 渠道标识号 根据分包参数配置(1-30)
	 */
	private String promotionChannel = "0";

	public BaseLog(boolean isInitData) {
		super();
		if (isInitData) {
			// 设置可变参数
			setChangingValue();
			// 设置固定值参数
			setFixValue();
		}
	}

	public BaseLog() {
		super();
	}

	public void setFixValue() {
		this.faId = HelpString.StrToInteger(ParameterTimelyManager.getFaId(), -1);
		device = new JSONObject();
		device.put("deviceNo", ParameterTimelyManager.getUUID());// 设备号
		device.put("phoneType", PublicParameter.getPublicParameter().phoneModel);// 手机型号
		device.put("phoneSystem", PublicParameter.getPublicParameter().systemVersion);// 手机系统
		device.put("phoneBrand", PublicParameter.getPublicParameter().phoneManufacturer);// 手机品牌
		this.version = String.valueOf(PublicParameter.getPublicParameter().appVersion);
		this.offsetTime = ConfigurationText.configurationText.serverTime;
		this.promotionChannel = PublicParameter.getPublicParameter().appChannel;// 渠道号
	}

	/**
	 * 设置可变参数c
	 */
	public void setChangingValue() {
		location = new JSONObject();
		location.put("_IP", PublicParameter.getPublicParameter().getIpAddress());
		double[] locationArray = PublicParameter.getPublicParameter().getLocation();
		location.put("longitude", String.valueOf(locationArray[0]));// 经度
		location.put("latitude", String.valueOf(locationArray[1]));// 维度
		this.createTime = System.currentTimeMillis();
		this.networkType = PublicParameter.getPublicParameter().getCurrentNetType();
	}

	/**
	 * 设置固定参数
	 * 
	 * @return
	 */
	public int getFaId() {
		return faId;
	}

	public void setFaId(int faId) {
		this.faId = faId;
	}

	public void toJson() {
	}

	public JSONObject getLocation() {
		return location;
	}

	public void setLocation(JSONObject location) {
		this.location = location;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getOffsetTime() {
		return offsetTime;
	}

	public void setOffsetTime(String offsetTime) {
		this.offsetTime = offsetTime;
	}

	public int getNetworkType() {
		return networkType;
	}

	public String getPromotionChannel() {
		return promotionChannel;
	}

	public void setPromotionChannel(String promotionChannel) {
		this.promotionChannel = promotionChannel;
	}

	public void setNetworkType(int networkType) {
		this.networkType = networkType;
	}

	public JSONObject getDevice() {
		return device;
	}

	public void setDevice(JSONObject device) {
		this.device = device;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	@Override
	public String toString() {
		return "BaseLog [location=" + location + ", createTime=" + createTime + ", networkType=" + networkType
				+ ", device=" + device + ", version=" + version + ", source=" + source + "]";
	}

}
