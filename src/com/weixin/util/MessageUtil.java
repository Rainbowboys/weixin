package com.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.weixin.entity.Data;
import com.weixin.entity.Image;
import com.weixin.entity.ImageMessage;
import com.weixin.entity.Music;
import com.weixin.entity.MusicMessage;
import com.weixin.entity.News;
import com.weixin.entity.NewsMessage;
import com.weixin.entity.TextMessage;
import com.weixin.entity.Weather;
import com.weixin.menu.Button;
import com.weixin.menu.ClickButton;
import com.weixin.menu.Menu;
import com.weixin.menu.ViewButton;

public class MessageUtil {
	/**
	 * 将XML格式信息转化成数组
	 * 
	 * @throws DocumentException
	 * @throws IOException
	 * 
	 */
	public static final String MESSAGE_TEXT = "text";
	public static final String MESSAGE_IMAGE = "image";
	public static final String MESSAGE_VOICE = "voice";
	public static final String MESSAGE_VIDEO = "video";
	public static final String MESSAGE_SHORTVIDEO = "shortvideo";
	public static final String MESSAGE_LOCATION = "location";
	public static final String MESSAGE_LINK = "link";
	public static final String MESSAGE_EVENT = "event";
	public static final String MESSAGE_SUBSCRIBE = "subscribe";
	public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
	public static final String MESSAGE_SCAN = "SCAN";
	public static final String MESSAGE_CLICK = "click";
	public static final String MESSAGE_EVEVT_CLICK = "CLICK";
	public static final String MESSAGE_EVEVT_VIEW = "VIEW";
	public static final String MESSAGE_VIEW = "view";
	public static final String MESSAGE_LOCATIONS = "location_select";
	public static final String MESSAGE_NEWS = "news";
	private static final String MESSAGE_MUSIC = "music";

	@SuppressWarnings("all")
	public static Map<String, String> XMLToMap(HttpServletRequest req) throws DocumentException, IOException {
		Map<String, String> map = new HashMap<String, String>();

		SAXReader reader = new SAXReader();
		// 从request获取输入流
		InputStream ins = req.getInputStream();

		Document doc = reader.read(ins);
		// 获取根节点
		Element root = doc.getRootElement();
		// 获取root封装成list
		List<Element> list = root.elements();
		for (Element e : list) {
			map.put(e.getName(), e.getText());
		}
		ins.close();
		return map;
	}

	/**
	 * 将消息对象类型转成XML类型
	 * 
	 * @param textMessage
	 * @return
	 */

	public static String MegToXML(TextMessage textMessage) {
		XStream xStream = new XStream();
		xStream.alias("xml", TextMessage.class);
		String xml = xStream.toXML(textMessage);
		return xml;
	}

	/**
	 * 初始化文本消息
	 * 
	 * @param ToUserName
	 * @param FromUserName
	 * @param Content
	 * @return
	 */

	public static String initText(String ToUserName, String FromUserName, String Content) {
		TextMessage textMessage = new TextMessage();
		textMessage.setFromUserName(ToUserName);
		textMessage.setCreateTime(new Date().getTime());
		textMessage.setToUserName(FromUserName);
		textMessage.setMsgType(MessageUtil.MESSAGE_TEXT);
		textMessage.setContent(Content);
		return MessageUtil.MegToXML(textMessage);
	}

	/**
	 * 初始化天气文本消息
	 * 
	 * @param ToUserName
	 * @param FromUserName
	 * @param Content
	 * @return
	 */

	public static String initWeatherText(Weather weather) {
		StringBuffer buffer = new StringBuffer();
		if (weather.getErrNum() == 0) {
			Data data = weather.getRetData();
			buffer.append("城市" + data.getCity() + "\n");
			buffer.append("拼音" + data.getPinyin() + "\n");
			buffer.append("天气" + data.getWeather() + "\n");
			buffer.append("最高温" + data.getH_tmp() + "\n");
			buffer.append("最低温" + data.getL_tmp() + "\n");
			buffer.append("发布时间" + data.getTime() + "\n");
		}

		return buffer.toString();
	}

	/**
	 * 初始化图文消息
	 * 
	 * @param ToUserName
	 * @param FromUserName
	 * @return
	 */
	public static String initNewsMessage(String ToUserName, String FromUserName) {
		// 初始化list 存放news 消息体
		List<News> list = new ArrayList<News>();
		// 实例化 图文消息
		NewsMessage newsMessage = new NewsMessage();
		// 实例化 消息体
		News news = new News();
		news.setTitle("小瑞的开发经历");
		news.setDescription(
				"JAVA（中文名佳沃）最初源自设计师名字的组合：JACOPOGOSLING（雅科波·高斯林）Andrea.WILIER（安德列亚·威廉）VITTORIO(维托里奥)ALBERTO.BARIN（阿尔伯特·博瑞），JAVA意大利设计团队的努力，同时这个名字也是出于JAVA团队对咖啡的喜爱，所以以Java咖啡来命名。JAVA是印度尼西亚爪哇岛的英文名称，因盛产咖啡而著名。JAVA自行车中的许多车类名称多与咖啡有关。如DECAF(去咖啡因的咖啡)，ESPRESSO(浓缩咖啡)以及MOKA（摩卡）等等，同时JAVA的LOGO也正如一杯冒着热气的咖啡。");
		news.setPicUrl(
				"http://e.hiphotos.baidu.com/baike/s%3D220/sign=cfe6c2a779f0f736dcfe4b033a54b382/7af40ad162d9f2d333aed037a8ec8a136227ccaf.jpg");
		news.setUrl("www.baidu.com");
		list.add(news);

		newsMessage.setFromUserName(ToUserName);
		newsMessage.setToUserName(FromUserName);
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setMsgType(MESSAGE_NEWS);
		newsMessage.setArticleCount(list.size());
		newsMessage.setArticles(list);
		return MessageUtil.newsToXML(newsMessage);

	}

	/**
	 * 多图文消息
	 * 
	 * @param ToUserName
	 * @param FromUserName
	 * @return
	 */
	public static String MuchNewsMessage(String ToUserName, String FromUserName) {
		// 初始化list 存放news 消息体
		List<News> list = new ArrayList<News>();
		// 实例化 图文消息
		NewsMessage newsMessage = new NewsMessage();
		// 实例化 消息体
		for (int i = 0; i < 5; i++) {
			News news = new News();
			news.setTitle("小瑞的开发经历");
			news.setDescription(
					"JAVA（中文名佳沃）最初源自设计师名字的组合：JACOPOGOSLING（雅科波·高斯林）Andrea.WILIER（安德列亚·威廉）VITTORIO(维托里奥)ALBERTO.BARIN（阿尔伯特·博瑞），JAVA意大利设计团队的努力，同时这个名字也是出于JAVA团队对咖啡的喜爱，所以以Java咖啡来命名。JAVA是印度尼西亚爪哇岛的英文名称，因盛产咖啡而著名。JAVA自行车中的许多车类名称多与咖啡有关。如DECAF(去咖啡因的咖啡)，ESPRESSO(浓缩咖啡)以及MOKA（摩卡）等等，同时JAVA的LOGO也正如一杯冒着热气的咖啡。");
			news.setPicUrl(
					"http://e.hiphotos.baidu.com/baike/s%3D220/sign=cfe6c2a779f0f736dcfe4b033a54b382/7af40ad162d9f2d333aed037a8ec8a136227ccaf.jpg");
			news.setUrl("www.baidu.com");
			list.add(news);
		}
		newsMessage.setFromUserName(ToUserName);
		newsMessage.setToUserName(FromUserName);
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setMsgType(MESSAGE_NEWS);
		newsMessage.setArticleCount(list.size());
		newsMessage.setArticles(list);
		return MessageUtil.newsToXML(newsMessage);

	}

	public static String initImageMessgae(String ToUserName, String FromUserName) {
		ImageMessage imageMessage = new ImageMessage();
		Image image = new Image();
		image.setMediaId("YEdiSAIlHbrhQ8oOG7T9nR72hn9m2zfkFLM_GRwGHelsi-2-0BbjyNx_X-LYCIYo");
		imageMessage.setFromUserName(ToUserName);
		imageMessage.setToUserName(FromUserName);
		imageMessage.setMsgType(MESSAGE_IMAGE);
		imageMessage.setCreateTime(new Date().getTime());
		imageMessage.setImage(image);
		return MessageUtil.imageToXML(imageMessage);
	}

	/**
	 * 组装音乐消息
	 * 
	 * @param ToUserName
	 * @param FromUserName
	 * @return
	 */
	public static String initMusicMessgae(String ToUserName, String FromUserName) {
		MusicMessage musicMessage = new MusicMessage();
		Music music = new Music();
		music.setThumbMediaId("kHWR3Ml04hGQI7obAdfdk_1_lU1r6kizcBCSLzuM4Pm6wr57WiQg1jC9WHlMiQ4z");
		music.setTitle("好听的音乐");
		music.setMusicUrl("http://wxtest.tunnel.qydev.com/weixin/music/test.mp3");
		music.setHQMusicUrl("http://wxtest.tunnel.qydev.com/weixin/music/test.mp3");
		music.setDescription("测试音乐消息回复");
		musicMessage.setFromUserName(ToUserName);
		musicMessage.setToUserName(FromUserName);
		musicMessage.setMsgType(MESSAGE_MUSIC);
		musicMessage.setCreateTime(new Date().getTime());
		musicMessage.setMusic(music);
		return MessageUtil.musicToXML(musicMessage);
	}

	public static Menu initMene() {

		Menu menu = new Menu();
		// 点击类型的菜单
		ClickButton click1 = new ClickButton();
		click1.setType(MESSAGE_CLICK);
		click1.setName("点击菜单");
		click1.setKey("11");

		ClickButton click2 = new ClickButton();
		click2.setName("扫码事件");
		click2.setType("scancode_push");
		click2.setKey("12");

		ClickButton click3 = new ClickButton();
		click3.setName("定位事件");
		click3.setType("location_select");
		click3.setKey("13");

		ViewButton viewButton = new ViewButton();
		viewButton.setName("view菜单");
		viewButton.setType("view");
		viewButton.setUrl("http://www.soso.com");

		Button button = new Button();
		button.setName("菜单");
		button.setType(MESSAGE_CLICK);
		button.setSub_button(new Button[] { click2, click3 });
		menu.setButton(new Button[] { click1, viewButton, button });
		return menu;

	}

	/**
	 * 音乐消息装成XML
	 * 
	 * @param musicMessage
	 * @return
	 */
	public static String musicToXML(MusicMessage musicMessage) {
		XStream xstream = new XStream();
		xstream.alias("xml", MusicMessage.class);
		return xstream.toXML(musicMessage);
	}

	/**
	 * 1 菜单选项的内容
	 * 
	 * @return
	 */

	public static String firtMenu() {
		StringBuffer sb = new StringBuffer();
		sb.append("天气预报就是应用大气变化的规律，根据当前及近期的天气形势，对某一地未来一定时期内");
		sb.append(
				"的天气状况进行预测。它是根据对卫星云图和天气图的分析，结合有关气象资料、地形和季节特点、群众经验等综合研究后作出的。如我国中央气象台的卫星云图，就是我国制造的“风云一号”气象卫星摄取的。利用卫星云图照片进行分析，能提高天气预报的准确率。");
		sb.append("天气预报就时效的长短通常分为三种：短期天气预报（2～3天）、中期天气预报（4～9天），长期天气预报（10～15天以上），中央电视台每天播放的主要是短期天气预报。");
		return sb.toString();
	}

	/**
	 * 主菜单
	 * 
	 * @return
	 */
	public static String MenuText() {
		StringBuffer sb = new StringBuffer();
		sb.append("欢迎关注小瑞的开发公众号   请按照以下规则操作\n\n");
		sb.append("1 天气预报\n");
		sb.append("2 手机归属地\n");
		sb.append("3 成绩查询\n");
		sb.append("? 调回主菜单\n");
		return sb.toString();
	}

	/**
	 * 2 菜单选项的内容
	 * 
	 * @return
	 */

	public static String secondText() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 图文消息转成XML格式
	 * 
	 * @param newsMessage
	 * @return
	 */
	public static String newsToXML(NewsMessage newsMessage) {
		XStream xStream = new XStream();
		xStream.alias("xml", NewsMessage.class);
		xStream.alias("item", News.class);
		String xml = xStream.toXML(newsMessage);
		return xml;
	}

	/**
	 * 图片消息转成XML格式
	 * 
	 * @param newsMessage
	 * @return
	 */
	public static String imageToXML(ImageMessage imageMessage) {
		XStream xStream = new XStream();
		xStream.alias("xml", ImageMessage.class);
		String xml = xStream.toXML(imageMessage);
		return xml;
	}

}
