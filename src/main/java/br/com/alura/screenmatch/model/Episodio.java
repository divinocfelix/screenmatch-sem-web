package br.com.alura.screenmatch.model;

import java.time.DateTimeException;
import java.time.LocalDate;

public class Episodio {
	
	private Integer temporada;
	private  String titulo;
	private  Integer numeroEpisodio; 
	private Double avaliacao;
	private LocalDate dataLancamento;
	
	public Episodio() {
		// TODO Auto-generated constructor stub
	}
	
	public Episodio(Integer temporada, DadosEpisodio dadosEpisodio) {
		super();
		this.temporada = temporada;
		this.titulo = dadosEpisodio.titulo();
		this.numeroEpisodio = dadosEpisodio.episodio();
		
		try {
			this.avaliacao = Double.valueOf(dadosEpisodio.avaliacao());
		} catch(NumberFormatException e) {
			this.avaliacao = 0.0;
		}
		
		try {
			this.dataLancamento = LocalDate.parse(dadosEpisodio.dataLancamento());
		} catch(DateTimeException e) {
			this.dataLancamento = null;
		}
		
	}

	public Integer getTemporada() {
		return temporada;
	}

	public String getTitulo() {
		return titulo;
	}

	public Integer getNumeroEpisodio() {
		return numeroEpisodio;
	}

	public Double getAvaliacao() {
		return avaliacao;
	}

	public LocalDate getDataLancamento() {
		return dataLancamento;
	}

	public void setTemporada(Integer temporada) {
		this.temporada = temporada;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setNumeroEpisodio(Integer numeroEpisodio) {
		this.numeroEpisodio = numeroEpisodio;
	}

	public void setAvaliacao(Double avaliacao) {
		this.avaliacao = avaliacao;
	}

	public void setDataLancamento(LocalDate dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	@Override
	public String toString() {
		return "temporada=" + temporada + ", titulo=" + titulo + ", numeroEpisodio=" + numeroEpisodio
				+ ", avaliacao=" + avaliacao + ", dataLancamento=" + dataLancamento;
	}
	
	
}
