package com.huoli.lyantool.repository;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.huoli.lyantool.domain.WeixinOrderDetail;
import com.huoli.lyantool.util.DateUtil;


public class OrderDao extends BaseDao {
	private static Map<Integer, String> argsMap = new HashMap<Integer, String>();
	private static final int FieldNum = 8;
	static{
		argsMap.put(0, "gdsStateName");
		argsMap.put(1, "gdsConnectorMobile");
		argsMap.put(2, "gdsLeaveDate");
		argsMap.put(3, "gdsTotalPrice");
		argsMap.put(4, "gdsArriveDate");
		argsMap.put(5, "gdsOrderId");
		argsMap.put(6, "orderId");
		argsMap.put(7, "gdsHotelName");
		argsMap.put(8, "gdsRoomTypeName");
		argsMap.put(9, "gdsConnectorMobile");
	}
	
//	/**
//	 * 获取内部酒店id与GDS酒店id的对应关系
//	 * 
//	 * @return
//	 */
//	public HotelMapping getHotelMappingByGDS(String gdsId, String gdsHotelId) {
//
//		String sql = "SELECT * FROM hotel_mapping WHERE gds_id = ? AND gds_hotelid = ?";
//		List<Map<String, Object>> list = this.find(sql, new Object[] { gdsId,
//				gdsHotelId });
//		if (list != null && list.size() > 0) {
//			Map<String, Object> item = list.get(0);
//			HotelMapping mapping = new HotelMapping();
//			mapping.setHotelId(String.valueOf((Long) item.get("hotelinfo_id")));
//			mapping.setGdsId(gdsId);
//			mapping.setGdsHotelId(gdsHotelId);
//
//			return mapping;
//		}
//
//		return null;
//	}

	
	
	public double getSumPrice(String startDate, String endDate, String stateName, String gdsId){
		try{
			String sql = "select sum(gdsTotalPrice) as sum from `order` where 1=1 and userId not like '%hangbanguanjia%'";
			Object[] objs = new Object[0];
			if(StringUtils.isNotBlank(startDate)){
				sql += " and to_days(gdsLeaveDate) >= to_days(?) ";
				objs = addObject(startDate, objs);
			}
			if(StringUtils.isNotBlank(endDate)){
				sql += " and to_days(gdsLeaveDate) <= to_days(?)";
				objs = addObject(endDate, objs);
			}
			if(StringUtils.isNotBlank(stateName)){
				sql += " and gdsStateName like concat('%',?,'%')";
				objs = addObject(stateName, objs);
			}
			if(StringUtils.isNotBlank(gdsId)){
				sql += "and gdsId = ?";
				objs = addObject(gdsId, objs);
			}
			
			List<Map<String, Object>> list = this.find(sql, objs);
			if(list != null && list.size() > 0){
				Map<String, Object> item = list.get(0);
				return (Double)(item.get("sum")==null?0:item.get("sum"));
			}
			return -1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public long getOrderNum(String telephone, String startDate, String endDate, String gdsId){
		try{
			String sql = "select count(*) as sum from `order` where 1=1 and userId not like '%hangbanguanjia%'";
			Object[] objs = new Object[0];
			if(StringUtils.isNotBlank(telephone)){
				sql += " and gdsConnectorMobile = ?";
				objs = addObject(telephone, objs);
			}
			if(StringUtils.isNotBlank(startDate)){
				sql += " and to_days(gdsLeaveDate) >= to_days(?) ";
				objs = addObject(startDate, objs);
			}
			if(StringUtils.isNotBlank(endDate)){
				sql += " and to_days(gdsLeaveDate) <= to_days(?)";
				objs = addObject(endDate, objs);
			}
			if(StringUtils.isNotBlank(gdsId)){
				sql += " and gdsId = ?";
				objs = addObject(gdsId, objs);
			}
			
						
			List<Map<String, Object>> list = this.find(sql, objs);
			if(list != null && list.size() > 0){
				Map<String, Object> item = list.get(0);
				return (Long)item.get("sum");
			}
			return -1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public List<WeixinOrderDetail> getOrderList(String telephone, String startDate, String endDate, int length, String args){
		if(length > 8){
			length = 8;
		}
		List<WeixinOrderDetail> orderList = new LinkedList<WeixinOrderDetail>();
		try{
			String sql = "select * from `order` where 1 = 1 and userId not like '%hangbanguanjia%'";
			Object[] objs = new Object[0];
			if(StringUtils.isNotBlank(telephone)){
				sql += " and gdsConnectorMobile = ?";
				objs = addObject(telephone, objs);
			}
			if(StringUtils.isNotBlank(startDate)){
				sql += " and to_days(gdsArriveDate) >= to_days(?) ";
				objs = addObject(startDate, objs);
			}
			if(StringUtils.isNotBlank(endDate)){
				sql += " and to_days(gdsLeaveDate) <= to_days(?)";
				objs = addObject(endDate, objs);
			}
			sql += " order by createTime desc limit ?";
			objs = addObject(length, objs);
			
			List<Map<String, Object>> list = this.find(sql, objs);
			if(length > list.size()){
				length = list.size();
			}
			for(int i=0; i<length; i++){
				Map<String, Object> item = list.get(i);
				WeixinOrderDetail orderDetail = generateOrderDetail(args, item);
				
				orderList.add(orderDetail);
			}
			return orderList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public WeixinOrderDetail getOrderDetail(String orderId, String telephone, String args){
		WeixinOrderDetail weixinOrderDetail = new WeixinOrderDetail();
		try{
			String sql = "select * from `order` where 1 = 1";
			Object[] objs = new Object[0];
			if(StringUtils.isNotBlank(orderId)){
				sql += " and orderId = ?";
				objs = addObject(orderId, objs);
			}
			if(StringUtils.isNotBlank(telephone)){
				sql += " and gdsConnectorMobile = ?";
				objs = addObject(telephone, objs);
			}
			List<Map<String, Object>> list = this.find(sql, objs);
			if(list != null && list.size() > 0){
				Map<String, Object> item = list.get(0);
				weixinOrderDetail = generateOrderDetail(args, item);
				return weixinOrderDetail;
			}
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取用户id
	 * 
	 * @param token
	 * @return
	 */
	public long getUid(String token) {

		try {
			String sql = "SELECT * FROM user WHERE userid = ?";
			List<Map<String, Object>> list = this.find(sql,
					new Object[] { token });
			if (list != null && list.size() > 0) {
				Map<String, Object> item = list.get(0);
				return (Long) item.get("uid");
			}

			return 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	public String getGdsStr(String gdsId){
		if(gdsId.equals("0001")){
			return "elong";
		}else if(gdsId.equals("0004")){
			return "jtour";
		}else if(gdsId.equals("0005")){
			return "lyan";
		}
		return null;
	}
	
	
	private WeixinOrderDetail generateOrderDetail(String argNum, Map<String, Object> item){
		int bit = 1;
		int outcome = 0;
		int argsNum;
		if(argNum == null){
			argsNum = 1+2+4+8+16+32+64+128+256+1024+512;
		}else{
			argsNum = Integer.parseInt(argNum);
		}
		WeixinOrderDetail orderDetail = new WeixinOrderDetail();
		String fieldName = "";
		for(int j=0; j<FieldNum; j++){
			if((outcome = argsNum&bit) == 1){
				fieldName = argsMap.get(j);
				set(fieldName, item.get(fieldName), orderDetail);
			}
			argsNum = argsNum/2;
		}
		
		return orderDetail;
	}

	private void set(final String fieldName, final Object fieldValue, final Object obj){
		Field field = null;
		try {
			field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			if(fieldValue instanceof Timestamp){
				field.set(obj, DateUtil.timestamp2str((Timestamp) fieldValue, "yyyy-MM-dd"));
			} else if(fieldValue instanceof String){
				field.set(obj, fieldValue);
			} else{
				field.set(obj, fieldValue.toString());
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private Object[] addObject(Object obj, Object[] objs){
		Object[] outcome = new Object[objs.length+1];
		for(int i=0; i<objs.length; i++){
			outcome[i] = objs[i];
		}
		outcome[objs.length] = obj;
		return outcome;
	}
	
	public static void main(String[] args){
		String user = "kof";
		System.out.println(user.getClass().getName());
	}
}
