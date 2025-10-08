package br.com.alura.screenmatch;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

public class Principal {
	
	private Scanner leitor;
	private ConsumoAPI consumoAPI; 
	private ConverteDados conversor;
	DateTimeFormatter formatador;
	
	private final String ENDERECO = "https://www.omdbapi.com/?t=";
	private final String API_KEY = "&apiKey=683bb0b4";
	
	public Principal() {
		this.leitor = new Scanner(System.in);
		this.consumoAPI = new ConsumoAPI();
		this.conversor = new ConverteDados();
		this.formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	}
	
	public void exibirMenu() {
		
		System.out.print("Digite o nome da série para buscar: ");
		var nomeSerie = this.leitor.nextLine();
		var nomeSerieNormalizado = this.normalizarNomeSerie(nomeSerie);
		
		var path = ENDERECO.concat(nomeSerieNormalizado).concat(API_KEY);
		var json = this.consumoAPI.obterDados(path);
		var dadosSerie = this.conversor.obterDados(json, DadosSerie.class);
		
		var temporada_path = ENDERECO.concat(nomeSerieNormalizado).concat("&season=%s").concat(API_KEY);
		var temporadas = new ArrayList<DadosTemporada>();
		
		for(int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
			json = consumoAPI.obterDados(String.format(temporada_path, i));
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		
		System.out.println(String.format("Série: %s", dadosSerie.titulo()));
	
		var episodios = temporadas.stream()
			.flatMap(t -> t.episodios().stream()
				.map(d -> new Episodio(t.temporada(), d))		
			)
			.collect(Collectors.toList());
	
		episodios.forEach(System.out::println);
		
		
		/*	
		
		System.out.print("A partir de que ano vocÊ deseja ver os episódios? ");
		var ano = this.leitor.nextInt();
		this.leitor.nextLine();
		
		LocalDate dataBusca = LocalDate.of(ano, 1, 1);
		
		episodios.stream()
			.filter(e -> Objects.nonNull(e.getDataLancamento()) && e.getDataLancamento().isAfter(dataBusca))
			.peek(e -> System.out.println("Primeiro filtro: " + e ))
			.forEach(e -> System.out.println(
					String.format("Temporada: %d Episódio: %s Data lançamento: %s", e.getTemporada(), e.getTitulo(),
							e.getDataLancamento().format(formatador))
			));
		
		System.out.print("Digite um trecho do titulo do epiódio: ");
		var trechoTitulo = Optional.ofNullable(this.leitor.nextLine());
		
		if (trechoTitulo.isPresent()) {
			var trechoTituloEmMaiusculo = trechoTitulo.get().toUpperCase();
			
			Optional<Episodio> episodioBuscado = episodios.stream()
					.peek(e -> System.out.println("Episodio: " + e))
					.filter(e -> e.getTitulo().toUpperCase().contains(trechoTituloEmMaiusculo))
					.peek(e -> System.out.println("Episodio filtrado: " + e))
					.findFirst();
			
			if (episodioBuscado.isPresent()) {
				System.out.println(String.format("Episódio encontrado: %s", episodioBuscado.get()));			
			} else {
				System.out.println("Episódio não encontrado.");
			}
		}*/
		
		
		Map<Integer, Double> avaliacoesTemporadas = episodios.stream()
			.filter(e -> e.getAvaliacao() > 0.0)
			.collect(
				Collectors.groupingBy(Episodio::getTemporada, Collectors.averagingDouble(Episodio::getAvaliacao))
			);
		
		System.out.println("\nMedia por temporada:");
		avaliacoesTemporadas.forEach((temporada, estatistica) -> System.out
				.println(String.format("Temporada: %s - Estatistica: %s", temporada, estatistica)));
		
		
		var episodiosAvaliados = episodios.stream()
			.filter(episodio -> episodio.getAvaliacao() > 0D)
			.collect(Collectors.toList());
		
		DoubleSummaryStatistics avaliacaoSerie = episodiosAvaliados.stream()
				.collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
		
		Optional<Episodio> melhorEpisodio = episodiosAvaliados.stream()
				.max(Comparator.comparingDouble(Episodio::getAvaliacao));
		
		Optional<Episodio> piorEpisodio = episodiosAvaliados.stream()
				.min(Comparator.comparingDouble(Episodio::getAvaliacao));
		
		System.out.println(
			"\nMédia de avaliação da série: " + avaliacaoSerie.getAverage() + 
			"\nMelhor episodio: " + melhorEpisodio +
			"\nPior episodio: " + piorEpisodio
		);
		
		
		/*
		episodios.stream()
			.collect(Collectors.groupingBy(
				Episodio::getTemporada, 
				Collectors.summarizingDouble(Episodio::getAvaliacao))
			)
			.forEach((temp, et) -> System.out.println("Temporada: " + temp + ", Estatisticas: " + et));
		*/
		
		/* Testes */
		/*
		var numeros = List.of(1, 2, 3, 4, 5);
		
		var numerosFiltrados = numeros.stream()
			.filter(num -> num %2 == 0)
			.collect(Collectors.toList());
		
		Supplier<Stream<Integer>> f1 = () -> numeros.stream()
			.filter(num -> num %2 == 0);
		
		long tempoInicial = System.nanoTime();
		Long total = f1.get().count();
		System.out.println("Total: " + total);
		
		Integer maior = f1.get().max(Integer::compare).get();
		System.out.println("Maior: " + maior);
		
		long tempoFinal = System.nanoTime();
		long tempoDecorrido = tempoFinal - tempoInicial;
		
		
		long tempoInicial1 = System.nanoTime();
		Long total1 = numerosFiltrados.stream().count();
		System.out.println("Total: " + total1);
		
		Integer maior1 = numerosFiltrados.stream().max(Integer::compare).get();
		System.out.println("Maior: " + maior1);
		
		long tempoFinal1 = System.nanoTime();
		long tempoDecorrido1 = tempoFinal1 - tempoInicial1;
		
		long dif = ((tempoDecorrido - tempoDecorrido1) * 100) / tempoDecorrido1;
		
		System.out.println("Execução com supplier: " + tempoDecorrido + " nano para executar.");
		System.out.println("Execução sem supplier: " + tempoDecorrido1 + " nano para executar.");
		System.out.println("Diferença: " + dif + "%");
		*/
		
		
		List<String> palavras = Arrays.asList("java", "stream", "lambda", "code");
		var map = palavras.stream()
			.collect(Collectors.groupingBy(String::length, Collectors.toList()));
		
		System.out.println(map);
		
		var n = palavras.stream()
			.collect(Collectors.joining(", "));
		System.out.println(n);
		
		List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5, 6);
		Integer t = numeros.stream()
			.filter(n1 -> n1 % 2 == 0)
			.reduce(Integer::sum)
			.get();
		System.out.println(t);
	}
	
	private String normalizarNomeSerie(final String nomeSerie) {
		return nomeSerie.replaceAll(" ", "+");
	}
}
