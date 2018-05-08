package br.com.gestaoginasio.converter.jsf;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

@FacesConverter("JsfCepConverter")
public class JsfCepConverter implements Converter<String> {

	@Override
	public String getAsObject(FacesContext context, UIComponent component, String value) {
		if (StringUtils.isNotBlank(value)) {
			return value.replaceAll("[^0-9]", "");
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, String value) {
		if (StringUtils.isNotBlank(value)) {
			StringBuilder sb = new StringBuilder();
			sb.append(value.substring(0, 2)).append(".").append(value.substring(2, 5)).append("-")
					.append(value.substring(5, 8));
			return sb.toString();
		}
		return null;
	}

}
