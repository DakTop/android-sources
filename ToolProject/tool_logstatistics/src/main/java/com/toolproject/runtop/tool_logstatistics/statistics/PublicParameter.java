package com.toolproject.runtop.tool_logstatistics.statistics;

import cn.caifuqiao.activity.MainActivity;
import cn.caifuqiao.request.ParameterTimelyManager;
import cn.caifuqiao.tool.ApplicationUtils;
import cn.caifuqiao.tool.HelpUtil;
/**
 * 统计公共参数
 * @author hao
 *
 */
public class PublicParameter {
	public int appVersion;//app版本号
	public String systemVersion;//系统版本号
	public String phoneModel;//手机型号
	public String phoneManufacturer;//手机品牌
	public int netWorkType;//网络类型
	public String uuId;
	public double [] location;//位置 下标0=经度 下标1=纬度
	public String ip;
	public String appChannel;
	private static PublicParameter publicParameter;
    private PublicParameter(){
    	
    }
    public static PublicParameter getPublicParameter(){
    	if(publicParameter==null){
    		publicParameter=new PublicParameter();
    		publicParameter.getBasicParameter();
    	}
    	return publicParameter;
    }
    
   /**
    * 获取公共参数
    * @param context
    */
    private void getBasicParameter(){
    	if(publicParameter!=null){
    		new Thread(){
    			@Override
    			public void run() {
    				publicParameter.appChannel=HelpUtil.getAppChannel();
    				publicParameter.appVersion=ApplicationUtils.getVersionCode();
    	    		publicParameter.systemVersion=ApplicationUtils.getSystemVersion();
    	    		publicParameter.phoneModel=ApplicationUtils.getPhoneModel();
    	    		publicParameter.phoneManufacturer=ApplicationUtils.getPhoneManufacturer();
    	    		publicParameter.location=ApplicationUtils.getLocation(1000*60,0);
    	    		publicParameter.uuId=ParameterTimelyManager.getUUID();
    	    		publicParameter.netWorkType=ApplicationUtils.getCurrentNetType();
    	    		publicParameter.ip=ApplicationUtils.getIpAddress();
    			}
    		}.start();
    		
    	}
    }
    
    /**
     * 获取位置信息 下标0=经度 下标1=纬度
     * @return 经纬度 为0 为获取到位置信息
     */
    public double [] getLocation(){
    	return ApplicationUtils.getLocation();
    }
    
    /**
     * 获取网络类型
     * @return
     */
    public int getCurrentNetType(){
    	return ApplicationUtils.getCurrentNetType();
    }
    /**
     * 获取IP地址
     * @return  null=未获取到IP
     */
    public String getIpAddress(){
    	return ApplicationUtils.getIpAddress();
    }
    
    /**
     * 获取当前页面
     * @return
     */
    public String getCurrentActivity(){
    	String currentActivity = ApplicationUtils.getCurrentActivity();
    	if(MainActivity.class.getSimpleName().equals(currentActivity)){
    		return MainActivity.getFragmentById(-1).getSimpleName();
    	}else{
    		return currentActivity;
    	}
    }
    /**
     * 获取上一个页面
     * @return
     */
    public String getBeforeActivity() {
    	return ApplicationUtils.getBeforeActivity();
    }
    
}
