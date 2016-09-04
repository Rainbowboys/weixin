package com.weixin.test;

import java.io.IOException;

import com.weixin.entity.AccessToken;
import com.weixin.util.MessageUtil;
import com.weixin.util.WeixinUtil;

import net.sf.json.JSONObject;

public class TestGetToken {
	public static void main(String[] args) throws IOException {
		AccessToken token = new AccessToken();
		token = WeixinUtil.geAccessToken();
		System.out.println("票据" + token.getToken());
		System.out.println("有效期" + token.getExpiresin());

		// String path="D:\\test.jpg";
		// //String path="‪‪D:\\前端面试问题.jpg";
		// String mediaid=WeixinUtil.upload(path, token.getToken(), "thumb");
		// System.out.println(mediaid);
		String menu = JSONObject.fromObject(MessageUtil.initMene()).toString();
		System.out.println(menu);

		int result = WeixinUtil.createMenu(token.getToken(), menu);
		if (result == 0) {
			System.out.println("菜单创建成功");
		} else {
			System.out.println("错误代码" + result);
		}
//		int result = WeixinUtil.deleteMenu(token.getToken());
//		if (result == 0) {
//			System.out.println("删除成功");
//		} else if (result == 1) {
//			System.out.println("菜单不存在");
//		} else {
//			System.out.println("错误代码" + result);
//		}

	}

}
