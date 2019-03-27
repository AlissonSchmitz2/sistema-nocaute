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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import br.com.nocaute.view.tableModel.StudentRegistrationModalitiesTableModel;

import javax.swing.text.NumberFormatter;

import com.toedter.calendar.JDateChooser;

import br.com.nocaute.dao.RegistrationDAO;
import br.com.nocaute.model.RegistrationModalityModel;
import br.com.nocaute.model.RegistrationModel;
import br.com.nocaute.model.StudentModel;
import br.com.nocaute.pojos.Graduation;
import br.com.nocaute.pojos.Modality;
import br.com.nocaute.pojos.Plan;
import br.com.nocaute.pojos.RegistrationModality;

public class StudentRegistrationWindow extends AbstractGridWindow implements KeyEventPostProcessor {
	private static final long serialVersionUID = -4201960150625152379L;
	
	private RegistrationDAO registrationDao;
	private RegistrationModel model = new RegistrationModel();
	private ListRegistrationsWindow searchRegistrationWindow;
	private ListStudentsWindow searchStudentWindow;

	// Guarda os fields em uma lista para facilitar manipula��o em massa
	private List<Component> formFields = new ArrayList<Component>();

	// Componentes
	private JButton btnBuscar, btnAdicionar, btnRemover, btnSalvar, btnAddModalidade;
	private JLabel label;
	private JTextField txfMatricula, txfAlunoDescricao;
	private JFormattedTextField txfVencFatura;
	private JDateChooser jDataMatricula;
	private PlaceholderTextField txfAluno;

	private JTable jTableRegistration;
	StudentRegistrationModalitiesTableModel studentRegistrationModalitiesTableModel;
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

		// Por padr�o campos s�o desabilitados ao iniciar
		disableComponents(formFields);

		// Seta as a��es esperadas para cada bot�o
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
		// Abre tela sele��o cidade ao clicar F9
		if (ke.getID() == KeyEvent.KEY_PRESSED && ke.getKeyCode() == KeyEvent.VK_F9) {
			if (btnSalvar.isEnabled()) {
				openSearchStudentWindow();
			}

			return true;
		}

		return false;
	}

	private void setButtonsActions() {
		// A��o Adicionar
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
				
				//Limpa grid
				studentRegistrationModalitiesTableModel.clear();

				// Ativa bot�o salvar
				btnSalvar.setEnabled(true);

				// Desativa bot�o Remover
				btnRemover.setEnabled(false);
			}
		});
		
		// A��o Remover
		btnRemover.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (isEditing()) {
						boolean result = registrationDao.delete(model);

						if (result) {
							bubbleSuccess("Matr�cula exclu�da com sucesso");

							// Seta form para modo Cadastro
							setFormMode(CREATE_MODE);

							// Desativa campos
							disableComponents(formFields);

							// Limpar dados dos campos
							clearFormFields(formFields);

							// Cria nova entidade model
							model = new RegistrationModel();
							
							//Limpa grid
							studentRegistrationModalitiesTableModel.clear();

							// Desativa bot�o salvar
							btnSalvar.setEnabled(false);

							// Desativa bot�o remover
							btnRemover.setEnabled(false);
						} else {
							bubbleError("Houve um erro ao excluir matr�cula");
						}
					}
				} catch (SQLException error) {
					bubbleError(error.getMessage());
					error.printStackTrace();
				}
			}
		});

		// A��o Salvar
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!validateFields()) {
					return;
				}
				
				Date registrationDate = jDataMatricula.getDate();
				model.setRegistrationDate(registrationDate);
				model.setExpirationDay(Integer.parseInt(txfVencFatura.getText().isEmpty() ? "" : txfVencFatura.getText()));
				
				//Set modalidades ao model
				model.setModalities(mapRegistrationModalitiesPojoToRegistrationModalitiesModel(studentRegistrationModalitiesTableModel.getModelsList()));

				try {
					// EDI��O CADASTRO
					if (isEditing()) {
						boolean result = registrationDao.update(model);

						if (result) {
							bubbleSuccess("Matr�cula editada com sucesso");
						} else {
							bubbleError("Houve um erro ao editar matr�cula");
						}
					// NOVO CADASTRO
					} else {
						RegistrationModel insertedModel = registrationDao.insert(model);

						if (insertedModel != null) {
							bubbleSuccess("Aluno matriculado com sucesso");

							// Atribui o model rec�m criado ao model
							model = insertedModel;
							
							//Atualiza o campo c�digo da matr�cula
							txfMatricula.setText(model.getRegistrationCode().toString());

							// Seta form para edi��o
							setFormMode(UPDATE_MODE);

							// Ativa bot�o Remover
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
		
		// A��o Buscar
		btnBuscar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (searchRegistrationWindow == null) {
					searchRegistrationWindow = new ListRegistrationsWindow(desktop);

					searchRegistrationWindow.addInternalFrameListener(new InternalFrameListener() {
						@Override
						public void internalFrameClosed(InternalFrameEvent e) {
							RegistrationModel selectedModel = ((ListRegistrationsWindow) e.getInternalFrame())
									.getSelectedModel();
							try {
								if (selectedModel != null) {
									//Faz uma nova consulta para busca o model selecionado
									//para for�ar o carregamento do relacionamentos do model
									model = registrationDao.findById(selectedModel.getRegistrationCode(), true);
	
									//Seta dados do model para os campos
									txfMatricula.setText(model.getRegistrationCode().toString());
									txfVencFatura.setText(model.getExpirationDay().toString());
									
									if (model.getRegistrationDate() != null) {
										jDataMatricula.setDate(model.getRegistrationDate());
									}
									
									//Seta dados do aluno
									if (model.getStudent() != null) {
										txfAluno.setText(model.getStudent().getCode().toString());
										txfAlunoDescricao.setText(model.getStudent().getName());
									}
									
									//Seta dados na grid
									studentRegistrationModalitiesTableModel.clear();
									studentRegistrationModalitiesTableModel.addModelsList(
										mapRegistrationModalitiesModelToRegistrationModalitiesPojo(model.getModalities())
									);
	
									// Seta form para modo Edi��o
									setFormMode(UPDATE_MODE);
	
									// Ativa campos
									enableComponents(formFields);
	
									// Ativa bot�o salvar
									btnSalvar.setEnabled(true);
	
									// Ativa bot�o remover
									btnRemover.setEnabled(true);
								}
							} catch (SQLException error) {
								bubbleError("Erro ao recuperar matr�cula");
								error.printStackTrace();
							}
							
							// Reseta janela
							searchRegistrationWindow = null;
						}
					});
				}
			}
		});

		// A��o Adicionar Modalidade
		btnAddModalidade.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (studentRegistrationAddModalitiesWindow == null) {
					studentRegistrationAddModalitiesWindow = new StudentRegistrationAddModalitiesWindow(desktop);

					studentRegistrationAddModalitiesWindow.addInternalFrameListener(new InternalFrameListener() {
						@Override
						public void internalFrameClosed(InternalFrameEvent e) {
							RegistrationModality registrationModality = ((StudentRegistrationAddModalitiesWindow) e.getInternalFrame())
									.getSelectedRegistrationModality();

							if (registrationModality != null) {
								studentRegistrationModalitiesTableModel.addModel(registrationModality);
							}
							
							// Reseta janela
							studentRegistrationAddModalitiesWindow = null;
						}
					});
				}
			}
		});
	}
	
	private List<RegistrationModalityModel> mapRegistrationModalitiesPojoToRegistrationModalitiesModel(List<RegistrationModality> registrationModalityList) {
		 return registrationModalityList.stream()
				.map(pojo -> { 
					RegistrationModalityModel model = new RegistrationModalityModel();
					model.setRegistrationCode(pojo.getRegistrationCode());
					model.setModalityId(pojo.getModality().getId());
					model.setGraduationId(pojo.getGraduation().getId());
					model.setPlanId(pojo.getPlan().getId());
					model.setStartDate(pojo.getStartDate());
					model.setFinishDate(pojo.getFinishDate());
					
					return model;
				}).collect(Collectors.toList());
	}
	
	private List<RegistrationModality> mapRegistrationModalitiesModelToRegistrationModalitiesPojo(List<RegistrationModalityModel> registrationModalityList) {
		 return registrationModalityList.stream()
				.map(model -> { 
					RegistrationModality pojo = new RegistrationModality();
					pojo.setRegistrationCode(model.getRegistrationCode());
					pojo.setModality(new Modality(model.getModalityId(), model.getModality().getName()));
					pojo.setGraduation(new Graduation(model.getGraduationId(), model.getGraduation().getName()));
					pojo.setPlan(new Plan(model.getPlanId(), model.getPlan().getName()));
					pojo.setStartDate(model.getStartDate());
					pojo.setFinishDate(model.getFinishDate());
					
					return pojo;
				}).collect(Collectors.toList());
	}
	
	private boolean validateFields() {
		return true;
	}
	
	private void openSearchStudentWindow() {
		if (searchStudentWindow == null) {
			searchStudentWindow = new ListStudentsWindow(desktop);

			searchStudentWindow.addInternalFrameListener(new InternalFrameListener() {
				@Override
				public void internalFrameClosed(InternalFrameEvent e) {
					StudentModel selectedModel = ((ListStudentsWindow) e.getInternalFrame()).getSelectedModel();

					if (selectedModel != null) {
						// Atribui cidade para o model
						model.setStudent(selectedModel);
						model.setStudentCode(selectedModel.getCode());

						// Seta valores da cidade para o campo
						txfAluno.setText(selectedModel.getCode().toString());
						txfAlunoDescricao.setText(selectedModel.getName());
					}

					// Reseta janela
					searchStudentWindow = null;
				}
			});
		}
	}

	private void criarComponentes() {

		btnBuscar = new JButton("Buscar");
		btnBuscar.setBounds(15, 5, 95, 40);
		btnBuscar.setIcon(iconBuscar);
		btnBuscar.setToolTipText("Clique aqui para buscar os usu�rios");
		getContentPane().add(btnBuscar);

		btnAdicionar = new JButton("Adicionar");
		btnAdicionar.setBounds(110, 5, 110, 40);
		btnAdicionar.setIcon(iconAdicionar);
		btnAdicionar.setToolTipText("Clique aqui para adicionar um usu�rio");
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

		label = new JLabel("Matr�cula: ");
		label.setBounds(5, 55, 50, 25);
		getContentPane().add(label);

		txfMatricula = new JTextField();
		txfMatricula.setBounds(90, 55, 70, 20);
		txfMatricula.setEditable(false);
		txfMatricula.setFocusable(false);
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
				// Retira o foco do campo ap�s abrir a tela de busca
				jDataMatricula.requestFocusInWindow();

				openSearchStudentWindow();
			}

			@Override
			public void focusLost(FocusEvent e) {
			}
		});
		getContentPane().add(txfAluno);
		formFields.add(txfAluno);

		txfAlunoDescricao = new JTextField();
		txfAlunoDescricao.setBounds(165, 80, 258, 20);
		txfAlunoDescricao.setEditable(false);
		txfAlunoDescricao.setFocusable(false);
		txfAlunoDescricao.setToolTipText("Nome do aluno");
		getContentPane().add(txfAlunoDescricao);
		formFields.add(txfAlunoDescricao);

		label = new JLabel("Data Matr�cula: ");
		label.setBounds(5, 105, 150, 25);
		getContentPane().add(label);
		
		label = new JLabel("Dia do vencimento da fatura: ");
		label.setBounds(223, 105, 150, 25);
		getContentPane().add(label);

		try {
			jDataMatricula = new JDateChooser(new Date());
			jDataMatricula.setBounds(90, 105, 90, 20);
			jDataMatricula.setDateFormatString("dd/MM/yyyy");
			jDataMatricula.setToolTipText("Data da matr�cula");
			getContentPane().add(jDataMatricula);
			formFields.add(jDataMatricula);

			NumberFormat customFormat = NumberFormat.getIntegerInstance();
	        customFormat.setMinimumIntegerDigits(2);
	        
			txfVencFatura = new JFormattedTextField(new NumberFormatter(customFormat));
			txfVencFatura.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
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
		studentRegistrationModalitiesTableModel = new StudentRegistrationModalitiesTableModel();
		jTableRegistration = new JTable(studentRegistrationModalitiesTableModel);
		
		jTableRegistration.addMouseListener(new MouseAdapter() {
	        public void mouseClicked (MouseEvent me) {
	            if (me.getClickCount() == 2) {
	            	//Caso esteja em modo de edi��o, adiciona para remo��o.
					if (isEditing()) {
						
					}
	            	
	            	//Clique duplo na linha para remov�-la.     	
	            	studentRegistrationModalitiesTableModel.removeModel(jTableRegistration.getSelectedRow());
	            }
	        }
	    });

		// Habilita a sele��o por linha
		jTableRegistration.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		grid = new JScrollPane(jTableRegistration);
		setLayout(null);
		resizeGrid(grid, 5, 170, 420, 170);
		grid.setVisible(true);

		add(grid);
	}
}
