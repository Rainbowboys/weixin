package com.weixin.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.weixin.entity.AccessToken;
import com.weixin.entity.Data;
import com.weixin.entity.Weather;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WeixinUtil {
	private static final String APPID = "wx68acc211459c5921";
	private static final String T_URL = "http://api.fanyi.baidu.com/api/trans/vip/translate?q=Q&from=F&to=T&appid=APPID&salt=SALT&sign=SIGN";
	private static final String APPSECRET = "53a983c2ff974f9ff7a6599c53683575";
	private static final String MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	private static final String MENU_DELETE = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	private static final String MENU_FIND = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	private static final String ACCEPT_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";

	public static JSONObject doGetStr(String url) throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = null;
		JSONObject jsonObject = null;
		try {
			response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, "UTF-8");
			if (result != null) {
				jsonObject = JSONObject.fromObject(result);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (response != null) {
				response.close();
			}
		}
		return jsonObject;
	}

	/**
	 * Dopost 请求
	 * 
	 * @param url
	 * @param outStr
	 * @return
	 * @throws IOException
	 */
	public static JSONObject doPostStr(String url, String outStr) throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		CloseableHttpResponse response = null;
		JSONObject jsonObject = null;
		try {
			httpPost.setEntity(new StringEntity(outStr, "utf-8"));
			response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, "utf-8");
			jsonObject = JSONObject.fromObject(result);

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (response != null) {
				response.close();
			}
		}
		return jsonObject;
	}

	/**
	 * 获取access_token
	 * 
	 * @return
	 * @throws IOException
	 */
	public static AccessToken geAccessToken() throws IOException {
		AccessToken accessToken = new AccessToken();
		BufferedReader br = null;
		FileInputStream is = null;
		InputStreamReader reader = null;
		FileOutputStream os = null;
		String url = ACCEPT_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
		File file = new File("D:\\token.txt");
		if (!file.exists() || !file.isFile()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			is = new FileInputStream(file);
			reader = new InputStreamReader(is);
			br = new BufferedReader(reader);
			String line = br.readLine();
			String token = line.substring(0, line.indexOf(","));
			String timestamp = line.substring(line.indexOf(",") + 1, line.lastIndexOf(","));
			long oldtime = Long.valueOf(timestamp);
			long newtime = System.currentTimeMillis();
			int expires_in = Integer.valueOf(line.substring(line.lastIndexOf(",") + 1));
			if ((newtime - oldtime) > expires_in * 1000) {
				try {
					JSONObject js = null;
					js = doGetStr(url);
					if (js != null) {
						accessToken.setToken(js.getString("access_token"));
						accessToken.setExpiresin(js.getInt("expires_in"));
						byte[] buf = new String(accessToken.getToken() + "," + System.currentTimeMillis() + ","
								+ accessToken.getExpiresin()).getBytes();
						os = new FileOutputStream(file);
						os.write(buf, 0, buf.length);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (os != null) {
						os.close();
					}
					if (br != null) {
						br.close();
					}
					if (is != null) {
						is.close();
					}
				}
			} else {
				accessToken.setToken(token);
				accessToken.setExpiresin(expires_in);

			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return accessToken;
	}

	/**
	 * 上传文件 返回 media_id
	 * 
	 * @param path
	 * @param access_token
	 * @param type
	 * @throws IOException
	 */

	public static String upload(String path, String access_token, String type) throws IOException {
		File file = new File(path);
		if (!file.exists() || !file.isFile()) {
			throw new IOException("文件不存在");
		}
		String url = UPLOAD_URL.replace("ACCESS_TOKEN", access_token).replace("TYPE", type);

		// URL
		URL urlObj = new URL(url);
		// 连接
		HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();

		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);

		// 设置请求头信息
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setRequestProperty("Charset", "UTF-8");

		// 设置边界
		String BOUNDARY = "----------" + System.currentTimeMillis();
		conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");

		byte[] head = sb.toString().getBytes("utf-8");

		// 获取输出流
		OutputStream os = new DataOutputStream(conn.getOutputStream());

		// 输出表头
		os.write(head);

		// 文件正文部分
		// 把文件已流文件的方式 推入到url中
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1) {
			os.write(bufferOut, 0, bytes);
		}
		in.close();

		// 结尾部分
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线

		os.write(foot);

		os.flush();
		os.close();

		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result = null;
		try {
			// 定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		JSONObject jsonObj = JSONObject.fromObject(result);
		System.out.println(jsonObj);
		String typeName = "media_id";
		if (!"image".equals(type)) {
			typeName = type + "_media_id";
		}
		String mediaId = jsonObj.getString(typeName);
		return mediaId;
	}

	/**
	 * 菜单创建
	 * 
	 * @param token
	 * @param menu
	 * @return
	 */
	public static int createMenu(String token, String menu) {
		String url = MENU_URL.replace("ACCESS_TOKEN", token);
		int result = 0;
		try {
			JSONObject jsonObject = doPostStr(url, menu);
			if (jsonObject != null) {
				result = jsonObject.getInt("errcode");
			}
			System.out.println(jsonObject);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 菜单查询
	 * 
	 * @param token
	 * @return
	 */
	public static String findMenu(String token) {
		String result = null;
		String url = MENU_FIND.replace("ACCESS_TOKEN", token);
		try {
			JSONObject jsonObject = doGetStr(url);
			if (jsonObject != null) {
				System.out.println(jsonObject);
				result = JSONObject.fromObject(jsonObject).toString();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 删除菜单
	 * 
	 * @param token
	 * @return
	 */
	public static int deleteMenu(String token) {
		int result = 1;
		String url = MENU_DELETE.replace("ACCESS_TOKEN", token);
		try {
			JSONObject jsonObject = doGetStr(url);
			if (jsonObject != null) {
				result = jsonObject.getInt("errcode");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 百度翻译
	 * 
	 * @param q
	 * @param from
	 * @param to
	 * @return
	 * @throws UnsupportedEncodingException
	 */

	public static String BaiduTranslate(String q, String from, String to) throws UnsupportedEncodingException {
		// //随机数，用于生成md5值 盐值
		String salt = StringUtil.getRandomString(10);
		String  text = null;// 翻译结果
		StringBuffer buffer=new StringBuffer();

		StringBuilder sb = new StringBuilder();
		sb.append(Consatants.BAIDUAPPID).append(q).append(salt).append(Consatants.BAIDUAPPSECRET);
		String MD5String = MD5.GetMD5Code(sb.toString());
		String url = T_URL.replace("SALT", salt).replace("Q", URLEncoder.encode(q, "UTF-8")).replace("F", from)
				.replace("T", to).replace("APPID", Consatants.BAIDUAPPID).replace("SIGN", MD5String);
		try {
			JSONObject jsonObject = doGetStr(url);
			if (jsonObject != null) {
				// 开发者自行处理错误，本示例失败返回为null
				try {
					String error_code = jsonObject.getString("error_code");
					if (error_code != null) {
						System.out.println("出错代码:" + error_code);
						System.out.println("出错信息:" + jsonObject.getString("error_msg"));
						return null;
					}
				} catch (Exception e) {
				}
			//	JSONArray array = (JSONArray) jsonObject.get("trans_result");
				List<Map> list=(List<Map>) jsonObject.get("trans_result");
				//JSONObject dst = (JSONObject) array.get(0);
				for (Map map : list) {
					buffer.append(map.get("dst"));
				}
			//	text = URLDecoder.decode(dst.getString("dst"), "UTF-8");
			  text = URLDecoder.decode(buffer.toString(), "UTF-8");

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;

	}

	/**
	 * 天气预报
	 * 
	 * @throws IOException
	 */
	public static Weather ForWeather(String city) throws IOException {
		String urlString = Consatants.WEATHERURL.replace("CITY", URLEncoder.encode(city, "UTF-8"));
		StringBuffer buffer = new StringBuffer();
		Weather weather = null;
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			// 填入apikey到HTTP header
			conn.setRequestProperty("apikey", Consatants.APPKEY);
			conn.connect();
			InputStream in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();
			in.close();
			JSONObject jsonObject = JSONObject.fromObject(buffer.toString());
			weather = (Weather) JSONObject.toBean(jsonObject, Weather.class);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return weather;

	}
}
