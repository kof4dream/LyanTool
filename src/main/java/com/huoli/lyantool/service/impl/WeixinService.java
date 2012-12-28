package com.huoli.lyantool.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huoli.lyantool.domain.WeixinOrderDetail;
import com.huoli.lyantool.repository.OrderDao;
import com.huoli.lyantool.repository.WeixinDao;
import com.huoli.lyantool.service.IWeixinService;
import com.huoli.lyantool.util.ArgsResolver;
import com.huoli.lyantool.util.DateUtil;
import com.huoli.lyantool.util.ReflectUtil;
import com.huoli.lyantool.util.RegexUtil;


@Service
public class WeixinService implements IWeixinService {
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private WeixinDao weixinDao;
	private ArgsResolver argsResolver;
	private Map<String, String> params = new HashMap<String, String>();

	@Override
	public String search(String args, String fromUser) {
		int level = this.weixinDao.getUserLevel(fromUser);
		analyseArgs(args);
		Object outcome = null;
		
		String command = argsResolver.getCommand();
		int newLevel = 0;
		if((newLevel = isPassword(command)) > 0){
			if(!isCommandAvailable("password",level)){
				this.weixinDao.insertUser(fromUser, newLevel);
				outcome = "你已经有权限了，可以继续使用，谢谢！";
			}else{
				outcome = constructHelp(command);
			}
		}else if(command.equals("count")){		//查询订单数量
			if(isCommandAvailable("count",level)){
				outcome = this.orderDao.getOrderNum(
						this.params.get("telephone"),
						this.params.get("startdate"),
						this.params.get("enddate"),
						null);
			}else{
				outcome = "failed";
			}
		}else if(command.equals("order")){
			if(isCommandAvailable("order",level)){
				outcome = this.orderDao.getOrderDetail(
						this.params.get("orderid"), 
						this.params.get("telephone"), 
						this.params.get("field"));
			}else{
				outcome = "failed";
			}
		}else if(command.equals("orders")){
			if(isCommandAvailable("orders",level)){
				outcome = this.orderDao.getOrderList(
						this.params.get("telephone"), 
						this.params.get("startdate"), 
						this.params.get("enddate"), 
						Integer.parseInt((this.params.get("length")==null?"3":this.params.get("length"))), 
						this.params.get("field"));
			}else{
				outcome = "failed";
			}
		}else if(command.equals("sum")){
			if(isCommandAvailable("sum",level)){
				outcome = this.orderDao.getSumPrice(this.params.get("startdate"), 
						this.params.get("enddate"), 
						this.params.get("statename"),
						null);
			}else{
				outcome = "failed";
			}
		}else{
			if(RegexUtil.isMobile(command)){
				if(isCommandAvailable("telephone",level)){
					outcome = this.orderDao.getOrderDetail(
							null, 
							command, 
							null);
				}else{
					outcome = "failed";
				}
			}else if(command.indexOf("今天赚多少") != -1){
				if(isCommandAvailable("今天赚多少",level)){
					outcome = this.orderDao.getSumPrice(
							DateUtil.convertDateToString("yyyy-MM-dd"), 
							DateUtil.convertDateToString("yyyy-MM-dd"), 
							"已结帐",
							null);
				}else{
					outcome = "failed";
				}
			}else if(command.indexOf("月赚多少") != -1){
				if(isCommandAvailable("月赚多少",level)){
					outcome = this.orderDao.getSumPrice(
							DateUtil.getMonthStart("yyyy-MM-dd"), 
							DateUtil.convertDateToString("yyyy-MM-dd"), 
							"已结帐",
							null);
				}else{
					outcome = "failed";
				}
			}else if(command.indexOf("年赚多少") != -1){
				if(isCommandAvailable("年赚多少",level)){
					outcome = this.orderDao.getSumPrice(
							DateUtil.getYearStart("yyyy-MM-dd"), 
							DateUtil.convertDateToString("yyyy-MM-dd"), 
							"已结帐",
							null);
				}else{
					outcome = "failed";
				}
			}else if(command.indexOf("help") != -1 || command.indexOf("帮助") != -1){
				outcome = constructHelp(command);
			}else if(command.equals("订单") || command.equals("数据")){
				if(isCommandAvailable("订单",level)){
					double 	sumElong, sumJltour;
					long 	countElong, countJltour;
					sumElong = this.orderDao.getSumPrice(
							DateUtil.convertDateToString("yyyy-MM-dd"), 
							DateUtil.convertDateToString("yyyy-MM-dd"), 
							null,
							"0001");
					countElong	= this.orderDao.getOrderNum(
							null, 
							DateUtil.convertDateToString("yyyy-MM-dd"), 
							DateUtil.convertDateToString("yyyy-MM-dd"), 
							"0001");
					sumJltour = this.orderDao.getSumPrice(
							DateUtil.convertDateToString("yyyy-MM-dd"), 
							DateUtil.convertDateToString("yyyy-MM-dd"), 
							null,
							"0004");
					countJltour = this.orderDao.getOrderNum(
							null, 
							DateUtil.convertDateToString("yyyy-MM-dd"), 
							DateUtil.convertDateToString("yyyy-MM-dd"), 
							"0004");
					outcome = outcome + "今日订单统计\n";
					outcome = outcome + "订单总数："+(countElong+countJltour)+"\n";
					outcome = outcome + "总金额："+(sumElong+sumJltour)+"\n\n";
					outcome = outcome + "捷旅订单数量："+countJltour+"\n";
					outcome = outcome + "捷旅订单总额："+sumJltour+"\n\n";
					outcome = outcome + "艺龙订单数量："+countElong+"\n";
					outcome = outcome + "艺龙订单总额："+sumElong+"\n";
				}else{
					outcome = "failed";
				}
			}
			else{
				outcome = "请输入help获取帮助";
			}
		}
		
		String weixinContent = analyseOutcome(outcome, command);
		if(StringUtils.isEmpty(weixinContent)){
			weixinContent = "请输入help获取帮助";
		}
		
		return weixinContent;
	}
	
	private int isPassword(String command){
		if(command.equals("lotus")){
			return 1;
		}else if(command.equals("lyan")){
			return 2;
		}else{
			return 0;
		}
	}
	
	/**
	 * 判断是否有权限执行指令
	 * @param command	指令
	 * @param level		用户权限
	 * @return
	 */
	private boolean isCommandAvailable(String command, int level){
		if(level > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 构建帮助说明文本
	 * @param command
	 * @return
	 */
	private String constructHelp(String command){
		String helpContent = "";
//		String helpArg = "";
//		if(command.equals("help")){
//			helpContent = "command .sargs .eargs\n";
//			helpContent += "第一个字符串<command>为命令\n";
//			helpContent += ".后面第一个字符<s/e>为参数名\n";
//			helpContent += "参数名后面<args>为参数值\n";
//			helpContent += "命令列表：order<查询订单详情>,orders<查询订单列表>,count<查询订单数>,sum<查询订单金额>";
//			helpContent += "参数名列表：p<手机号>,i<内部订单id>,s<开始日期>,e<结束日期>,n<订单数量>,f<查询的订单字段>,S<订单状态>";
//			helpContent += "要查询具体参数使用,请输入: helpp 查p参数  helpi查i指令，等等";
//		} else if(command.equals("帮助")){
//			helpContent += "输入手机号可以查看订单详情。\n<今天赚多少>查看今天结帐订单总金额，\n<月赚多少>查看月初到现在的结帐订单总金额，\n<年赚多少>查看年初到现在的结帐订单总金额";
//		} else{
//			char arg = command.charAt(4);
//			if(arg == 'p'){
//				helpContent += "例如：查询指定手机号的所有订单  orders .p15271815593";
//			}else if(arg == 'i'){
//				helpContent += "例如：查询指定订单号的订单 order .i1312312314";
//			}else if(arg == 's'){
//				helpContent += "例如: 查询离店日期从指定日期开始的订单(日期格式yyyy-MM-dd) orders .s2012-11-22";
//			}else if(arg == 'e'){
//				helpContent += "例如: 查询离店日期到指定日期结束的订单(日期格式yyyy-MM-dd) orders .e2012-11-22";
//			}else if(arg == 'n'){
//				helpContent += "例如: 查询指定数量的订单(最多8条) orders .i15271815593 .n6";
//			}else if(arg == 'f'){
//				helpContent += "订单字段如内{内部订单号(1),\n联系人电话(2),\n离店日期(4),\n订单总价(8),\n入店日期(16),\n外部订单号(32),\n内部订单号(64),\n酒店名(128),\n房型名(256)，\n联系人电话(512)} " +
//						"例如：查询内部订单号和联系人电话字段(参数为字段数值之和) order .i1312312314 -f3";
//			}else if(arg == 'S'){
//				helpContent += "查询订单指定订单状态的订单总价 sum .e2012-11-22 -S结帐";
//			}else{
//				helpContent += "请输入help查看自定义命令帮助,帮助查看简单指令说明";
//			}
//		}
		helpContent = "输入“订单/数据”，查看今天订单情况\n"+
						"输入电话号码，查看该号码最新订单信息\n"+
						"输入“help/帮助”，查看使用帮助";
						
		return helpContent;
	}
	
	/**
	 * 解析命令执行的内容
	 * @param obj			命令执行的结果对象
	 * @param command		命令
	 * @return 微信上显示的字符串
	 */
	@SuppressWarnings("unchecked")
	private String analyseOutcome(Object obj, String command){
		String weixinContent = "";
		if(obj instanceof Long){
			long outcome = (Long)obj;
			if(command.equals("count")){
				weixinContent += "订单总数: ";
			}else{
				weixinContent += "订单总额:";
			}
			weixinContent += outcome;  
		}else if(obj instanceof WeixinOrderDetail){
			weixinContent += ReflectUtil.constructContent(obj);
		}else if(obj instanceof List){
			List<WeixinOrderDetail> weixinContentList
							= (List<WeixinOrderDetail>)obj;
			for(int i=0; i<weixinContentList.size(); i++){
				weixinContent += "订单("+i+")";
				weixinContent += ReflectUtil.constructContent(weixinContentList.get(i));
				weixinContent += "\n";
			}
		}else if(obj instanceof String){
			if(obj.equals("failed")){
				weixinContent += "您没有权限执行这条指令,请输入密码";
			}
			weixinContent += obj;
		}
		return weixinContent;
	}

	private void analyseArgs(String args){
		argsResolver = new ArgsResolver();
		argsResolver.resolver(args);
		char tmp;
		while ((tmp = argsResolver.getArgCommand()) != '?'){
			switch(tmp){
			case 'p':
				this.params.put("telephone", argsResolver.getArgContent(tmp));
				break;
			case 'i':
				this.params.put("orderid", argsResolver.getArgContent(tmp));
				break;
			case 's':
				this.params.put("startdate", argsResolver.getArgContent(tmp));
				break;
			case 'e':
				this.params.put("enddate", argsResolver.getArgContent(tmp));
				break;
			case 'n':
				this.params.put("length", argsResolver.getArgContent(tmp));
				break;
			case 'f':
				this.params.put("field", argsResolver.getArgContent(tmp));
				break;
			case 'S':
				this.params.put("statename", argsResolver.getArgContent(tmp));
				break;
			case '?':
				return ;
			}
		}
	}
}
