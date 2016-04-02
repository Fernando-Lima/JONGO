package br.edu.unoesc.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import br.edu.unoesc.model.Cliente;

public class FrameListaCliente extends JFrame {
	private JTable jltlistar;
	private JScrollPane jspListar;
	private DefaultTableModel dtmListar;
	private Integer telaSelecionada;

	public Integer getTelaSelecionada() {
		return telaSelecionada;
	}

	public void setTelaSelecionada(Integer telaSelecionada) {
		this.telaSelecionada = telaSelecionada;
	}

	MongoClient mongoClient = new MongoClient("localhost", 27017);
	DB db = mongoClient.getDB("projeto");

	Jongo jongo = new Jongo(db);

	MongoCollection collectionCliente = jongo.getCollection("br.edu.unoesc.model.Cliente");

	public void BotaoAdicionar() {
		JButton jbtAdicionar = new JButton("Selecionar");
		jbtAdicionar.setBounds(22, 252, 120, 30);
		getContentPane().add(jbtAdicionar);
		jbtAdicionar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = jltlistar.getSelectedRow();
				if (index == -1) {
					JOptionPane.showMessageDialog(null, "Selecione um cliente");
				} else {
					int linhaSelecionada = jltlistar.getSelectedRow();
					if (telaSelecionada.equals(1)) {
						FrameVenda frameVenda = new FrameVenda();
						frameVenda.txtCliente.setText(jltlistar.getValueAt(linhaSelecionada, 0).toString());
						frameVenda.txtCpf.setText(jltlistar.getValueAt(linhaSelecionada, 1).toString());

					} else {
						FramePedido framePedido = new FramePedido();
						framePedido.jlbgetCliente.setText(jltlistar.getValueAt(linhaSelecionada, 0).toString());
						framePedido.jlbgetCpf.setText(jltlistar.getValueAt(linhaSelecionada, 1).toString());
						framePedido.jlbGetcod.setText(jltlistar.getValueAt(linhaSelecionada, 2).toString());
					}
					dispose();
				}
			}
		});
	}

	public void listarClientes() {
		dtmListar.setNumRows(0);
		MongoCursor<Cliente> cursorBlog = collectionCliente.find().as(Cliente.class);
		cursorBlog.forEach(c -> {
			dtmListar.addRow(new String[] { c.getNome().toString(), c.getCpf().toString(), c.get_id().toString() });

		});
	}

	public void Lista() {
		dtmListar = new DefaultTableModel();
		dtmListar.addColumn("Cliente");
		dtmListar.addColumn("CPF");
		dtmListar.addColumn("Codigo");

		jltlistar = new JTable(dtmListar);
		jspListar = new JScrollPane(jltlistar);
		jspListar.setBounds(22, 22, 362, 200);
		getContentPane().add(jspListar);
		listarClientes();

	}

	public void BotaoSair() {
		JButton jbtSair = new JButton("sair");
		jbtSair.setBounds(217, 252, 120, 30);
		getContentPane().add(jbtSair);

		jbtSair.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();

			}

		});
	}

	public FrameListaCliente() {
		setTitle("Cadastro de Clientes");
		getContentPane().setLayout(null);
		Lista();
		BotaoAdicionar();
		BotaoSair();
		setSize(420, 330);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
