package br.com.gestaoginasio.converter.jsf;

import javax.faces.convert.FacesConverter;

import br.com.gestaoginasio.model.Estado;

@FacesConverter(value = "JsfEstadoConverter")
public class JsfEstadoConverter extends JsfGenericConverter<Estado, Byte> {

}
