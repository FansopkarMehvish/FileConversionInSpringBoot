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
    public ResponseEntity<List<Employee>> importFromCSVtoList() {
        try{
            List<Employee> addedEmployees = employeeService.importFromCSVtoList();
            return new ResponseEntity<>(addedEmployees, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/exportToCSVFromList")
    public ResponseEntity<String> exportToCSVFromList() {
        try {
            employeeService.exportToCSVFromList();
            return new ResponseEntity<>("Export Successful", HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>("Error exporting CSV", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/importFromCSVtoDB")
    public List<Employee> importFromCSVtoDB() {
        return employeeService.importFromCSVtoDB();
    }

    @PostMapping("/exportToCSVFromDB")
    public ResponseEntity<String> exportToCSVFromDB() {
        try {
            employeeService.exportToCSVFromDB();
            return new ResponseEntity<>("Export Successful", HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>("Error exporting CSV", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/exportToPDF")
    public ResponseEntity<String> exportToPDF() {
        try {
            employeeService.exportToPDFfromDB();
            return new ResponseEntity<>("PDF Exported Successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error exporting PDF", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
