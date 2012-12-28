package com.huoli.lyantool.domain;

import java.util.HashMap;
import java.util.Map;

public class WeixinCommandBean {
	private String command;
	private Map<String, String> args = new HashMap<String, String>();
	
	
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public Map<String, String> getArgs() {
		return args;
	}
	public void setArgs(Map<String, String> args) {
		this.args = args;
	}
	
	public static void main(String[] args){
		Map<String, String> test = new HashMap<String, String>();
		test.put("1", "test");
		test.put("2", "product");
		
		String[] array = (String[]) test.keySet().toArray();
		System.out.println(array.length);
	}
}
