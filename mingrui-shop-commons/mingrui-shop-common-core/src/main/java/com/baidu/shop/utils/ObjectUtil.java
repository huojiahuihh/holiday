package com.baidu.shop.utils;

public class ObjectUtil {
    public static Boolean isNull(Object obj){
        return obj ==null;
    }

    public static Boolean isNotNull(Object obj){
        return obj != null;
    }

    public static Boolean isEmpty(Object obj){
        return (obj != null && !"".equals(obj))?true:false;
    }
}
