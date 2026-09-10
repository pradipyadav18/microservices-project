package com.employee.service.impl;

import com.employee.client.AddressClient;
import com.employee.exception.BadRequestException;
import com.employee.exception.ResourceNotFoundException;
import com.employee.model.Employee;
import com.employee.model.dto.AddressDto;
import com.employee.model.dto.EmployeeDto;
import com.employee.repository.EmployeeRespository;
import com.employee.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log =
            LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRespository employeeRespository;
    private final ModelMapper modelMapper;
    private final AddressClient addressClient;



    public EmployeeServiceImpl(EmployeeRespository employeeRespository,
                               ModelMapper modelMapper, AddressClient addressClient) {
        this.employeeRespository = employeeRespository;
        this.modelMapper = modelMapper;
        this.addressClient = addressClient;
    }

    // SAVE EMPLOYEE
    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {

        // New employee ke liye ID null honi chahiye
        if (employeeDto.getId() != null) {
            throw new RuntimeException("Employee already exists");
        }

        // DTO -> Entity
        Employee entity = modelMapper.map(employeeDto, Employee.class);

        // Save into database
        Employee savedEntity = employeeRespository.save(entity);

        // Entity -> DTO
        return modelMapper.map(savedEntity, EmployeeDto.class);
    }


    // UPDATE EMPLOYEE
    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {

        if(id==null || employeeDto.getId()==null){
            throw new BadRequestException("Please provide the employee id");
        }

        if(Objects.equals(id,employeeDto.getId())){
            throw new ResourceNotFoundException(" Employee not fonund with id " + id);
        }

        employeeRespository.findById(id).orElseThrow( ()-> new ResourceNotFoundException(" Employee not fonund with id " + id) );

        Employee entity=modelMapper.map(employeeDto,Employee.class);
        Employee updatedEmployee=employeeRespository.save(entity);

        // Entity -> DTO
        return modelMapper.map(updatedEmployee, EmployeeDto.class);
    }


    // DELETE EMPLOYEE
    @Override
    public void deleteEmployee(Long id) {

        Employee employee = employeeRespository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(" Employee not fonund")
                );

        employeeRespository.delete(employee);
    }


    // GET SINGLE EMPLOYEE
    @Override
    public EmployeeDto getSingleEmployee(Long id) {

        System.out.println("========== getSingleEmployee START ==========");

        Employee employee = employeeRespository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found with id " + id
                        )
                );

        System.out.println("Employee found: " + employee.getId());

        EmployeeDto dto =
                modelMapper.map(employee, EmployeeDto.class);

        System.out.println("DTO BEFORE ADDRESS: " + dto);

        try {

            List<AddressDto> addresses =
                    addressClient.getAddressByEmpId(employee.getId());

            System.out.println("ADDRESS SIZE: " + addresses.size());
            System.out.println("ADDRESS DATA: " + addresses);

            dto.setAddress(addresses);

            System.out.println("DTO AFTER ADDRESS: " + dto);

        } catch (Exception e) {

            System.out.println("========== ADDRESS CALL FAILED ==========");
            e.printStackTrace();
        }

        System.out.println("========== getSingleEmployee END ==========");

        return dto;
    }

    // GET ALL EMPLOYEES
    @Override
    public List<EmployeeDto> getAllEmployee() {

        List<Employee> employeeList =
                employeeRespository.findAll();

        if(employeeList.isEmpty()){
            throw new ResourceNotFoundException("No employee Found");
        }

        List<EmployeeDto>employeeDtoList= employeeList.stream()
                .map(employee ->
                        modelMapper.map(employee, EmployeeDto.class)
                )
                .toList();
        List<EmployeeDto>response=new ArrayList<>();


        for(EmployeeDto employee:employeeDtoList){

            List<AddressDto>addresses=new ArrayList<>();

            try {
                addresses=addressClient.getAddressByEmpId(employee.getId());
                employee.setAddress(addresses);

            }catch (Exception e){
                log.info("No address found for employee id " + employee.getId());

            }

            response.add(employee);
        }

        return response;
    }

    @Override
    public EmployeeDto getEmployeeByEmpCodeAndCompanyName(String empCode, String companyName) {

        Employee employee=employeeRespository.findByEmpCodeAndCompanyName(empCode,companyName).orElseThrow( ()-> new ResourceNotFoundException("Employee not found with : " + empCode + " company name : " + companyName) );


        return modelMapper.map(employee,EmployeeDto.class);


    }

    @Override
    public EmployeeDto getEmployeeById(Long id) {

        System.out.println("=================================");
        System.out.println("GET EMPLOYEE BY ID START");
        System.out.println("ID = " + id);

        Employee employee = employeeRespository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found with id " + id
                        )
                );

        System.out.println("EMPLOYEE FOUND = " + employee.getId());

        EmployeeDto dto =
                modelMapper.map(employee, EmployeeDto.class);

        System.out.println("DTO BEFORE ADDRESS = " + dto);

        try {

            System.out.println("CALLING ADDRESS SERVICE...");

            List<AddressDto> addresses =
                    addressClient.getAddressByEmpId(employee.getId());

            System.out.println("ADDRESS RESPONSE = " + addresses);
            System.out.println("ADDRESS SIZE = " + addresses.size());

            dto.setAddress(addresses);

            System.out.println("DTO AFTER ADDRESS = " + dto);

        } catch (Exception e) {

            System.out.println("!!!!!!!! ADDRESS SERVICE FAILED !!!!!!!!");

            e.printStackTrace();
        }

        System.out.println("GET EMPLOYEE BY ID END");
        System.out.println("=================================");

        return dto;
    }

}