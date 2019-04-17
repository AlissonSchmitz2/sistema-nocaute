package br.com.nocaute.view;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
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
import br.com.nocaute.util.JNumberFormatField;
import br.com.nocaute.view.tableModel.PaymentsTableModel;
import br.com.nocaute.view.tableModel.PaymentsTableRenderer;

public class RegistrationPaymentsFormWindow extends AbstractGridWindow {
	private static final long serialVersionUID = 3113169303092452322L;
	
	// Componentes
	private JButton btnSearch;
	private JLabel label;
	private JComboBox<String> cbxSituation;
	private JDateChooser startDate;
	private JDateChooser finishDate;	
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

	public RegistrationPaymentsFormWindow(JDesktopPane desktop) {
		super("Pagamentos de Faturas", 610, 380, desktop, false);
		setFrameIcon(MasterImage.financial_16x16);
		
		try {
			invoicesRegistrationDAO = new InvoicesRegistrationDAO(CONNECTION);
			registrationDAO = new RegistrationDAO(CONNECTION);
			registrationModalityDAO = new RegistrationModalityDAO(CONNECTION);
			planDAO = new PlanDAO(CONNECTION);
			modalityDAO = new ModalityDAO(CONNECTION);
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

								// Calculos para recuperar o valor total da fatura.
								BigDecimal valorAux;
								float valorTotal = 0;
								List<String> descricaoDesconto = new ArrayList<>();
								for (int k = 0; k < modalitiesList.size(); k++) {
									int currentMonth = currentDate.getMonth();
									
									// Verifica se o aluno está matriculado na modadalide (dataFim não preenchida).
									if (modalitiesList.get(k).getFinishDate() == null) {
										PlanModel planModel = planDAO.findById(modalitiesList.get(k).getPlanId());
										valorAux = planModel.getMonthlyValue();
										valorTotal += valorAux.floatValue();
									} 
									// Caso não esteja, recupera o valor e monta a descriação do desconto.
									else if(modalitiesList.get(k).getFinishDate().getMonth() >= currentMonth){
										PlanModel planModel = planDAO.findById(modalitiesList.get(k).getPlanId());
										valorAux = planModel.getMonthlyValue();
										
										ModalityModel modalityModel = modalityDAO.findById(modalitiesList.get(k).getModalityId());
										String descricao = "- R$" + valorAux + " -> Cancelamento da modalidade " + modalityModel.getName();
										descricaoDesconto.add(descricao);
									}
								}

								for (int j = 0; j < invoicesRegistrationList.size(); j++) {
									InvoicesRegistrationModel invoicesRegistrationModel = invoicesRegistrationList.get(j);

									int currentMonth = currentDate.getMonth();
									int invoiceMonth = invoicesRegistrationModel.getDueDate().getMonth();

									// Caso o mês da fatura seja maior que o mês atual e o valor total seja
									// diferente do valor atual da fatura (modalidades alteradas), o valor da fatura
									// será destacado e alterado.
									if (invoiceMonth > currentMonth && invoicesRegistrationModel.getValue() > valorTotal
											&& invoicesRegistrationModel.getRegistrationCode() == registrationModel
													.getRegistrationCode()) {
										// Seta novo valor da fatura.
										invoicesRegistrationModel.setValue(valorTotal);
										
										// Seta o destaque da fatura para true.										
										invoicesRegistrationModel.setHighlightValue(true);
										
										// Seta a descrição do desconto da fatura.
										invoicesRegistrationModel.setDiscountDescription(descricaoDesconto);
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
		
		jTablePayments.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent me) {
				//Se clicou com o botão direito do mouse na linha
				if ((me.getClickCount() == 1) && (me.getButton() == MouseEvent.BUTTON3)) {
					InvoicesRegistrationModel model = ((PaymentsTableModel) jTablePayments.getModel()).getModel(jTablePayments.getSelectedRow());
					// Caso a coluna esteja destacada, abre o popupmenu com a opção detalhes habilitada.
					if(model.isHighlightValue()) {
						createPopupMenu(true).show(jTablePayments, me.getX(), me.getY());
					} else {
						createPopupMenu(false).show(jTablePayments, me.getX(), me.getY());
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
	private JPopupMenu createPopupMenu(boolean detailsEnable) {
		JPopupMenu jPopupMenu = new JPopupMenu();
		
		JMenuItem jMenuItemPayInvoice = new JMenuItem("Pagar Fatura");
		jMenuItemPayInvoice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				InvoicesRegistrationModel selectedModel = tableModel.getModel(jTablePayments.getSelectedRow());
				
				int selectedOption = JOptionPane.showConfirmDialog(null, "Deseja realizar o pagamento da fatura?");
				
				// Realiza o pagamento da fatura.
				if(selectedOption == 0) {
						if(selectedModel.getPaymentDate() == null && selectedModel.getCancellationDate() == null) {
						selectedModel.setPaymentDate(currentDate);					
						try {
							if(invoicesRegistrationDAO.update(selectedModel)) {
								bubbleSuccess("Fatura atualizada com sucesso");
							}
							
							// Atualiza a tabela.
							tableModel.fireTableDataChanged();
						} catch (SQLException error) {
							error.printStackTrace();
						}
					} else {
						bubbleError("A fatura selecionada já foi paga ou está cancelada");
					}
				}
			}
		});
		
		JMenuItem jMenuItemCancelInvoice = new JMenuItem("Cancelar Fatura");
		jMenuItemCancelInvoice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				InvoicesRegistrationModel selectedModel = tableModel.getModel(jTablePayments.getSelectedRow());
				
				int selectedOption = JOptionPane.showConfirmDialog(null, "Deseja cancelar a fatura?");
				
				// Realiza o cancelamento da fatura.
				if(selectedOption == 0) {
					if(selectedModel.getCancellationDate() == null) {
						selectedModel.setCancellationDate(currentDate);
						try {
							if(invoicesRegistrationDAO.update(selectedModel)) {
								bubbleSuccess("Fatura atualizada com sucesso");
							}
							
							// Atualiza a tabela.
							tableModel.fireTableDataChanged();
						} catch (SQLException error) {
							error.printStackTrace();
						}
					} else {
						bubbleError("A fatura selecionada já foi cancelada");
					}
				}
			}
		});
		
		JMenuItem jMenuItemReopenInvoice = new JMenuItem("Reabrir Fatura");
		jMenuItemReopenInvoice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				InvoicesRegistrationModel selectedModel = tableModel.getModel(jTablePayments.getSelectedRow());
				
				int selectedOption = JOptionPane.showConfirmDialog(null, "Deseja reabrir a fatura?");
				
				// Reabre a fatura.
				if(selectedOption == 0) {
					if(selectedModel.getCancellationDate() != null) {
						// Verifica se a matrícula correspondente a fatura não está encerrada.
						if(!selectedModel.isRegistrationFinished()) {
							selectedModel.setCancellationDate(null);
							try {
								if(invoicesRegistrationDAO.update(selectedModel)) {
									bubbleSuccess("Fatura atualizada com sucesso");
								}
								
								// Atualiza a tabela.
								tableModel.fireTableDataChanged();
							} catch (SQLException error) {
								error.printStackTrace();
							}
						} else {
							bubbleError("Não é possível reabrir a fatura de uma matrícula encerrada");
						}
					} else {
						bubbleError("A fatura selecionada já está aberta");
					}
				}
			}
		});
		
		JMenuItem jMenuItemUpdateValueInvoice = new JMenuItem("Desconto/Acréscimo");
		jMenuItemUpdateValueInvoice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				InvoicesRegistrationModel selectedModel = tableModel.getModel(jTablePayments.getSelectedRow());
				
				if(selectedModel.getCancellationDate() == null && selectedModel.getPaymentDate() == null) {										
					JDialog valueDialog = new JDialog();
					valueDialog.setTitle("Desconto/Acréscimo");
					valueDialog.setSize(325, 135);
					valueDialog.setVisible(true);
					valueDialog.setBackground(new Color(239, 239, 239));
					valueDialog.setLayout(null);
					valueDialog.setLocationRelativeTo(null);
					valueDialog.setResizable(false);
					
					JLabel label = new JLabel("Insira um novo valor para a fatura (Valor atual: "
						+ NumberFormat.getCurrencyInstance().format(selectedModel.getValue()) + ").");
					label.setBounds(10, 5, 350, 25);
					valueDialog.add(label);
					
					JNumberFormatField txfValue = new JNumberFormatField();
					txfValue.setBounds(10, 30, 290, 20);
					txfValue.setToolTipText("Informe o valor");
					valueDialog.add(txfValue);
					
					JButton btnOK = new JButton("Confirmar");
					btnOK.setBounds(10, 60, 100, 25);
					valueDialog.add(btnOK);
					
					btnOK.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							float newValue = txfValue.getValue().floatValue();
							selectedModel.setValue(newValue);
							
							try {
								if(invoicesRegistrationDAO.update(selectedModel)) {
									bubbleSuccess("Fatura atualizada com sucesso");
									valueDialog.dispose();
								}
								
								// Atualiza a tabela.
								tableModel.fireTableDataChanged();
							} catch (SQLException error) {
								error.printStackTrace();
							}
						}
					});
				} else {
					bubbleError("A fatura selecionada já foi paga ou está cancelada");
				}
								
			}
		}); 
		
		JMenuItem jMenuItemDetails = new JMenuItem("Detalhes");
		jMenuItemDetails.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				InvoicesRegistrationModel model = ((PaymentsTableModel) jTablePayments.getModel()).getModel(jTablePayments.getSelectedRow());
				
				String descricao = "";
            	for(int i = 0; i < model.getDiscountDescription().size(); i++){
            		descricao += model.getDiscountDescription().get(i) + "\n";
            	}
            	
                JOptionPane.showMessageDialog(null, descricao);
			}
		});
		jMenuItemDetails.setEnabled(detailsEnable);		
		
		jPopupMenu.add(jMenuItemPayInvoice);
		jPopupMenu.add(new JSeparator());
		jPopupMenu.add(jMenuItemCancelInvoice);
		jPopupMenu.add(new JSeparator());
		jPopupMenu.add(jMenuItemReopenInvoice);
		jPopupMenu.add(new JSeparator());
		jPopupMenu.add(jMenuItemUpdateValueInvoice);
		jPopupMenu.add(new JSeparator());
		jPopupMenu.add(jMenuItemDetails);
		
		return jPopupMenu;
	}
	
}
