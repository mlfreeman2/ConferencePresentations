package com.advancedrft.common.hazelcast;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.advancedrft.common.automation.rft.Log;
import com.advancedrft.common.lang.Reflection;
import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.AtomicNumber;
import com.hazelcast.core.Cluster;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;
import com.hazelcast.core.IMap;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.ISet;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.IdGenerator;
import com.hazelcast.core.Instance;
import com.hazelcast.core.InstanceListener;
import com.hazelcast.core.LifecycleService;
import com.hazelcast.core.MultiMap;
import com.hazelcast.core.Transaction;
import com.hazelcast.logging.LoggingService;
import com.hazelcast.partition.PartitionService;

/**
 * Our Hazelcast instance wrapper class
 */
public class HazelcastAPI
{
    
    private static boolean           isHazelcastUp;
    
    private static HazelcastInstance node;
    
    /**
     * This is the name of the class + method that started Hazelcast. <br>
     * We track that and refuse to stop Hazelcast unless the method calling {@link #safeShutdown()} is the same method. <br>
     * It's not perfect but it's better than blindly praying that nothing shuts down Hazelcast
     */
    private static String            startedBy;
    
    /**
     * Add a instance listener which will be notified when a new instance such as map, queue, multimap, topic, lock is added or removed.
     * 
     * @param instanceListener
     *            instance listener
     */
    public static void addInstanceListener(InstanceListener instanceListener)
    {
        if (HazelcastAPI.isHazelcastUp)
        {
            HazelcastAPI.getNode().addInstanceListener(instanceListener);
        }
    }
    
    /**
     * Creates cluster-wide atomic number. AtomicNumber is distributed implementation of <tt>java.util.concurrent.atomic.AtomicLong</tt>.
     * 
     * @param name
     *            name of the AtomicNumber
     * @return AtomicNumber for the given name
     */
    public static AtomicNumber getAtomicNumber(String name)
    {
        if (HazelcastAPI.isHazelcastUp)
        {
            return HazelcastAPI.getNode().getAtomicNumber(name);
        }
        return null;
    }
    
    /**
     * Returns the Cluster that this Hazelcast instance is part of. Cluster interface allows you to add listener for membership events and learn more about the cluster that this Hazelcast instance is part of.
     * 
     * @return cluster that this Hazelcast instance is part of
     */
    public static Cluster getCluster()
    {
        if (HazelcastAPI.isHazelcastUp)
        {
            return HazelcastAPI.getNode().getCluster();
        }
        return null;
    }
    
    /**
     * Returns the configuration of this Hazelcast instance.
     * 
     * @return configuration of this Hazelcast instance
     */
    public static Config getConfig()
    {
        if (HazelcastAPI.isHazelcastUp)
        {
            return HazelcastAPI.getNode().getConfig();
        }
        return null;
    }
    
    /**
     * Returns the default distributed executor service. Executor service enables you to run your <tt>Runnable</tt>s and <tt>Callable</tt>s on the Hazelcast cluster.
     * 
     * @return distributed executor service of this Hazelcast instance
     */
    public static ExecutorService getExecutorService()
    {
        if (HazelcastAPI.isHazelcastUp)
        {
            return HazelcastAPI.getNode().getExecutorService();
        }
        return null;
    }
    
    /**
     * Returns the distributed executor service for the given name.
     * 
     * @param name
     *            name of the executor service
     * @return executor service for the given name
     */
    public static ExecutorService getExecutorService(String name)
    {
        if (HazelcastAPI.isHazelcastUp)
        {
            return HazelcastAPI.getNode().getExecutorService(name);
        }
        return null;
    }
    
    /**
     * Returns the active hazelcast instance, creating it if necessary
     * 
     * @return The active hazelcast instance, creating it if necessary. If this returns null then Hazelcast is not available for some reason.
     */
    public static HazelcastInstance getNode()
    {
        return HazelcastAPI.node;
    }
    
    /**
     * Creates cluster-wide unique IDs. Generated IDs are long type primitive values between <tt>0</tt> and <tt>Long.MAX_VALUE</tt> . Id generation occurs almost at the speed of <tt>AtomicLong.incrementAndGet()</tt> . Generated IDs are unique during the life cycle of the cluster. If the entire cluster is restarted, IDs start from <tt>0</tt> again.
     * 
     * @param name
     *            name of the IdGenerator
     * @return IdGenerator for the given name
     */
    public static IdGenerator getIdGenerator(String name)
    {
        if (HazelcastAPI.isHazelcastUp)
        {
            return HazelcastAPI.getNode().getIdGenerator(name);
        }
        return null;
    }
    
    /**
     * Returns all queue, map, set, list, topic, lock, multimap instances created by Hazelcast.
     * 
     * @return the collection of instances created by Hazelcast.
     */
    public static Collection<Instance> getInstances()
    {
        if (HazelcastAPI.isHazelcastUp)
        {
            return HazelcastAPI.getNode().getInstances();
        }
        return null;
    }
    
    /**
     * Returns the lifecycle service for this instance. LifecycleService allows you to shutdown, restart, pause and resume this HazelcastInstance and listen for the lifecycle events.
     * 
     * @return lifecycle service
     */
    public static LifecycleService getLifecycleService()
    {
        if (HazelcastAPI.isHazelcastUp)
        {
            return HazelcastAPI.getNode().getLifecycleService();
        }
        return null;
    }
    
    /**
     * Returns the distributed list instance with the specified name. Index based operations on the list are not supported.
     * 
     * @param <X>
     *            The type of the members of the list
     * @param name
     *            name of the distributed list
     * @return distributed list instance with the specified name
     */
    public static <X> List<X> getList(String name)
    {
        if (HazelcastAPI.isHazelcastUp)
        {
            return HazelcastAPI.getNode().getList(name);
        }
        return null;
    }
    
    /**
     * Returns the distributed lock instance for the specified key object. The specified object is considered to be the key for this lock. So keys are considered equals cluster-wide as long as they are serialized to the same byte array such as String, long, Integer.
     * <p/>
     * Locks are fail-safe. If a member holds a lock and some of the members go down, cluster will keep your locks safe and available. Moreover, when a member leaves the cluster, all the locks acquired by this dead member will be removed so that these locks can be available for live members immediately.
     * 
     * <pre>
     * Lock lock = Hazelcast.getLock(&quot;PROCESS_LOCK&quot;);
     * lock.lock();
     * try
     * {
     *     // process
     * }
     * finally
     * {
     *     lock.unlock();
     * }
     * </pre>
     * 
     * @param key
     *            key of the lock instance
     * @return distributed lock instance for the specified key.
     */
    public static ILock getLock(Object key)
    {
        if (HazelcastAPI.isHazelcastUp)
        {
            return HazelcastAPI.getNode().getLock(key);
        }
        return null;
    }
    
    /**
     * Returns the logging service of this Hazelcast instance. LoggingService allows you to listen for LogEvents generated by Hazelcast runtime. You can log the events somewhere or take action base on the message.
     * 
     * @return logging service
     */
    public static LoggingService getLoggingService()
    {
        if (HazelcastAPI.isHazelcastUp)
        {
            return HazelcastAPI.getNode().getLoggingService();
        }
        return null;
    }
    
    /**
     * Returns the distributed map instance with the specified name.
     * 
     * @param <K>
     *            The type of the map's keys
     * @param <V>
     *            The type of the map's values
     * @param name
     *            name of the distributed map
     * @return distributed map instance with the specified name
     */
    public static <K, V> IMap<K, V> getMap(String name)
    {
        if (HazelcastAPI.isHazelcastUp)
        {
            return HazelcastAPI.getNode().getMap(name);
        }
        return null;
    }
    
    /**
     * Returns the distributed multimap instance with the specified name.
     * 
     * @param <K>
     *            The type of the multimap's keys
     * @param <V>
     *            The type of the multimap's values
     * @param name
     *            name of the distributed multimap
     * @return distributed multimap instance with the specified name
     */
    public static <K, V> MultiMap<K, V> getMultiMap(String name)
    {
        if (HazelcastAPI.isHazelcastUp)
        {
            return HazelcastAPI.getNode().getMultiMap(name);
        }
        return null;
    }
    
    /**
     * Returns the name of this Hazelcast instance
     * 
     * @return name of this Hazelcast instance
     */
    public static String getName()
    {
        if (HazelcastAPI.isHazelcastUp)
        {
            return HazelcastAPI.getNode().getName();
        }
        return null;
    }
    
    /**
     * Returns the partition service of this Hazelcast instance. PartitionService allows you to introspect current partitions in the cluster, partition owner members and listen for partition migration events.
     * 
     * @return partition service
     */
    public static PartitionService getPartitionService()
    {
        if (HazelcastAPI.isHazelcastUp)
        {
            return HazelcastAPI.getNode().getPartitionService();
        }
        return null;
    }
    
    /**
     * Returns the distributed queue instance with the specified name.
     * 
     * @param <E>
     *            The type of the items in the queue
     * @param name
     *            name of the distributed queue
     * @return distributed queue instance with the specified name
     */
    public static <E> IQueue<E> getQueue(String name)
    {
        if (HazelcastAPI.isHazelcastUp)
        {
            return HazelcastAPI.getNode().getQueue(name);
        }
        return null;
    }
    
    /**
     * Returns the distributed set instance with the specified name.
     * 
     * @param <E>
     *            The type of the values in the set
     * @param name
     *            name of the distributed set
     * @return distributed set instance with the specified name
     */
    public static <E> ISet<E> getSet(String name)
    {
        if (HazelcastAPI.isHazelcastUp)
        {
            return HazelcastAPI.getNode().getSet(name);
        }
        return null;
    }
    
    /**
     * Returns the distributed topic instance with the specified name.
     * 
     * @param <E>
     *            The type of the message being sent over the topic
     * @param name
     *            name of the distributed topic
     * @return distributed topic instance with the specified name
     */
    public static <E> ITopic<E> getTopic(String name)
    {
        if (HazelcastAPI.isHazelcastUp)
        {
            return HazelcastAPI.getNode().getTopic(name);
        }
        return null;
    }
    
    /**
     * Returns the transaction instance associated with the current thread, creates a new one if it wasn't already.
     * <p/>
     * Transaction doesn't start until you call <tt>transaction.begin()</tt> and if a transaction is started then all transactional Hazelcast operations are automatically transactional.
     * 
     * <pre>
     * Map map = Hazelcast.getMap(&quot;mymap&quot;);
     * Transaction txn = Hazelcast.getTransaction();
     * txn.begin();
     * try
     * {
     *     map.put(&quot;key&quot;, &quot;value&quot;);
     *     txn.commit();
     * }
     * catch (Exception e)
     * {
     *     txn.rollback();
     * }
     * </pre>
     * 
     * Isolation is always <tt>READ_COMMITTED</tt> . If you are in a transaction, you can read the data in your transaction and the data that is already committed and if not in a transaction, you can only read the committed data. Implementation is different for queue and map/set. For queue operations (offer,poll), offered and/or polled objects are copied to the next member in order to safely commit/rollback. For map/set, Hazelcast first acquires the locks for the write operations (put, remove) and holds the differences (what is added/removed/updated) locally for each transaction. When transaction is set to commit, Hazelcast will release the locks and apply the differences. When rolling back, Hazelcast will simply releases the locks and discard the differences. Transaction instance is attached to the current thread and each Hazelcast operation checks if the current thread holds a transaction, if so, operation will be transaction aware. When transaction is committed, rolled back or timed out, it will be detached from the thread holding it.
     * 
     * @return transaction for the current thread
     */
    public static Transaction getTransaction()
    {
        if (HazelcastAPI.isHazelcastUp)
        {
            return HazelcastAPI.getNode().getTransaction();
        }
        return null;
    }
    
    public static boolean isHazelcastUp()
    {
        return HazelcastAPI.isHazelcastUp;
    }
    
    /**
     * Removes the specified instance listener. Returns silently if specified instance listener doesn't exist.
     * 
     * @param instanceListener
     *            instance listener to remove
     */
    public static void removeInstanceListener(InstanceListener instanceListener)
    {
        if (HazelcastAPI.isHazelcastUp)
        {
            HazelcastAPI.getNode().removeInstanceListener(instanceListener);
        }
    }
    
    /**
     * Initializes this hazelcast superclient instance
     * 
     * @param configFileName
     *            The name of the Hazelcast config file to use
     * @return True if it started, False otherwise
     */
    public static boolean start(String configFileName)
    {
        return HazelcastAPI.start(configFileName, false);
    }
    
    /**
     * Initializes this hazelcast instance as either a superclient instance or a full instance
     * 
     * @param configFileName
     *            The name of the Hazelcast config file to use
     * @param asInstance
     *            True to make this hazelcast instance a full instance instead of a superclient, False for it to be a superclient
     * @return True if it started, False otherwise
     */
    public static boolean start(String configFileName, boolean asInstance)
    {
        if (!HazelcastAPI.isHazelcastUp || HazelcastAPI.node == null)
        {
            System.setProperty("hazelcast.logging.type", "log4j");
            if (!asInstance)
            {
                System.setProperty("hazelcast.super.client", "true");
            }
            Logger.getLogger("com.hazelcast").addAppender(new Log());
            Logger.getLogger("com.hazelcast").setLevel(Level.ERROR);
            StackTraceElement[] ste = Log.getFilteredStackTrace();
            HazelcastAPI.startedBy = ste[1].getClassName() + "->" + ste[1].getMethodName();
            try
            {
                XmlConfigBuilder xcb = new XmlConfigBuilder(configFileName);
                Config c = xcb.build();
                c.setClassLoader(Reflection.getClassLoader());
                HazelcastAPI.node = Hazelcast.newHazelcastInstance(c);
            }
            catch (IllegalStateException e)
            {
                return false;
            }
            catch (FileNotFoundException e)
            {
                return false;
            }
            HazelcastAPI.isHazelcastUp = true;
        }
        return true;
    }
    
    /**
     * This function will only shut down Hazelcast if the caller here is the same method in the same class that called {@link #start()}.<br>
     */
    public static void safeShutdown()
    {
        StackTraceElement[] ste = Log.getFilteredStackTrace();
        if (HazelcastAPI.startedBy.equals(ste[1].getClassName() + "->" + ste[1].getMethodName()))
        {
            Hazelcast.shutdownAll();
        }
        
    }
}
