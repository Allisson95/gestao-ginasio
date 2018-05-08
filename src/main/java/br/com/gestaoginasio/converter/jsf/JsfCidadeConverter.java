package br.com.gestaoginasio.converter.jsf;

import javax.faces.convert.FacesConverter;

import br.com.gestaoginasio.model.Cidade;

@FacesConverter(value = "JsfCidadeConverter")
public class JsfCidadeConverter extends JsfGenericConverter<Cidade, Short> {

}
