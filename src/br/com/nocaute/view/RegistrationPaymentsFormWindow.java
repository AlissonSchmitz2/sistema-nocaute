package br.com.nocaute.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import com.toedter.calendar.JDateChooser;

import br.com.nocaute.image.MasterImage;
import br.com.nocaute.model.RegistrationModel;
import br.com.nocaute.view.tableModel.PaymentsTableModel;

public class RegistrationPaymentsFormWindow extends AbstractGridWindow {
	private static final long serialVersionUID = 3113169303092452322L;
	
	// Componentes
	private JButton btnPesquisar;
	private JLabel label;
	// Refatorar para padrao
	private JComboBox<String> cbxSituacao;

	private PaymentsTableModel model;
	private JTable jTablePayments;

	public RegistrationPaymentsFormWindow(JDesktopPane desktop) {
		super("Pagamentos de Faturas", 610, 380, desktop, false);
		setFrameIcon(MasterImage.financial_16x16);

		criarComponentes();

		// Seta as ações esperadas para cada botão
		setButtonsActions();
	}

	private void setButtonsActions() {
		// Ação Pesquisar
		btnPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: pesquisar faturas
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

		btnPesquisar = new JButton("Buscar", MasterImage.search_16x16);
		btnPesquisar.setBounds(475, 6, 110, 27);
		btnPesquisar.setToolTipText("Clique aqui para pesquisar as faturas");
		getContentPane().add(btnPesquisar);

		carregarGrid();
	}

	private void carregarGrid() {
		model = new PaymentsTableModel();
		jTablePayments = new JTable(model);

		// Habilita a seleção por linha
		jTablePayments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTablePayments.setDefaultRenderer(Object.class, renderer);
		jTablePayments.getColumnModel().getColumn(0).setMaxWidth(55);
		jTablePayments.getColumnModel().getColumn(1).setPreferredWidth(155);
		jTablePayments.getColumnModel().getColumn(3).setPreferredWidth(40);
		
		jTablePayments.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent me) {
				//Se clicou com o botão direito do mouse na linha
				if ((me.getClickCount() == 1) && (me.getButton() == MouseEvent.BUTTON3)) {
					criarPopupMenu().show(jTablePayments, me.getX(), me.getY());
				}
			}
		});

		//TESTE
		RegistrationModel teste1 = new RegistrationModel();
		teste1.setStudentCode(1);
		teste1.setRegistrationCode(1);

		RegistrationModel teste2 = new RegistrationModel();
		teste2.setStudentCode(2);
		teste2.setRegistrationCode(2);

		try {
			model.addModel(teste1);
			model.addModel(teste2);
		} catch (Exception e) {
			System.err.printf("Erro ao iniciar lista de cursos: %s.\n", e.getMessage());
		}
		
		grid = new JScrollPane(jTablePayments);
		setLayout(null);
		resizeGrid(grid, 5, 45, 580, 295);
		grid.setVisible(true);

		add(grid);
	}
	
	private JPopupMenu criarPopupMenu() {
		JPopupMenu jPopupMenu = new JPopupMenu();
		
		JMenuItem jMenuItemPagarFatura = new JMenuItem("Pagar Fatura");
		jMenuItemPagarFatura.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO: Ação pagar fatura
				
				//Pega o index da linha selecionada
				//int index = jTablePayments.getSelectedRow();
			}
		});
		
		JMenuItem jMenuItemCancelarFatura = new JMenuItem("Cancelar Fatura");
		jMenuItemCancelarFatura.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO: Ação cancelar fatura
				
				//Pega o index da linha selecionada
				//int index = jTablePayments.getSelectedRow();
			}
		});
		
		JMenuItem jMenuItemDescAcresFatura = new JMenuItem("Desconto/Acréscimo");
		jMenuItemDescAcresFatura.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO: Ação desconto/acréscimo na fatura
				
				//Pega o index da linha selecionada
				//int index = jTablePayments.getSelectedRow();
			}
		});
		
		jPopupMenu.add(jMenuItemPagarFatura);
		jPopupMenu.add(new JSeparator());
		jPopupMenu.add(jMenuItemCancelarFatura);
		jPopupMenu.add(new JSeparator());
		jPopupMenu.add(jMenuItemDescAcresFatura);
		
		return jPopupMenu;
	}
	
}
