package com.employee.repository;

import com.employee.model.Employee;
import com.employee.service.EmployeeService;
import com.employee.service.impl.EmployeeServiceImpl;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRespository  extends JpaRepository<Employee, Long> {


//  Optional<Employee> findByEmpCodeAndCompanyName(String empCode , String companyName);

         Optional<Employee>findByEmpCodeAndCompanyName(String empCode , String companyName);

}

