package com.mybatis;

import com.user.pojo.User;
import jdk.internal.org.objectweb.asm.Handle;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapperProxyFactory {
    
   static Map<String, TypeHandler> typeMap = new HashMap<>();
    static {
        try {
            typeMap.put("java.lang.String",new StringTypeHandler());
            typeMap.put("java.lang.Integer",new IntegerTypeHandler());
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static <T> T getMapper(Class<T> mapper) {
        Map<String, Object> paramMap = new HashMap<>();

        Object instance = Proxy.newProxyInstance(mapper.getClassLoader(), new Class[]{mapper}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //解析SQl --> 执行SQL -->  结果处理
                //JDBC
                //1.创建数据库连接
                Connection connection = getConnection();

                //方法的参数
                Parameter[] parameters = method.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    Parameter parameter = parameters[i];
                    String value = parameter.getAnnotation(Param.class).value();
                    paramMap.put(parameters[i].getName(), args[i]);
                    paramMap.put(value, args[i]);
                }
                //获取Mapper的SQl
                Selector annotation = method.getAnnotation(Selector.class);
                String sql = annotation.value();
                //将#{} 转换成  ？
                ParameterMappingTokenHandler mappingTokenHandler = new ParameterMappingTokenHandler();
                GenericTokenParser parser = new GenericTokenParser("#{", "}", mappingTokenHandler);
                String parseSql = parser.parse(sql);
                List<ParameterMapping> parameterMappings = mappingTokenHandler.getParameterMappings();
                //2.构建PrepareStatement
                PreparedStatement preparedStatement = connection.prepareStatement(parseSql);
                //Annotation[] declaredAnnotations = UserMapper.class.getDeclaredAnnotations();
                //当前执行的方法
                String methodName = method.getName();
                //返回数据类型  现在是List<User>
//                Class<Object> returnType = method.getReturnType();

                //args就是传入的参数
                Object arg = args[0];

                for (int i = 0; i < parameterMappings.size(); i++) {
                    Object value = paramMap.get(parameterMappings.get(i).getProperty());
                    String typeName = paramMap.get(parameterMappings.get(i).getProperty()).getClass().getName();
                    typeMap.get(typeName).setParam(preparedStatement,i+1,value);
                }


                preparedStatement.execute();

                ResultSet resultSet = preparedStatement.getResultSet();
                Type returnType = method.getGenericReturnType();
                Class clazz = null;
                if(returnType instanceof Class){
                    //不是泛型
                    clazz = (Class)returnType;
                }else if(returnType instanceof ParameterizedType){
                    //泛型
                    Type[] actualTypeArguments = ((ParameterizedType) returnType).getActualTypeArguments();
                    clazz = (Class)actualTypeArguments[0];
                }
                ResultSetMetaData metaData = resultSet.getMetaData();
                List<String> columnList= new ArrayList<>();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    columnList.add(metaData.getColumnName(i+1));
                }
                
                //处理对象的set方法
                Map<String,Method> methodMap= new HashMap<>();
                for (Method declaredMethod : clazz.getDeclaredMethods()) {
                    if(declaredMethod.getName().startsWith("set")){
                        String name = declaredMethod.getName();
                        String propertyName = name.substring(3);
                        propertyName = propertyName.substring(0, 1).toLowerCase(Locale.ROOT) + propertyName.substring(1);
                        methodMap.put(propertyName,declaredMethod);
                    }
                }

                //返回的结果集
                ArrayList<Object> list = new ArrayList<>();
                Object  result = null;
                while (resultSet.next()) {
                    Object object = clazz.newInstance();
                    for (int i = 0; i < columnList.size(); i++) {
                        String column = columnList.get(i);
                        Method setMethod = methodMap.get(column);
                        String parameterType = setMethod.getParameterTypes()[0].getName();
                        setMethod.invoke(object, typeMap.get(parameterType).getParam(resultSet, column));
                    }
                    list.add(object);
                }
                if(method.getReturnType().equals(List.class)){
                    result = list;
                }else{
                    result =list.get(0);
                }

                connection.close();
                return list;
            }
        });
        return (T) instance;
    }

    private static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mybatis_study?useSSL=false" +
                "&serverTimezone=UTC&characterEncoding=UTF-8", "root", "123456");
        return connection;
    }

}
