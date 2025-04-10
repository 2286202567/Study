package com.lz.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    @RequestMapping("/employee/list")
    public String list(){
        return "employee.list";
    }
    @RequestMapping("/employee/add")
    public String add(){
        return "employee.add";
    }
    @RequestMapping("/employee/update")
    public String update(){
        return "employee.update";
    }
    @RequestMapping("/employee/delete")
    public String delete(){
        return "employee.delete";
    }
}
