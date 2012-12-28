package com.huoli.lyantool.web;

import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.huoli.lyantool.domain.WeiXinBean;
import com.huoli.lyantool.service.impl.WeixinService;
import com.huoli.lyantool.util.AddSHA1;
import com.huoli.lyantool.util.HttpRequest;
import com.huoli.lyantool.util.WeiXinTool;

@Controller
public class MessageController {
	private static Logger log = Logger.getLogger(MessageController.class);
	
	@Autowired
	private WeixinService weixinService;

	@RequestMapping("/weixin")
	public void weixin(HttpServletRequest request, 
			HttpServletResponse response, String signature, String timestamp,
			String nonce, String echostr) throws Exception {
		response.setContentType("text/html;charset=utf-8");

		// @RequestBody String requestBody,
		// String requestStr = requestBody;
		String requestStr = HttpRequest.getContent(request);
		
//		String requestStr = requestBody;//test

		log.info("come from weixin:" + requestStr);
		WeiXinBean message = WeiXinTool.parseMessage(requestStr);
		log.info("come from weixin content:" + message.getContent());
		PrintWriter print = response.getWriter();

		// logger.info("signature: " + signature + "\ntimestamp: " + timestamp
		// + "\nonce: " + nonce + "\nechostr: " + echostr);
//		 timestamp = "sdf"; //for test
//		 nonce = "sdf"; //for test

		String[] array = new String[3];
		array[0] = "lyan"; // 微信公众平台上填写的token
		array[1] = timestamp;
		array[2] = nonce;
		Arrays.sort(array);
		String source = array[0] + array[1] + array[2];
		// logger.info("source: " + source);
		String destSource = AddSHA1.SHA1(source);
		// logger.info("destSource: " + destSource);

		if (destSource.equals(signature)) { // 来自微信(should change)
			String fromUser = message.getFromUserName();
			String toUser = message.getToUserName();

			String outcome = this.weixinService.search(message.getContent(),
					fromUser);
			message.setContent(outcome);

			message.setToUserName(fromUser);
			message.setFromUserName(toUser);
			print.print(WeiXinTool.wrapMessage(message));
		} else {
			print.print("非法请求");
		}

		print.flush();
		print.close();

	}
}
