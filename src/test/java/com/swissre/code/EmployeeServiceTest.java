package com.swissre.code;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.swissre.code.model.Employee;
import com.swissre.code.service.EmployeeService;

public class EmployeeServiceTest {

    EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService();

        // Create manager and subordinates

        Employee emp1 = new Employee(1, "John", "CEO", 300000, null);
        Employee emp2 = new Employee(2, "Alice", "Manager2", 100000, 1);
        Employee emp3 = new Employee(3, "Bob", "Manager3", 95000, 2);
        Employee emp4 = new Employee(4, "Carol", "Manager4", 105000, 3);
        Employee emp5 = new Employee(5, "David", "Manager5", 98000, 3);
        Employee emp6 = new Employee(6, "Eva", "Manager6", 90000, 4);
        Employee emp7 = new Employee(7, "Frank", "Manager7", 92000, 4);
        Employee emp8 = new Employee(8, "Grace", "Manager8", 91000, 5);
        Employee emp9 = new Employee(9, "Hank", "Manager9", 93000, 5);
        Employee emp10 = new Employee(10, "Ian", "Manager10", 94000, 6);

        // Populate employeeMap

        employeeService.employeeMap.put(emp1.getId(), emp1);
        employeeService.employeeMap.put(emp2.getId(), emp2);
        employeeService.employeeMap.put(emp3.getId(), emp3);
        employeeService.employeeMap.put(emp4.getId(), emp4);
        employeeService.employeeMap.put(emp5.getId(), emp5);
        employeeService.employeeMap.put(emp6.getId(), emp6);
        employeeService.employeeMap.put(emp7.getId(), emp7);
        employeeService.employeeMap.put(emp8.getId(), emp8);
        employeeService.employeeMap.put(emp9.getId(), emp9);
        employeeService.employeeMap.put(emp10.getId(), emp10);

        // Populate managersWithSubOrdinates

        employeeService.getManagersHierarchy();

        // Reset StringBuilders
        employeeService.managersWithLessSalary = new StringBuilder();
        employeeService.managersWithHighSalary = new StringBuilder();
    }

    @Test
    void testFindManagersWithGivenSalaryConditions_ManagerWithLessSalary() {
        // Set manager's salary below 1.2 * avg subordinate salary

        employeeService.findManagersWithGivenSalaryConditions();

        String result = employeeService.managersWithLessSalary.toString();
        assertTrue(result.contains("Manager Alice earns less than average salary by amount 14000.0"));

    }

    @Test
    void testFindManagersWithGivenSalaryConditions_ManagerWithHighSalary() {
        // Set manager's salary above 1.5 * avg subordinate salary

        employeeService.findManagersWithGivenSalaryConditions();

        String result = employeeService.managersWithHighSalary.toString();
        assertTrue(result.contains("Manager John earns more than average salary by amount 150000.0"));
    }

    @Test
    void testFindManagersWithGivenSalaryConditions_ManagerWithinRange() {
        // Set manager's salary between 1.2*avg and 1.5*avg
        // = 84000, 1.5*avg = 105000
        employeeService.employeeMap.get(1).setSalary(280000);
        employeeService.employeeMap.get(2).setSalary(190000);
        employeeService.employeeMap.get(3).setSalary(155000);
        employeeService.employeeMap.get(4).setSalary(130000);
        employeeService.employeeMap.get(5).setSalary(112000);
        employeeService.employeeMap.get(6).setSalary(113000);

        employeeService.findManagersWithGivenSalaryConditions();

        assertEquals("", employeeService.managersWithLessSalary.toString());
        assertEquals("", employeeService.managersWithHighSalary.toString());
    }

    @Test
    public void checkEmployeeReportingLines() {
        // Test for employees with reporting lines more than 4
        employeeService.checkEmployeeReportingLines();

        String result = employeeService.employeesWithReportingLinesMoreThan4.toString();
        assertTrue(result.contains("Employee Ian have reporting line which is too long by 1"));
    }
}
