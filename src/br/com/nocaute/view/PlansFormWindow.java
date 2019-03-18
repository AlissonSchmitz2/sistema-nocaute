package br.com.nocaute.view;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JTextField;

import br.com.nocaute.util.JNumberFormatField;

public class PlansFormWindow extends AbstractWindowFrame {
	private static final long serialVersionUID = 5227409767477555089L;

	// Componentes
	private JButton btnBuscar, btnAdicionar, btnRemover, btnSalvar;
	private JLabel label;
	private JComboBox<String> cbxModalidade;
	private JTextField txfPlano;
	private JNumberFormatField txfValor;

	// Icones
	private ImageIcon iconBuscar = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/localizar.png"));
	private ImageIcon iconAdicionar = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/adicionar.png"));
	private ImageIcon iconRemover = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/remover.png"));
	private ImageIcon iconSalvar = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/salvar.png"));
	private ImageIcon iconJanela = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/16x16/financeiro.png"));

	public PlansFormWindow(JDesktopPane desktop) {
		super("Planos", 450, 165, desktop);
		setFrameIcon(iconJanela);
		
		criarComponentes();
	}

	private void criarComponentes() {

		btnBuscar = new JButton("Buscar");
		btnBuscar.setBounds(15, 5, 95, 40);
		btnBuscar.setIcon(iconBuscar);
		btnBuscar.setToolTipText("Clique aqui para buscar os usuários");
		getContentPane().add(btnBuscar);

		btnAdicionar = new JButton("Adicionar");
		btnAdicionar.setBounds(110, 5, 110, 40);
		btnAdicionar.setIcon(iconAdicionar);
		btnAdicionar.setToolTipText("Clique aqui para adicionar um usuário");
		getContentPane().add(btnAdicionar);

		btnRemover = new JButton("Remover");
		btnRemover.setBounds(220, 5, 110, 40);
		btnRemover.setIcon(iconRemover);
		btnRemover.setToolTipText("Clique aqui para remover");
		getContentPane().add(btnRemover);

		btnSalvar = new JButton("Salvar");
		btnSalvar.setBounds(330, 5, 95, 40);
		btnSalvar.setIcon(iconSalvar);
		btnSalvar.setToolTipText("Clique aqui para salvar");
		getContentPane().add(btnSalvar);

		label = new JLabel("Modalidade: ");
		label.setBounds(5, 55, 150, 25);
		getContentPane().add(label);

		cbxModalidade = new JComboBox<String>();
		cbxModalidade.addItem("-- Selecione --");
		cbxModalidade.setBounds(70, 55, 355, 20);
		cbxModalidade.setToolTipText("Informe a modalidade");
		getContentPane().add(cbxModalidade);

		label = new JLabel("Plano: ");
		label.setBounds(5, 80, 150, 25);
		getContentPane().add(label);

		txfPlano = new JTextField();
		txfPlano.setBounds(70, 80, 355, 20);
		txfPlano.setToolTipText("Digite o plano");
		getContentPane().add(txfPlano);

		label = new JLabel("Valor: ");
		label.setBounds(5, 105, 150, 25);
		getContentPane().add(label);

		txfValor = new JNumberFormatField();
		txfValor.setBounds(70, 105, 70, 20);
		txfValor.setLimit(6);
		txfValor.setToolTipText("Informe o valor");
		getContentPane().add(txfValor);

	}
}
