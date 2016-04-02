package br.edu.unoesc.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Date;

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
import br.edu.unoesc.model.Item;
import br.edu.unoesc.model.Pedido;

public class FramePedido extends JFrame implements ActionListener {

	private JLabel jlbTotal;
	private JLabel jlbnomeCliente;
	private JLabel jlbcpfCliente;
	JLabel jlbgetCliente;
	JLabel jlbgetCpf;
	private JTextField jtfTotal;
	private DefaultTableModel dtmListar;
	private JTable jltlistar;
	private JScrollPane jspListar;
	private JButton jbtCadastrarItem;
	private Double valorTotal = 0.0;
	private JButton btnSelecioneUmCliente;
	private JLabel jlbCodigo;
	JLabel jlbGetcod;
	int linhaSelecionada;
	MongoClient mongoClient = new MongoClient("localhost", 27017);
	DB db = mongoClient.getDB("projeto");
	Jongo jongo = new Jongo(db);
	MongoCollection collectionItem = jongo.getCollection("br.edu.unoesc.model.Item");

	public void criarEntradas() {

		jlbnomeCliente = new JLabel("Cliente");
		jlbnomeCliente.setBounds(12, 42, 138, 15);
		getContentPane().add(jlbnomeCliente);

		jlbgetCliente = new JLabel("");
		jlbgetCliente.setBounds(12, 62, 138, 15);
		getContentPane().add(jlbgetCliente);

		jlbcpfCliente = new JLabel("CPF");
		jlbcpfCliente.setBounds(200, 42, 138, 15);
		getContentPane().add(jlbcpfCliente);

		jlbgetCpf = new JLabel("");
		jlbgetCpf.setBounds(200, 62, 138, 15);
		getContentPane().add(jlbgetCpf);

		jlbCodigo = new JLabel("Codigo");
		jlbCodigo.setBounds(350, 42, 70, 15);
		getContentPane().add(jlbCodigo);

		jlbGetcod = new JLabel("");
		jlbGetcod.setBounds(350, 62, 70, 15);
		getContentPane().add(jlbGetcod);

		// ----------------------------------------TOTAL----------------------------
		jlbTotal = new JLabel("Total:");
		jlbTotal.setBounds(465, 227, 50, 20);
		getContentPane().add(jlbTotal);

		jtfTotal = new JTextField();
		jtfTotal.setBounds(510, 227, 90, 30);
		jtfTotal.setText("0,00");
		getContentPane().add(jtfTotal);

		jbtCadastrarItem = new JButton("Adicionar Item");
		jbtCadastrarItem.setBounds(430, 10, 150, 25);
		jbtCadastrarItem.addActionListener(this);
		getContentPane().add(jbtCadastrarItem);
		jbtCadastrarItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new FrameItem();
				dtmListar.setNumRows(0);
			}
		});

		// -------------------------------------GERAR
		// PEDIDO----------------------
		JButton jbtCriarPedido = new JButton("Gerar Pedido");
		jbtCriarPedido.setBounds(12, 333, 160, 25);
		jbtCriarPedido.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (jlbgetCliente.getText() == "") {
					JOptionPane.showMessageDialog(null, "Selecione um cliente");
				} else if (dtmListar.getRowCount() == 0) {
					JOptionPane.showMessageDialog(null, "Atualise a lista de itens!");
				} else {
					MongoDAO<Cliente> clientes = new MongoDAO<Cliente>();

					Cliente cliente = new Cliente();
					cliente.setCpf(jlbgetCpf.getText());

					Pedido pedido = new Pedido();
					pedido.setData(new Date());
					pedido.setValorTotal(Double.parseDouble(valorTotal.toString()));

					cliente.addPedido(pedido);

					int cont = 1;
					while (cont <= dtmListar.getRowCount()) {
						Item item = new Item();
						item.setNomeItem(jltlistar.getValueAt(cont - 1, 0).toString());
						item.setQuantidade(Integer.parseInt(jltlistar.getValueAt(cont - 1, 1).toString()));
						item.setValor(Double.parseDouble(jltlistar.getValueAt(cont - 1, 2).toString()));
						cont++;
						pedido.addItem(item);
					}
					clientes.update(cliente, "cpf", cliente.getCpf());

					JOptionPane.showMessageDialog(null,
							"Pedido gerado com sucesso para o cliente " + jlbgetCliente.getText());
					collectionItem.remove("{}");
					dispose();
				}
			}
		});
		getContentPane().add(jbtCriarPedido);

	}

	public void BotaoListar() {
		JButton jbtListar = new JButton("Atualizar Lista");
		jbtListar.setBounds(10, 100, 155, 25);
		getContentPane().add(jbtListar);
		jbtListar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				listarItem();

			}
		});

	}

	public void BotaoExcluir() {
		JButton btnExcluirItem = new JButton("Excluir item");
		btnExcluirItem.setBounds(210, 100, 117, 25);
		getContentPane().add(btnExcluirItem);
		btnExcluirItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = jltlistar.getSelectedRow();
				if (index == -1) {
					JOptionPane.showMessageDialog(null, "Nenhum produto selecionado");
				} else {
					Item item = new Item();
					MongoDAO<Item> itens = new MongoDAO<Item>();
					linhaSelecionada = jltlistar.getSelectedRow();
					item.setNomeItem(jltlistar.getValueAt(linhaSelecionada, 0).toString());
					itens.excluir(item, "nomeItem", item.getNomeItem());

					JOptionPane.showMessageDialog(null, "Produto deletado com sucesso!");
					listarItem();
				}

			}
		});
	}

	public void BotaoCliente() {
		btnSelecioneUmCliente = new JButton("Selecione um cliente");
		btnSelecioneUmCliente.setBounds(30, 5, 180, 25);
		getContentPane().add(btnSelecioneUmCliente);
		btnSelecioneUmCliente.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FrameListaCliente frameLista = new FrameListaCliente();
				frameLista.setTelaSelecionada(2);
				dispose();
			}
		});
	}

	public void BotaoSair() {
		JButton jbtSair = new JButton("Cancelar Pedido");
		jbtSair.setBounds(430, 333, 150, 25);
		getContentPane().add(jbtSair);

		jbtSair.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				collectionItem.remove("{}");
				dispose();

			}
		});
	}

	public void listarItem() {
		dtmListar.setNumRows(0);
		MongoCursor<Item> cursor = collectionItem.find().as(Item.class);
		valorTotal = 0.0;
		cursor.forEach(c -> {
			Double valorItem = c.getValor() * c.getQuantidade();
			dtmListar.addRow(new String[] { c.getNomeItem().toString(), c.getQuantidade().toString(),
					c.getValor().toString(), valorItem.toString() });
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

		jltlistar = new JTable(dtmListar);
		jspListar = new JScrollPane(jltlistar);
		jspListar.setBounds(10, 137, 450, 184);
		getContentPane().add(jspListar);
		listarItem();
	}

	public FramePedido() {
		setTitle("Pedido");
		getContentPane().setLayout(null);
		BotaoCliente();
		criarEntradas();
		BotaoListar();
		BotaoExcluir();
		Lista();
		BotaoSair();
		setSize(600, 400);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}
