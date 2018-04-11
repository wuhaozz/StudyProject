package com.wuhaozz.mvcframework.action;

import com.wuhaozz.mvcframework.annotation.MyAutowired;
import com.wuhaozz.mvcframework.annotation.MyController;
import com.wuhaozz.mvcframework.annotation.MyRequestMapping;
import com.wuhaozz.mvcframework.annotation.MyRequestParam;
import com.wuhaozz.mvcframework.service.IDemoService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MyController
@MyRequestMapping("/demo")
public class DemoAction {

    @MyAutowired
    private IDemoService demoService;

    @MyRequestMapping("/query.json")
    public void query(HttpServletRequest req, HttpServletResponse resp,
                      @MyRequestParam("name") String name) {
        String result = demoService.get(name);
        try {
            resp.getWriter().write(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @MyRequestMapping("/edit.json")
    public void edit(HttpServletRequest req, HttpServletResponse resp, Integer id) {

    }

    @MyRequestMapping("/remove.json")
    public void remove(HttpServletRequest req, HttpServletResponse resp, Integer id) {

    }

}
