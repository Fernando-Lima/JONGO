package br.edu.unoesc.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import br.edu.unoesc.model.Cliente;

public class FrameVenda extends JFrame implements ActionListener {
	private DefaultTableModel dtmListar;
	private DefaultTableModel dtmListarItem;
	private JTable jltlistar;
	private JTable jltlistarItem;
	private JScrollPane jspListar;
	private JScrollPane jspListarItem;
	private JButton btnSelecioneUmCliente;
	public JTextField txtCliente;
	public JTextField txtCpf;
	private JLabel lblTabelaDePedidos;
	private JLabel lblTabelaDeItens;
	private JButton btnCarregar;

	MongoClient mongoClient = new MongoClient("localhost", 27017);
	DB db = mongoClient.getDB("projeto");

	Jongo jongo = new Jongo(db);

	MongoCollection collectionCliente = jongo.getCollection("br.edu.unoesc.model.Cliente");

	public void criarEntradas() {
		JLabel lblNome = new JLabel("Nome:");
		lblNome.setBounds(248, 29, 70, 15);
		getContentPane().add(lblNome);

		JLabel lblCpf = new JLabel("CPF:");
		lblCpf.setBounds(424, 29, 70, 15);
		getContentPane().add(lblCpf);

		txtCliente = new JTextField();
		txtCliente.setText("");
		txtCliente.setBounds(248, 47, 114, 19);
		txtCliente.setEditable(false);
		getContentPane().add(txtCliente);
		txtCliente.setColumns(10);

		txtCpf = new JTextField();
		txtCpf.setText("");
		txtCpf.setBounds(424, 47, 114, 19);
		txtCpf.setEditable(false);
		getContentPane().add(txtCpf);
		txtCpf.setColumns(10);

		lblTabelaDePedidos = new JLabel("Tabela de pedidos");
		lblTabelaDePedidos.setBounds(12, 103, 155, 15);
		getContentPane().add(lblTabelaDePedidos);

		lblTabelaDeItens = new JLabel("Tabela de Itens");
		lblTabelaDeItens.setBounds(480, 103, 155, 15);
		getContentPane().add(lblTabelaDeItens);

	}

	public void BotaoCliente() {
		btnSelecioneUmCliente = new JButton("Selecione um cliente");
		btnSelecioneUmCliente.setBounds(12, 7, 180, 25);
		getContentPane().add(btnSelecioneUmCliente);

		btnSelecioneUmCliente.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				FrameListaCliente frameLista = new FrameListaCliente();
				frameLista.setTelaSelecionada(1);
				dispose();
			}
		});
	}

	// -------------------------------------------------------------------------------------------------------------------
	public void BotaoListar() {
		JButton jbtListar = new JButton("Buscar:");
		jbtListar.setBounds(12, 44, 155, 25);
		getContentPane().add(jbtListar);
		jbtListar.addActionListener(this);
		jbtListar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (txtCpf.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Selecione um cliente");
				} else {
					listarPedidos();
				}

			}
		});
	}

	public void BotaoSair() {
		JButton jbtSair = new JButton("sair");
		jbtSair.setBounds(12, 362, 120, 25);
		getContentPane().add(jbtSair);
		jbtSair.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	public void BotaoCarregar() {
		btnCarregar = new JButton("Carregar >>");
		btnCarregar.setBounds(324, 155, 139, 25);
		getContentPane().add(btnCarregar);
		btnCarregar.setEnabled(false);
		btnCarregar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				listarItens();

			}
		});
	}

	public void listarPedidos() {
		dtmListar.setNumRows(0);
		MongoCursor<Cliente> cursor = collectionCliente.find("{cpf :{$regex: #}}", txtCpf.getText()).as(Cliente.class);

		cursor.forEach(c -> {
			if (c.getPedidos() != null) {
				c.getPedidos().forEach(p -> {
					dtmListar.addRow(new String[] { p.getData().toString(), p.getValorTotal().toString() });
				});
			} else {
				JOptionPane.showMessageDialog(null, "Nenhum pedido para esse cliente");
			}
		});
		btnCarregar.setEnabled(true);
	}

	public void listarItens() {
		dtmListarItem.setNumRows(0);
		MongoCursor<Cliente> cursor = collectionCliente.find("{cpf :{$regex: #}}", txtCpf.getText()).as(Cliente.class);
		cursor.forEach(c -> {
			c.getPedidos().forEach(p -> {
				p.getItens().forEach(i -> {
					dtmListarItem.addRow(new String[] { i.getNomeItem().toString(), i.getValor().toString(),
							i.getQuantidade().toString() });
				});
			});

		});
	}

	public void Lista() {
		dtmListar = new DefaultTableModel();
		dtmListar.addColumn("Data");
		dtmListar.addColumn("Valor Total");

		jltlistar = new JTable(dtmListar);
		jspListar = new JScrollPane(jltlistar);
		jspListar.setBounds(12, 130, 300, 200);
		getContentPane().add(jspListar);

		dtmListarItem = new DefaultTableModel();
		dtmListarItem.addColumn("Nome");
		dtmListarItem.addColumn("Valor");
		dtmListarItem.addColumn("Quantidade");

		jltlistarItem = new JTable(dtmListarItem);
		jspListarItem = new JScrollPane(jltlistarItem);
		jspListarItem.setBounds(480, 130, 300, 200);
		getContentPane().add(jspListarItem);

	}

	public FrameVenda() {
		setTitle("TODOS OS PEDIDOS");
		getContentPane().setLayout(null);

		criarEntradas();
		BotaoListar();
		Lista();
		BotaoSair();
		BotaoCliente();
		BotaoCarregar();
		setSize(800, 430);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	public void actionPerformed(ActionEvent e) {

	}
}
