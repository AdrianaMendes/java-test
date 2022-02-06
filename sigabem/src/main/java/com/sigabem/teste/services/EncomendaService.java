package com.sigabem.teste.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.sigabem.teste.entities.Address;
import com.sigabem.teste.entities.EncomendaEntity;
import com.sigabem.teste.entities.dto.EncomendaDTO;
import com.sigabem.teste.repositories.EncomendaRepository;

@Service
public class EncomendaService {

	@Autowired
	private EncomendaRepository repository;

	public EncomendaEntity find(Long id) {
		return repository.findById(id).orElse(null);
	}
	
	public List<EncomendaEntity> findAll() {
		return repository.findAll();
	}

	public EncomendaDTO sendPackage(
			final Double peso,
			final String cepOrigem,
			final String cepDestino,
			final String nomeDestinatario) throws Exception {

		// Remove qualquer caractere inválido na string, por exemplo: 35.500-700 vira
		// 35500700.
		final String cepOrigemSanitize = cepOrigem.replaceAll("[^\\d]", "");
		final String cepDestinoSanitize = cepDestino.replaceAll("[^\\d]", "");

		if (peso <= 0D) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Peso inválido.");
		}

		if (cepOrigemSanitize.length() != 8) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CEP origem inválido.");
		}

		if (cepDestinoSanitize.length() != 8) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CEP destino inválido.");
		}

		final RestTemplate restTemplate = new RestTemplate();

		Address addressOrigem = restTemplate.getForObject("https://viacep.com.br/ws/" + cepOrigemSanitize + "/json/", Address.class);
		Address addressDestino = restTemplate.getForObject("https://viacep.com.br/ws/" + cepDestinoSanitize + "/json/", Address.class);

		if (addressOrigem.getCep() == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CEP origem: " + cepOrigemSanitize + " não encontrado.");
		}

		if (addressDestino.getCep() == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CEP destino: " + cepDestinoSanitize + " não encontrado.");
		}

		Double vlTotalFrete;
		Date dataPrevistaEntrega;
		final Boolean isSameDDD = addressDestino.getDdd().equals(addressDestino.getDdd());
		final Boolean isSameState = addressDestino.getUf().equals(addressDestino.getUf());

		if (isSameDDD) {
			dataPrevistaEntrega = getDataPrevistaEntrega(1);
			vlTotalFrete = peso * 0.50D; // 50% desconto
		} else if (isSameState) {
			dataPrevistaEntrega = getDataPrevistaEntrega(3);
			vlTotalFrete = peso * 0.25D; // 75% desconto
		} else {
			dataPrevistaEntrega = getDataPrevistaEntrega(10);
			vlTotalFrete = peso;
		}

		final EncomendaEntity newEncomenda = new EncomendaEntity(null, nomeDestinatario, vlTotalFrete, new Date(), dataPrevistaEntrega, cepOrigemSanitize, cepDestinoSanitize, peso);

		repository.save(newEncomenda);
		
		return generateDTO(newEncomenda);
	}

	private Date getDataPrevistaEntrega(final Integer days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, days);
		return calendar.getTime();
	}
	
	private EncomendaDTO generateDTO(EncomendaEntity encomenda) {
		return new EncomendaDTO(encomenda.getVlTotalFrete(), encomenda.getDataPrevistaEntrega(), encomenda.getCepOrigem(), encomenda.getCepDestino());
	}
}
