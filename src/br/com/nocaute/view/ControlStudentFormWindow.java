package br.com.nocaute.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import java.sql.Timestamp;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;

import br.com.nocaute.dao.AssiduityDAO;
import br.com.nocaute.dao.InvoicesRegistrationDAO;
import br.com.nocaute.dao.RegistrationDAO;
import br.com.nocaute.dao.StudentDAO;
import br.com.nocaute.image.MasterImage;
import br.com.nocaute.model.AssiduityModel;
import br.com.nocaute.model.InvoicesRegistrationModel;
import br.com.nocaute.model.RegistrationModalityModel;
import br.com.nocaute.model.RegistrationModel;
import br.com.nocaute.model.StudentModel;
import br.com.nocaute.pojos.Graduation;
import br.com.nocaute.pojos.Modality;
import br.com.nocaute.pojos.Plan;
import br.com.nocaute.pojos.RegistrationModality;
import br.com.nocaute.util.JNumberFormatField;
import br.com.nocaute.util.MasterMonthChooser;
import br.com.nocaute.view.tableModel.AssiduityTableModel;
import br.com.nocaute.view.tableModel.PaymentsSituationTableModel;
import br.com.nocaute.view.tableModel.PaymentsSituationTableRenderer;
import br.com.nocaute.view.tableModel.StudentRegistrationModalitiesTableModel;

public class ControlStudentFormWindow extends AbstractGridWindow {
	private static final long serialVersionUID = -8041165617342297479L;

	// Componentes
	private JTextField txfCodMatriculate, txfStudent;
	private JPanel panelPhoto, panelSituation;
	private JLabel label;
	private MasterMonthChooser masterMonthChooser;
	private JButton btnDataStudent, btnDataMatriculate;

	// Grid Matrícula de alunos
	private JTable jTableRegistration;
	private StudentRegistrationModalitiesTableModel studentRegistrationModalitiesTableModel;

	// Grid Assiduidade
	private JTable jTableAttendance;
	private AssiduityTableModel assiduityTableModel;

	// Grid Situação de pagamento
	private JTable jTablePayments;
	private PaymentsSituationTableModel paymentsSituationTableModel;

	private StudentModel studentModel = new StudentModel();
	private RegistrationModel registrationModel = new RegistrationModel();
	private AssiduityModel assiduityModel = new AssiduityModel();

	private List<AssiduityModel> assiduityList = new ArrayList<AssiduityModel>();
	private List<InvoicesRegistrationModel> invoicesList;
	
	private StudentDAO studentDao = null;
	private RegistrationDAO registrationDao = null;
	private InvoicesRegistrationDAO invoicesDao = null;
	private AssiduityDAO assiduityDao = null;

	private static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	private static Rectangle screenRect = ge.getMaximumWindowBounds();
	private static int height = screenRect.height;// area total da altura tirando subtraindo o menu iniciar
	private static int width = screenRect.width;// area total da altura tirando subtraindo o menu iniciar

	private StudentFormWindow frameStudentForm;
	private StudentRegistrationWindow frameStudentRegistrationForm;

	private JDesktopPane desktop;

	private Date currentDate = new Date();

	public ControlStudentFormWindow(JDesktopPane desktop) {
		super("Controle de Alunos", width / 2 + 100, height - 150, desktop, false);

		this.desktop = desktop;

		try {
			studentDao = new StudentDAO(CONNECTION);
			registrationDao = new RegistrationDAO(CONNECTION);
			invoicesDao = new InvoicesRegistrationDAO(CONNECTION);
			assiduityDao = new AssiduityDAO(CONNECTION);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		setClosable(false);
		setIconifiable(true);
		setFrameIcon(MasterImage.control_16x16);

		setLocation((screenRect.width - this.getSize().width) / 2, (screenRect.height - this.getSize().height) / 2);

		createComponents();

		setSituationColor(0);
		// Seta as ações esperadas para cada botão
		setButtonsActions();
	}

	private void setButtonsActions() {
		txfCodMatriculate.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (!searchDataStudent(Integer.parseInt(txfCodMatriculate.getText()))) {
						bubbleWarning("Nenhum aluno foi encontrado!");
						txfStudent        .setText("");
					    txfCodMatriculate .setText("");
						
						clearTables();

						btnDataStudent     .setEnabled(false);
						btnDataMatriculate .setEnabled(false);

						setSituationColor(0);
					}
				}
			}
		});

		btnDataStudent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameStudentForm = new StudentFormWindow(desktop, studentModel);
				abrirFrame(frameStudentForm);
			}
		});

		btnDataMatriculate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frameStudentRegistrationForm = new StudentRegistrationWindow(desktop, registrationModel);
				abrirFrame(frameStudentRegistrationForm);
			}
		});

	}

	private void createComponents() {

		panelPhoto = new JPanel();
		panelPhoto.setBounds(10, 10, 185, 200);
		panelPhoto.setLayout(null);
		panelPhoto.setBorder(new LineBorder(Color.GRAY));
		panelPhoto.setToolTipText("Foto do Aluno");
		getContentPane().add(panelPhoto);

		txfCodMatriculate = new JTextField();
		txfCodMatriculate.setBounds(220, 10, 80, 25);
		txfCodMatriculate.setHorizontalAlignment(JTextField.RIGHT);
		txfCodMatriculate.setToolTipText("Código da matrícula do aluno");
		getContentPane().add(txfCodMatriculate);

		txfStudent = new JTextField();
		txfStudent.setBounds(310, 10, 448, 25);
		txfStudent.setEditable(false);
		txfStudent.setToolTipText("Aluno");
		getContentPane().add(txfStudent);

		createGridMatriculate();

		panelSituation = new JPanel();
		panelSituation.setBounds(220, 160, 538, 50);
		panelSituation.setLayout(null);
		panelSituation.setBorder(new LineBorder(Color.GRAY));
		getContentPane().add(panelSituation);

		label = new JLabel();
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		label.setBounds(0, 0, panelSituation.getWidth(), panelSituation.getHeight());
		label.setFont(new Font("Arial Black", Font.BOLD, 18));
		label.setForeground(Color.white);
		panelSituation.add(label);

		btnDataStudent = new JButton("Acessar dados do Aluno");
		btnDataStudent.setBounds(220, 220, 267, 30);
		btnDataStudent.setEnabled(false);
		btnDataStudent.setToolTipText("Acessar cadastro do aluno");
		getContentPane().add(btnDataStudent);

		btnDataMatriculate = new JButton("Acessar dados da Matrícula");
		btnDataMatriculate.setBounds(490, 220, 267, 30);
		btnDataMatriculate.setEnabled(false);
		btnDataMatriculate.setToolTipText("Acessar matrícula do aluno");
		getContentPane().add(btnDataMatriculate);

		masterMonthChooser = new MasterMonthChooser();
		masterMonthChooser.setBounds(10, 230, 185, 25);
		masterMonthChooser.setBorder(new LineBorder(Color.gray));
		getContentPane().add(masterMonthChooser);

		createGridAssiduity();

		createGridSituationPayments();
	}

	private void createGridMatriculate() {
		studentRegistrationModalitiesTableModel = new StudentRegistrationModalitiesTableModel();
		jTableRegistration = new JTable(studentRegistrationModalitiesTableModel);
		jTableRegistration.setDefaultRenderer(Object.class, renderer);

		// Habilita a seleção por linha
		jTableRegistration.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		grid = new JScrollPane(jTableRegistration);
		setLayout(null);
		resizeGrid(grid, 220, 50, 538, 100);
		grid.setVisible(true);

		add(grid);
	}

	private void createGridAssiduity() {
		assiduityTableModel = new AssiduityTableModel();
		jTableAttendance = new JTable(assiduityTableModel);
		jTableAttendance.setDefaultRenderer(Object.class, renderer);

		// Habilita a seleção por linha
		jTableAttendance.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		grid = new JScrollPane(jTableAttendance);
		setLayout(null);
		resizeGrid(grid, 10, 260, 185, 280);
		grid.setVisible(true);

		add(grid);
	}

	private List<RegistrationModality> mapRegistrationModalitiesModelToRegistrationModalitiesPojo(
			List<RegistrationModalityModel> registrationModalityList) {
		return registrationModalityList.stream().map(model -> {
			RegistrationModality pojo = new RegistrationModality();
			pojo.setId(model.getId());
			pojo.setRegistrationCode(model.getRegistrationCode());
			pojo.setModality(new Modality(model.getModalityId(), model.getModality().getName()));
			pojo.setGraduation(new Graduation(model.getGraduationId(), model.getGraduation().getName()));
			pojo.setPlan(new Plan(model.getPlanId(), model.getPlan().getName()));
			pojo.setStartDate(model.getStartDate());
			pojo.setFinishDate(model.getFinishDate());

			return pojo;
		}).collect(Collectors.toList());
	}

	@SuppressWarnings("deprecation")
	public boolean searchDataStudent(int code) {
		try {
			studentModel = studentDao.findById(code);
			if (studentModel instanceof StudentModel) {
				txfStudent.setText(studentModel.getName());

				clearTables();

				//Insere os dados da matricula na TableModel de matriculas.
				registrationModel = registrationDao.findByStudentId(studentModel.getCode(), true);
				studentRegistrationModalitiesTableModel.addModelsList(
						mapRegistrationModalitiesModelToRegistrationModalitiesPojo(registrationModel.getModalities()));

				
				//Insere os dados de pagamento do aluno na TableModel de pagamento.
				invoicesList = invoicesDao
						.getByRegistrationCode(registrationModel.getRegistrationCode());
				paymentsSituationTableModel
						.addModelsList(invoicesDao.getByRegistrationCode(registrationModel.getRegistrationCode()));

				//Inicia processo de assiduidade do aluno.
				assiduityModel.setRegistrationCode(registrationModel.getRegistrationCode());
				assiduityModel.setInputDate(getDateTime());

				assiduityModel = assiduityDao.insert(assiduityModel);
				assiduityList = assiduityDao.search(String.valueOf(assiduityModel.getRegistrationCode()));

				assiduityTableModel.addModelsList(assiduityList.stream()
						.filter(a -> a.getInputDate().getMonth() == masterMonthChooser.getDate().getMonth())
						.collect(Collectors.toList()));

				int situation = verificateSituation(invoicesList);
				setSituationColor(situation);

				btnDataStudent.setEnabled(true);
				btnDataMatriculate.setEnabled(true);

				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public int verificateSituation(List<InvoicesRegistrationModel> listModel) {
		int situation = 1;
		
		for (InvoicesRegistrationModel invoices : listModel) {
			if (invoices.getPaymentDate() != null && (invoices.getCancellationDate() == null
					|| invoices.getCancellationDate().toString().isEmpty())) {
				situation = 2;
			}else {
				situation = 1;
			}
		}

		return situation;
	}

	private void setSituationColor(int stateSituation) {
		switch (stateSituation) {
		case 0:
			label.setText("Aguardando Consulta...");
			panelSituation.setBackground(Color.LIGHT_GRAY);
			break;
		case 1:
			label.setText("Débitos Pendentes");
			panelSituation.setBackground(Color.RED);
			break;
		case 2:
			label.setText("Situação Regular");
			panelSituation.setBackground(Color.GREEN);
			break;
		default:
			bubbleError("Situação Inválida!");
		}
	}

	// HELPERS
	private void abrirFrame(AbstractWindowFrame frame) {
		boolean frameAlreadyExists = false;

		// Percorre todos os frames adicionados
		for (JInternalFrame addedFrame : desktop.getAllFrames()) {

			// Se o frame adiconado ja estiver
			if (addedFrame.getClass().toString().equalsIgnoreCase(frame.getClass().toString())) {
				// Remove janelas duplicadas
				addedFrame.moveToFront();
				frameAlreadyExists = true;
			}

		}

		try {
			if (!frameAlreadyExists) {
				desktop.add(frame);
				frame.moveToFront();
			}

			frame.setSelected(true);
			frame.setVisible(true);
		} catch (PropertyVetoException e) {
			JOptionPane.showMessageDialog(rootPane, "Houve um erro ao abrir a janela", "", JOptionPane.ERROR_MESSAGE,
					null);
		}
	}

	private Timestamp getDateTime() {
		long datahoraEmMillisegundos = new Date().getTime();
		Timestamp ts = new Timestamp(datahoraEmMillisegundos);
		return ts;
	}

	private void clearTables() {
		paymentsSituationTableModel.clear();
		studentRegistrationModalitiesTableModel.clear();
		assiduityTableModel.clear();
	}

	private void createGridSituationPayments() {
		paymentsSituationTableModel = new PaymentsSituationTableModel();
		jTablePayments = new JTable(paymentsSituationTableModel);
		//jTablePayments.setDefaultRenderer(Object.class, renderer);

		// Habilita a seleção por linha
		jTablePayments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTablePayments.setDefaultRenderer(Object.class, new PaymentsSituationTableRenderer());

		jTablePayments.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent me) {
				// Se clicou com o botão direito do mouse na linha
				if ((me.getClickCount() == 1) && (me.getButton() == MouseEvent.BUTTON3)) {
					InvoicesRegistrationModel model = ((PaymentsSituationTableModel) jTablePayments.getModel())
							.getModel(jTablePayments.getSelectedRow());
					// Caso a coluna esteja destacada, abre o popupmenu com a opção detalhes
					// habilitada.
					if (model.isHighlightValue()) {
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
		resizeGrid(grid, 220, 260, 538, 280);
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
				InvoicesRegistrationModel selectedModel = paymentsSituationTableModel
						.getModel(jTablePayments.getSelectedRow());

				int selectedOption = JOptionPane.showConfirmDialog(null, "Deseja realizar o pagamento da fatura?");

				// Realiza o pagamento da fatura.
				if (selectedOption == 0) {
					if (selectedModel.getPaymentDate() == null && selectedModel.getCancellationDate() == null) {
						selectedModel.setPaymentDate(currentDate);
						try {
							if (invoicesDao.update(selectedModel)) {
								bubbleSuccess("Fatura atualizada com sucesso");
								verificateSituation(invoicesList);
							}

							// Atualiza a tabela.
							paymentsSituationTableModel.fireTableDataChanged();
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
				InvoicesRegistrationModel selectedModel = paymentsSituationTableModel
						.getModel(jTablePayments.getSelectedRow());

				int selectedOption = JOptionPane.showConfirmDialog(null, "Deseja cancelar a fatura?");

				// Realiza o cancelamento da fatura.
				if (selectedOption == 0) {
					if (selectedModel.getCancellationDate() == null) {
						selectedModel.setCancellationDate(currentDate);
						try {
							if (invoicesDao.update(selectedModel)) {
								bubbleSuccess("Fatura atualizada com sucesso");
							}

							// Atualiza a tabela.
							paymentsSituationTableModel.fireTableDataChanged();
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
				InvoicesRegistrationModel selectedModel = paymentsSituationTableModel
						.getModel(jTablePayments.getSelectedRow());

				int selectedOption = JOptionPane.showConfirmDialog(null, "Deseja reabrir a fatura?");

				// Reabre a fatura.
				if (selectedOption == 0) {
					if (selectedModel.getCancellationDate() != null) {
						// Verifica se a matrícula correspondente a fatura não está encerrada.
						if (!selectedModel.isRegistrationFinished()) {
							selectedModel.setCancellationDate(null);
							try {
								if (invoicesDao.update(selectedModel)) {
									bubbleSuccess("Fatura atualizada com sucesso");
								}

								// Atualiza a tabela.
								paymentsSituationTableModel.fireTableDataChanged();
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
				InvoicesRegistrationModel selectedModel = paymentsSituationTableModel
						.getModel(jTablePayments.getSelectedRow());

				if (selectedModel.getCancellationDate() == null && selectedModel.getPaymentDate() == null) {
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
								if (invoicesDao.update(selectedModel)) {
									bubbleSuccess("Fatura atualizada com sucesso");
									valueDialog.dispose();
								}

								// Atualiza a tabela.
								paymentsSituationTableModel.fireTableDataChanged();
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
				InvoicesRegistrationModel model = ((PaymentsSituationTableModel) jTablePayments.getModel())
						.getModel(jTablePayments.getSelectedRow());

				//String descricao = "";
				//for (int i = 0; i < model.getDiscountDescription().size(); i++) {
				//	descricao += model.getDiscountDescription().get(i) + "\n";
				//}

				//JOptionPane.showMessageDialog(null, descricao);
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