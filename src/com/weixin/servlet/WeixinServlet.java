package com.weixin.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.eclipse.jdt.internal.compiler.util.Sorting;

import com.weixin.util.Check;
import com.weixin.util.MessageUtil;
import com.weixin.util.WeixinUtil;

public class WeixinServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String timestamp = req.getParameter("timestamp");
		String signature = req.getParameter("signature");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		boolean flag = Check.CheckString(signature, timestamp, nonce);
		PrintWriter out = resp.getWriter();
		// 判断校验成功后返回随机字符串
		if (flag) {
			out.write(echostr);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();

		try {
			Map<String, String> map = MessageUtil.XMLToMap(req);
			String FromUserName = map.get("FromUserName");
			String ToUserName = map.get("ToUserName");
			String MsgType = map.get("MsgType");
			String Content = map.get("Content");

			String message = null;
			if (MsgType.equals(MessageUtil.MESSAGE_TEXT)) {

				if ("1".equals(Content)) {
					message = MessageUtil.initText(ToUserName, FromUserName, MessageUtil.firtMenu());
					// System.out.println(message);
				} else if ("2".equals(Content)) {
					message = MessageUtil.initNewsMessage(ToUserName, FromUserName);
					System.out.println(message);
				} else if ("?".equals(Content) || "？".equals(Content)) {
					message = MessageUtil.initText(ToUserName, FromUserName, MessageUtil.MenuText());

				} else if ("3".equals(Content)) {
					// message = MessageUtil.MuchNewsMessage(ToUserName,
					// FromUserName);
					message = MessageUtil.initImageMessgae(ToUserName, FromUserName);
					System.out.println(message);
				} else if ("4".equals(Content)) {
					message = MessageUtil.initMusicMessgae(ToUserName, FromUserName);
				} else if (Content.trim().endsWith("天气") || Content.trim().startsWith("天气")) {
					Content = Content.replaceAll("天气", "").trim();
					Content = MessageUtil.initWeatherText(WeixinUtil.ForWeather(Content));
					message = MessageUtil.initText(ToUserName, FromUserName, Content);
				} else if (Content.startsWith("翻译")) {
					Content = Content.replaceAll("^翻译", "").trim();
					Content = WeixinUtil.BaiduTranslate(Content, "en", "zh");
					message = MessageUtil.initText(ToUserName, FromUserName, Content);
				}
			}
			// 事件推送
			else if (MsgType.equals(MessageUtil.MESSAGE_EVENT)) {
				String event = map.get("Event");
				if (event.equals(MessageUtil.MESSAGE_SUBSCRIBE)) {
					message = MessageUtil.initText(ToUserName, FromUserName, MessageUtil.MenuText());

				} else if (event.equals(MessageUtil.MESSAGE_EVEVT_CLICK)) {
					String key = map.get("EventKey");
					message = MessageUtil.initText(ToUserName, FromUserName, key);

				} else if (event.equals(MessageUtil.MESSAGE_UNSUBSCRIBE)) {

				} else if (event.equals(MessageUtil.MESSAGE_EVEVT_VIEW)) {
					String url = map.get("EventKey");
					message = MessageUtil.initText(ToUserName, FromUserName, url);
				}
			} // 定位消息
			else if (MsgType.equals(MessageUtil.MESSAGE_LOCATION)) {

				String text = map.get("Label");
				message = MessageUtil.initText(ToUserName, FromUserName, text);
			}
			// 图片消息回复
			else if (MsgType.equals(MessageUtil.MESSAGE_IMAGE)) {
				message = MessageUtil.initImageMessgae(ToUserName, FromUserName);
			}
			out.write(message);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
	
	public static void main(String[] args) {
	
		boolean flag=false;
//		Integer num=Integer.parseInt(name,10);
		String name="123";
		//Integer num =new Integer(new String("123"));
		Integer num=Integer.valueOf(name);
		//String num=String.valueOf(flag);
		System.out.println(num);
	}
}
