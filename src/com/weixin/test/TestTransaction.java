package com.weixin.test;

import java.io.IOException;

import com.weixin.util.WeixinUtil;

public class TestTransaction {
	public static void main(String[] args) throws IOException {
//		String text =WeixinUtil.BaiduTranslate("beautiful girl", "en", "zh");
//		System.out.println(text);
		WeixinUtil.ForWeather("beijing");
	}

}
