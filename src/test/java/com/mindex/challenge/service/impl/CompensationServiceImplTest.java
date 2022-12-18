package com.mindex.challenge.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {
	 	private String compensationUrl;
	    private String compensationIdUrl;
	    @MockBean
	    private EmployeeService employeeService;

	    @LocalServerPort
	    private int port;

	    @Autowired
	    private TestRestTemplate restTemplate;

	    @Before
	    public void setup() {
	    	compensationUrl = "http://localhost:" + port + "/Compensation";
	    	compensationIdUrl = "http://localhost:" + port + "/Compensation/{id}";
	    }
	    @Test
	    public void testCreateRead() {
	        Employee testEmployee = new Employee();
	        testEmployee.setFirstName("John");
	        testEmployee.setLastName("Doe");
	        testEmployee.setDepartment("Engineering");
	        testEmployee.setPosition("Developer");
	        testEmployee.setEmployeeId("1");
	        
	        Compensation testCompensation = new Compensation();
	        testCompensation.setEmployee(testEmployee);
	        testCompensation.setSalary(10000);
	        testCompensation.setEffectiveDate("12/17/2022");
	        
	        // Create checks
	        Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, testCompensation, Compensation.class).getBody();
	        assertNotNull(createdCompensation.getEmployee());
	        assertCompensationEquivalence(testCompensation, createdCompensation);
	        
	        Mockito.when(employeeService.read(testEmployee.getEmployeeId())).thenReturn(testEmployee);
	        
	        // Read checks
	        Compensation readCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation.class, createdCompensation.getEmployee().getEmployeeId()).getBody();
	        assertCompensationEquivalence(testCompensation, readCompensation);

	        
	    }
	    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
	        assertEquals(expected.getSalary(), actual.getSalary());
	        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
	        assertEmployeeEquivalence(expected.getEmployee(), actual.getEmployee());
	    }
	    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
	        assertEquals(expected.getFirstName(), actual.getFirstName());
	        assertEquals(expected.getLastName(), actual.getLastName());
	        assertEquals(expected.getDepartment(), actual.getDepartment());
	        assertEquals(expected.getPosition(), actual.getPosition());
	        assertEquals(expected.getEmployeeId(), actual.getEmployeeId());
	        
	    }
}
