package com.weixin.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Check {
	private static final String token = "weixin";

	public static boolean CheckString(String signature, String timestamp, String nonce) {
		String[] tmpArr = new String[] { timestamp, token, nonce };
		// 排序
		Arrays.sort(tmpArr);
		// 获取字符串
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < tmpArr.length; i++) {
			buffer.append(tmpArr[i]);
		}
		String tmpStr = buffer.toString();
		String sha1 = SHA1(tmpStr);
		if (sha1.equals(signature)) {
			return true;
		}
		return false;
	}

	public static String SHA1(String decript) {
		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
			digest.update(decript.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

}
