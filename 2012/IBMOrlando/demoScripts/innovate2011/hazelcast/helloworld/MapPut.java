package demoScripts.innovate2011.hazelcast.helloworld;

import java.io.File;

import resources.demoScripts.innovate2011.hazelcast.helloworld.MapPutHelper;

import com.advancedrft.common.hazelcast.HazelcastAPI;
import com.rational.test.ft.script.RationalTestScript;

/**
 * This and MapGet show just how easy it is to start using hazelcast.<br>
 * If you run MapPut first and then this you can see how in just 4 lines of code you relay a value from one script to the other <br>
 * Don't forget to run "normalMode.cmd" in the compiledHazelcastServer directory first to start the instance container.
 */
public class MapPut extends MapPutHelper
{
    /**
     * @param args
     *            The script arguments
     */
    public void testMain(Object[] args)
    {
        HazelcastAPI.start(RationalTestScript.getCurrentProject().getLocation() + File.separator + "rftConfigs\\automation.xml");
        HazelcastAPI.getMap("HelloWorld").put("Hello", "World");
    }
}
