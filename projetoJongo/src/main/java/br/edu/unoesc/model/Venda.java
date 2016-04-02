package br.edu.unoesc.model;

public class Venda {

	private Pedido pedidos;
	private String cliente;

	public Venda() {
	}

	public Pedido getPedidos() {
		return pedidos;
	}

	public void setPedidos(Pedido pedidos) {
		this.pedidos = pedidos;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

}
