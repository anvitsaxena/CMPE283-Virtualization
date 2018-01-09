package com.vstack.dao;

import java.util.List;
import javax.sql.DataSource;

import com.vstack.beans.Compute;

public interface ComputeDAO {
		
	   public void setDataSource(DataSource ds);
	   public void createInstances(String table, String name);
	   public Compute getInstance(String table, Integer id);
	   public void deleteInstance(String table, Integer id);
	   public void updateInstance(String table, String name, Integer id);
	   public List<Compute> listInstances(String table);
}
