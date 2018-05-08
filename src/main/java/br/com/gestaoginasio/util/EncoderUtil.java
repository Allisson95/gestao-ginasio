package br.com.gestaoginasio.util;

import javax.swing.JOptionPane;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncoderUtil {
	
	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String senha = JOptionPane.showInputDialog(null, "Digite uma senha:");
		String senhaEncodada = encoder.encode(senha);
		System.out.println("Senha Encodada: " + senhaEncodada);
	}

}
