package com.spring;


import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestApplicationContext {
    private Class configClass;
    private ConcurrentHashMap<String, Object> singletonObject = new ConcurrentHashMap<>();//单例池
    private ConcurrentHashMap<String, BeanDefinition> definitionMap = new ConcurrentHashMap<>();//Bean实例对象

    private List<BeanPostProcessor> processorList = new ArrayList<>();//存放BeanPostProcessor对象数组

    public TestApplicationContext(Class configClass) {
        //获取配置类
        this.configClass = configClass;
        //解析配置类
        //ComponentScan注解-->扫描路径 -->解析注解
        scan(configClass);

        //创建Bean
        for (Map.Entry<String, BeanDefinition> entry : definitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if (beanDefinition.getScope().equals("singleton")) {
                Object object = createBean(beanName, beanDefinition);
                singletonObject.put(beanName, object);
            } else {
                //原型的
                createBean(beanName, beanDefinition);
            }
        }
    }

    private void scan(Class configClass) {
        ComponentScan componentScan = (ComponentScan) configClass.getDeclaredAnnotation(ComponentScan.class);
        String path = componentScan.value();
        System.out.println("扫描路径：" + path);
        path = path.replace(".", "/");
        //扫描路径  根据包名获取包下面所有的类
        //BootStrap -->lib/jre
        //Ext  -->lib/jre/ext
        //Application-->classPath
        ClassLoader classLoader = TestApplicationContext.class.getClassLoader();
        URL resource = classLoader.getResource(path);
        System.out.println("包名获取包下面所有的类：" + resource);
        //将目录转化成文件
        File files = new File(resource.getFile());
        if (files.isDirectory()) {
            File[] listFiles = files.listFiles();
            for (File file : listFiles) {
                System.out.println("文件转化ClasssName:" + file);
                System.out.println(file.getName());
                String fileName = file.getAbsolutePath();
                if (fileName.endsWith(".class")) {
                    String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                    String name = className.replace("\\", ".");
                    System.out.println("完整文件路径" + name);
                    try {
                        Class<?> loadClass = classLoader.loadClass(name);
                        if (loadClass.isAnnotationPresent(Component.class)) {
                            //bean -->  beanPostProcessor
                            if (BeanPostProcessor.class.isAssignableFrom(loadClass)) {
                                BeanPostProcessor instance = (BeanPostProcessor) loadClass.getDeclaredConstructor().newInstance();
                                processorList.add(instance);
                            }

                            //如果类上面有Component注解  说明这个类是需要被扫描的
                            Component component = loadClass.getDeclaredAnnotation(Component.class);
                            //解析Bean是单例还是原型
                            String beanName = component.value();
                            System.out.println("----" + beanName);
                            BeanDefinition definition = new BeanDefinition();
                            definition.setClazz(loadClass);
                            //判断Bean的类型  是原型还是单例的
                            if (loadClass.isAnnotationPresent(Scope.class)) {
                                Scope scope = loadClass.getDeclaredAnnotation(Scope.class);
                                definition.setScope(scope.value());
                            } else {
                                definition.setScope("singleton");
                            }
                            definitionMap.put(beanName, definition);
                        }
                    } catch (ClassNotFoundException | NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Object getBean(String beanName) {
        if (definitionMap.containsKey(beanName)) {
            BeanDefinition beanDefinition = definitionMap.get(beanName);
            if (beanDefinition.getScope().equals("singleton")) {
                return singletonObject.get(beanName);
            } else {
                //原型的  需要手动去创建
                return createBean(beanName, beanDefinition);
            }
        } else {
            throw new NullPointerException();
        }
    }


    public Object createBean(String beanName, BeanDefinition definition) {
        Object instance = null;
        try {
            Class clazz = definition.getClazz();
            instance = clazz.getDeclaredConstructor().newInstance();

            //依赖注入
            for (Field declaredField : clazz.getDeclaredFields()) {
                System.out.println(declaredField);
                if (declaredField.isAnnotationPresent(Autowired.class)) {
                    Object bean = getBean(declaredField.getName());
                    if (bean == null) {
                        throw new NullPointerException("未找到依赖注入对象");
                    }
                    //访问对象中的私有变量时可以绕开安全检查
                    declaredField.setAccessible(true);
                    declaredField.set(instance, getBean(declaredField.getName()));
                }
            }

            //Aware 回调
            if (instance instanceof BeanNameAware) {
                ((BeanNameAware) instance).setBeanName(beanName);
            }
            //BeanPostProcessor初始化前

            for (BeanPostProcessor processor : processorList) {
                instance = processor.postProcessBeforeInitialization(instance, beanName);
            }

            //初始化
            if (instance instanceof InitializingBean) {
                ((InitializingBean) instance).afterPropertiesSet();
            }

            //BeanPostProcessor初始化后

            for (BeanPostProcessor processor : processorList) {
                instance = processor.postProcessAfterInitialization(instance, beanName);
            }


        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }
}
