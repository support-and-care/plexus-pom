package org.codehaus.plexus.component.manager;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.codehaus.plexus.util.SweeperPool;

/**
 * Pools comnponents
 *
 * @author <a href="mailto:jason@maven.org">Jason van Zyl</a>
 *
 * @version $Id$
 */
public class PoolableComponentManager
    extends AbstractComponentManager
{
    private SweeperPool pool;

    /**
     *
     */
    public PoolableComponentManager()
    {
        super();
    }

    public void initialize()
        throws Exception
    {
        super.initialize();
        pool = newSeeperPool( getConfiguration() );
    }

    /**
     * @see ComponentManager#configure(Configuration)
     */
    public void configure( Configuration configuration )
        throws ConfigurationException
    {
        super.configure( configuration );
    }

    /**
     * @see ComponentManager#release(Object)
     */
    public void release( Object component )
    {
        pool.put( component );
    }

    /**
     * @see org.codehaus.plexus.component.manager.ComponentManager#dispose()
     */
    public void dispose()
    {
        //@todo really need to wait for all components to be returned.
        //however blocking on this call may prevent plexus servicing
        //other requests and hence prevent cleanup. Have to look
        //at this. For now just assume all connections have been
        //released.
        pool.dispose();

    }

    /**
     * @see org.codehaus.plexus.component.manager.ComponentManager#getComponent()
     */
    public Object getComponent()
        throws Exception
    {
        Object component = pool.get();

        if ( component == null )
        {
            component = createComponentInstance();
        }

        return component;
    }

    /**
     * Create a new ObjectPool based on the provided configurations. Default
     * hardcoded values are used if neither configurations have a value
     * for a particular setting.
     *
     *
     * @param config the custom configuration for the pool
     * the custom configuration.
     * @return a new ObjectPool
     */
    private SweeperPool newSeeperPool( Configuration config )
    {
        int sweepInterval = config.getChild( "sweep-interval" ).getValueAsInteger( 5 );

        int minCapacity = config.getChild( "min-capacity" ).getValueAsInteger( 3 );

        int maxCapacity = config.getChild( "max-capacity" ).getValueAsInteger( 30 );

        int triggerSize = config.getChild( "trigger-size" ).getValueAsInteger( 15 );

        int initialCapacity = config.getChild( "initial-capacity" ).getValueAsInteger( 10 );

        return new SweeperPool( maxCapacity,
                                minCapacity,
                                initialCapacity,
                                sweepInterval,
                                triggerSize );
    }
}
