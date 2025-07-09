                                                    Employee Organization Check

Problem Statement:
  From the given list of employee data, make sure that 
  1. Every manager earns at least 20% more than the average salary of their direct subordinates, but no more than 50% more than that average.
  2. The company wants to avoid too long reporting lines; therefore, we would like to identify all employees who have more than 4 managers between them and the CEO.

Sample Employee data:
  Id,firstName,lastName,salary,managerId
  123,Joe,Doe,60000,
  124,Martin,Chekov,45000,123
  125,Bob,Ronstad,47000,123
  300,Alice,Hasacat,50000,124
  305,Brett,Hardleaf,34000,300

Tasks to be achieved:
  1. Reads employee data from a CSV file
  2. Build a managerial hierarchy to identify managers with subordinates
  3. Identify
       a. Managers earning less than 20% more than their subordinates’ average, and by how much.
       b. Managers earning more than 50% more, and by how much.
       c. Employees with more than 4 managers between them and the CEO, and by how many extra levels.
  4. Print the resulting output to the console.

Technologies used:
  1. Java 8
  2. Spring boot 3.5
  3. Junit 5

