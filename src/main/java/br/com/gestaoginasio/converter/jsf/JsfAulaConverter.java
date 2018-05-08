package br.com.gestaoginasio.converter.jsf;

import javax.faces.convert.FacesConverter;

import br.com.gestaoginasio.model.Aula;

@FacesConverter("JsfAulaConverter")
public class JsfAulaConverter extends JsfGenericConverter<Aula, Long> {

}
