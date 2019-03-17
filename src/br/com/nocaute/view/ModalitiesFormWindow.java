package br.com.nocaute.view;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.MaskFormatter;

import br.com.nocaute.table.model.UniversityGraduateTableModel;

public class ModalitiesFormWindow extends AbstractGridWindow{
	private static final long serialVersionUID = 7836235315411293572L;
	
	// Componentes
	private JButton btnBuscar, btnAdicionar, btnRemover, btnSalvar, btnOk;
	private JLabel label;
	private JTextField txfModalidade, txfGraduacao;
	
	private UniversityGraduateTableModel model;
	private JTable jTableGraduacoes;

	// Icones
	private ImageIcon iconBuscar = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/localizar.png"));
	private ImageIcon iconAdicionar = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/adicionar.png"));
	private ImageIcon iconRemover = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/remover.png"));
	private ImageIcon iconSalvar = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/salvar.png"));
	private ImageIcon iconOK = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/13x13/ok.png"));

	
	public ModalitiesFormWindow(JDesktopPane desktop) {
			super("Modalidades e Graduações", 450, 335, desktop);
			
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

		txfModalidade = new JTextField();
		txfModalidade.setBounds(70, 55, 355, 20);
		txfModalidade.setToolTipText("Digite a modalidade");
		getContentPane().add(txfModalidade);

		label = new JLabel("Graduação: ");
		label.setBounds(5, 80, 150, 25);
		getContentPane().add(label);

		txfGraduacao = new JTextField();
		txfGraduacao.setBounds(70, 80, 280, 20);
		txfGraduacao.setToolTipText("Digite a graduação");
		getContentPane().add(txfGraduacao);
		
		btnOk = new JButton("OK");
		btnOk.setBounds(355, 77, 70, 25);
		btnOk.setIcon(iconOK);
		btnOk.setToolTipText("Clique aqui para confirmar");
		getContentPane().add(btnOk);
		
		carregarGrid();
		
		label = new JLabel("Duplo clique na linha da graduação para removê-la.");
		label.setBounds(5, 280, 250, 25);
		getContentPane().add(label);
	}
	
	private void carregarGrid() {
		model = new UniversityGraduateTableModel();
		jTableGraduacoes = new JTable(model);

		// Habilita a seleção por linha
		jTableGraduacoes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		grid = new JScrollPane(jTableGraduacoes);
		setLayout(null);
		redimensionarGrid(grid, 5, 110, 420, 170);
		grid.setVisible(true);

		add(grid);
	}
}
