package com.huoli.lyantool.util;

import java.lang.reflect.Field;

import com.huoli.lyantool.annotation.AliasAnnotation;

public class ReflectUtil {
	public static String constructContent(Object obj){
		String outcome = "";
		Field[] fields = obj.getClass().getDeclaredFields();
		Field fieldTmp = null;
		try {
			for(int i=0; i<fields.length; i++){
				fieldTmp = fields[i];
				fieldTmp.setAccessible(true);
				if(fieldTmp.get(obj) != null){
					if(fieldTmp.isAnnotationPresent(AliasAnnotation.class)){
						String aliasName = fieldTmp.getAnnotation(AliasAnnotation.class).value();
						outcome += aliasName;
					}else{
						outcome += fieldTmp.getName();
					}
					
					outcome += ": "+fieldTmp.get(obj);
					outcome += "\n";
				} 
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return outcome;
	}
}
