package com.bootcamp.backend.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Optional<Employee> findUserByUsername(String username);

    @Query("SELECT e FROM Employee e WHERE e.firstName LIKE %:name% OR e.surname LIKE %:name%")
    List<Employee> findByFirstNameOrSurname(@Param("name") String name);

}