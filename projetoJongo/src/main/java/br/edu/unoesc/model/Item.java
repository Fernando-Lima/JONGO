package br.edu.unoesc.model;

public class Item implements Cloneable {

	private String nomeItem;
	private Double valor = 0.0;
	private Integer quantidade;

	public Item() {

	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	@Override
	public Item clone() {
		Object clone;
		try {
			clone = super.clone();// super.clone irá clonar a lista de Itens
			return (Item) clone;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getNomeItem() {
		return nomeItem;
	}

	public void setNomeItem(String nomeItem) {
		this.nomeItem = nomeItem;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

}
