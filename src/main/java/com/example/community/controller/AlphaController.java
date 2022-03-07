package com.example.community.controller;

import com.example.community.service.AlphaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/alpha")
public class AlphaController {

    @Autowired
    private AlphaService alphaService;

    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return "test";
    }

    @ResponseBody
    @GetMapping("/data")
    public String getData(){
        return alphaService.find();
    }

    //原生的http请求与响应
    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response){
        System.out.println("请求头:");
        System.out.println("请求方法:"+request.getMethod());
        System.out.println(request.getServletPath());

        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            String header = request.getHeader(name);
            System.out.println(name+":"+header);
        }
        System.out.println("请求携带的参数为："+request.getParameter("param"));

        response.setContentType("text/html;charset=utf-8");
        try (
                PrintWriter writer = response.getWriter();
        ){
            writer.write("<h1>响应内容</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    总结: 获取请求的路径是getServletPath 写错为getContextPath contextPath为获取当前应用的根路径
//    设置响应内容类型为中文utf-8 应该在获取writer打印流之前
    
    
    //student?current=1&limit=12
    @RequestMapping(path ="/students" ,method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(@RequestParam(name = "current",required = false,defaultValue = "1")
                                          int current,
                              @RequestParam(name = "limit",required = false,defaultValue ="1")
                                      int limit){
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }

    //student/123
    @RequestMapping(path = "/student/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id")int id){
        System.out.println(id);
        return "a student";
    }

    @RequestMapping(path = "/student",method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name,int age){
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    //响应html数据 第一种方式
    @RequestMapping(path = "/teacher",method = RequestMethod.GET)
    public ModelAndView getTeacher(){
        ModelAndView mv=new ModelAndView();
        mv.addObject("name","张三");
        mv.addObject("age",21);
        mv.setViewName("/demo/view");
        return mv;
    }

    //第二种方式
    @RequestMapping(path = "/school",method = RequestMethod.GET)
    public String getSchool(Model model){
        model.addAttribute("name","齐鲁工业大学");
        model.addAttribute("age",20);
        return "/demo/view";
    }

    //响应JSON数据（异步请求）  什么是异步请求? 比如注册bilibili时候，点击注册页面，输入昵称，提示该账号已经被注册，但是页面并没有
    //刷新，这个就是异步
    //Java对象 -> JSON字符串 ->JS对象
    @RequestMapping(path = "/emp",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getEmp(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name","榨干啊");
        map.put("age",20);
        map.put("salary",8000.00);
        return map;  //浏览器自动转为JSON对象
    }

    @RequestMapping(path = "/emps",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> getEmps(){
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        Map<String,Object> map=new HashMap<>();
        map.put("name","李四");
        map.put("salary",8000.00);
        map.put("age",20);
        list.add(map);

        map=new HashMap<>();
        map.put("name","张三");
        map.put("age",23);
        map.put("salary",3999.00);
        list.add(map);

        map=new HashMap<>();
        map.put("name","王五");
        map.put("age",33);
        map.put("salary",22999.00);
        list.add(map);

        return list;
    }
}
