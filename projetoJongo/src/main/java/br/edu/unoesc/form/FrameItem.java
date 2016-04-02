package br.edu.unoesc.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
import br.edu.unoesc.model.Item;
import br.edu.unoesc.model.Produto;

public class FrameItem extends JFrame implements ActionListener {
	private JTable jltlistar;
	private JScrollPane jspListar;
	private DefaultTableModel dtmListar;
	private JLabel jlbQuantidade;
	private Double valorTotal = 0.0;
	private JComboBox comboBox;
	private JButton jbtAdicionar;

	MongoClient mongoClient = new MongoClient("localhost", 27017);
	DB db = mongoClient.getDB("projeto");
	Jongo jongo = new Jongo(db);
	MongoCollection collectionProduto = jongo.getCollection("br.edu.unoesc.model.Produto");

	private JTextField jtfProduto;
	private JTextField jtfValor;
	private JLabel lblProduto;
	private JLabel lblValorUnt;

	public void criarLabel() {
		jlbQuantidade = new JLabel();
		jlbQuantidade = new JLabel("Quantidade: ");
		jlbQuantidade.setBounds(343, 9, 100, 20);
		getContentPane().add(jlbQuantidade);

		comboBox = new JComboBox();
		comboBox.setBounds(438, 7, 52, 24);
		getContentPane().add(comboBox);
		comboBox.setEnabled(false);

		jtfProduto = new JTextField();
		jtfProduto.setText("");
		jtfProduto.setBounds(12, 28, 114, 19);
		getContentPane().add(jtfProduto);
		jtfProduto.setColumns(10);
		jtfProduto.setEditable(false);

		jtfValor = new JTextField();
		jtfValor.setText("");
		jtfValor.setBounds(199, 31, 114, 19);
		getContentPane().add(jtfValor);
		jtfValor.setColumns(10);
		jtfValor.setEditable(false);

		lblProduto = new JLabel("Produto");
		lblProduto.setBounds(12, 12, 70, 15);
		getContentPane().add(lblProduto);

		lblValorUnt = new JLabel("Valor unt.");
		lblValorUnt.setBounds(204, 12, 70, 15);
		getContentPane().add(lblValorUnt);

	}

	public void BotaoAdicionar() {
		jbtAdicionar = new JButton("Adicionar");
		jbtAdicionar.setBounds(12, 59, 120, 30);
		getContentPane().add(jbtAdicionar);
		jbtAdicionar.setEnabled(false);
		jbtAdicionar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Item item = new Item();
				MongoDAO<Item> itens = new MongoDAO<Item>();
				item.setNomeItem(jtfProduto.getText());
				item.setValor(Double.parseDouble(jtfValor.getText()));
				item.setQuantidade(comboBox.getSelectedIndex() + 1);
				itens.salvar(item);
				JOptionPane.showMessageDialog(null, "Ok");
				dispose();
			}
		});
	}

	public void BotaoSelecionar() {
		JButton btnNewButton = new JButton("Selecionar");
		btnNewButton.setBounds(155, 62, 117, 25);
		getContentPane().add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				int linhaSelecionada = jltlistar.getSelectedRow();
				if (linhaSelecionada == -1) {
					JOptionPane.showMessageDialog(null, "Selecione um item!");
				} else {
					comboBox.setEnabled(true);
					jbtAdicionar.setEnabled(true);
					int qtdEstoque = Integer.parseInt(jltlistar.getValueAt(linhaSelecionada, 2).toString());
					for (int i = 1; i <= qtdEstoque; i++) {
						comboBox.addItem(i);
					}
					jtfProduto.setText(jltlistar.getValueAt(linhaSelecionada, 0).toString());
					jtfValor.setText(jltlistar.getValueAt(linhaSelecionada, 1).toString());
					dtmListar.setNumRows(0);
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
			dtmListar.addRow(
					new String[] { c.getNome().toString(), c.getValor().toString(), c.getQuantidade().toString() });
			valorTotal += valorItem;
		});
	}

	public void Lista() {
		dtmListar = new DefaultTableModel();
		dtmListar.addColumn("Produto");
		dtmListar.addColumn("Valor Unt.");
		dtmListar.addColumn("Qtd Estoque");
		jltlistar = new JTable(dtmListar);
		jspListar = new JScrollPane(jltlistar);
		jspListar.setBounds(12, 101, 450, 259);
		getContentPane().add(jspListar);
		listarProduto();

	}

	public FrameItem() {
		setTitle("Itens");
		getContentPane().setLayout(null);
		criarLabel();
		BotaoSelecionar();
		Lista();
		BotaoAdicionar();
		BotaoSair();
		setSize(600, 400);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}
