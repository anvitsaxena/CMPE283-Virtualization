package com.vstack.db;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.vstack.beans.Compute;
import com.vstack.dao.ComputeDAO;

public class ComputeJDBCTemplate implements ComputeDAO {
	
	private JdbcTemplate jdbcTemplateObject;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}

	public void createTable(String tableName) {
		String SQL = "create table if not exists " + tableName + "(ID INT NOT NULL AUTO_INCREMENT, NAME VARCHAR(20) "
				+ "NOT NULL, PRIMARY KEY (ID));";

		jdbcTemplateObject.update(SQL);
		System.out.println("Created table Name " + tableName);
		return;

	}

	@Override
	public void createInstances(String table, String name) {
		// TODO Auto-generated method stub
		String SQL = "insert into " + table + " (name) values (?)";
		Object[] values = new Object[] { name };
		jdbcTemplateObject.update(SQL, values);
		System.out.println("Created Record Name = " + name );
		return;
	}

	@Override
	public Compute getInstance(String table, Integer id) {
		// TODO Auto-generated method stub
		String SQL = "select * from " + table + " where id = ?";
		Compute instance = (Compute) jdbcTemplateObject.queryForObject(SQL, new Object[] { id }, new ComputeMapper());

		return instance;
	}

	@Override
	public List<Compute> listInstances(String table) {
		// TODO Auto-generated method stub
		String SQL = "select * from " + table;
		List<Compute> instances = jdbcTemplateObject.query(SQL, new ComputeMapper());
		return instances;
	}

	@Override
	public void deleteInstance(String table, Integer id) {
		// TODO Auto-generated method stub
		String SQL = "delete from " + table + " where id = ?";
		Object[] values = new Object[] { id };
		jdbcTemplateObject.update(SQL, values);
		System.out.println("Deleted Record with ID = " + id);
		return;
	}

	@Override
	public void updateInstance(String table, String name, Integer id) {
		// TODO Auto-generated method stub
		 String SQL = "update " + table + " set name = ? where id = ?";
	      Object[] values = new Object[] { name, id };
			
	      jdbcTemplateObject.update(SQL, values);
	      System.out.println("Updated Record with ID = " + id );
	      return;
	}

}