package br.com.nocaute.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import com.toedter.calendar.JDateChooser;

import br.com.nocaute.view.tableModel.PaymentsTableModel;

public class ListPaymentsWindow extends AbstractGridWindow {
	private static final long serialVersionUID = -4201960150625152379L;

	// Componentes
	private JButton btnPesquisar;
	private JLabel label;
	//Refatorar para padrao
	private JComboBox<String> cbxSituacao; 
	
	private PaymentsTableModel model;
	private JTable jTablePayments;

	// Icones
	private ImageIcon iconPesquisar = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/16x16/localizar.png"));

	public ListPaymentsWindow(JDesktopPane desktop) {
		super("Consultar Faturas", 610, 380, desktop);
		setFrameIcon(iconPesquisar);
		
		criarComponentes();

		// Seta as ações esperadas para cada botão
		setButtonsActions();
	}

	private void setButtonsActions() {
		// Ação Pesquisar
		btnPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO: pesquisar faturas
			}
		});
	}

	private void criarComponentes() {
		
		label = new JLabel("De: ");
		label.setBounds(5, 10, 50, 25);
		getContentPane().add(label);

		JDateChooser jDateChooserDe = new JDateChooser(new Date());
		jDateChooserDe.setBounds(30, 10, 90, 20);
		jDateChooserDe.setDateFormatString("dd/MM/yyyy");
		getContentPane().add(jDateChooserDe);
		
		label = new JLabel("Até: ");
		label.setBounds(130, 10, 50, 25);
		getContentPane().add(label);

		JDateChooser jDateChooserAte = new JDateChooser(new Date());
		jDateChooserAte.setBounds(160, 10, 90, 20);
		jDateChooserAte.setDateFormatString("dd/MM/yyyy");
		getContentPane().add(jDateChooserAte);
		
		label = new JLabel("Situação: ");
		label.setBounds(260, 10, 50, 25);
		getContentPane().add(label);
		
		cbxSituacao = new JComboBox<String>();
		cbxSituacao.addItem("Todas");
		cbxSituacao.setBounds(315, 10, 150, 20);
		cbxSituacao.setToolTipText("Informe a situação");
		getContentPane().add(cbxSituacao);
		
		btnPesquisar = new JButton("Buscar");
		btnPesquisar.setBounds(475, 6, 110, 27);
		btnPesquisar.setIcon(iconPesquisar);
		btnPesquisar.setToolTipText("Clique aqui para pesquisar as faturas");
		getContentPane().add(btnPesquisar);


		carregarGrid();
	}

	private void carregarGrid() {
		model = new PaymentsTableModel();
		jTablePayments = new JTable(model);

		// Habilita a seleção por linha
		jTablePayments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTablePayments.getColumnModel().getColumn(0).setMaxWidth(55);
		jTablePayments.getColumnModel().getColumn(1).setPreferredWidth(155);
		jTablePayments.getColumnModel().getColumn(3).setPreferredWidth(40);
		

		grid = new JScrollPane(jTablePayments);
		setLayout(null);
		resizeGrid(grid, 5, 45, 580, 295);
		grid.setVisible(true);

		add(grid);
	}
}
