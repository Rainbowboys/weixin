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
	 * ��XML��ʽ��Ϣת��������
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
		// ��request��ȡ������
		InputStream ins = req.getInputStream();

		Document doc = reader.read(ins);
		// ��ȡ���ڵ�
		Element root = doc.getRootElement();
		// ��ȡroot��װ��list
		List<Element> list = root.elements();
		for (Element e : list) {
			map.put(e.getName(), e.getText());
		}
		ins.close();
		return map;
	}

	/**
	 * ����Ϣ��������ת��XML����
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
	 * ��ʼ���ı���Ϣ
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
	 * ��ʼ�������ı���Ϣ
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
			buffer.append("����" + data.getCity() + "\n");
			buffer.append("ƴ��" + data.getPinyin() + "\n");
			buffer.append("����" + data.getWeather() + "\n");
			buffer.append("�����" + data.getH_tmp() + "\n");
			buffer.append("�����" + data.getL_tmp() + "\n");
			buffer.append("����ʱ��" + data.getTime() + "\n");
		}

		return buffer.toString();
	}

	/**
	 * ��ʼ��ͼ����Ϣ
	 * 
	 * @param ToUserName
	 * @param FromUserName
	 * @return
	 */
	public static String initNewsMessage(String ToUserName, String FromUserName) {
		// ��ʼ��list ���news ��Ϣ��
		List<News> list = new ArrayList<News>();
		// ʵ���� ͼ����Ϣ
		NewsMessage newsMessage = new NewsMessage();
		// ʵ���� ��Ϣ��
		News news = new News();
		news.setTitle("С��Ŀ�������");
		news.setDescription(
				"JAVA�����������֣����Դ�����ʦ���ֵ���ϣ�JACOPOGOSLING���ſƲ�����˹�֣�Andrea.WILIER���������ǡ�������VITTORIO(ά�����)ALBERTO.BARIN���������ء����𣩣�JAVA���������Ŷӵ�Ŭ����ͬʱ�������Ҳ�ǳ���JAVA�ŶӶԿ��ȵ�ϲ����������Java������������JAVA��ӡ��������צ�۵���Ӣ�����ƣ���ʢ�����ȶ�������JAVA���г��е���೵�����ƶ��뿧���йء���DECAF(ȥ������Ŀ���)��ESPRESSO(Ũ������)�Լ�MOKA��Ħ�����ȵȣ�ͬʱJAVA��LOGOҲ����һ��ð�������Ŀ��ȡ�");
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
	 * ��ͼ����Ϣ
	 * 
	 * @param ToUserName
	 * @param FromUserName
	 * @return
	 */
	public static String MuchNewsMessage(String ToUserName, String FromUserName) {
		// ��ʼ��list ���news ��Ϣ��
		List<News> list = new ArrayList<News>();
		// ʵ���� ͼ����Ϣ
		NewsMessage newsMessage = new NewsMessage();
		// ʵ���� ��Ϣ��
		for (int i = 0; i < 5; i++) {
			News news = new News();
			news.setTitle("С��Ŀ�������");
			news.setDescription(
					"JAVA�����������֣����Դ�����ʦ���ֵ���ϣ�JACOPOGOSLING���ſƲ�����˹�֣�Andrea.WILIER���������ǡ�������VITTORIO(ά�����)ALBERTO.BARIN���������ء����𣩣�JAVA���������Ŷӵ�Ŭ����ͬʱ�������Ҳ�ǳ���JAVA�ŶӶԿ��ȵ�ϲ����������Java������������JAVA��ӡ��������צ�۵���Ӣ�����ƣ���ʢ�����ȶ�������JAVA���г��е���೵�����ƶ��뿧���йء���DECAF(ȥ������Ŀ���)��ESPRESSO(Ũ������)�Լ�MOKA��Ħ�����ȵȣ�ͬʱJAVA��LOGOҲ����һ��ð�������Ŀ��ȡ�");
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
	 * ��װ������Ϣ
	 * 
	 * @param ToUserName
	 * @param FromUserName
	 * @return
	 */
	public static String initMusicMessgae(String ToUserName, String FromUserName) {
		MusicMessage musicMessage = new MusicMessage();
		Music music = new Music();
		music.setThumbMediaId("kHWR3Ml04hGQI7obAdfdk_1_lU1r6kizcBCSLzuM4Pm6wr57WiQg1jC9WHlMiQ4z");
		music.setTitle("����������");
		music.setMusicUrl("http://wxtest.tunnel.qydev.com/weixin/music/test.mp3");
		music.setHQMusicUrl("http://wxtest.tunnel.qydev.com/weixin/music/test.mp3");
		music.setDescription("����������Ϣ�ظ�");
		musicMessage.setFromUserName(ToUserName);
		musicMessage.setToUserName(FromUserName);
		musicMessage.setMsgType(MESSAGE_MUSIC);
		musicMessage.setCreateTime(new Date().getTime());
		musicMessage.setMusic(music);
		return MessageUtil.musicToXML(musicMessage);
	}

	public static Menu initMene() {

		Menu menu = new Menu();
		// ������͵Ĳ˵�
		ClickButton click1 = new ClickButton();
		click1.setType(MESSAGE_CLICK);
		click1.setName("����˵�");
		click1.setKey("11");

		ClickButton click2 = new ClickButton();
		click2.setName("ɨ���¼�");
		click2.setType("scancode_push");
		click2.setKey("12");

		ClickButton click3 = new ClickButton();
		click3.setName("��λ�¼�");
		click3.setType("location_select");
		click3.setKey("13");

		ViewButton viewButton = new ViewButton();
		viewButton.setName("view�˵�");
		viewButton.setType("view");
		viewButton.setUrl("http://www.soso.com");

		Button button = new Button();
		button.setName("�˵�");
		button.setType(MESSAGE_CLICK);
		button.setSub_button(new Button[] { click2, click3 });
		menu.setButton(new Button[] { click1, viewButton, button });
		return menu;

	}

	/**
	 * ������Ϣװ��XML
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
	 * 1 �˵�ѡ�������
	 * 
	 * @return
	 */

	public static String firtMenu() {
		StringBuffer sb = new StringBuffer();
		sb.append("����Ԥ������Ӧ�ô����仯�Ĺ��ɣ����ݵ�ǰ�����ڵ��������ƣ���ĳһ��δ��һ��ʱ����");
		sb.append(
				"������״������Ԥ�⡣���Ǹ��ݶ�������ͼ������ͼ�ķ���������й��������ϡ����κͼ����ص㡢Ⱥ�ھ�����ۺ��о��������ġ����ҹ���������̨��������ͼ�������ҹ�����ġ�����һ�š�����������ȡ�ġ�����������ͼ��Ƭ���з��������������Ԥ����׼ȷ�ʡ�");
		sb.append("����Ԥ����ʱЧ�ĳ���ͨ����Ϊ���֣���������Ԥ����2��3�죩����������Ԥ����4��9�죩����������Ԥ����10��15�����ϣ����������̨ÿ�첥�ŵ���Ҫ�Ƕ�������Ԥ����");
		return sb.toString();
	}

	/**
	 * ���˵�
	 * 
	 * @return
	 */
	public static String MenuText() {
		StringBuffer sb = new StringBuffer();
		sb.append("��ӭ��עС��Ŀ������ں�   �밴�����¹������\n\n");
		sb.append("1 ����Ԥ��\n");
		sb.append("2 �ֻ�������\n");
		sb.append("3 �ɼ���ѯ\n");
		sb.append("? �������˵�\n");
		return sb.toString();
	}

	/**
	 * 2 �˵�ѡ�������
	 * 
	 * @return
	 */

	public static String secondText() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * ͼ����Ϣת��XML��ʽ
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
	 * ͼƬ��Ϣת��XML��ʽ
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
