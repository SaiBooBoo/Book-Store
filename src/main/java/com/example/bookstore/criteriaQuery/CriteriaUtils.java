package com.example.bookstore.criteriaQuery;

import org.springframework.data.domain.Pageable;

import java.lang.reflect.Field;

public class CriteriaUtils {
    private CriteriaUtils() {}

    public static Pageable getPageable(Object criteria) {
        if(criteria == null) return null;

        Class<?> cls = criteria.getClass();

        while (cls != null) {
            try{
                Field f = cls.getDeclaredField("pageable");
                f.setAccessible(true);
                Object val = f.get(criteria);
                if (val instanceof Pageable) {
                    return (Pageable) val;
                }
                break;
            } catch (NoSuchFieldException e) {
                cls = cls.getSuperclass();
            } catch (IllegalAccessException ignored) {
                break;
            }
        }
        return null;
    }
}
