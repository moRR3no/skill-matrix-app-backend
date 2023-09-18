package com.bootcamp.backend;

import com.bootcamp.backend.employee.Employee;
import com.bootcamp.backend.employee.EmployeeDTO;
import com.bootcamp.backend.employee.EmployeeRepository;
import com.bootcamp.backend.employee.EmployeeService;
import com.bootcamp.backend.exceptions.NotFoundException;
import com.bootcamp.backend.exceptions.WrongInputException;
import com.bootcamp.backend.mappers.MapStructMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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

    private Employee employee;

    EmployeeDTO employeeDTO;

    private UUID id;


    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        String firstName = "Andrew";
        String surname = "Owen";
        LocalDate date = LocalDate.now();

        employee = new Employee();
        employee.setId(id);
        employee.setFirstName(firstName);
        employee.setSurname(surname);
        employee.setDate(date);

        employeeDTO = new EmployeeDTO();
        employeeDTO.setId(id);
        employeeDTO.setFirstName(firstName);
        employeeDTO.setSurname(surname);
        employeeDTO.setDate(date);
    }

    @Test
    void canGetEmployees() {
        //given
        List<Employee> employeeList = List.of(employee);
        when(employeeRepository.findAll()).thenReturn(employeeList);

        //when
        List<EmployeeDTO> employeeDTOList = this.underTest.getEmployees();

        //then
        assertThat(mapstructMapper.employeesToEmployeeDTOs(employeeList)).isEqualTo(employeeDTOList);
        verify(this.employeeRepository).findAll();
    }

    @Test
    void canGetEmployeeById() {
        //given
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));

        //when
        employeeDTO = this.underTest.getEmployeeById(id);

        //then
        assertThat(mapstructMapper.employeeToEmployeeDTO(employee)).isEqualTo(employeeDTO);
        verify(this.employeeRepository).findById(id);
    }

    @Test
    void canGetEmployeeByIdNotFound() {
        // given
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        // when - then
        assertThrows(NotFoundException.class, () -> underTest.getEmployeeById(id));
    }

    @Test
    void canSaveEmployee() {
        // given
        when(employeeRepository.findById(employeeDTO.getId())).thenReturn(Optional.empty());
        when(mapstructMapper.employeeDTOToEmployee(employeeDTO)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(mapstructMapper.employeeToEmployeeDTO(employee)).thenReturn(employeeDTO);

        // when
        EmployeeDTO savedEmployeeDTO = underTest.saveEmployee(employeeDTO);

        // then
        assertThat(savedEmployeeDTO).isEqualTo(employeeDTO);
        verify(employeeRepository, times(1)).findById(employeeDTO.getId());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void canUpdateEmployee() {
        //given
        when(mapstructMapper.employeeDTOToEmployee(employeeDTO)).thenReturn(employee);
        when(mapstructMapper.employeeToEmployeeDTO(employee)).thenReturn(employeeDTO);
        when(employeeRepository.existsById(id)).thenReturn(true);
        when(employeeRepository.save(employee)).thenReturn(employee);

        // when
        EmployeeDTO updatedEmployeeDTO = underTest.updateEmployee(id, employeeDTO);

        // then
        assertThat(updatedEmployeeDTO).isEqualTo(employeeDTO);
        verify(employeeRepository, times(1)).existsById(id);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void canUpdateEmployeeWrongInput() {
        //given
        UUID differentId = UUID.randomUUID();

        // when - then
        assertThatThrownBy(() -> underTest.updateEmployee(differentId, employeeDTO)).isInstanceOf(WrongInputException.class);
    }

    @Test
    void canDeleteEmployeeById() {
        // given
        when(employeeRepository.existsById(id)).thenReturn(true);

        // when
        underTest.deleteById(id);

        // then
        verify(employeeRepository, times(1)).deleteById(id);
    }

    @Test
    void canDeleteEmployeeByIdNotFound() {
        // given
        UUID employeeId = UUID.randomUUID();
        when(employeeRepository.existsById(employeeId)).thenReturn(false);

        // when - then
        assertThrows(NotFoundException.class, () -> underTest.deleteById(employeeId));
    }
}