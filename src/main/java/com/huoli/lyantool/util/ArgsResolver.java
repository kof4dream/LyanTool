package com.huoli.lyantool.util;

import java.util.Map;

import com.huoli.lyantool.domain.WeixinCommandBean;

public class ArgsResolver {
	private WeixinCommandBean weixinCommand;
	private int argLength;
	private int index;
	private char[] keys = new char[10];
	
	public void resolver(String arg){
		String[] args = arg.split(" ");
		this.weixinCommand = new WeixinCommandBean();
		
		weixinCommand.setCommand(args[0]);
		Map<String, String> argMap = weixinCommand.getArgs();
		
		if(args.length > 1){
		for(int i=0; i<args.length-1; i++){
			String tmp = args[i+1];
			
			if(tmp.charAt(0) == '.'){
				argMap.put(tmp.substring(1,2), tmp.substring(2));
				keys[i] = tmp.charAt(1);
			}
		}
		weixinCommand.setArgs(argMap);
		this.argLength = argMap.size();
		this.index = 0;
		}
//		this.keys = argMap.
	}
	
	public String getCommand(){
		return this.weixinCommand.getCommand();
	}
	
	public char getArgCommand(){
		if(this.index < argLength){
			return this.keys[this.index++];
		}else{
			return '?';
		}
	}
	
	public String getArgContent(char argCommand){
		return this.weixinCommand.getArgs().get(argCommand+"");
	}
}
