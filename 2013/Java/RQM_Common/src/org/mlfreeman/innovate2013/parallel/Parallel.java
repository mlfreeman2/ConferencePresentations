package org.mlfreeman.innovate2013.parallel;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * A simple reimplementation of Parallel.For and Parallel.ForEach from C#
 */
public class Parallel
{
    /**
     * Executes a for loop, in which iterations may run in parallel.<br>
     * This will run at most <code>Runtime.getRuntime().availableProcessors()</code> items at a time.<br>
     * Modeled after Parallel.For in C#.<br>
     * Behind the scenes, all this does is build a list of Integers and then pass that to {@link #ForEach(Iterable, int, LoopBody)}.
     * 
     * @param start
     *            The starting index, included as one of the values
     * @param stop
     *            The stopping index, not included as one of the values.
     * @param loopBody
     *            The code to invoke on each iteration.
     */
    public static void For(int start, int stop, final LoopBody<Integer> loopBody)
    {
        For(start, stop, Runtime.getRuntime().availableProcessors(), loopBody);
    }
    
    /**
     * Executes a for loop, in which iterations may run in parallel.<br>
     * Modeled after Parallel.For in C#.<br>
     * Behind the scenes, all this does is build a list of Integers and then pass that to {@link #ForEach(Iterable, int, LoopBody)}.
     * 
     * @param start
     *            The starting index, included as one of the values
     * @param stop
     *            The stopping index, not included as one of the values.
     * @param maxThreadCount
     *            The maximum number of items to run at once
     * @param loopBody
     *            The code to invoke on each iteration.
     */
    public static void For(int start, int stop, int maxThreadCount, final LoopBody<Integer> loopBody)
    {
        List<Integer> iterable = new Vector<Integer>();
        for (int i = start; i < stop; i++)
        {
            iterable.add(i);
        }
        ForEach(iterable, maxThreadCount, loopBody);
    }
    
    /**
     * Executes a foreach operation on an Iterable in which iterations may run in parallel.<br>
     * This will run at most <code>Runtime.getRuntime().availableProcessors()</code> items at a time.<br>
     * Modeled after Parallel.ForEach in C#.
     * 
     * @param <T>
     *            The type of the items in the {@link Iterable} object.
     * @param parameters
     *            The items to iterate over in a foreach style manner
     * @param loopBody
     *            The code to run on each one in parallel
     */
    public static <T> void ForEach(Iterable<T> parameters, final LoopBody<T> loopBody)
    {
        ForEach(parameters, Runtime.getRuntime().availableProcessors(), loopBody);
    }
    
    /**
     * Executes a foreach operation on an Iterable in which iterations may run in parallel.<br>
     * Modeled after Parallel.ForEach in C#.
     * 
     * @param <T>
     *            The type of the items in the {@link Iterable} object.
     * @param parameters
     *            The items to iterate over in a foreach style manner
     * @param maxThreadCount
     *            The maximum number of items to run at once
     * @param loopBody
     *            The code to run on each one in parallel
     */
    public static <T> void ForEach(Iterable<T> parameters, int maxThreadCount, final LoopBody<T> loopBody)
    {
        ExecutorService executor = Executors.newFixedThreadPool(maxThreadCount);
        List<Future<?>> futures = new LinkedList<Future<?>>();
        
        for (final T param : parameters)
        {
            Future<?> future = executor.submit(new Runnable()
            {
                public void run()
                {
                    loopBody.run(param);
                }
            });
            
            futures.add(future);
        }
        
        for (Future<?> f : futures)
        {
            try
            {
                f.get();
            }
            catch (InterruptedException e)
            {
            }
            catch (ExecutionException e)
            {
            }
        }
        
        executor.shutdown();
    }
    
    /**
     * Code to be consumed by Parallel.ForEach or Parallel.For must implement this interface.
     * 
     * @param <T>
     *            The type of the items being iterated over.<br>
     *            This could be anything in Parallel.ForEach and will always be {@link Integer} in Parallel.For.
     */
    public static interface LoopBody<T>
    {
        /**
         * Code to be consumed by Parallel.ForEach or Parallel.For must implement this method.
         * 
         * @param p
         *            The iteration item (an object in Parallel.ForEach or an integer in Parallel.For)
         */
        public void run(T p);
    }
}
