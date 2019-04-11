package br.com.nocaute.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import com.toedter.calendar.JDateChooser;

import br.com.nocaute.dao.InvoicesRegistrationDAO;
import br.com.nocaute.dao.PlanDAO;
import br.com.nocaute.dao.RegistrationDAO;
import br.com.nocaute.dao.RegistrationModalityDAO;
import br.com.nocaute.image.MasterImage;
import br.com.nocaute.model.InvoicesRegistrationModel;
import br.com.nocaute.model.PlanModel;
import br.com.nocaute.model.RegistrationModalityModel;
import br.com.nocaute.model.RegistrationModel;
import br.com.nocaute.view.tableModel.PaymentsTableModel;
import br.com.nocaute.view.tableModel.PaymentsTableRenderer;

public class ListPaymentsWindow extends AbstractGridWindow {
	private static final long serialVersionUID = 3905054631992455187L;
	
	// Componentes
	private JButton btnSearch;
	private JLabel label;
	private JDateChooser startDate;
	private JDateChooser finishDate;
	private JComboBox<String> cbxSituation; 	
	private PaymentsTableModel tableModel;
	private JTable jTablePayments;
	
	// InvoicesRegistration
	private InvoicesRegistrationDAO invoicesRegistrationDAO;
	
	// Registration Modalities
	private RegistrationModalityDAO registrationModalityDAO;
	
	// Registration
	private RegistrationDAO registrationDAO;
	
	// Plan
	private PlanDAO planDAO;
	
	// Data atual
	private Date currentDate = new Date();

	public ListPaymentsWindow(JDesktopPane desktop) {
		super("Consultar Faturas", 610, 380, desktop, false);
		setFrameIcon(MasterImage.search_16x16);
		
		try {
			invoicesRegistrationDAO = new InvoicesRegistrationDAO(CONNECTION);
			registrationDAO = new RegistrationDAO(CONNECTION);
			registrationModalityDAO = new RegistrationModalityDAO(CONNECTION);
			planDAO = new PlanDAO(CONNECTION);
		} catch (SQLException error) {
			error.printStackTrace();
		}
		
		createComponents();

		// Seta as ações esperadas para cada botão
		setButtonsActions();
	}

	private void setButtonsActions() {
		// Ação Pesquisar
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// Auxiliares para consulta ao banco.
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					String formatedStartDate = dateFormat.format(startDate.getDate());
					String formatedFinishDate = dateFormat.format(finishDate.getDate());

					// Recupera a lista de faturas entre as datas e a situação selecionada.
					List<InvoicesRegistrationModel> invoicesRegistrationList = invoicesRegistrationDAO.searchInvoices(
							formatedStartDate, formatedFinishDate, cbxSituation.getSelectedItem().toString());

					tableModel.clear();

					if(invoicesRegistrationList.isEmpty()) {
						bubbleWarning("Nenhuma fatura foi encontrada");
					} else {
						// Recupera todas as matrículas.
						List<RegistrationModel> registrationsList = registrationDAO.selectAll();

						// Percorre as matrículas
						for (int i = 0; i < registrationsList.size(); i++) {
							RegistrationModel registrationModel = registrationsList.get(i);
							// Caso a matrícula tenha sido encerrada, seta o cancelamento_registro das
							// faturas como true.
							if (registrationModel.getClosingDate() != null) {
								for (int j = 0; j < invoicesRegistrationList.size(); j++) {
									InvoicesRegistrationModel invoicesRegistrationModel = invoicesRegistrationList.get(j);
									if (invoicesRegistrationModel.getRegistrationCode() == registrationModel.getRegistrationCode()) {
										invoicesRegistrationModel.setRegistrationFinish(true);
									}
								}
							}
							// Caso não tenha sido cancelada, inicia o processo para verificar se alguma
							// modalidade foi cancelada após a geração das faturas futuras (mês vencimento
							// maior que o mês atual).
							else {
								// Lista de modalidades relacionadas a um determinado codigo_matricula.
								List<RegistrationModalityModel> modalitiesList = registrationModalityDAO
										.getByRegistrationCode(registrationModel.getRegistrationCode());

								// Calculos para recuperar o valor total da fatura.
								BigDecimal valorFatura;
								float valorTotal = 0;
								for (int k = 0; k < modalitiesList.size(); k++) {
									// Verifica se o aluno está matriculado na modadalide (dataFim não preenchida).
									if (modalitiesList.get(k).getFinishDate() == null) {
										PlanModel planModel = planDAO.findById(modalitiesList.get(k).getPlanId());
										valorFatura = planModel.getMonthlyValue();
										valorTotal += valorFatura.floatValue();
									}
								}

								for (int j = 0; j < invoicesRegistrationList.size(); j++) {
									InvoicesRegistrationModel invoicesRegistrationModel = invoicesRegistrationList.get(j);

									@SuppressWarnings("deprecation")
									int currentMonth = currentDate.getMonth();
									@SuppressWarnings("deprecation")
									int invoiceMonth = invoicesRegistrationModel.getDueDate().getMonth();

									// Caso o mês da fatura seja maior que o mês atual e o valor total seja
									// diferente do valor atual da fatura (modalidades alteradas), o valor da fatura
									// será destacado e alterado.
									if (invoiceMonth > currentMonth && invoicesRegistrationModel.getValue() > valorTotal
											&& invoicesRegistrationModel.getRegistrationCode() == registrationModel
													.getRegistrationCode()) {
										// System.out.println("Vencimento: " + invoicesRegistrationModel.getDueDate() +
										// "Valor fatura: " + invoicesRegistrationModel.getValue() + "Novo valor: " +
										// valorTotal);
										invoicesRegistrationModel.setValue(valorTotal);
										invoicesRegistrationModel.setHighlightValue(true);
									}
								}
							}
						}

						// Adiciona as faturas a table.
						tableModel.addModelsList(invoicesRegistrationList);
					}										
				} catch (SQLException error) {
					error.printStackTrace();
				}
			}
		});
	}

	private void createComponents() {
		
		label = new JLabel("De: ");
		label.setBounds(5, 10, 50, 25);
		getContentPane().add(label);

		startDate = new JDateChooser(new Date());
		startDate.setBounds(30, 10, 90, 20);
		startDate.setDateFormatString("dd/MM/yyyy");
		getContentPane().add(startDate);
		
		label = new JLabel("Até: ");
		label.setBounds(130, 10, 50, 25);
		getContentPane().add(label);

		finishDate = new JDateChooser(new Date());
		finishDate.setBounds(160, 10, 90, 20);
		finishDate.setDateFormatString("dd/MM/yyyy");
		getContentPane().add(finishDate);
		
		label = new JLabel("Situação: ");
		label.setBounds(260, 10, 50, 25);
		getContentPane().add(label);
		
		cbxSituation = new JComboBox<String>();
		cbxSituation.addItem("Todas");
		cbxSituation.addItem("Em Aberto");
		cbxSituation.addItem("Pagas");
		cbxSituation.addItem("Canceladas");
		cbxSituation.setBounds(315, 10, 150, 20);
		cbxSituation.setToolTipText("Informe a situação");
		getContentPane().add(cbxSituation);
		
		btnSearch = new JButton("Buscar", MasterImage.search_16x16);
		btnSearch.setBounds(475, 6, 110, 27);
		btnSearch.setToolTipText("Clique aqui para pesquisar as faturas");
		getContentPane().add(btnSearch);


		loadGrid();
	}

	private void loadGrid() {
		tableModel = new PaymentsTableModel();
		jTablePayments = new JTable(tableModel);

		// Habilita a seleção por linha
		jTablePayments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTablePayments.setDefaultRenderer(Object.class, new PaymentsTableRenderer());
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
