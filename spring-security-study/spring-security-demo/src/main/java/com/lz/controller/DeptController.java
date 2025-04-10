package com.lz.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeptController {

    @RequestMapping("/dept/list")
    @PreAuthorize("@ss.hasPermi('syslog:list')")
    public String list(){
        return "dept.list";
    }

    @RequestMapping("/dept/add")
    @PreAuthorize("@fun.getBoolean('qwe')")
    public String add(){
        return "dept.add";
    }

    @RequestMapping("/dept/update")
    public String update(){
        return "dept.update";
    }

    @RequestMapping("/dept/delete")
    public String delete(){
        return "dept.delete";
    }

}
