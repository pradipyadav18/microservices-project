package com.employee.service;

import com.employee.model.Employee;
import com.employee.model.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {

    EmployeeDto saveEmployee(EmployeeDto employeeDto);

    EmployeeDto updateEmployee(Long id,EmployeeDto employeeDto);

    void deleteEmployee(Long id);

    EmployeeDto getSingleEmployee(Long id);

    List<EmployeeDto> getAllEmployee();

   EmployeeDto getEmployeeByEmpCodeAndCompanyName(String empCode , String companyName);

    EmployeeDto getEmployeeById(Long id);


}
