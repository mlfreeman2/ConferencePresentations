package com.lps.rsdc2010.common.lang;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

/**
 * This class is a collection of reflection helper utility methods.
 */
public class Reflection
{
    public static Vector<ClassLoader> foreignClassLoaders = new Vector<ClassLoader>();
    
    /**
     * The function executes the specified method in the specified class. <br>
     * If the method takes varargs you must box them up into an array first.<br>
     * The parameters you supply must match the parameters that the function requires.
     * 
     * @param theObject
     *            The object to execute the method on, or null if the method is a static one.
     * @param fullClassName
     *            The class containing the method
     * @param methodName
     *            The name of the method.
     * @param args
     *            The arguments for the method.
     * @return The result of the method, as an Object
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Object executeMethod(Object theObject, String fullClassName, String methodName, Object...args) throws ClassNotFoundException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        Class<?> targetClass = Reflection.getClass(fullClassName);
        if (targetClass == null)
        {
            throw new ClassNotFoundException();
        }
        Method m = targetClass.getDeclaredMethod(methodName, Reflection.getClassObjectsFromActualObjects(args));
        m.setAccessible(true);
        return m.invoke(theObject, args);
    }
    
    /**
     * Creates an instance of the given class using the appropriate constructor for the supplied arguments
     * 
     * @param className
     *            The name of the class to create
     * @param args
     *            The arguments for the constructor.
     * @return An object, or null if an error occurred
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Object createInstanceOf(String className, Object...args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, IllegalArgumentException, InvocationTargetException
    {
        Class<?> theClass = Reflection.getClass(className);
        if (theClass == null)
        {
            throw new ClassNotFoundException();
        }
        if (args.length > 0)
        {
            return theClass.getConstructor(Reflection.getClassObjectsFromActualObjects(args)).newInstance(args);
        }
        else
        {
            return theClass.getConstructor().newInstance();
        }
    }
    
    /**
     * This function executes getClass() on an array of objects, because the object's Class instance is often necessary in reflection.
     * 
     * @param args
     *            The objects
     * @return An array of Class<T> objects, one for each object in the source array
     */
    public static Class<?>[] getClassObjectsFromActualObjects(Object...args)
    {
        if (args == null)
        {
            return new Class<?>[0];
        }
        Class<?>[] types = new Class[args.length];
        for (int i = 0; i < args.length; i++)
        {
            types[i] = args[i].getClass();
        }
        return types;
    }
    
    /**
     * This function will attempt to load a class from multiple classloaders.
     * 
     * @param fullClassName
     *            The name of the class to load
     * @return The loaded class object, or null if none of the classloaders could supply it
     */
    public static Class<?> getClass(String fullClassName)
    {
        if (fullClassName.equals("int"))
        {
            return Integer.TYPE;
        }
        else if (fullClassName.equals("boolean"))
        {
            return Boolean.TYPE;
        }
        else if (fullClassName.equals("short"))
        {
            return Short.TYPE;
        }
        else if (fullClassName.equals("long"))
        {
            return Long.TYPE;
        }
        else if (fullClassName.equals("byte"))
        {
            return Byte.TYPE;
        }
        else if (fullClassName.equals("float"))
        {
            return Float.TYPE;
        }
        else if (fullClassName.equals("double"))
        {
            return Double.TYPE;
        }
        Class<?> targetClass = null;
        try
        {
            targetClass = Class.forName(fullClassName);
        }
        catch (ClassNotFoundException e)
        {
            if (Reflection.foreignClassLoaders.size() > 0)
            {
                for (ClassLoader cl : Reflection.foreignClassLoaders)
                {
                    if (cl.equals(Reflection.class.getClassLoader()))
                    {
                        continue;
                    }
                    boolean success = false;
                    try
                    {
                        targetClass = Class.forName(fullClassName, true, cl);
                        success = true;
                    }
                    catch (Exception ex)
                    {
                        
                    }
                    if (success == true)
                    {
                        break;
                    }
                }
            }
        }
        return targetClass;
    }
}
