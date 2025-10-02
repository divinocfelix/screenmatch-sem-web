package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodio(
			@JsonAlias("Title") String titulo,
			@JsonAlias("Year") Integer ano,
			@JsonAlias("Runtime") String duracao,
			@JsonAlias("Episode") Integer numeroEpisodio, 
			@JsonAlias("imdbRating") String avaliacao,
			@JsonAlias("Released") String dataLancamento) {
}
