package com.ronaldo.cursomc.domain;

import java.util.Date;

import javax.persistence.Entity;

import com.ronaldo.cursomc.domain.enums.EstadoPagamento;

@Entity
public class PagamentoComBoleto extends Pagamento{
	private static final long serialVersionUID = 1L;

	
	private Date dataVencimento;
	private Date dataPagamento;
	
	public PagamentoComBoleto() {	
	
	}
	public PagamentoComBoleto(Integer id, Integer estado, Pedido pedido, Date dataVenciomento, Date dataPagamento) {
		super(id, estado, pedido);
		// TODO Auto-generated constructor stub
		this.dataPagamento = dataPagamento;
		this.dataVencimento = dataVenciomento;
	}
	public Date getDataVencimento() {
		return dataVencimento;
	}
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	public Date getDataPagamento() {
		return dataPagamento;
	}
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	
	
	
 
}
