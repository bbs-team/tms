package com.bbs.tms.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * ModelUtils
 */
public class ModelUtils {

  private static Logger log = LoggerFactory.getLogger(ModelUtils.class);

  public static Object fillNewModel(Object newObj, Object oldObj) {
    if (newObj.getClass() != oldObj.getClass()) {
      log.error("The class of the object is different.");
      return newObj;
    }

    Class<?> objClass = newObj.getClass();
    try {
      for (Field field : objClass.getDeclaredFields()) {
        String fieldName = toUpperCaseFirstString(field.getName());
        Method getter = objClass.getMethod("get" + fieldName);
        Object newVal = getter.invoke(newObj, new Object[]{});

        if (ObjectUtils.isEmpty(newVal) || (newVal instanceof Number && (int) newVal == 0)) {
          Method setter = objClass.getMethod("set" + fieldName, getter.getReturnType());
          Object oldVal = getter.invoke(oldObj, new Object[]{});
          setter.invoke(newObj, oldVal);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return newObj;
  }

  private static String toUpperCaseFirstString(String str) {
    if (StringUtils.isEmpty(str)) {
      return str;
    }
    return str.substring(0, 1).toUpperCase() + str.substring(1);
  }
}