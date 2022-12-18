package com.mindex.challenge.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.ArrayList;

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

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NumberOfReportsServiceImplTest {
	
    private String numberOfReportsUrl;
    
    @MockBean
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
    	numberOfReportsUrl = "http://localhost:" + port + "/numberOfReports/{id}";
    }
    @Test
    public void testCountNumberOfReports() {
    	Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");
        testEmployee.setEmployeeId("1");

        Employee testReportEmployee = new Employee();
        testReportEmployee.setFirstName("Paul");
        testReportEmployee.setLastName("McCartney");
        testReportEmployee.setDepartment("Engineering");
        testReportEmployee.setPosition("Developer 1");
        testReportEmployee.setEmployeeId("2");
        
        List<Employee> testRepots = new ArrayList<>();
        testRepots.add(testReportEmployee);
        
        testEmployee.setDirectReports(testRepots);
        
        Mockito.when(employeeService.read(testEmployee.getEmployeeId())).thenReturn(testEmployee);
        Mockito.when(employeeService.read(testReportEmployee.getEmployeeId())).thenReturn(testReportEmployee);
                
        ReportingStructure reportingStructure = restTemplate.getForEntity(numberOfReportsUrl, ReportingStructure.class,testEmployee.getEmployeeId()).getBody();

        assertEquals(1,reportingStructure.getNumberOfReports());
        
    }


}
