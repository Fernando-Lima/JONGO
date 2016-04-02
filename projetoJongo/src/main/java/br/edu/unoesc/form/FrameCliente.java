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

import br.edu.unoesc.DAO.MongoDAO;
import br.edu.unoesc.model.Cliente;

public class FrameCliente extends JFrame implements ActionListener {

	private JTextField jtfNome;
	private JTextField jtfCpf;
	private JTable jltlistar;
	private JScrollPane jspListar;
	private DefaultTableModel dtmListar;
	private JButton jbtSalvar;
	private JTextField jtfCod;
	int linhaSelecionada;

	MongoClient mongoClient = new MongoClient("localhost", 27017);
	DB db = mongoClient.getDB("projeto");

	Jongo jongo = new Jongo(db);

	MongoCollection collectionCliente = jongo.getCollection("br.edu.unoesc.model.Cliente");

	public void criarLabel() {
		JLabel jlbNome = new JLabel();
		jlbNome = new JLabel("Nome: ");
		jlbNome.setBounds(5, 10, 80, 20);
		getContentPane().add(jlbNome);

		jtfNome = new JTextField();
		jtfNome.setBounds(80, 10, 150, 20);
		getContentPane().add(jtfNome);

		JLabel jlbCpf = new JLabel();
		jlbCpf = new JLabel("CPF: ");
		jlbCpf.setBounds(5, 35, 80, 20);
		getContentPane().add(jlbCpf);

		jtfCpf = new JTextField();
		jtfCpf.setBounds(80, 35, 150, 20);
		getContentPane().add(jtfCpf);

		JLabel jlbCod = new JLabel();
		jlbCod = new JLabel("COD:");
		jlbCod.setBounds(380, 35, 80, 20);
		getContentPane().add(jlbCod);

		jtfCod = new JTextField();
		jtfCod.setText("");
		jtfCod.setBounds(424, 36, 114, 19);
		jtfCod.setEditable(false);
		getContentPane().add(jtfCod);
		jtfCod.setColumns(10);
	}

	public void BotaoSalvar() {
		jbtSalvar = new JButton("Salvar");
		jbtSalvar.setBounds(5, 130, 120, 30);
		jbtSalvar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Cliente cliente = new Cliente();
					MongoDAO<Cliente> clientes = new MongoDAO<Cliente>();
					cliente.setCpf(jtfCpf.getText());
					cliente.setNome(jtfNome.getText());
					if (jtfCpf.isEditable() == false) {
						clientes.update(cliente, "cpf", cliente.getCpf());
						JOptionPane.showMessageDialog(null, "Alterado com sucesso!");

					} else {
						if (jtfCpf.getText().equals("") || jtfNome.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Preencha os campos");
							return;
						} else {
							clientes.salvar(cliente);
							JOptionPane.showMessageDialog(null, "Salvo com sucesso!");
						}
					}
					jtfNome.setText("");
					jtfCpf.setText("");
					jtfCod.setText("");
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				listarClientes();
				jtfCpf.setEditable(true);
			}
		});
		getContentPane().add(jbtSalvar);
	}

	public void BotaoDeletar() {
		JButton jbtDeletar = new JButton("Deletar");
		jbtDeletar.setBounds(266, 130, 120, 30);
		getContentPane().add(jbtDeletar);
		jbtDeletar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = jltlistar.getSelectedRow();
				if (index == -1) {
					JOptionPane.showMessageDialog(null, "Selecione um cliente");
				} else {
					Cliente cliente = new Cliente();
					MongoDAO<Cliente> clientes = new MongoDAO<Cliente>();
					linhaSelecionada = jltlistar.getSelectedRow();
					cliente.setCpf(jltlistar.getValueAt(linhaSelecionada, 1).toString());
					clientes.excluir(cliente, "cpf", cliente.getCpf());
					JOptionPane.showMessageDialog(null, "Deletado com sucesso!" + cliente.getCpf());

					listarClientes();
				}

			}
		});
	}

	public void BotaoSair() {
		JButton jbtSair = new JButton("sair");
		jbtSair.setBounds(404, 330, 120, 30);
		getContentPane().add(jbtSair);

		jbtSair.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();

			}
		});
	}

	public void BotaoEditar() {
		JButton btnEditar = new JButton("Editar");
		btnEditar.setBounds(137, 131, 117, 28);
		getContentPane().add(btnEditar);
		btnEditar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = jltlistar.getSelectedRow();
				if (index == -1) {
					JOptionPane.showMessageDialog(null, "Selecione um cliente");
				} else {
					linhaSelecionada = jltlistar.getSelectedRow();
					jtfNome.setText(jltlistar.getValueAt(linhaSelecionada, 0).toString());
					jtfCpf.setText(jltlistar.getValueAt(linhaSelecionada, 1).toString());
					jtfCod.setText(jltlistar.getValueAt(linhaSelecionada, 2).toString());
					jtfCpf.setEditable(false);
				}
			}
		});

	}

	public void listarClientes() {
		dtmListar.setNumRows(0);
		MongoCursor<Cliente> cursor = collectionCliente.find().as(Cliente.class);
		cursor.forEach(c -> {
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
		jspListar.setBounds(10, 170, 362, 200);
		getContentPane().add(jspListar);
		listarClientes();

	}

	public void actionPerformed(ActionEvent e) {

	}

	public FrameCliente() {
		setTitle("Cadastro de Clientes");
		getContentPane().setLayout(null);
		criarLabel();
		BotaoEditar();
		Lista();
		BotaoSalvar();
		BotaoDeletar();
		BotaoSair();
		setSize(600, 400);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
