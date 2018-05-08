package br.com.gestaoginasio.converter.jsf;

import java.util.Arrays;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

@FacesConverter("JsfTelefoneConverter")
public class JsfTelefoneConverter implements Converter<String> {

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
			String telefone = value;
			return formatarTelefone(telefone);
		}
		return null;
	}

	private String formatarTelefone(String telefone) {
		StringBuilder sb = new StringBuilder();
		if (telefone.length() == 11) {
			sb.append("(").append(telefone.substring(0, 2)).append(") ").append(telefone.substring(2, 3)).append(" ")
					.append(telefone.substring(3, 7)).append("-").append(telefone.substring(7, 11));
		} else if (telefone.length() == 10) {
			sb.append("(").append(telefone.substring(0, 2)).append(") ").append(telefone.substring(2, 6)).append("-")
					.append(telefone.substring(6, 10));
		} else if (telefone.length() == 9) {
			sb.append(telefone.substring(0, 1)).append(" ").append(telefone.substring(1, 5)).append("-")
					.append(telefone.substring(5, 9));
		} else {
			sb.append(telefone.substring(0, 4)).append("-").append(telefone.substring(4, 8));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String telefone1 = "6199999999";
		String telefone2 = "61999999999";
		String telefone3 = "99999999";
		String telefone4 = "999999999";

		JsfTelefoneConverter tfv = new JsfTelefoneConverter();
		Arrays.asList(telefone1, telefone2, telefone3, telefone4).forEach(t -> {
			System.out.println(tfv.formatarTelefone(t));
		});

	}

}
