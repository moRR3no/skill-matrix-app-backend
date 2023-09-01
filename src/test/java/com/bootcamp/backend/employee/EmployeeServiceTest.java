package com.bootcamp.backend.employee;

import com.bootcamp.backend.exceptions.NotFoundException;
import com.bootcamp.backend.exceptions.WrongInputException;
import com.bootcamp.backend.mappers.MapStructMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private MapStructMapper mapstructMapper;

    @InjectMocks
    private EmployeeService underTest;

    @BeforeEach
    void setUp() {
    }

    @Test
    void underTestIsCreated() {
        assertThat(underTest).isNotNull();
    }

    @Test
    void testGetEmployees() {
        // given
        List<Employee> mockEmployees = new ArrayList<>();
        when(employeeRepository.findAll()).thenReturn(mockEmployees);

        // when
        List<EmployeeDTO> result = underTest.getEmployees();

        // then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetEmployeeById() {
        // given
        UUID employeeId = UUID.randomUUID();
        Employee mockEmployee = new Employee();
        EmployeeDTO mockEmployeeDTO = new EmployeeDTO();

        // when
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(mockEmployee));
        when(mapstructMapper.employeeToEmployeeDTO(mockEmployee)).thenReturn(mockEmployeeDTO);

        EmployeeDTO result = underTest.getEmployeeById(employeeId);

        // then
        assertNotNull(result);
    }

    @Test
    void testGetEmployeeByIdNotFound() {
        // given
        UUID employeeId = UUID.randomUUID();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> underTest.getEmployeeById(employeeId));
    }

    @Test
    void testSaveEmployee() {
        // given
        EmployeeDTO employeeDTO = new EmployeeDTO();
        Employee employee = new Employee();
        when(mapstructMapper.employeeDTOToEmployee(employeeDTO)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);

        // when
        EmployeeDTO result = underTest.saveEmployee(employeeDTO);

        // then
        assertNotNull(result);
    }

    @Test
    void testUpdateEmployee() {
        // given
        UUID pathId = UUID.randomUUID();
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(pathId);
        Employee employee = new Employee();
        when(mapstructMapper.employeeDTOToEmployee(employeeDTO)).thenReturn(employee);
        when(employeeRepository.existsById(pathId)).thenReturn(true);
        when(employeeRepository.save(employee)).thenReturn(employee);

        // when
        EmployeeDTO result = underTest.updateEmployee(pathId, employeeDTO);

        // then
        assertNotNull(result);
    }

    @Test
    void testUpdateEmployeeWrongInput() {
        // given
        UUID pathId = UUID.randomUUID();
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(UUID.randomUUID()); // Different ID than pathId
        Employee employee = new Employee();
        when(mapstructMapper.employeeDTOToEmployee(employeeDTO)).thenReturn(employee);
        when(employeeRepository.existsById(pathId)).thenReturn(true);

        // then
        assertThrows(WrongInputException.class, () -> underTest.updateEmployee(pathId, employeeDTO));
    }

    @Test
    void testDeleteById() {
        // given
        UUID employeeId = UUID.randomUUID();
        when(employeeRepository.existsById(employeeId)).thenReturn(true);

        // when
        underTest.deleteById(employeeId);

        // then
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }

    @Test
    void testDeleteByIdNotFound() {
        // given
        UUID employeeId = UUID.randomUUID();
        when(employeeRepository.existsById(employeeId)).thenReturn(false);

        // then
        assertThrows(NotFoundException.class, () -> underTest.deleteById(employeeId));
    }
}
