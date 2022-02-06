package com.sigabem.teste.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public final class EncomendaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ApiModelProperty(value = "Nome do destinatário")
	@Column
	private String nomeDestinatario;

	@ApiModelProperty(value = "Valor total do frete")
	@Column
	private Double vlTotalFrete;

	@ApiModelProperty(value = "Data da consulta")
	@Column
	private Date dataConsulta;

	@ApiModelProperty(value = "Data prevista de entrega")
	@Column
	private Date dataPrevistaEntrega;

	@ApiModelProperty(value = "CEP destino")
	@Column
	private String cepOrigem;

	@ApiModelProperty(value = "CEP destino")
	@Column
	private String cepDestino;

	@ApiModelProperty(value = "Peso")
	@Column
	private Double peso;
}
