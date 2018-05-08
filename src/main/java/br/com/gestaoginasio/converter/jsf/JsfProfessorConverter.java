package br.com.gestaoginasio.converter.jsf;

import javax.faces.convert.FacesConverter;

import br.com.gestaoginasio.model.Professor;

@FacesConverter("JsfProfessorConverter")
public class JsfProfessorConverter extends JsfGenericConverter<Professor, Long> {

}
