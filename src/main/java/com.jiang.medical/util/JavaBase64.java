package com.jiang.medical.util;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class JavaBase64 {
	public static void main(String[] args) {

		String resource = "http://gps.7ow.cn:8088/test.html";

		// 迅雷的编码规则为：原地址前面加"AA"，后面加"ZZ"，然后进行Base64编码，最后加上迅雷下载协议"Thunder://"组成完整的下载链接
		String resourceForThunder = "Thunder://"
				+ getBASE64("AA" + resource + "ZZ");

		System.out.println(resourceForThunder);

		

	}

	// BASE64 编码
	public static String getBASE64(String s) {
		if (s == null)
			return null;
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(s.getBytes());

	}

	// BASE64 解码
	public static String getFromBASE64(String s) {
		if (s == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b);
		} catch (Exception e) {
			return null;
		}
	}

}
