package com.wr.utils;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 创建对象copy工具类 复制对象采用cglib beanCopier, cglib性能高于spring BeanUtils
 * 注意：当被复制的实体类使用lombok @Accessors(chain = true)注解时 使用BeanUtil.copy方法失败，请使用 org.springframework.beans.BeanUtils.copyProperties 方法
 *
 * @author zhaoyang10
 * @date 2018/10/25
 */
public class BeanUtil {

    private BeanUtil() {
    }

    /**
     * 缓存copier
     */
    private static final Map<String, BeanCopier> BEAN_COPIERS = new ConcurrentHashMap<>();

    /**
     * 对象复制
     *
     * @param source 源对象-被复制的对象
     * @param target 目标对象-新对象
     * @author zhaoyang10
     */
    public static void copy(Object source, Object target) {
        String beanKey = generateKey(source.getClass(), target.getClass());
        BeanCopier copier;
        if (!BEAN_COPIERS.containsKey(beanKey)) {
            copier = BeanCopier.create(source.getClass(), target.getClass(), false);
            BEAN_COPIERS.put(beanKey, copier);
        } else {
            copier = BEAN_COPIERS.get(beanKey);
        }
        copier.copy(source, target, null);
    }

    private static String generateKey(Class<?> sourceClass, Class<?> targetClass) {
        return sourceClass.getName() + ":" + targetClass.getName();
    }

    public static <T> T beanToBean(Object object,T bean) {
        return mapToBean(beanToMap(object),bean);
    }

    /**
     * 将对象装换为map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> beanToMap(Object bean) {
        return BeanMap.create(bean);
    }

    /**
     * 将map装换为javabean对象
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     * 通过对象属性名获取对象中的属性值
     */
    public static Object getProp(Object object, String name) {
        if (object == null || name == null || "".equals(name.trim())) {
            return null;
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String tempName = property.getName();

                if (name.equals(tempName)) {
                    Method getter = property.getReadMethod();
                    Object value = null;
                    if (getter != null) {
                        value = getter.invoke(object);
                    }
                    return value;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
