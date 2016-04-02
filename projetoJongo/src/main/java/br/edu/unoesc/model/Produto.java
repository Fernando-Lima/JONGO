package br.edu.unoesc.model;

import java.util.ArrayList;
import java.util.List;

public class Produto {

	private String codProduto;
	private String nome;
	private Double valor;
	private Integer quantidade;
	private List<Produto> produtos = new ArrayList<>();

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCodProduto() {
		return codProduto;
	}

	public void setCodProduto(String codProduto) {
		this.codProduto = codProduto;
	}

	public Produto(String codProduto, String nome, List<Item> itens, Integer quantidade, Double valor) {
		super();
		this.codProduto = codProduto;
		this.nome = nome;
		this.quantidade = quantidade;
		this.valor = valor;
	}

	public Produto() {
		super();
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public void addProduto(Produto produto) {
		produtos.add(produto);
	}

}
