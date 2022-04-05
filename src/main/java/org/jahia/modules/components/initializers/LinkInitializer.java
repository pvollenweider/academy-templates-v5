package org.jahia.modules.components.initializers;

import org.jahia.services.content.JCRPropertyWrapper;
import org.jahia.services.content.nodetypes.ExtendedPropertyDefinition;
import org.jahia.services.content.nodetypes.ValueImpl;
import org.jahia.services.content.nodetypes.initializers.ChoiceListValue;
import org.jahia.services.content.nodetypes.initializers.ModuleChoiceListInitializer;
import org.jahia.services.content.nodetypes.renderer.AbstractChoiceListRenderer;
import org.jahia.services.content.nodetypes.renderer.ModuleChoiceListRenderer;
import org.jahia.services.render.RenderContext;
import org.osgi.service.component.annotations.Component;

import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component(name = "linkInitializer", service = ModuleChoiceListInitializer.class, immediate = true)
public class LinkInitializer extends AbstractChoiceListRenderer implements ModuleChoiceListInitializer, ModuleChoiceListRenderer {

    private String key = "linkInitializer";

    /**
     * {@inheritDoc}
     */
    public List<ChoiceListValue> getChoiceListValues(ExtendedPropertyDefinition epd, String param, List<ChoiceListValue> values,
                                                     Locale locale, Map<String, Object> context) {

        //Create the list of ChoiceListValue to return
        List<ChoiceListValue> myChoiceList = new ArrayList<ChoiceListValue>();

        if (context == null) {
            return myChoiceList;
        }

        HashMap<String, Object> myPropertiesMap = null;

        //link internal
        myPropertiesMap = new HashMap<String, Object>();
        myPropertiesMap.put("addMixin", "jacademix:internalLink");
        myChoiceList.add(new ChoiceListValue("internal", myPropertiesMap, new ValueImpl("internal", PropertyType.STRING, false)));

        //link external
        myPropertiesMap = new HashMap<String, Object>();
        myPropertiesMap.put("addMixin", "jacademix:externalLink");
        myChoiceList.add(new ChoiceListValue("external", myPropertiesMap, new ValueImpl("external", PropertyType.STRING, false)));

        //link file
        myPropertiesMap = new HashMap<String, Object>();
        myPropertiesMap.put("addMixin", "jacademix:fileLink");
        myChoiceList.add(new ChoiceListValue("file", myPropertiesMap, new ValueImpl("file", PropertyType.STRING, false)));

        //no link
        myPropertiesMap = new HashMap<String, Object>();
        myChoiceList.add(new ChoiceListValue("noLink", myPropertiesMap, new ValueImpl("noLink", PropertyType.STRING, false)));

        //Return the list
        return myChoiceList;
    }

    /**
     * {@inheritDoc}
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * {@inheritDoc}
     */
    public String getKey() {
        return key;
    }

    /**
     * {@inheritDoc}
     */
    public String getStringRendering(RenderContext context, JCRPropertyWrapper propertyWrapper) throws RepositoryException {
        final StringBuilder sb = new StringBuilder();

        if (propertyWrapper.isMultiple()) {
            sb.append('{');
            final Value[] values = propertyWrapper.getValues();
            for (Value value : values) {
                sb.append('[').append(value.getString()).append(']');
            }
            sb.append('}');
        } else {
            sb.append('[').append(propertyWrapper.getValue().getString()).append(']');
        }

        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    public String getStringRendering(Locale locale, ExtendedPropertyDefinition propDef, Object propertyValue) throws RepositoryException {
        return "[" + propertyValue.toString() + "]";
    }
}

