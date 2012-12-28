package com.huoli.lyantool.repository;

import java.sql.Types;
import java.util.List;
import java.util.Map;

public class WeixinDao extends BaseDao  {
	public int getUserLevel(String username){
		String sql = "select level from weixin_user where user = ?";
		List<Map<String, Object>> list = this
				.find(sql, new Object[] { username });
		if (list != null && list.size() > 0) {
			Map<String, Object> item = list.get(0);
			int level = (Integer) item.get("level");
			return level;
		}
		return 0;
	}
	
	public long insertUser(String username, int level){
		String sql = "insert into weixin_user values(? , ?)";
		
		return this.insert(sql, new Object[] { username, level},
				new int[] { Types.VARCHAR, Types.INTEGER});
	}
}
