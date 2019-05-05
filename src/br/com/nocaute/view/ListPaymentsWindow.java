package br.com.nocaute.view;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import com.toedter.calendar.JDateChooser;

import br.com.nocaute.dao.InvoicesRegistrationDAO;
import br.com.nocaute.dao.ModalityDAO;
import br.com.nocaute.dao.PlanDAO;
import br.com.nocaute.dao.RegistrationDAO;
import br.com.nocaute.dao.RegistrationModalityDAO;
import br.com.nocaute.image.MasterImage;
import br.com.nocaute.model.InvoicesRegistrationModel;
import br.com.nocaute.model.ModalityModel;
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
	
	// Modality
	private ModalityDAO modalityDAO;
	
	// Data atual
	private Date currentDate = new Date();

	public ListPaymentsWindow(JDesktopPane desktop, Connection CONNECTION) {
		super("Consultar Faturas", 610, 380, desktop, false);
		setFrameIcon(MasterImage.search_16x16);
		
		try {
			invoicesRegistrationDAO = new InvoicesRegistrationDAO(CONNECTION);
			registrationDAO = new RegistrationDAO(CONNECTION);
			registrationModalityDAO = new RegistrationModalityDAO(CONNECTION);
			planDAO = new PlanDAO(CONNECTION);
			modalityDAO = new ModalityDAO(CONNECTION);
			tableModel = new PaymentsTableModel(CONNECTION);
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
			@SuppressWarnings("deprecation")
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
								
								// Quantidade de modalidades de uma fatura.
								Integer quantityModality = 0;
								
								// Calculos para recuperar o valor total da fatura.
								BigDecimal auxValue;
								float amount = 0;
								List<String> changeDescription = new ArrayList<>();
								for (int k = 0; k < modalitiesList.size(); k++) {
									int currentMonth = currentDate.getMonth();
									quantityModality++;		
									
									// Verifica se o aluno está matriculado na modadalide (dataFim não preenchida).
									if (modalitiesList.get(k).getFinishDate() == null) {
										PlanModel planModel = planDAO.findById(modalitiesList.get(k).getPlanId());
										auxValue = planModel.getMonthlyValue();
										amount += auxValue.floatValue();
									} 
									// Caso não esteja, recupera o valor e monta a descriação do desconto.
									else if(modalitiesList.get(k).getFinishDate().getMonth() >= currentMonth){
										PlanModel planModel = planDAO.findById(modalitiesList.get(k).getPlanId());
										auxValue = planModel.getMonthlyValue();
										
										ModalityModel modalityModel = modalityDAO.findById(modalitiesList.get(k).getModalityId());
										String descricao = "- R$" + auxValue + " -> Cancelamento da modalidade " + modalityModel.getName();
										changeDescription.add(descricao);
									}
								}
								
								// Percorre todas as faturas recuperadas aplicando o desconto e a descrição necessária as faturas.
								for (int j = 0; j < invoicesRegistrationList.size(); j++) {
									InvoicesRegistrationModel invoicesRegistrationModel = invoicesRegistrationList.get(j);									

									Integer oldQuantityModality = invoicesRegistrationModel.getQuantityModality();
									int currentMonth = currentDate.getMonth();
									int invoiceMonth = invoicesRegistrationModel.getDueDate().getMonth();

									// Caso o mês da fatura seja maior que o mês atual, o valor total da fatura seja
									// diferente da fatura gerada e o código de matrícula da fatura seja igual ao
									// código da matrícula do aluno atual, atualiza o valor da fatura.
									if (invoiceMonth > currentMonth && amount != invoicesRegistrationModel.getValue()
											&& invoicesRegistrationModel.getRegistrationCode() == registrationModel
													.getRegistrationCode() && invoicesRegistrationModel.getPaymentDate() == null) {
										// Seta novo valor da fatura.
										invoicesRegistrationModel.setValue(amount);
									}	
									
									// Verifica se foi adicionada alguma nova modalidade.
									if (invoiceMonth > currentMonth && invoicesRegistrationModel
											.getRegistrationCode() == registrationModel.getRegistrationCode()
											&& oldQuantityModality != quantityModality) {
										Date lowerStartDate = new Date();
										
										// Recupera a menor das datas de inicio.
										for (int k = 0; k < modalitiesList.size(); k++) {											
											if(k == 0) {
												lowerStartDate = modalitiesList.get(k).getStartDate();
											} else if (lowerStartDate.compareTo(modalitiesList.get(k).getStartDate()) > 0) {
												lowerStartDate = modalitiesList.get(k).getStartDate();
											}
										}
										
										// Recupera a descrição do aumento.
										for (int k = 0; k < modalitiesList.size(); k++) {	
											Date startDate = modalitiesList.get(k).getStartDate();
											if ((startDate.compareTo(currentDate) > 0
													|| startDate.compareTo(currentDate) == 0)
													&& startDate.compareTo(lowerStartDate) > 0
													&& modalitiesList.get(k).getFinishDate() == null) {											
												PlanModel planModel = planDAO.findById(modalitiesList.get(k).getPlanId());
												auxValue = planModel.getMonthlyValue();
												
												ModalityModel modalityModel = modalityDAO.findById(modalitiesList.get(k).getModalityId());
												String descricao = "+ R$" + auxValue + " -> Adição da modalidade " + modalityModel.getName();
												changeDescription.add(descricao);												
											}
										}
									}
									
									// Caso o mês da fatura seja maior que o mês atual e o array de descrição
									// das alterações não estiver vazio, o valor da fatura
									// será destacado e a descrição será aplicada.
									if (invoiceMonth > currentMonth && changeDescription.size() > 0
											&& invoicesRegistrationModel.getRegistrationCode() == registrationModel
													.getRegistrationCode() && invoicesRegistrationModel.getPaymentDate() == null) {										
										// Seta o destaque da fatura para true.										
										invoicesRegistrationModel.setHighlightValue(true);
										
										// Seta a descrição do desconto da fatura.
										invoicesRegistrationModel.setChangeDescription(changeDescription);
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
		jTablePayments = new JTable(tableModel);

		// Habilita a seleção por linha
		jTablePayments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTablePayments.setDefaultRenderer(Object.class, new PaymentsTableRenderer());
		jTablePayments.getColumnModel().getColumn(0).setMaxWidth(55);
		jTablePayments.getColumnModel().getColumn(1).setPreferredWidth(155);
		jTablePayments.getColumnModel().getColumn(3).setPreferredWidth(40);
        
        jTablePayments.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent me) {
				// Se clicou com o botão direito do mouse na linha.
				if ((me.getClickCount() == 1) && (me.getButton() == MouseEvent.BUTTON3)) {
					InvoicesRegistrationModel model = ((PaymentsTableModel) jTablePayments.getModel()).getModel(jTablePayments.getSelectedRow());
					// Caso a coluna esteja destacada, abre o popupmenu.
					if(model.isHighlightValue()) {
						createPopupMenu().show(jTablePayments, me.getX(), me.getY());
					}					
				}
			}
			
			@Override
		    public void mousePressed(MouseEvent event) {
		        // Seleciona a linha no ponto em que o mouse foi pressionado.
		        Point point = event.getPoint();
		        int currentRow = jTablePayments.rowAtPoint(point);
		        jTablePayments.setRowSelectionInterval(currentRow, currentRow);
		    }
		});

		grid = new JScrollPane(jTablePayments);
		setLayout(null);
		resizeGrid(grid, 5, 45, 580, 295);
		grid.setVisible(true);

		add(grid);
	}
	
	// Cria e atribui as ações aos menus exibidos com o clique direito.
	private JPopupMenu createPopupMenu() {
		JPopupMenu jPopupMenu = new JPopupMenu();
		
		JMenuItem jMenuItemDetails = new JMenuItem("Detalhes");
		jMenuItemDetails.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				InvoicesRegistrationModel model = ((PaymentsTableModel) jTablePayments.getModel()).getModel(jTablePayments.getSelectedRow());
				
				String descricao = "";
            	for(int i = 0; i < model.getChangeDescription().size(); i++){
            		descricao += model.getChangeDescription().get(i) + "\n";
            	}
            	
                JOptionPane.showMessageDialog(null, descricao);
			}
		});
		
		jPopupMenu.add(jMenuItemDetails);
		
		return jPopupMenu;
	}
}
