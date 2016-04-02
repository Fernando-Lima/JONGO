package br.edu.unoesc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pedido {

	private Integer cod;
	private Date data;
	private List<Item> itens;
	private Double valorTotal;

	public Pedido() {
		itens = new ArrayList<Item>();
	}

	public List<Item> getItens() {
		return itens;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public void addItem(Item item) {
		if (itens == null) {
			itens = new ArrayList<>();
		}
		itens.add(item);
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getCod() {
		return cod;
	}

	public void setCod(Integer cod) {
		this.cod = cod;
	}

}
