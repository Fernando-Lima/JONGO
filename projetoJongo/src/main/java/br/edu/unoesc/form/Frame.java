package br.edu.unoesc.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

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
import br.edu.unoesc.model.Produto;

public class Frame extends JFrame implements ActionListener {

	private JTextField jtfNome;
	private JTextField jtfValor;
	private JTable jltlistar;
	private JScrollPane jspListar;
	private DefaultTableModel dtmListar;
	private JLabel jlbQuantidade;
	private JTextField jtfQauntidade;
	private Double valorTotal = 0.0;
	private JLabel jlbTotal;
	private JTextField jtfTotal;
	private JTextField jtfCod;
	int linhaSelecionada;

	MongoClient mongoClient = new MongoClient("localhost", 27017);
	DB db = mongoClient.getDB("projeto");
	Jongo jongo = new Jongo(db);
	MongoCollection collectionProduto = jongo.getCollection("br.edu.unoesc.model.Produto");

	public void criarLabel() {
		JLabel jlbNome = new JLabel();
		jlbNome = new JLabel("Nome: ");
		jlbNome.setBounds(5, 10, 80, 20);
		getContentPane().add(jlbNome);

		jtfNome = new JTextField();
		jtfNome.setBounds(95, 10, 150, 20);
		getContentPane().add(jtfNome);

		JLabel jlbValor = new JLabel();
		jlbValor = new JLabel("Valor: ");
		jlbValor.setBounds(5, 35, 80, 20);
		getContentPane().add(jlbValor);

		jtfValor = new JTextField();
		jtfValor.setBounds(95, 35, 150, 20);
		getContentPane().add(jtfValor);

		jlbQuantidade = new JLabel();
		jlbQuantidade = new JLabel("Quantidade: ");
		jlbQuantidade.setBounds(5, 60, 100, 20);
		getContentPane().add(jlbQuantidade);

		jtfQauntidade = new JTextField();
		jtfQauntidade.setBounds(95, 60, 150, 20);
		getContentPane().add(jtfQauntidade);

		jlbTotal = new JLabel("Total:");
		jlbTotal.setBounds(470, 200, 50, 20);
		getContentPane().add(jlbTotal);

		jtfTotal = new JTextField();
		jtfTotal.setBounds(520, 200, 80, 30);
		jtfTotal.setText("0,00");
		getContentPane().add(jtfTotal);

		jtfCod = new JTextField();
		jtfCod.setText("");
		jtfCod.setBounds(439, 11, 114, 19);
		getContentPane().add(jtfCod);
		jtfCod.setColumns(10);

		JLabel lblCod = new JLabel("COD:");
		lblCod.setBounds(390, 13, 70, 15);
		getContentPane().add(lblCod);
	}

	public void BotaoSalvar() {
		JButton jbtSalvar = new JButton("Salvar");
		jbtSalvar.setBounds(30, 130, 120, 30);
		jbtSalvar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Produto produto = new Produto();
					MongoDAO<Produto> produtos = new MongoDAO<Produto>();
					if (jtfCod.isEditable() == true) {
						if (jtfNome.getText().equals("") || jtfQauntidade.getText().equals("")
								|| jtfValor.getText().equals("") || jtfCod.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Preencha todos os campos");
							return;
						} else {
							produto.setNome(jtfNome.getText());
							produto.setQuantidade(Integer.parseInt(jtfQauntidade.getText()));
							produto.setValor(Double.parseDouble(jtfValor.getText()));
							produto.setCodProduto(jtfCod.getText());
							produtos.salvar(produto);
							JOptionPane.showMessageDialog(null, "Produto salvo com sucesso!");
						}
					} else {
						produto.setNome(jtfNome.getText());
						produto.setQuantidade(Integer.parseInt(jtfQauntidade.getText()));
						produto.setValor(Double.parseDouble(jtfValor.getText()));
						produto.setCodProduto(jtfCod.getText());
						produtos.update(produto, "codProduto", produto.getCodProduto());

						JOptionPane.showMessageDialog(null, "Alterado com sucesso!");
					}
					jtfNome.setText("");
					jtfQauntidade.setText("");
					jtfValor.setText("");
					jtfCod.setText("");
					jtfCod.setEditable(true);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				listarProduto();
			}

		});
		getContentPane().add(jbtSalvar);
	}

	public void BotaoDeletar() {
		JButton jbtDeletar = new JButton("Deletar");
		jbtDeletar.setBounds(175, 130, 120, 30);
		getContentPane().add(jbtDeletar);
		jbtDeletar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = jltlistar.getSelectedRow();
				if (index == -1) {
					JOptionPane.showMessageDialog(null, "Nenhum produto selecionado");
				} else {
					linhaSelecionada = jltlistar.getSelectedRow();
					MongoDAO<Produto> produtos = new MongoDAO<Produto>();
					Produto produto = new Produto();
					produto.setNome(jltlistar.getValueAt(linhaSelecionada, 0).toString());
					produtos.excluir(produto, "nome", produto.getNome());
					JOptionPane.showMessageDialog(null, "Produto deletado com sucesso!");
					listarProduto();
				}

			}
		});
	}

	public void BotaoEditar() {
		JButton jbtEditar = new JButton("Editar");
		jbtEditar.setBounds(330, 130, 120, 30);
		getContentPane().add(jbtEditar);
		jbtEditar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = jltlistar.getSelectedRow();
				if (index == -1) {
					JOptionPane.showMessageDialog(null, "Selecione um produto");
				} else {
					linhaSelecionada = jltlistar.getSelectedRow();
					jtfNome.setText(jltlistar.getValueAt(linhaSelecionada, 0).toString());
					jtfQauntidade.setText(jltlistar.getValueAt(linhaSelecionada, 1).toString());
					jtfValor.setText(jltlistar.getValueAt(linhaSelecionada, 2).toString());
					jtfCod.setText(jltlistar.getValueAt(linhaSelecionada, 4).toString());
					jtfCod.setEditable(false);
				}
			}
		});
	}

	public void BotaoSair() {
		JButton jbtSair = new JButton("sair");
		jbtSair.setBounds(468, 330, 120, 30);
		getContentPane().add(jbtSair);
		jbtSair.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	public void listarProduto() {
		dtmListar.setNumRows(0);
		MongoCursor<Produto> cursor = collectionProduto.find().as(Produto.class);
		valorTotal = 0.0;
		cursor.forEach(c -> {
			Double valorItem = c.getValor() * c.getQuantidade();
			dtmListar.addRow(new String[] { c.getNome().toString(), c.getQuantidade().toString(),
					c.getValor().toString(), valorItem.toString(), c.getCodProduto().toString() });
			valorTotal += valorItem;
		});
		DecimalFormat df = new DecimalFormat("#,##0.00");
		jtfTotal.setText(df.format(valorTotal));
	}

	public void Lista() {
		dtmListar = new DefaultTableModel();
		dtmListar.addColumn("Produto");
		dtmListar.addColumn("Quantidade");
		dtmListar.addColumn("Valor Unt.");
		dtmListar.addColumn("ValorTotal");
		dtmListar.addColumn("Codigo");

		jltlistar = new JTable(dtmListar);
		jspListar = new JScrollPane(jltlistar);
		jspListar.setBounds(10, 170, 450, 200);
		getContentPane().add(jspListar);
		listarProduto();
	}

	public Frame() {
		setTitle("Cadastro de Produtos");
		getContentPane().setLayout(null);
		criarLabel();
		Lista();
		BotaoSalvar();
		BotaoDeletar();
		BotaoEditar();
		BotaoSair();
		setSize(600, 400);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}
