package com.swissre.code;

import com.swissre.code.model.Employee;
import com.swissre.code.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmployeeServiceTest {

    EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService();

        // Create manager and subordinates
        Employee manager = new Employee(1, "Alice", "Smith", 120000, null);
        Employee emp1 = new Employee(2, "Bob", "Jones", 60000, 1);
        Employee emp2 = new Employee(3, "Carol", "White", 70000, 1);
        Employee emp3 = new Employee(4, "David", "Brown", 80000, 1);

        // Populate employeeMap
        employeeService.employeeMap.put(manager.getId(), manager);
        employeeService.employeeMap.put(emp1.getId(), emp1);
        employeeService.employeeMap.put(emp2.getId(), emp2);
        employeeService.employeeMap.put(emp3.getId(), emp3);

        // Populate managersWithSubOrdinates
        List<Employee> subordinates = new ArrayList<>(Arrays.asList(emp1, emp2, emp3));
        employeeService.managersWithSubOrdinates.put(manager.getId(), subordinates);

        // Reset StringBuilders
        employeeService.managersWithLessSalary = new StringBuilder();
        employeeService.managersWithHighSalary = new StringBuilder();
    }

    @Test
    void testFindManagersWithGivenSalaryConditions_ManagerWithLessSalary() {
        // Set manager's salary below 1.2 * avg subordinate salary
        employeeService.employeeMap.get(1).setSalary(70000); // avg = 70000, 1.2*avg = 84000

        employeeService.findManagersWithGivenSalaryConditions();

        String result = employeeService.managersWithLessSalary.toString();
        assertTrue(result.contains("Manager Alice earns less than average salary by amount 14000.0"));
        assertEquals("", employeeService.managersWithHighSalary.toString());
    }

    @Test
    void testFindManagersWithGivenSalaryConditions_ManagerWithHighSalary() {
        // Set manager's salary above 1.5 * avg subordinate salary
        employeeService.employeeMap.get(1).setSalary(120000); // avg = 70000, 1.5*avg = 105000

        employeeService.findManagersWithGivenSalaryConditions();

        String result = employeeService.managersWithHighSalary.toString();
        assertTrue(result.contains("Manager Alice earns more than average salary by amount 15000.0"));
        assertEquals("", employeeService.managersWithLessSalary.toString());
    }

    @Test
    void testFindManagersWithGivenSalaryConditions_ManagerWithinRange() {
        // Set manager's salary between 1.2*avg and 1.5*avg
        employeeService.employeeMap.get(1).setSalary(90000); // avg = 70000, 1.2*avg = 84000, 1.5*avg = 105000

        employeeService.findManagersWithGivenSalaryConditions();

        assertEquals("", employeeService.managersWithLessSalary.toString());
        assertEquals("", employeeService.managersWithHighSalary.toString());
    }
}
