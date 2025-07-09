package com.swissre.code.service;

import com.swissre.code.model.Employee;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {

    public Map<Integer, Employee> employeeMap = new HashMap<>();
    public Map<Integer, List<Employee>> managersWithSubOrdinates = new HashMap<>();
    public StringBuilder managersWithLessSalary = new StringBuilder();
    public StringBuilder managersWithHighSalary = new StringBuilder();
    public StringBuilder employeesWithReportingLinesMoreThan4 = new StringBuilder();
    Employee ceo = null;

    @PostConstruct
    public void loadEmployees() {
        readEmployeesFromCSVFile();
        getManagersHierarchy();
        findManagersWithGivenSalaryConditions();
        checkEmployeeReportingLines();
        printEmployeeResult();
    }

    public void readEmployeesFromCSVFile() {
        // Logic to read employees from CSV file and populate the employees list
        ClassPathResource resource = new ClassPathResource("static/employeeList.csv");
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 3) {
                    extracted(values);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void extracted(String[] values) {
        int id = Integer.parseInt(values[0].trim());
        String firstName = values[1].trim();
        String lastName = values[2].trim();
        double salary = Double.parseDouble(values[3].trim());
        Integer managerId = values.length > 4 && !values[4].trim().isEmpty()
                ? Integer.parseInt(values[4].trim())
                : null;
        Employee employee = new Employee(id, firstName, lastName, salary, managerId);
        employeeMap.put(employee.getId(), employee);

        if (employee.getManagerId() == null) {
            ceo = employee;
        }
    }

    public void getManagersHierarchy() {
        for (Employee employee : employeeMap.values()) {
            Integer managerId = employee.getManagerId();
            if (managerId != null) {
                managersWithSubOrdinates.computeIfAbsent(managerId, mId -> new ArrayList<>()).add(employee);
            }
        }
    }

    public void findManagersWithGivenSalaryConditions() {

        for (Map.Entry<Integer, List<Employee>> entry : managersWithSubOrdinates.entrySet()) {

            double avgSalary = entry.getValue().stream().mapToDouble(Employee::getSalary).average().orElse(0.0);

            double lowTarget = avgSalary * (1.2);
            double highTarget = avgSalary * (1.5);

            Employee managerEmployee = employeeMap.get(entry.getKey());
            if (managerEmployee.getSalary() <= lowTarget) {
                double lowDifference = lowTarget - managerEmployee.getSalary();
                managersWithLessSalary.append("Manager ").append(managerEmployee.getFirstName())
                        .append(" earns less than average salary by amount ").append(lowDifference).append("\n");
            } else if (managerEmployee.getSalary() >= highTarget) {
                double highDifference = managerEmployee.getSalary() - highTarget;
                managersWithHighSalary.append("Manager ").append(managerEmployee.getFirstName())
                        .append(" earns more than average salary by amount ").append(highDifference).append("\n");
            }
        }
    }

    public void checkEmployeeReportingLines() {

        for (Employee employee : employeeMap.values()) {
            int lines = countManagerLines(employee);
            if (lines > 4) {
                employeesWithReportingLinesMoreThan4.append("Employee ").append(employee.getFirstName())
                        .append(" have reporting line which is too long by ").append(lines - 4).append("/n");
            }

        }
    }

    private int countManagerLines(Employee employee) {
        int count = 0;
        while (employee.getManagerId() != null) {
            count++;
            employee = employeeMap.get(employee.getManagerId());
        }
        return count;
    }

    private void printEmployeeResult() {
        System.out.println(managersWithLessSalary.toString());
        System.out.println(managersWithHighSalary.toString());
        System.out.println(employeesWithReportingLinesMoreThan4.toString());
    }
}