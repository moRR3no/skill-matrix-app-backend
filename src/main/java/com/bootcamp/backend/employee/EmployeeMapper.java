package com.bootcamp.backend.employee;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "managerId", source = "manager.id")
    EmployeeDTO employeeToEmployeeDTO (Employee employee);
    List<EmployeeDTO> employeesToEmployeeDTOs (List<Employee> employees);
    Employee employeeDTOToEmployee (EmployeeDTO employeeDTO);
}