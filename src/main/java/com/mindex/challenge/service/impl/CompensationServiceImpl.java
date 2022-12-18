package com.mindex.challenge.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;

@Service
public class CompensationServiceImpl implements CompensationService {

	private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private CompensationRepository compensationRepository;

	@Override
	public Compensation create(Compensation compensation) {
		// TODO Auto-generated method stub
		LOG.debug("Creating compensation [{}]", compensation);
		compensationRepository.insert(compensation);
		return compensation;
	}

	@Override
	public Compensation read(String id) {
		// TODO Auto-generated method stub
		LOG.debug("Creating compensation with id [{}]", id);

		Employee employee = employeeService.read(id);

		Compensation compensation = compensationRepository.findByEmployee(employee);

		if (compensation == null) {
			throw new RuntimeException("compensation does not exists: " + id);
		}

		return compensation;
	}

}
