package com.huoli.lyantool.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class BaseDao {
	private static Logger logger = Logger.getLogger(BaseDao.class);

	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public String update(String sql, Object[] args) {

		try {
			jdbcTemplate.update(sql, args);
		} catch (DataAccessException e) {
			logger.error("数据库操作异常，错误信息：" + e.getMessage());
			e.printStackTrace();
			return "-1";
		} finally {
			try {
				jdbcTemplate.getDataSource().getConnection().close();
			} catch (SQLException e) {
				logger.error("数据库操作异常，错误信息：" + e.getMessage());
				e.printStackTrace();
			}
		}
		return "0";
	}

	public String update(String sql) {

		try {
			jdbcTemplate.update(sql);
		} catch (DataAccessException e) {
			logger.error("数据库操作异常，错误信息：" + e.getMessage());
			e.printStackTrace();
			return "-1";
		} finally {
			try {
				jdbcTemplate.getDataSource().getConnection().close();
			} catch (SQLException e) {
				logger.error("数据库操作异常，错误信息：" + e.getMessage());
				e.printStackTrace();
			}
		}
		return "0";
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> find(String sql, final Object... args) {
		List<Map<String, Object>> rows;
		try {
			rows = jdbcTemplate.queryForList(sql, args);
		} catch (DataAccessException e) {
			logger.error("数据库操作异常，错误信息：" + e.getMessage());
			e.printStackTrace();
			return null;
		} finally {
			try {
				jdbcTemplate.getDataSource().getConnection().close();
			} catch (SQLException e) {
				logger.error("数据库操作异常，错误信息：" + e.getMessage());
				e.printStackTrace();
			}
		}

		Iterator it = rows.iterator();
		if (it.hasNext()) {
			return rows;
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> find(String sql) {
		List<Map<String, Object>> rows;
		try {
			rows = jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			logger.error("数据库操作异常，错误信息：" + e.getMessage());
			e.printStackTrace();
			return null;
		} finally {
			try {
				jdbcTemplate.getDataSource().getConnection().close();
			} catch (SQLException e) {
				logger.error("数据库操作异常，错误信息：" + e.getMessage());
				e.printStackTrace();
			}
		}

		Iterator it = rows.iterator();
		if (it.hasNext()) {
			return rows;
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public Map findOne(String sql) {
		List rows = this.find(sql);
		if (rows == null || rows.size() == 0) {
			return null;
		}

		return (Map) rows.get(0);
	}

	public Map<String, Object> findForMap(String sql, Object... args) {
		List<Map<String, Object>> rs = this.find(sql, args);
		if (rs == null || rs.size() == 0) {
			return null;
		}

		return rs.get(0);
	}

	public boolean execute(String sql) {
		Connection conn = null;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			jdbcTemplate.execute(sql);
		} catch (DataAccessException e) {
			logger.error("数据库操作异常，错误信息：" + e.getMessage());
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			logger.error("数据库操作异常，错误信息：" + e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("数据库操作异常，错误信息：" + e.getMessage());
				e.printStackTrace();
			}
		}
		return true;
	}

	public long count(String sql) {
		long count = 0;
		try {
			count = jdbcTemplate.queryForLong(sql);
		} catch (DataAccessException e) {
			logger.error("数据库操作异常，错误信息：" + e.getMessage());
			e.printStackTrace();
			return count;
		} finally {
			try {
				jdbcTemplate.getDataSource().getConnection().close();
			} catch (SQLException e) {
				logger.error("数据库操作异常，错误信息：" + e.getMessage());
				e.printStackTrace();
			}
		}
		return count;
	}

	public boolean executeBatch(String sql[]) {
		Connection conn = null;
		Statement stat = null;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			stat = conn.createStatement();
			for (int i = 0; i < sql.length; i++) {
				stat.addBatch(sql[i]);
			}
			stat.executeBatch();
			return (true);
		} catch (Exception e) {
			logger.error("数据库操作异常，错误信息：" + e.getMessage());
			e.printStackTrace();
			return (false);
		} finally {
			try {
				if (stat != null) {
					stat.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error("数据库操作异常，错误信息：" + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public boolean executeBatch(List sql, String text) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			stat = conn.prepareStatement(sql.get(0).toString());
			stat.setString(1, text);
			stat.addBatch();
			for (int i = 1; i < sql.size(); i++) {
				stat.addBatch(sql.get(i).toString());
			}
			stat.executeBatch();
			return (true);
		} catch (Exception e) {
			logger.error("数据库操作异常，错误信息：" + e.getMessage());
			e.printStackTrace();
			return (false);
		} finally {
			try {
				if (stat != null) {
					stat.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error("数据库操作异常，错误信息：" + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public boolean executeBatch(List sql) {
		Connection conn = null;
		Statement stat = null;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			stat = conn.createStatement();
			for (int i = 0; i < sql.size(); i++) {
				stat.addBatch(sql.get(i).toString());
			}
			stat.executeBatch();
			return (true);
		} catch (Exception e) {
			logger.error("数据库操作异常，错误信息：" + e.getMessage());
			e.printStackTrace();
			return (false);
		} finally {
			try {
				if (stat != null) {
					stat.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error("数据库操作异常，错误信息：" + e.getMessage());
				e.printStackTrace();
			}

		}

	}

	@SuppressWarnings("unchecked")
	public String executeBatch2(List sql) {
		Connection conn = null;
		Statement stat = null;
		String str;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			stat = conn.createStatement();
			for (int i = 0; i < sql.size(); i++) {
				stat.addBatch(sql.get(i).toString());
			}
			stat.executeBatch();
			str = "true";
			return str;
		} catch (Exception e) {
			logger.error("数据库操作异常，错误信息：" + e.getMessage());
			e.printStackTrace();
			return e.getMessage();
		} finally {
			try {
				if (stat != null) {
					stat.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error("数据库操作异常，错误信息：" + e.getMessage());
				e.printStackTrace();
			}

		}

	}

	public boolean executeBatch(Object sql[]) {
		Connection conn = null;
		Statement stat = null;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			stat = conn.createStatement();
			for (int i = 0; i < sql.length; i++) {
				stat.addBatch(sql[i].toString());
			}
			stat.executeBatch();
			return (true);
		} catch (Exception e) {
			logger.error("数据库操作异常，错误信息：" + e.getMessage());
			e.printStackTrace();
			return (false);
		} finally {
			try {
				if (stat != null) {
					stat.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error("数据库操作异常，错误信息：" + e.getMessage());
				e.printStackTrace();
			}

		}
	}

	public Connection getConnection() throws SQLException {
		return jdbcTemplate.getDataSource().getConnection();
	}

	public boolean h2Runscript(String fileName, String options) {
		Connection conn = null;
		Statement stat = null;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			stat = conn.createStatement();
			String sql = "RUNSCRIPT FROM '" + fileName + "' " + options;
			stat.execute(sql);
		} catch (SQLException e) {
			logger.error("数据库操作异常，错误信息：" + e.getMessage());
			e.printStackTrace();
			return (false);
		} finally {
			try {
				if (stat != null) {
					stat.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error("数据库操作异常，错误信息：" + e.getMessage());
				e.printStackTrace();
			}
		}

		return (true);
	}

	/**
	 * 插入记录并返回自动生成的主键Id
	 * 
	 * @param sql
	 * @return
	 */
	public int insert(String sql) {
		int id = 0;

		try {
			SqlUpdate su = new SqlUpdate();
			su.setDataSource(this.jdbcTemplate.getDataSource());
			su.setSql(sql);
			su.setReturnGeneratedKeys(true);
			su.compile();
			KeyHolder keyHolder = new GeneratedKeyHolder();
			su.update(null, keyHolder);
			id = keyHolder.getKey() == null ? 0 : keyHolder.getKey().intValue();
		} catch (Exception e) {
			logger.error("数据库操作异常，错误信息：" + e.getMessage());
		}
		return id;
	}

	public long insert(String sql, Object[] args, int[] argTypes) {
		long id = 0;
		try {
			SqlUpdate su = new SqlUpdate();
			su.setDataSource(this.jdbcTemplate.getDataSource());
			su.setSql(sql);

			su.setTypes(argTypes);
			su.setReturnGeneratedKeys(true);
			su.compile();

			KeyHolder keyHolder = new GeneratedKeyHolder();
			su.update(args, keyHolder);
			id = keyHolder.getKey() == null ? 0 : keyHolder.getKey().intValue();
		} catch (Exception e) {
			logger.error("数据库操作异常，错误信息：" + e.getMessage());
			// e.printStackTrace();
			return 0;
		}
		return id;
	}

}