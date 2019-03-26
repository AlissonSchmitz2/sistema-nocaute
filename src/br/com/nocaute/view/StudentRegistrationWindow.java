package br.com.nocaute.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameEvent;
import br.com.nocaute.util.InternalFrameListener;
import br.com.nocaute.util.PlaceholderTextField;
import br.com.nocaute.view.tableModel.StudentRegistrationTableModel;

import javax.swing.text.MaskFormatter;

import br.com.nocaute.dao.RegistrationDAO;
import br.com.nocaute.model.RegistrationModel;

public class StudentRegistrationWindow extends AbstractGridWindow implements KeyEventPostProcessor {
	private static final long serialVersionUID = -4201960150625152379L;
	
	private RegistrationDAO registrationDao;
	private RegistrationModel model = new RegistrationModel();
	private ListModalitiesWindow searchRegistrationWindow;

	// Guarda os fields em uma lista para facilitar manipulação em massa
	private List<Component> formFields = new ArrayList<Component>();

	// Componentes
	private JButton btnBuscar, btnAdicionar, btnRemover, btnSalvar, btnAddModalidade;
	private JLabel label;
	private JTextField txfMatricula, txfAlunoDescricao;
	private JFormattedTextField txfDtMatricula, txfVencFatura;
	private PlaceholderTextField txfAluno;

	private JTable jTableRegistration;
	private StudentRegistrationAddModalitiesWindow studentRegistrationAddModalitiesWindow;
	
	private JDesktopPane desktop;

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
			this.getClass().getResource("/br/com/nocaute/image/16x16/estudante.png"));

	public StudentRegistrationWindow(JDesktopPane desktop) {
		super("Matricular Aluno", 450, 380, desktop);
		setFrameIcon(iconJanela);

		this.desktop = desktop;
		
		try {
			this.registrationDao = new RegistrationDAO(CONNECTION);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		criarComponentes();

		// Por padrão campos são desabilitados ao iniciar
		disableComponents(formFields);

		// Seta as ações esperadas para cada botão
		setButtonsActions();
		
		//Key events
		registerKeyEvent();
	}
	
	private void registerKeyEvent() {
		//Register key event post processor.
		StudentRegistrationWindow windowInstance = this;
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(windowInstance);
		
		//Unregister key event
		addInternalFrameListener(new InternalFrameListener() {
			@Override
			public void internalFrameClosed(InternalFrameEvent e) {
				KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventPostProcessor(windowInstance);
			}
		});
	}
	
	@Override
	public boolean postProcessKeyEvent(KeyEvent ke) {
		// Abre tela seleção cidade ao clicar F9
		if (ke.getID() == KeyEvent.KEY_PRESSED && ke.getKeyCode() == KeyEvent.VK_F9) {
			if (btnSalvar.isEnabled()) {
				//openSearchStudentWindow();
			}

			return true;
		}

		return false;
	}

	private void setButtonsActions() {
		// Ação Adicionar
		btnAdicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Seta form para modo Cadastro
				setFormMode(CREATE_MODE);

				// Ativa campos
				enableComponents(formFields);

				// Limpar dados dos campos
				clearFormFields(formFields);

				// Cria nova entidade model
				model = new RegistrationModel();

				// Ativa botão salvar
				btnSalvar.setEnabled(true);

				// Desativa botão Remover
				btnRemover.setEnabled(false);
			}
		});
		
		// Ação Remover
		btnRemover.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (isEditing()) {
						boolean result = registrationDao.delete(model);

						if (result) {
							bubbleSuccess("Matrícula excluída com sucesso");

							// Seta form para modo Cadastro
							setFormMode(CREATE_MODE);

							// Desativa campos
							disableComponents(formFields);

							// Limpar dados dos campos
							clearFormFields(formFields);

							// Cria nova entidade model
							model = new RegistrationModel();

							// Desativa botão salvar
							btnSalvar.setEnabled(false);

							// Desativa botão remover
							btnRemover.setEnabled(false);
						} else {
							bubbleError("Houve um erro ao excluir matrícula");
						}
					}
				} catch (SQLException error) {
					bubbleError(error.getMessage());
					error.printStackTrace();
				}
			}
		});

		// Ação Salvar
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!validateFields()) {
					return;
				}
				
				//TODO: set input field data to model

				try {
					// EDIÇÃO CADASTRO
					if (isEditing()) {
						boolean result = registrationDao.update(model);

						if (result) {
							bubbleSuccess("Matrícula editada com sucesso");
						} else {
							bubbleError("Houve um erro ao editar matrícula");
						}
					// NOVO CADASTRO
					} else {
						RegistrationModel insertedModel = registrationDao.insert(model);

						if (insertedModel != null) {
							bubbleSuccess("Aluno matriculado com sucesso");

							// Atribui o model recém criado ao model
							model = insertedModel;

							// Seta form para edição
							setFormMode(UPDATE_MODE);

							// Ativa botão Remover
							btnRemover.setEnabled(true);
						} else {
							bubbleError("Houve um erro ao matricular aluno");
						}
					}
				} catch (SQLException error) {
					bubbleError(error.getMessage());
					error.printStackTrace();
				}
			}
		});
		
		// Ação Buscar
		btnBuscar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (searchRegistrationWindow == null) {
					/*searchRegistrationWindow = new ListRegistrationsWindow(desktop);

					searchRegistrationWindow.addInternalFrameListener(new InternalFrameListener() {
						@Override
						public void internalFrameClosed(InternalFrameEvent e) {
							RegistrationModel selectedModel = ((ListRegistrationsWindow) e.getInternalFrame())
									.getSelectedModel();

							if (selectedModel != null) {
								// Atribui o model selecionado
								model = selectedModel;

								// Seta dados do model para os campos
								//TODO: seta dados do model para campos

								// Seta form para modo Edição
								setFormMode(UPDATE_MODE);

								// Ativa campos
								enableComponents(formFields);

								// Ativa botão salvar
								btnSalvar.setEnabled(true);

								// Ativa botão remover
								btnRemover.setEnabled(true);
							}

							// Reseta janela
							searchRegistrationWindow = null;
						}
					});*/
				}
			}
		});

		// Ação Adicionar Modalidade
		btnAddModalidade.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (studentRegistrationAddModalitiesWindow == null) {
					studentRegistrationAddModalitiesWindow = new StudentRegistrationAddModalitiesWindow(desktop);

					studentRegistrationAddModalitiesWindow.addInternalFrameListener(new InternalFrameListener() {
						@Override
						public void internalFrameClosed(InternalFrameEvent e) {
							
							// Reseta janela
							studentRegistrationAddModalitiesWindow = null;
						}

						@Override
						public void internalFrameOpened(InternalFrameEvent e) {
						}

						@Override
						public void internalFrameIconified(InternalFrameEvent e) {
						}

						@Override
						public void internalFrameDeiconified(InternalFrameEvent e) {
						}

						@Override
						public void internalFrameDeactivated(InternalFrameEvent e) {
						}

						@Override
						public void internalFrameClosing(InternalFrameEvent e) {
						}

						@Override
						public void internalFrameActivated(InternalFrameEvent e) {
						}
					});
				}
			}
		});
	}
	
	private boolean validateFields() {
		return true;
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
		btnRemover.setEnabled(false);

		btnSalvar = new JButton("Salvar");
		btnSalvar.setBounds(330, 5, 95, 40);
		btnSalvar.setIcon(iconSalvar);
		btnSalvar.setToolTipText("Clique aqui para salvar");
		getContentPane().add(btnSalvar);
		btnSalvar.setEnabled(false);

		label = new JLabel("Matrícula: ");
		label.setBounds(5, 55, 50, 25);
		getContentPane().add(label);

		txfMatricula = new JTextField();
		txfMatricula.setBounds(90, 55, 70, 20);
		getContentPane().add(txfMatricula);
		formFields.add(txfMatricula);

		label = new JLabel("Aluno: ");
		label.setBounds(5, 80, 150, 25);
		getContentPane().add(label);

		txfAluno = new PlaceholderTextField();
		txfAluno.setBounds(90, 80, 70, 20);
		txfAluno.setBackground(Color.yellow);
		txfAluno.setToolTipText("Tecle F9 para selecionar um aluno");
		txfAluno.setEditable(false);
		txfAluno.setPlaceholder("Teclar F9");
		txfAluno.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				// Retira o foco do campo após abrir a tela de busca
				txfAlunoDescricao.requestFocusInWindow();

				//openSearchStudentWindow();
			}

			@Override
			public void focusLost(FocusEvent e) {
			}
		});
		getContentPane().add(txfAluno);
		formFields.add(txfAluno);

		txfAlunoDescricao = new JTextField();
		txfAlunoDescricao.setBounds(165, 80, 258, 20);
		txfAlunoDescricao.setEnabled(false);
		txfAlunoDescricao.setToolTipText("Nome do aluno");
		getContentPane().add(txfAlunoDescricao);
		formFields.add(txfAlunoDescricao);

		label = new JLabel("Data Matrícula: ");
		label.setBounds(5, 105, 150, 25);
		getContentPane().add(label);
		
		/*
		 * JDateChooser jDateChooser = new JDateChooser(new Date());
		 * jDateChooser.setBounds(90, 105, 90, 20);
		 * jDateChooser.setDateFormatString("dd/MM/yyyy");
		 * getContentPane().add(jDateChooser);
		 * jDateChooser.addPropertyChangeListener(new PropertyChangeListener() {
		 * 
		 * @Override public void propertyChange(PropertyChangeEvent evt) { // Recuperar
		 * data do campo. Esse método é chamado // na primeira vez que executa o sistema
		 * //SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy"); //String
		 * novaData = formatador.format(jDateChooser.getDate());
		 * //System.out.println(novaData); } });
		 * 
		 */
		label = new JLabel("Dia do vencimento da fatura: ");
		label.setBounds(223, 105, 150, 25);
		getContentPane().add(label);

		try {
			txfDtMatricula = new JFormattedTextField(new MaskFormatter("##/##/####"));
			txfDtMatricula.setFocusLostBehavior(JFormattedTextField.COMMIT);
			txfDtMatricula.setBounds(90, 105, 90, 20);
			txfDtMatricula.setToolTipText("Data da matrícula");
			getContentPane().add(txfDtMatricula);
			formFields.add(txfDtMatricula);

			txfVencFatura = new JFormattedTextField(new MaskFormatter("#######"));
			txfVencFatura.setFocusLostBehavior(JFormattedTextField.COMMIT);
			txfVencFatura.setBounds(373, 105, 50, 20);
			getContentPane().add(txfVencFatura);
			formFields.add(txfVencFatura);
		} catch (Exception e) {
			e.printStackTrace();
		}

		btnAddModalidade = new JButton("Adicionar Modalidade");
		btnAddModalidade.setBounds(5, 140, 150, 23);
		btnAddModalidade.setToolTipText("Clique aqui para adicionar uma modalidade");
		getContentPane().add(btnAddModalidade);
		formFields.add(btnAddModalidade);

		createGrid();
	}

	private void createGrid() {
		StudentRegistrationTableModel tableModel = new StudentRegistrationTableModel();
		jTableRegistration = new JTable(tableModel);

		// Habilita a seleção por linha
		jTableRegistration.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		grid = new JScrollPane(jTableRegistration);
		setLayout(null);
		resizeGrid(grid, 5, 170, 420, 170);
		grid.setVisible(true);

		add(grid);
	}
}
