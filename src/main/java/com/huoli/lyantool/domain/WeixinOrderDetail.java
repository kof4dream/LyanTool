package com.huoli.lyantool.domain;

import com.huoli.lyantool.annotation.AliasAnnotation;

public class WeixinOrderDetail {
	@AliasAnnotation("内部订单号")
	private String orderId;
	@AliasAnnotation("联系人电话")
	private String gdsConnectorMobile;
	@AliasAnnotation("入住人")
	private String gdsGuestNames;
	@AliasAnnotation("酒店名")
	private String gdsHotelName;
	@AliasAnnotation("房型名")
	private String gdsRoomTypeName;
	@AliasAnnotation("入店日期")
	private String gdsArriveDate;
	@AliasAnnotation("离店日期")
	private String gdsLeaveDate;
	@AliasAnnotation("总价")
	private String gdsTotalPrice;
	@AliasAnnotation("外部订单号")
	private String gdsOrderId;
	@AliasAnnotation("订单状态")
	private String gdsStateName;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getGdsConnectorMobile() {
		return gdsConnectorMobile;
	}
	public void setGdsConnectorMobile(String gdsConnectorMobile) {
		this.gdsConnectorMobile = gdsConnectorMobile;
	}
	public String getGdsGuestNames() {
		return gdsGuestNames;
	}
	public void setGdsGuestNames(String gdsGuestNames) {
		this.gdsGuestNames = gdsGuestNames;
	}
	public String getGdsHotelName() {
		return gdsHotelName;
	}
	public void setGdsHotelName(String gdsHotelName) {
		this.gdsHotelName = gdsHotelName;
	}
	public String getGdsRoomTypeName() {
		return gdsRoomTypeName;
	}
	public void setGdsRoomTypeName(String gdsRoomTypeName) {
		this.gdsRoomTypeName = gdsRoomTypeName;
	}
	public String getGdsArriveDate() {
		return gdsArriveDate;
	}
	public void setGdsArriveDate(String gdsArriveDate) {
		this.gdsArriveDate = gdsArriveDate;
	}
	public String getGdsLeaveDate() {
		return gdsLeaveDate;
	}
	public void setGdsLeaveDate(String gdsLeaveDate) {
		this.gdsLeaveDate = gdsLeaveDate;
	}
	public String getGdsTotalPrice() {
		return gdsTotalPrice;
	}
	public void setGdsTotalPrice(String gdsTotalPrice) {
		this.gdsTotalPrice = gdsTotalPrice;
	}
	public String getGdsOrderId() {
		return gdsOrderId;
	}
	public void setGdsOrderId(String gdsOrderId) {
		this.gdsOrderId = gdsOrderId;
	}
	public String getGdsStateName() {
		return gdsStateName;
	}
	public void setGdsStateName(String gdsStateName) {
		this.gdsStateName = gdsStateName;
	}
	
	
	
}
