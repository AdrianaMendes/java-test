package com.sigabem.teste.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sigabem.teste.entities.EncomendaEntity;
import com.sigabem.teste.entities.dto.EncomendaDTO;
import com.sigabem.teste.services.EncomendaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Api(tags = "Encomendas")
@RestController
@RequestMapping(value = "encomendas")
public class EncomendaController {

	@Autowired
	private EncomendaService service;

	@GetMapping(value = "find")
	public ResponseEntity<EncomendaEntity> find(final @ApiParam(value = "ID da encomenda") Long id) {
		return new ResponseEntity<>(service.find(id), HttpStatus.OK);
	}
	
	@GetMapping(value = "find-all")
	public ResponseEntity<List<EncomendaEntity>> findAll() {
		return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
	}

	@PostMapping(value = "send-package")
	public ResponseEntity<EncomendaDTO> sendPackage(
			@RequestParam @ApiParam(value = "Peso da encomenda") final Double peso,
			@RequestParam @ApiParam(value = "CEP origem") final String cepOrigem,
			@RequestParam @ApiParam(value = "CEP destino") final String cepDestino,
			@RequestParam @ApiParam(value = "Nome do destinatário") final String nomeDestinatario) throws Exception {
		return new ResponseEntity<>(service.sendPackage(peso, cepOrigem, cepDestino, nomeDestinatario), HttpStatus.OK);
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<String> encomendaExecption(ResponseStatusException exception) {
		return ResponseEntity.status(exception.getStatus()).body(exception.getMessage());
	}
}
