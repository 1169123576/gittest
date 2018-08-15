package to8to_app.to8to_app_Factory;

import io.appium.java_client.android.AndroidDriver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;

public class OverallSituation {
	 private static String devicesName=null;
	 private static String platformVersion=null;
	public AndroidDriver setUp(AndroidDriver driver) throws Exception{
		File classpathRoot = new File(System.getProperty("user.dir"));
		File appDir = new File(classpathRoot, "apps");
		File app = new File(appDir, "app-xiaomi-release(0720).apk");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		devicesName=OverallSituation.getDeviceName(devicesName);
		platformVersion=OverallSituation.platformVersion(platformVersion);
		capabilities.setCapability("noReset", true);
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("deviceName", devicesName);
		capabilities.setCapability("platformVersion",platformVersion);
		capabilities.setCapability("app", app.getAbsolutePath());
		capabilities.setCapability("appActivity",
				"com.to8to.steward.TLaunchActivity");
    	//capabilities.setCapability("sessionOverride", true); // 每次启动时覆盖session，否则第二次后运行会报错不能新建session
    	driver = new AndroidDriver(new URL("http://192.168.3.95:4723/wd/hub"),
    			capabilities);
    	//System.out.println("--driver--"+driver);
    	return driver;
	}
	//截图公共方法
	@Test
	public static void snapshot(TakesScreenshot driver, File screenShot) {
		//获取当前工作文件夹+"devicesName:"+devicesName
		devicesName=OverallSituation.getDeviceName(devicesName);
		devicesName=devicesName+"-";
		String screenShotName=screenShot.getName();	
		System.out.println("screenShotName++++"+screenShotName);
		StringBuffer sBuffer = new StringBuffer();
		StringBuffer devicesname= sBuffer.append(screenShotName).insert(0,devicesName);
        String currentpath = System.getProperty("user.dir");     
        File imgDir = new File(currentpath, "Images");
        File scrFile = driver.getScreenshotAs(OutputType.FILE); 
        //huqu 
        try {
            System.out.println("截图保存目录:" + imgDir + "\\" + devicesname + "\n");
            //保存屏幕截图
            FileUtils.copyFile(scrFile, new File(imgDir + "\\" + devicesname));          
        } catch (IOException e) {
            System.out.println("保存截图失败");
            e.printStackTrace();
        } finally{
            System.out.println("已保存的屏幕截图:" + currentpath); 
       }
    }
	//获取当前時間
	public String getDatetime(){
		SimpleDateFormat date = new SimpleDateFormat("yyyymmdd hhmmss");
		return date.format(new Date());
	}
	//获取设备号name：集成adb命令
	public static String getDeviceName(String devicesName){
		String command="adb devices";
		String s="设备ID：";
		String line = null;
		StringBuilder sbuider = new StringBuilder();
		Runtime runtime = Runtime.getRuntime();
		try {
			Process process = runtime.exec(command);
			BufferedReader	bufferedReader = new BufferedReader
				(new InputStreamReader(process.getInputStream()));
			while ((line = bufferedReader.readLine()) != null) {
				sbuider.append(line );
				if (line.contains(s)) {
					System.out.println(line);
				}
			}
			//分割出devices
			//System.out.println("原字符："+sbuider+";");
			String devicesCode=sbuider.toString();
			//System.out.println("设备号："+devicesCode+";");
			//已第24個字符开始,到右边-7个位置结束
			devicesName=StringUtils.substring(devicesCode,24, -7); 
			System.out.println("设备号截取后："+devicesName+";");
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return devicesName;
	}
	//获取Android版本号
	public static String platformVersion(String platformVersion){
		//adb -devices
		String command="adb shell getprop ro.build.version.release";
		String s="android版本：";
		String line = null;
		//System.out.println("command："+command);
		StringBuilder sb = new StringBuilder();
		Runtime runtime = Runtime.getRuntime();
		Process process;
		try {
			process = runtime.exec(command);
			BufferedReader	bufferedReader = new BufferedReader
					(new InputStreamReader(process.getInputStream()));
			while ((line = bufferedReader.readLine()) != null) {
					sb.append(line);
					if (line.contains(s)) {
						System.out.println(line);
					}
				}
			platformVersion=sb.toString();
			//System.out.println("原字符："+sb+";");
			System.out.println("新字符："+platformVersion+";");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return platformVersion;
	}
}