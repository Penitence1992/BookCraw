package org.penitence.craw.uitl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by RenJie on 2017/4/13 0013.
 */
public class ReflectUtil {

    public static <T> T getClassInstance(String classFullName,Class<?>[] argsTypes,Object[] argsParams){
        try {
            Class<T> tClass = (Class<T>) Class.forName(classFullName);
            Constructor<T> constructor = tClass.getConstructor(argsTypes);
            return constructor.newInstance(argsParams);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T getClassByExternalClass(String classFullName,String classFilePath,Class<?>[] argsTypes,Object[] argsParams){
        try {
            Class<T> tClass = (Class<T>) Class.forName(classFullName,true,new CustomClassLoader(classFilePath,classFullName));
            Constructor<T> constructor = tClass.getConstructor(argsTypes);
            return constructor.newInstance(argsParams);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
