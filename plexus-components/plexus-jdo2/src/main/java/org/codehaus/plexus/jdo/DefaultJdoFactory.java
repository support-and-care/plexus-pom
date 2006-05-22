package org.codehaus.plexus.jdo;

/*
 * Created on Jan 13, 2005
 *
 * Copyright STPenable Ltd. (c) 2005
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.util.Properties;
import java.sql.DriverManager;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Disposable;

/**
 * @author David Wynter
 * @author <a href="mailto:trygvis@inamo.no">Trygve Laugst&oslash;l</a>
 * @version $Id: DefaultJdoFactory.java 2023 2005-05-28 19:15:47Z jvanzyl $
 */
public class DefaultJdoFactory
    extends AbstractLogEnabled
    implements JdoFactory,
    Initializable,
    Disposable
{
    /**
     * @configuration
     */
    private Properties properties;

    private PersistenceManagerFactory persistenceManagerFactory;

    public PersistenceManagerFactory getPersistenceManagerFactory()
    {
        return persistenceManagerFactory;
    }

    // ----------------------------------------------------------------------
    // Component Lifecycle
    // ----------------------------------------------------------------------

    public void initialize()
        throws InitializationException
    {
        getLogger().info( "Initializing JDO." );

        persistenceManagerFactory = JDOHelper.getPersistenceManagerFactory( properties );

        String driverClass = null;

        try
        {
            driverClass = (String) properties.get( "javax.jdo.option.ConnectionDriverName" );

            //TDDO: Class.forName is evil
            Class.forName( driverClass );
        }
        catch ( ClassNotFoundException e )
        {
            throw new InitializationException( "Cannot find driver class: " + driverClass, e );
        }
    }

    public void shutdown()
        throws Exception
    {
        dispose();
    }

    public void dispose()
    {
        if ( properties != null )
        {
            String databaseUrl = properties.getProperty( "javax.jdo.option.ConnectionURL" );

            if ( databaseUrl != null )
            {

                if ( databaseUrl.indexOf( "derby" ) > 0 )
                {
                    try
                    {
                        DriverManager.getConnection( "jdbc:derby:" + databaseUrl + ";shutdown=true" );
                    }
                    catch ( Exception e )
                    {
                        // Shouldn't happen.
                    }

                    System.gc();
                }
            }
        }
    }
}
