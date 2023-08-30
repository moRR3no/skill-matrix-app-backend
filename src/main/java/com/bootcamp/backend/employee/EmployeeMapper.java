package com.bootcamp.backend.employee;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {

    @Mapping(target = "managerId", source = "manager.id")
    EmployeeDTO employeeToEmployeeDTO (Employee employee);
    List<EmployeeDTO> employeesToEmployeeDTOs (List<Employee> employees);
    Employee employeeDTOToEmployee (EmployeeDTO employeeDTO);
}