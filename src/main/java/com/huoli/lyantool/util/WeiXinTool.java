package com.huoli.lyantool.util;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.huoli.lyantool.domain.WeiXinBean;
import com.huoli.lyantool.domain.WeiXinBean.Item;


/**
 * 微信消息解析
 * 
 * @author liaorui
 * 
 */
public class WeiXinTool {

	public static final String MESSAGE_TEXT = "text";
	public static final String MESSAGE_LOCATION = "location";
	public static final String MESSAGE_IMAGE = "image";
	public static final String MESSAGE_IMAGE_TEXT = "news";

	public static String wrapMessage(WeiXinBean message) {
		if (message == null) {
			return null;
		}
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("xml");
		root.addElement("ToUserName").addCDATA(message.getToUserName());
		root.addElement("FromUserName").addCDATA(message.getFromUserName());
		root.addElement("CreateTime").setText(message.getCreateTime());
		root.addElement("MsgType").addCDATA(message.getMsgType());
		root.addElement("Content").addCDATA(message.getContent());
		root.addElement("FuncFlag").setText(message.getFuncFlag() + "");
		if (message.getMsgType().equals(MESSAGE_IMAGE_TEXT)) {
			int articleCount = message.getArticleCount();
			if (articleCount > 0) {
				root.addElement("ArticleCount").setText(
						message.getArticleCount() + "");
				Element articlesNode = root.addElement("Articles");
				for (Item item : message.getArticles()) {
					Element itemNode = articlesNode.addElement("item");
					itemNode.addElement("Title").addCDATA(item.getTitle());
					itemNode.addElement("Description")
							.addCDATA(item.getTitle());
					itemNode.addElement("PicUrl").addCDATA(item.getTitle());
					itemNode.addElement("Url").addCDATA(item.getTitle());
				}
			}
		}

		return doc.asXML();
	}

	public static WeiXinBean parseMessage(String body) {

		if (StringUtils.isBlank(body)) {
			return null;
		}

		WeiXinBean message = new WeiXinBean();
		try {
			Document doc = DocumentHelper.parseText(body);
			Node toUserNameNode = doc.selectSingleNode("/xml/ToUserName");
			String toUserName = toUserNameNode.getText();
			message.setToUserName(toUserName);
			Node fromUserNameNode = doc.selectSingleNode("/xml/FromUserName");
			String fromUserName = fromUserNameNode.getText();
			message.setFromUserName(fromUserName);
			Node createTimeNode = doc.selectSingleNode("/xml/CreateTime");
			String createTime = createTimeNode.getText();
			message.setCreateTime(createTime);
			Node msgTypeNode = doc.selectSingleNode("/xml/MsgType");
			String msgType = msgTypeNode.getText();
			message.setMsgType(msgType);
			if (msgType.equals(MESSAGE_TEXT)) {
				Node contentNode = doc.selectSingleNode("/xml/Content");
				String content = contentNode.getText();
				message.setContent(content);
			} else if (msgType.equals(MESSAGE_LOCATION)) {
				Node locationXNode = doc.selectSingleNode("/xml/Location_X");
				String locationX = locationXNode.getText();
				if (Tools.isNumeric(locationX)) {
					message.setLocation_X(Double.parseDouble(locationX));
				}
				Node locationYNode = doc.selectSingleNode("/xml/Location_Y");
				String locationY = locationYNode.getText();
				if (Tools.isNumeric(locationY)) {
					message.setLocation_Y(Double.parseDouble(locationY));
				}
				Node scaleNode = doc.selectSingleNode("/xml/Scale");
				String scale = scaleNode.getText();
				if (Tools.isNumeric(scale)) {
					message.setScale(Integer.parseInt(scale));
				}
				Node labelNode = doc.selectSingleNode("/xml/Label");
				String label = labelNode.getText();
				message.setLabel(label);
			} else if (msgType.equals(MESSAGE_IMAGE)) {
				Node picUrlNode = doc.selectSingleNode("/xml/PicUrl");
				String picUrl = picUrlNode.getText();
				message.setContent(picUrl);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return message;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String src = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><FromUserName><![CDATA[fromUser]]></FromUserName><CreateTime>1348831860</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[this is a test]]></Content></xml> ";
		WeiXinBean message = parseMessage(src);
		System.out.println(message);
		System.out.println(wrapMessage(message));
	}

}
