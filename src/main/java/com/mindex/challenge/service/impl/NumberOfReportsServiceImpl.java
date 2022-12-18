package com.mindex.challenge.service.impl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.NumberOfReportsService;

@Service
public class NumberOfReportsServiceImpl implements NumberOfReportsService {

	private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	@Autowired
	private EmployeeService employeeService;

	@Override
	public ReportingStructure reportingStructure(String id) {
		LOG.debug("Get Reporting Structure of employee with Id[{}]", id);
		Employee employee = employeeService.read(id);
		return new ReportingStructure(employee, countnumberOfReports(employee));
	}

	private int countnumberOfReports(Employee employee) {
		LOG.debug("Get numberOfReports of employee[{}]", employee.getEmployeeId());
		
		int totalNumberofReports = 0;
		Queue<Employee> employeeQueue = new LinkedList<>();
		Set<String> visitedEmployees = new HashSet<>(); 
		employeeQueue.add(employee);
		while (!employeeQueue.isEmpty()) {
			Employee currentEmployee = employeeQueue.poll();
			//checks noDirectReportees or already counted the current employee
			if (currentEmployee.getDirectReports() == null && visitedEmployees.contains(currentEmployee.getEmployeeId())) {
				continue;
			}
			visitedEmployees.add(currentEmployee.getEmployeeId());
			for (Employee reportiees : currentEmployee.getDirectReports()) {
				totalNumberofReports++;
				employeeQueue.add(employeeService.read(reportiees.getEmployeeId()));
			}
		}
		return totalNumberofReports;
	}
}
