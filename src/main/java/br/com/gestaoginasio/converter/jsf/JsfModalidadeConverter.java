package br.com.gestaoginasio.converter.jsf;

import javax.faces.convert.FacesConverter;

import br.com.gestaoginasio.model.Modalidade;

@FacesConverter(value = "JsfModalidadeConverter")
public class JsfModalidadeConverter extends JsfGenericConverter<Modalidade, Short> {

}
