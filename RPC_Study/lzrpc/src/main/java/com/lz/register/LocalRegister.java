package com.lz.register;

import java.util.HashMap;
import java.util.Map;

//本地注册
public class LocalRegister {
    private static Map<String, Class> map = new HashMap<>();

    public void register(String interfaceName, String version, Class clazz) {
        map.put(interfaceName + version, clazz);
    }

    public Class get(String interfaceName, String version) {
        return map.get(interfaceName + version);
    }
}
