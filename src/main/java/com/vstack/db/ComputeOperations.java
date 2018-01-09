package com.vstack.db;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.vstack.beans.Compute;

public class ComputeOperations {

	ApplicationContext dbContext;

	public ComputeOperations(ApplicationContext dbContext) {
		this.dbContext = dbContext;
	}

	public void computeDatabaseOperation() {
		ComputeJDBCTemplate computeJDBCTemplate = (ComputeJDBCTemplate) dbContext.getBean("computeJDBCTemplate");

		String computeTable = "Compute";
		computeJDBCTemplate.createTable(computeTable);

		System.out.println("------Records Creation--------");
		computeJDBCTemplate.createInstances(computeTable, "instance1");
		computeJDBCTemplate.createInstances(computeTable, "instance2");
		computeJDBCTemplate.createInstances(computeTable, "instance3");

		System.out.println("------Listing Multiple Records--------");
		List<Compute> instances = computeJDBCTemplate.listInstances(computeTable);
		for (Compute record : instances) {
			System.out.print("Name : " + record.getName());
		}

		System.out.println("----Updating Record with ID = 2 -----");
		computeJDBCTemplate.updateInstance(computeTable, "updated", 2);

		System.out.println("----Listing Record with ID = 2 -----");
		Compute student = computeJDBCTemplate.getInstance(computeTable, 2);
		System.out.print("Name : " + student.getName());

	}
}