package com.example.FileConversion.controller;

import com.example.FileConversion.model.Employee;
import com.example.FileConversion.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;


    @PostMapping("/importFromCSVtoList")
    public ResponseEntity<List<Employee>> importFromCSVtoList(@RequestParam String filePath) {
        try {
            List<Employee> addedEmployees = employeeService.importFromCSVtoList(filePath);
            return new ResponseEntity<>(addedEmployees, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/exportToCSVFromList")
    public ResponseEntity<String> exportToCSVFromList(@RequestBody List<Employee> employees, @RequestParam String filePath) {
        try {
            employeeService.exportToCSVFromList(employees, filePath);
            return new ResponseEntity<>("Export Successful", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error exporting CSV", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/importFromCSVtoDB")
    public ResponseEntity<List<Employee>> importFromCSVtoDB(@RequestParam String filePath) {
        try {
            List<Employee> employees = employeeService.importFromCSVtoDB(filePath);
            return new ResponseEntity<>(employees, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/exportToCSVFromDB")
    public ResponseEntity<String> exportToCSVFromDB(@RequestParam String filePath) {
        try {
            employeeService.exportToCSVFromDB(filePath);
            return new ResponseEntity<>("Export Successful", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error exporting CSV", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/exportToPDF")
    public ResponseEntity<String> exportToPDF(@RequestParam String filePath) {
        try {
            employeeService.exportToPDFfromDB(filePath);
            return new ResponseEntity<>("PDF Exported Successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error exporting PDF", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
