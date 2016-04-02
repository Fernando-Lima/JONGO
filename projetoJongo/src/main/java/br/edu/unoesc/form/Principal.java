package br.edu.unoesc.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Principal extends JFrame implements ActionListener {

	private JMenuBar barra;
	private JMenu jmnCadastrar;
	private JMenu jmnListar;

	private JMenuItem jmiCliente;
	private JMenuItem jmiProduto;
	private JMenuItem jmiNewPedido;
	private JMenuItem jmiListPedido;

	public Principal() {
		setTitle("GERENCIAR");
		setLayout(null);
		barra = new JMenuBar();

		jmnCadastrar = new JMenu("CADASTRAR");// Menu principal
		jmiCliente = new JMenuItem("CLIENTE");// Item menu
		jmiCliente.addActionListener(this);// Item menu açao
		jmiProduto = new JMenuItem("PRODUTO");// Item menu
		jmiProduto.addActionListener(this);
		jmnCadastrar.add(jmiCliente);
		jmnCadastrar.add(jmiProduto);

		jmnListar = new JMenu("PEDIDO");// Menu Principal
		barra.add(jmnCadastrar);

		jmiNewPedido = new JMenuItem("NOVO PEDIDO");// Item menu
		jmiListPedido = new JMenuItem("LISTAR PEDIDOS");// Item menu
		jmnListar.add(jmiNewPedido);
		jmiNewPedido.addActionListener(this);

		jmnListar.add(jmiListPedido);
		jmiListPedido.addActionListener(this);

		barra.add(jmnListar);

		setJMenuBar(barra);
		setSize(600, 400);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		new Principal();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jmiCliente) {
			new FrameCliente();
		}
		if (e.getSource() == jmiProduto) {
			new Frame();
		}
		if (e.getSource() == jmiNewPedido) {
			new FramePedido();
		}
		if (e.getSource() == jmiListPedido) {
			new FrameVenda();
		}
	}
}