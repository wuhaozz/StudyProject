package com.wuhaozz.mvcframework.service.impl
        ;

import com.wuhaozz.mvcframework.annotation.MyService;
import com.wuhaozz.mvcframework.service.IDemoService;

@MyService("myDemoService")
public class DemoService implements IDemoService {

    @Override
    public String get(String name) {
        return "Hello," + name;
    }

}
