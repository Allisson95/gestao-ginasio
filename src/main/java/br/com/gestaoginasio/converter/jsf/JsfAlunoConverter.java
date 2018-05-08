package br.com.gestaoginasio.converter.jsf;

import javax.faces.convert.FacesConverter;

import br.com.gestaoginasio.model.Aluno;

@FacesConverter("JsfAlunoConverter")
public class JsfAlunoConverter extends JsfGenericConverter<Aluno, Long> {

}
