package com.example.FileConversion.service;

import com.example.FileConversion.model.Employee;
import com.example.FileConversion.repository.EmployeeRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
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
    private static final File IMPORT_FILE_PATH = new File("C:\\Users\\mehvishf\\Desktop\\BITS_Assessment\\FileConversion\\src\\main\\java\\com\\example\\FileConversion\\EmployeeData.csv");
    private static final File EXPORT_FROM_DB_FILE_PATH = new File("C:\\Users\\mehvishf\\Desktop\\BITS_Assessment\\FileConversion\\EmployeeDataFromDB.csv");
    private static final File EXPORT_FROM_LIST_FILE_PATH = new File("C:\\Users\\mehvishf\\Desktop\\BITS_Assessment\\FileConversion\\EmployeeDataFromList.csv");
    private static final String PDF_EXPORT_PATH = "C:\\Users\\mehvishf\\Desktop\\BITS_Assessment\\FileConversion\\EmployeeData.pdf";

    // Import data from CSV to List of Employees
    public List<Employee> importFromCSVtoList() {
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(IMPORT_FILE_PATH))) {
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

    public void exportToCSVFromList() {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(4L, "Michael Brown", "Sales Representative", 60000.0, "Sales"));
        employees.add(new Employee(5L, "Sarah Davis", "HR Specialist", 62000.0, "Human Resources"));
        employees.add(new Employee(6L, "Chris Wilson", "Data Analyst", 68000.0, "Data Science"));
        try (FileWriter fileWriter = new FileWriter(EXPORT_FROM_LIST_FILE_PATH, true)) {
            fileWriter.append("id,name,position,salary,department\n");
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

    public List<Employee> importFromCSVtoDB() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(IMPORT_FILE_PATH))) {
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

    public void exportToCSVFromDB() {
        try (FileWriter fileWriter = new FileWriter(EXPORT_FROM_DB_FILE_PATH, true)) {
            if (EXPORT_FROM_DB_FILE_PATH.length() == 0) {
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

    public void exportToPDFfromDB() {
        List<Employee> employees = employeeRepository.findAll();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(PDF_EXPORT_PATH));
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
        System.out.println("PDF created successfully at " + PDF_EXPORT_PATH);
    }
}
