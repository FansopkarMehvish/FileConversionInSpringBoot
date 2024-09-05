package com.example.FileConversion.service;

import com.example.FileConversion.model.Employee;
import com.example.FileConversion.repository.EmployeeRepository;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    private static final String CSV_SEPARATOR = ",";

    // Import data from CSV to List of Employees
    public List<Employee> importFromCSVtoList(String filePath) {
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            bufferedReader.readLine();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(CSV_SEPARATOR);
                employees.add(new Employee(Long.parseLong(data[0]), data[1], data[2], Double.parseDouble(data[3]), data[4]));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to import CSV file", e);
        }
        return employees;
    }

    // Export data to CSV from List of Employees
    public void exportToCSVFromList(List<Employee> employees, String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath, true)) {
            File file = new File(filePath);
            if (file.length() == 0) {
                fileWriter.append("id,name,position,salary,department\n");
            }
            for (Employee employee : employees) {
                fileWriter.append(String.valueOf(employee.getId()))
                        .append(CSV_SEPARATOR)
                        .append(employee.getName())
                        .append(CSV_SEPARATOR)
                        .append(employee.getPosition())
                        .append(CSV_SEPARATOR)
                        .append(String.valueOf(employee.getSalary()))
                        .append(CSV_SEPARATOR)
                        .append(employee.getDepartment())
                        .append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while exporting data to CSV" + e);
        }
    }

    // Import data from CSV to Database of Employees
    public List<Employee> importFromCSVtoDB(String filePath) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            bufferedReader.readLine();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(CSV_SEPARATOR);
                if (data.length == 5) {
                    Employee employee = new Employee(Long.parseLong(data[0]), data[1], data[2], Double.parseDouble(data[3]), data[4]);
                    employeeRepository.save(employee);
                } else {
                    System.err.println("Unexpected record format: " + line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to import CSV file", e);
        }
        return employeeRepository.findAll();
    }

    // Export data to CSV from Database of Employees
    public void exportToCSVFromDB(String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath, true)) {
            File file = new File(filePath);
            if (file.length() == 0) {
                fileWriter.append("id,name,position,salary,department\n");
            }

            List<Employee> employees = employeeRepository.findAll();

            for (Employee employee : employees) {
                fileWriter.append(String.valueOf(employee.getId()))
                        .append(CSV_SEPARATOR)
                        .append(employee.getName())
                        .append(CSV_SEPARATOR)
                        .append(employee.getPosition())
                        .append(CSV_SEPARATOR)
                        .append(String.valueOf(employee.getSalary()))
                        .append(CSV_SEPARATOR)
                        .append(employee.getDepartment())
                        .append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while exporting data to CSV", e);
        }

    }

    // Export data to PDF from Database of Employees
    public void exportToPDFfromDB(String filePath) {
        List<Employee> employees = employeeRepository.findAll();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Phrase("Employee Data"));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            table.addCell("ID");
            table.addCell("Name");
            table.addCell("Position");
            table.addCell("Salary");
            table.addCell("Department");

            for (Employee employee : employees) {
                table.addCell(String.valueOf(employee.getId()));
                table.addCell(employee.getName());
                table.addCell(employee.getPosition());
                table.addCell(String.valueOf(employee.getSalary()));
                table.addCell(employee.getDepartment());
            }
            document.add(table);
        } catch (DocumentException | FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            document.close();
        }
        System.out.println("PDF created successfully at " + filePath);
    }
}
