package edu.unl.cc.smilehub.view.security;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.util.UUID;
import java.util.Map;

@FacesConverter("entityConverter")
public class EntityConverter implements Converter<Object> {

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        // Crea un ID único temporal para el objeto en esta vista
        String uuid = UUID.randomUUID().toString();
        // Lo guarda en el mapa de la vista para recuperarlo después
        context.getViewRoot().getViewMap().put(uuid, value);
        return uuid;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        // Recupera el objeto original usando el ID
        return context.getViewRoot().getViewMap().get(value);
    }
}