package org.codehaus.plexus.formica.web.element;

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

import org.codehaus.plexus.formica.Element;
import org.codehaus.plexus.util.xml.XMLWriter;
import org.codehaus.plexus.i18n.I18N;

/**
 * @author <a href="mailto:jason@maven.org">Jason van Zyl</a>
 * @version $Id$
 */
public class TextElementRenderer
    extends AbstractElementRenderer
{
    public void render( Element element, Object data, XMLWriter w, I18N i18n )
    {
        w.startElement( "input" );

        w.addAttribute( "type", element.getType() );

        w.addAttribute( "name", element.getId() );

        renderValue( element, data, w, i18n );

        //TODO: take the size from the from the element. Probably need some visual
        //      configuration there.
        w.addAttribute( "size", "60" );

        w.endElement();

        // ----------------------------------------------------------------------
        // Help message for the user
        // ----------------------------------------------------------------------

        w.startElement( "p" );

        w.writeText( i18n.getString( element.getMessageKey()) );

        w.endElement();
    }
}
