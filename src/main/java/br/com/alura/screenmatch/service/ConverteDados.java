package br.com.alura.screenmatch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConverteDados implements IConverteDados {
	private ObjectMapper mapper;
	
	public ConverteDados() {
		mapper = new ObjectMapper();
	}

	@Override
	public <T> T obterDados(String json, Class<T> classe) {
		try {
			return this.mapper.readValue(json, classe);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	
}



