package br.edu.unoesc.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

public class Cliente {

	private ObjectId _id;
	private String nome;
	private String cpf;
	private List<Pedido> pedidos;

	public Cliente() {

	}

	public Cliente(String nome, String cpf, List<Pedido> pedidos) {
		super();
		this.nome = nome;
		this.cpf = cpf;
		this.pedidos = pedidos;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	public void addPedido(Pedido pedido) {
		if (pedidos == null) {
			pedidos = new ArrayList<>();
		}
		pedidos.add(pedido);
	}

	public ObjectId get_id() {
		return _id;
	}

}
