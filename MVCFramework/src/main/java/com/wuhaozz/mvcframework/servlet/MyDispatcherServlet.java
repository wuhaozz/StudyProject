package com.wuhaozz.mvcframework.servlet;

import com.wuhaozz.mvcframework.annotation.MyAutowired;
import com.wuhaozz.mvcframework.annotation.MyController;
import com.wuhaozz.mvcframework.annotation.MyRequestMapping;
import com.wuhaozz.mvcframework.annotation.MyRequestParam;
import com.wuhaozz.mvcframework.annotation.MyService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyDispatcherServlet extends HttpServlet {

    // 配置文件，存放application.properties中的配置信息
    private Properties p = new Properties();

    // 存放被扫描到的类名
    private List<String> classNames = new ArrayList<>();

    // IOC容器
    private Map<String, Object> ioc = new HashMap<>();

    // handlerMapping
    private List<Handler> handlerMapping = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        // 1、加载配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));

        // 2、扫描所有相关的类
        doSacnner(p.getProperty("scanPackage"));

        // 3、初始化所有相关Class的实例、并且将其保存到IOC容器中
        doInstance();

        // 4、自动化的依赖注入
        doAutowired();

        // 5、初始化HandleMapping
        initHandleMapping();

        System.out.println("自定义Dispatcher初始化完成。");
    }

    private void doLoadConfig(String location) {
        // 获取流
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(location);

        // 加载进配置文件类
        try {
            p.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void doSacnner(String packageName) {
        // 把包名转成url
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));

        // 获取url对应的类
        File classesDir = new File(url.getFile());

        for (File file : classesDir.listFiles()) {

            // 判断是否是文件夹
            if (file.isDirectory()) {

                // 递归所有的文件夹
                doSacnner(packageName + "." + file.getName());

            } else {

                // 类名
                String className = packageName + "." + file.getName().replace(".class", "");

                // 保存进IOC容器
                classNames.add(className);
            }

        }

    }

    private void doInstance() {

        if (classNames.isEmpty()) {
            // 没有扫描到任何类
            System.out.println("没有扫描到任何类");
            return;
        }

        try {

            for (String className : classNames) {

                // 把class扫描到
                Class<?> clazz = Class.forName(className);

                // 进行实例化，原则问题
                //判断，只有加了特定的注解的类才初始化
                if (clazz.isAnnotationPresent(MyController.class)) {

                    String beanName = lowerFirst(clazz.getSimpleName());

                    ioc.put(beanName, clazz.newInstance());

                } else if (clazz.isAnnotationPresent(MyService.class)) {
                    // beanName beanId
                    // 1、默认采用类名的首字母小写
                    // 2、如果自己定义了beanName、beanId，优先使用自定义的beanName、beanId
                    // 3、根据类型匹配，利用接口作为key

                    // 获取MyService注解
                    MyService service = clazz.getAnnotation(MyService.class);

                    // 自定义的beanName
                    String beanName = service.value();

                    // 如果有自定义beanName不为空，就使用自定义的beanName
                    if ("".equals(beanName.trim())) {
                        beanName = lowerFirst(clazz.getSimpleName());
                    }

                    Object instance = clazz.newInstance();
                    ioc.put(beanName, instance);

                    // 判断是否有多个实现
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> i : interfaces) {
                        ioc.put(i.getName(), instance);
                    }

                } else {
                    // 没有加特定注解的continue
                    continue;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void doAutowired() {

        if (ioc.isEmpty()) {
            System.out.println("没有一个实例");
            return;
        }

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {

            // 在Spring里面没有隐私
            // 包括私有的
            // 反射机制里的暴力反射
            Field[] fields = entry.getValue().getClass().getDeclaredFields();

            for (Field field : fields) {

                // 没有加Autowired注解，忽视
                if (!field.isAnnotationPresent(MyAutowired.class)) {
                    continue;
                }

                MyAutowired autowired = field.getAnnotation(MyAutowired.class);
                String beanName = autowired.value().trim();

                // 空的话，可能是一个接口，进行根正
                if ("".equals(beanName)) {
                    beanName = field.getType().getName();
                }

                // 暴力访问
                field.setAccessible(true);
                try {
                    // 依赖注入
                    field.set(entry.getValue(), ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                }

            }

        }

    }

    private void initHandleMapping() {

        if (ioc.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {

            // 只扫描MyController中的MyRequestMapping
            Class<?> clazz = entry.getValue().getClass();
            if (!clazz.isAnnotationPresent(MyController.class)) {
                continue;
            }

            String url = null;
            // 获取Controller的url配置
            if (clazz.isAnnotationPresent(MyRequestMapping.class)) {
                MyRequestMapping requestMapping = clazz.getAnnotation(MyRequestMapping.class);
                url = requestMapping.value();
            }

            // 获取Method的url配置，扫描类下public的方法
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {

                // 方法上没有加MyRequestMapping注解的直接忽略
                if (!method.isAnnotationPresent(MyRequestMapping.class)) {
                    continue;
                }

                // 映射url
                MyRequestMapping requestMapping = method.getAnnotation(MyRequestMapping.class);
                String regex = ("/" + url + requestMapping.value()).replaceAll("/+", "/");
                Pattern pattern = Pattern.compile(regex);
                handlerMapping.add(new Handler(pattern, entry.getValue(), method));
                System.out.println("Mapping:" + regex + "," + method);
            }

        }

    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        try {
            Handler handler = getHandler(req);

            if (handler == null) {
                // 如果没有匹配上，返回404
                resp.getWriter().write("404 Not Found!");
                return;
            }

            // 获取方法的参数列表
            Class<?>[] paramTypes = handler.method.getParameterTypes();

            // 保存所有需要自动赋值的参数值
            Object[] paramValues = new Object[paramTypes.length];

            Map<String, String[]> params = req.getParameterMap();
            for (Map.Entry<String, String[]> param : params.entrySet()) {
                String value = Arrays.toString(param.getValue())
                        .replaceAll("\\[|\\]", "")
                        .replaceAll(",\\s", ",");

                // 如果找到匹配的对象，则开始填充参数值
                if (!handler.paramIndexMapping.containsKey(param.getKey())) {
                    continue;
                }
                int index = handler.paramIndexMapping.get(param.getKey());
                paramValues[index] = convert(paramTypes[index], value);
            }

            //设置方法中的request和response对象
            int reqIndex = handler.paramIndexMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex] = req;
            int respIndex = handler.paramIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[respIndex] = resp;

            handler.method.invoke(handler.controller, paramValues);

        } catch (Exception e) {
            throw e;
        }

    }

    private String lowerFirst(String str) {
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private Handler getHandler(HttpServletRequest req) throws Exception {
        if (handlerMapping.isEmpty()) {
            return null;
        }

        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");

        for (Handler handler : handlerMapping) {
            Matcher matcher = handler.pattern.matcher(url);
            // 如果没有匹配上，继续匹配下一个
            if (!matcher.matches()) {
                continue;
            }

            return handler;
        }

        return null;
    }

    private Object convert(Class<?> type, String value) {
        if (Integer.class == type) {
            return Integer.valueOf(value);
        }
        return value;
    }

    /**
     * Hander记录controller中的RequestMapping和Method的对应关系
     */
    private class Handler {

        protected Object controller;// 保存方法对应的实例
        protected Method method;// 保存映射的方法
        protected Pattern pattern;
        protected Map<String, Integer> paramIndexMapping; // 参数顺序

        /**
         * 构建一个Handler的基本参数
         *
         * @param pattern
         * @param controller
         * @param method
         */
        protected Handler(Pattern pattern, Object controller, Method method) {
            this.controller = controller;
            this.method = method;
            this.pattern = pattern;

            paramIndexMapping = new HashMap<>();
            putParamIndexMapping(method);
        }

        private void putParamIndexMapping(Method method) {

            // 提取方法中加了注解的参数
            Annotation[][] pa = method.getParameterAnnotations();
            for (int i = 0; i < pa.length; i++) {
                for (Annotation a : pa[i]) {
                    if (a instanceof MyRequestParam) {
                        String paramName = ((MyRequestParam) a).value();
                        if (!"".equals(paramName.trim())) {
                            paramIndexMapping.put(paramName, i);
                        }
                    }
                }
            }

            // 提取方法中的request和response参数
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> type = parameterTypes[i];
                if (type == HttpServletRequest.class || type == HttpServletResponse.class) {
                    paramIndexMapping.put(type.getName(), i);
                }
            }

        }


    }

}
