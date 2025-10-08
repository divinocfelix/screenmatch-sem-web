package br.com.alura.screenmatch;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var principal = new Principal();
		principal.exibirMenu();
	}
}

/*
https://portal.app.foursys.com/
	
1 - Treinamento obrigatorio assim que receber a maquina do cliente.
2 - lançar o ponto na foursys e no otl (sistema do cliente)
*/
