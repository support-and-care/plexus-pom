package org.codehaus.plexus.formica.web;

/*
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.codehaus.plexus.formica.Form;
import org.codehaus.plexus.i18n.I18N;
import org.codehaus.plexus.util.xml.XMLWriter;
import org.codehaus.plexus.util.xml.PrettyPrintXMLWriter;
import org.codehaus.plexus.summit.rundata.RunData;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable;
import org.codehaus.plexus.context.ContextException;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.PlexusConstants;

import java.io.Writer;
import java.util.Map;

/**
 * @author <a href="mailto:jason@maven.org">Jason van Zyl</a>
 * @version $Id$
 */
public abstract class AbstractFormRenderer
    implements FormRenderer, Contextualizable
{
    protected PlexusContainer container;

    // ----------------------------------------------------------------------
    // Render
    // ----------------------------------------------------------------------

    public void render( Form form, Writer writer, I18N i18n, Object data, String baseUrl, RunData rundata )
        throws FormRenderingException
    {
        XMLWriter w = new PrettyPrintXMLWriter( writer );

        header( form, w, i18n );

        body( form, w, i18n, data, baseUrl, rundata );

        footer( form, w, i18n );
    }

    // ----------------------------------------------------------------------
    // Header
    // ----------------------------------------------------------------------

    protected void header( Form form, XMLWriter w, I18N i18n )
    {
        w.startElement( "div" );

        w.addAttribute( "class", "app" );

        w.startElement( "h3" );

        w.writeText( getHeaderTitle( form, i18n ) );

        w.endElement();

        w.startElement( "p" );
    }

    // ----------------------------------------------------------------------
    // Body
    // ----------------------------------------------------------------------

    protected abstract String getHeaderTitle( Form form, I18N i18n );

    protected abstract void body( Form form, XMLWriter writer, I18N i18n, Object data, String baseUrl, RunData rundata )
        throws FormRenderingException;

    // ----------------------------------------------------------------------
    // Footer
    // ----------------------------------------------------------------------

    protected void footer( Form form, XMLWriter w, I18N i18n )
    {
        w.endElement(); // p

        w.endElement(); // div
    }

    // ----------------------------------------------------------------------
    // Lifecylce Management
    // ----------------------------------------------------------------------

    public void contextualize( Context context )
        throws ContextException
    {
        container = (PlexusContainer) context.get( PlexusConstants.PLEXUS_KEY );
    }
}
