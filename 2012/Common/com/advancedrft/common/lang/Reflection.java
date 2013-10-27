package com.advancedrft.common.lang;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

/**
 * This class is a collection of reflection helper utility methods.
 */
public class Reflection
{
    /**
     * RFT uses a lot of classloaders, so we try to track them all here to make sure getClass() will definitely work. <br>
     * When running from the command line, JARs in the RFT customization folder use one class loader and the RFT project holding the script to be run uses another classloader.
     */
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
     *             Thrown if we can't load the supplied class name
     * @throws NoSuchMethodException
     *             Thrown if the object doesn't have a method matching the specified signature
     * @throws IllegalArgumentException
     *             Thrown if the method is an instance method and the specified object argument is not an instance of the class or interface declaring the underlying method (or of a subclass or implementor thereof); if the number of actual and formal parameters differ; if an unwrapping conversion for primitive arguments fails; or if, after possible unwrapping, a parameter value cannot be converted to the corresponding formal parameter type by a method invocation conversion.
     * @throws IllegalAccessException
     *             Thrown if this <code>Method</code> object enforces Java language access control and the underlying method is inaccessible.
     * @throws InvocationTargetException
     *             Thrown if the underlying method throws an exception.
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
     * A convenience wrapper to enumerate all possible fields of the given class and any class it inherits from.<br>
     * All public, protected, default visibility, and private ones will be found and setAccessible will be pre-called with a value of true for every field.<br>
     * To then use these Field objects you merely need to use the method {@link Field#set(Object, Object)}.
     * 
     * @param c
     *            The Class object you want the fields for.
     * @return The fields (all public, protected, default visibility, and private ones).
     */
    public static ArrayList<Field> getFields(Class<?> c)
    {
        if (c == null)
        {
            return new ArrayList<Field>(0);
        }
        ArrayList<Field> fields = new ArrayList<java.lang.reflect.Field>();
        Class<?> current = c;
        while (current != null)
        {
            Field[] currentFields = current.getDeclaredFields();
            AccessibleObject.setAccessible(currentFields, true);
            fields.addAll(Arrays.asList(currentFields));
            current = current.getSuperclass();
        }
        return fields;
    }
    
    /**
     * Sets the specified field inthe specified class or object to the specified value
     * 
     * @param theObject
     *            The object to set the field in, or null if the variable is a static one
     * @param fullClassName
     *            The name of the class holding the field
     * @param variableName
     *            The name of the field
     * @param value
     *            The value to set the field to.
     * @throws ClassNotFoundException
     *             Thrown if we can't load the supplied class name
     * @throws NoSuchFieldException
     *             Thrown if a field with the specified name is not found.
     * @throws IllegalAccessException
     *             Thrown if the underlying field is inaccessible.
     */
    public static void setVariable(Object theObject, String fullClassName, String variableName, Object value) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException
    {
        Class<?> targetClass = null;
        if (theObject == null || !theObject.getClass().getName().equals(fullClassName))
        {
            targetClass = Reflection.getClass(fullClassName);
            if (targetClass == null)
            {
                throw new ClassNotFoundException();
            }
        }
        else
        {
            targetClass = theObject.getClass();
        }
        Field f = targetClass.getDeclaredField(variableName);
        f.setAccessible(true);
        f.set(theObject, value);
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
     *             Thrown if we can't load the supplied class name
     * @throws NoSuchMethodException
     *             Thrown if a matching method is not found.
     * @throws IllegalAccessException
     *             Thrown if this <code>Constructor</code> object enforces Java language access control and the underlying constructor is inaccessible.
     * @throws InstantiationException
     *             Thrown if the class that declares the underlying constructor represents an abstract class.
     * @throws IllegalArgumentException
     *             Thrown if the number of actual and formal parameters differ; if an unwrapping conversion for primitive arguments fails; or if, after possible unwrapping, a parameter value cannot be converted to the corresponding formal parameter type by a method invocation conversion; if this constructor pertains to an enum type.
     * @throws InvocationTargetException
     *             Thrown if the underlying constructor throws an exception.
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
    
    /**
     * Gets a classloader that uses the class loaders in {@link #foreignClassLoaders}.<br>
     * Useful if something needs a classloader rather than a call to {@link #getClass(String)}.<br>
     * Hazelcast uses this, among other things.
     * 
     * @return A ClassLoader that does the same thing as {@link #getClass(String)}
     */
    public static URLClassLoader getClassLoader()
    {
        return new URLClassLoader(new URL[] {}, Reflection.class.getClassLoader())
        {
            /**
             * Finds and loads the class with the specified name from the URL search path. Any URLs referring to JAR files are loaded and opened as needed until the class is found.
             * 
             * @param name
             *            the name of the class
             * @return the resulting class
             * @exception ClassNotFoundException
             *                if the class could not be found
             */
            @Override
            public Class<?> findClass(String name) throws ClassNotFoundException
            {
                Class<?> clazz = Reflection.getClass(name);
                if (clazz == null)
                {
                    return super.findClass(name);
                }
                return clazz;
            }
            
            /**
             * Invoked by the Virtual Machine when resolving class references. Equivalent to loadClass(className, false);
             * 
             * @return java.lang.Class the Class object.
             * @param className
             *            String the name of the class to search for.
             * @exception ClassNotFoundException
             *                If the class could not be found.
             */
            @Override
            public Class<?> loadClass(String className) throws ClassNotFoundException
            {
                Class<?> clazz = Reflection.getClass(className);
                if (clazz == null)
                {
                    return super.loadClass(className);
                }
                return clazz;
            }
        };
    }
}
