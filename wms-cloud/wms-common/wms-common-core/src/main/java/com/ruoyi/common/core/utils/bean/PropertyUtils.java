package com.ruoyi.common.core.utils.bean;

import com.ruoyi.common.core.utils.StringUtils;

import java.lang.reflect.Field;

public class PropertyUtils {


    /**
     * 判断属性是否为null或""
     * @param args
     * @return 是则返回true
     */
    public static Boolean isEmpty(Object ...args){
        for (Object arg : args) {
            if(arg instanceof String){
                String s = String.valueOf(arg);
                if(StringUtils.isEmpty(s)) return true;
            }else {
                if(arg == null) return true;
            }
        }
        return false;
    }


    /**
     *判断对象的属性是否为空
     * @param obj 传入的对象
     * @return
     */
    public static Boolean isEmpty(Object obj) {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object o = field.get(obj);
                if(o != null){
                    return false;
                }
                if(o instanceof String){
                    if(!o.equals(""))
                        return false;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     *判断对象的属性是否为空
     * @param obj 传入的对象
     * @param exclude 排除的属性 (不区分大小写)
     * @return
     */
    public static Boolean isEmpty(Object obj,String ...exclude) {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            outer:
            for (Field field : fields) {
                field.setAccessible(true);
                Object o = field.get(obj);
                String name = field.getName();
                for (String arg : exclude) {
                    if(name.equalsIgnoreCase(arg)) continue outer;
                }
                if(o == null){
                    return false;
                }
                if(o instanceof String){
                    if(o.equals("")) return false;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}