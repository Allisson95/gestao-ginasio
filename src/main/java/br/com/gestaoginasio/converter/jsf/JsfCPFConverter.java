package br.com.gestaoginasio.converter.jsf;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

@FacesConverter("JsfCPFConverter")
public class JsfCPFConverter implements Converter<String> {

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
			Pattern p = Pattern.compile("(\\d{3})(\\d{3})(\\d{3})(\\d{2})");
			Matcher matcher = p.matcher(value);
			if (matcher.matches()) {
				return matcher.replaceAll("$1.$2.$3-$4");
			} else {
				return null;
			}
		}
		return null;
	}

}
