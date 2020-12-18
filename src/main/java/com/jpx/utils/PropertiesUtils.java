package com.jpx.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * 配置文件的工具类
 * @author HASEE
 *
 */
public class PropertiesUtils {

	private static Properties properties;
	
	static {
		//把文件转换成输入流
		InputStream is = PropertiesUtils.class.getClassLoader().getResourceAsStream("config.properties");
		properties = new Properties();
		try {
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取properties对象
	 * @return
	 */
	public static Properties getProperties() {
		return properties;
	}
	
	/**
	 * 获取配置文件里面的内容
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		return properties.getProperty(key);
	}
}
