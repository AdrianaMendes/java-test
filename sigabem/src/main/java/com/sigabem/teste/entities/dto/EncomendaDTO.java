package com.sigabem.teste.entities.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class EncomendaDTO {
	private Double vlTotalFrete;
	private Date dataPrevistaEntrega;
	private String cepOrigem;
	private String cepDestino;
}
