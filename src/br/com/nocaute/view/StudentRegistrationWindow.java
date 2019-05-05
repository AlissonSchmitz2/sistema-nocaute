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
import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

import br.com.nocaute.dao.InvoicesRegistrationDAO;
import br.com.nocaute.dao.RegistrationDAO;
import br.com.nocaute.image.MasterImage;
import br.com.nocaute.model.InvoicesRegistrationModel;
import br.com.nocaute.model.RegistrationModalityModel;
import br.com.nocaute.model.RegistrationModel;
import br.com.nocaute.model.StudentModel;
import br.com.nocaute.pojos.Graduation;
import br.com.nocaute.pojos.Modality;
import br.com.nocaute.pojos.Plan;
import br.com.nocaute.pojos.RegistrationModality;

public class StudentRegistrationWindow extends AbstractToolbar implements KeyEventPostProcessor {
	private static final long serialVersionUID = -6790083507948009923L;
	
	private RegistrationDAO registrationDao;
	private RegistrationModel model = new RegistrationModel();
	private ListRegistrationsWindow searchRegistrationWindow;
	private ListStudentsWindow searchStudentWindow;
	
	private InvoicesRegistrationDAO invoicesRegistrationDAO;

	// Guarda os fields em uma lista para facilitar manipulação em massa
	private List<Component> formFields = new ArrayList<Component>();

	// Componentes
	private JButton btnAddModalidade, btnUnlockRegistration;
	private JLabel label;
	private JLabel labelCloseRegistration = new JLabel();
	private JTextField txfMatricula, txfAlunoDescricao;
	private JFormattedTextField txfVencFatura;
	private JDateChooser jDataMatricula;
	private PlaceholderTextField txfAluno;

	private JTable jTableRegistration;
	private StudentRegistrationModalitiesTableModel studentRegistrationModalitiesTableModel;
	private StudentRegistrationAddModalitiesWindow studentRegistrationAddModalitiesWindow;
	
	private JDesktopPane desktop;
	private boolean isOnlyView = false;
	private Connection CONNECTION;
	
	public StudentRegistrationWindow(JDesktopPane desktop, RegistrationModel model) {
		super("Matricular Aluno", 450, 380, desktop, false);
		
		setFrameIcon(MasterImage.student_16x16);

		this.desktop    = desktop;
		this.isOnlyView = true;
		
		createComponents();
		
		btnAdicionar .setEnabled(false);
		btnBuscar    .setEnabled(false);
	
		assignModelToForm(model);
		
		btnRemover   .setEnabled(false);
	    btnSalvar    .setEnabled(false);
		
	    disableComponents(formFields); 
	}
	
	public StudentRegistrationWindow(JDesktopPane desktop, Connection CONNECTION) {
		super("Matricular Aluno", 450, 380, desktop, false);
		setFrameIcon(MasterImage.student_16x16);

		this.desktop = desktop;
		this.CONNECTION = CONNECTION;
		
		try {
			this.registrationDao = new RegistrationDAO(CONNECTION);
			this.invoicesRegistrationDAO = new InvoicesRegistrationDAO(CONNECTION);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		createComponents();

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
			if (btnSalvar.isEnabled() && txfAluno.isEnabled()) {
				openSearchStudentWindow();
			}

			return true;
		}

		return false;
	}

	protected void setButtonsActions() {
		// Ação Adicionar
		btnAdicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Seta form para modo Cadastro
				setFormMode(CREATE_MODE);

				// Ativa campos
				enableComponents(formFields);

				// Limpar dados dos campos
				clearFormFields(formFields);

				// Remove a mensagem de encerramento de matrícula.
				getContentPane().remove(labelCloseRegistration);
				getContentPane().repaint();
				
				// Cria nova entidade model
				model = new RegistrationModel();
				
				//Limpa grid
				studentRegistrationModalitiesTableModel.clear();
				
				// Esconde botão reativar matrícula
				btnUnlockRegistration.setVisible(false);

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
							
							// Esconde botão reativar matrícula
							btnUnlockRegistration.setVisible(false);

							// Limpar dados dos campos
							clearFormFields(formFields);

							// Cria nova entidade model
							model = new RegistrationModel();
							
							//Limpa grid
							studentRegistrationModalitiesTableModel.clear();

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
				
				Date registrationDate = jDataMatricula.getDate();
				model.setRegistrationDate(registrationDate);
				model.setExpirationDay(Integer.parseInt(txfVencFatura.getText().isEmpty() ? "" : txfVencFatura.getText()));
				
				//Set modalidades ao model
				model.setModalities(mapRegistrationModalitiesPojoToRegistrationModalitiesModel(studentRegistrationModalitiesTableModel.getModelsList()));

				try {
					// EDIÇÃO CADASTRO
					if (isEditing()) {			
						//Verificar se todas as modalidades foram encerradas.
						List<RegistrationModalityModel> modalitiesList = model.getModalities();
						boolean modalitiesFinished = isAllModalitiesFinished(modalitiesList);
						
						// Caso todas as modalidades tenham sido encerradas, solicita uma confirmação do
						// usuário para cancelamento da matrícula.
						if(modalitiesFinished) {
							int optionSelected = JOptionPane.showConfirmDialog(null, "Todas as modalidades foram encerradas.\nDeseja realmente encerrar a matrícula?");
							
							// 0 -> Sim.
							if(optionSelected == 0) {
								Date finishDate = modalitiesList.stream()
										.filter(obj -> obj.getFinishDate() != null)
										.sorted((p1, p2) -> p2.getFinishDate().compareTo(p1.getFinishDate()))
										.findFirst()
										.map(obj -> obj.getFinishDate())
										.get();
								
								//Seta data de fechamento
								model.setClosingDate(finishDate);

								boolean result = registrationDao.update(model);

								if (result) {
									bubbleSuccess("Matrícula encerrada com sucesso");
									cancelInvoices(model);
									closeFormForClosedRegistration(finishDate);
								} else {
									bubbleError("Houve um erro ao editar matrícula");
								}
							}
						} 
						// Caso não tenham sido encerradas todas as modalidades, procede com o update normalmente.
						else {
							boolean result = registrationDao.update(model);
	
							if (result) {
								bubbleSuccess("Matrícula editada com sucesso");
							} else {
								bubbleError("Houve um erro ao editar matrícula");
							}
						}
					// NOVO CADASTRO
					} else {
						//Antes de salvar, faz uma consulta para verificar se o usuário já foi matriculado
						RegistrationModel existentRegistration = registrationDao.findByStudentId(model.getStudentCode(), false);
						if (existentRegistration != null) {
							bubbleError("Já existe uma matrícula para o aluno selecionado");
							
							return;
						}
						
						RegistrationModel insertedModel = registrationDao.insert(model);

						if (insertedModel != null) {
							bubbleSuccess("Aluno matriculado com sucesso");

							// Atribui o model recém criado ao model
							model = insertedModel;
							
							//Atualiza o campo código da matrícula
							txfMatricula.setText(model.getRegistrationCode().toString());

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
					searchRegistrationWindow = new ListRegistrationsWindow(desktop, CONNECTION);

					searchRegistrationWindow.addInternalFrameListener(new InternalFrameListener() {
						@Override
						public void internalFrameClosed(InternalFrameEvent e) {
							RegistrationModel selectedModel = ((ListRegistrationsWindow) e.getInternalFrame())
									.getSelectedModel();
							try {
								if (selectedModel != null) {
									//Faz uma nova consulta para busca o model selecionado
									//para forçar o carregamento do relacionamentos do model
									model = registrationDao.findById(selectedModel.getRegistrationCode(), true);
									
									assignModelToForm(model);
								}
							} catch (SQLException error) {
								bubbleError("Erro ao recuperar matrícula");
								error.printStackTrace();
							}
							
							// Reseta janela
							searchRegistrationWindow = null;
						}
					});
				}
			}
		});

		// Ação Adicionar Modalidade
		btnAddModalidade.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (studentRegistrationAddModalitiesWindow == null) {
					studentRegistrationAddModalitiesWindow = new StudentRegistrationAddModalitiesWindow(desktop, CONNECTION);

					studentRegistrationAddModalitiesWindow.addInternalFrameListener(new InternalFrameListener() {
						@Override
						public void internalFrameClosed(InternalFrameEvent e) {
							RegistrationModality registrationModality = ((StudentRegistrationAddModalitiesWindow) e.getInternalFrame())
									.getSelectedRegistrationModality();

							if (registrationModality != null) {
								//Se estiver em modo edição, insere o código da matrícula
								if (isEditing()) {
									registrationModality.setRegistrationCode(model.getRegistrationCode());
								}
								studentRegistrationModalitiesTableModel.addModel(registrationModality);
							
							}
							
							// Reseta janela
							studentRegistrationAddModalitiesWindow = null;
						}
					});
				}
			}
		});
		
		// Ação Remover
		btnUnlockRegistration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//Set modalidades ao model
					model.setModalities(mapRegistrationModalitiesPojoToRegistrationModalitiesModel(studentRegistrationModalitiesTableModel.getModelsList()));
					
					boolean modalitiesFinished = isAllModalitiesFinished(model.getModalities());
					
					if (modalitiesFinished) {
						bubbleWarning("É preciso conter ao menos uma modalidade sem data fim para reativar.");
						
						return;
					}
					
					//Remove data de encerramento
					model.setClosingDate(null);
					boolean result = registrationDao.update(model);
					
					if (result) {
						//Esconde botão reativar matrícula
						btnUnlockRegistration.setVisible(false);
						
						//Faz uma nova consulta para busca o model selecionado
						//para forçar o carregamento do relacionamentos do model
						model = registrationDao.findById(model.getRegistrationCode(), true);
						
						assignModelToForm(model);
						
						bubbleSuccess("Matrícula reativada com sucesso");
					} else {
						bubbleError("Houve um erro ao reativar matrícula");
					}
				} catch (SQLException error) {
					bubbleError(error.getMessage());
					error.printStackTrace();
				}
			}
		});
	}
	
	public void assignModelToForm(RegistrationModel model) {
		// Remove a mensagem de encerramento de matrícula.
		getContentPane().remove(labelCloseRegistration);
		getContentPane().repaint();
		
		//Remover botão reativar matrícula
		btnUnlockRegistration.setVisible(false);
		
		//Verificar se todas as modalidades foram encerradas.
		boolean modalitiesFinished = isAllModalitiesFinished(model.getModalities());							
		
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
		
		// Seta form para modo Edição
		setFormMode(UPDATE_MODE);

		if(modalitiesFinished) {
			closeFormForClosedRegistration(model.getClosingDate());
		} else {

			// Ativa campos
			enableComponents(formFields);
			
			// Desabilita campo aluno, pois não é permitido alterar aluno da matrícula
			txfAlunoDescricao.setEnabled(false);
			txfAluno.setEnabled(false);

			// Ativa botão salvar
			btnSalvar.setEnabled(true);

			// Ativa botão remover
			btnRemover.setEnabled(true);
		}								
	}
	
	private boolean isAllModalitiesFinished(List<RegistrationModalityModel> modalitiesList) {
		return modalitiesList.stream()
				.filter(obj -> obj.getFinishDate() != null)
				.count() == modalitiesList.size();
	}
	
	private List<RegistrationModalityModel> mapRegistrationModalitiesPojoToRegistrationModalitiesModel(List<RegistrationModality> registrationModalityList) {
		 return registrationModalityList.stream()
				.map(pojo -> { 
					RegistrationModalityModel model = new RegistrationModalityModel();
					model.setId(pojo.getId());
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
	
	private void openSearchStudentWindow() {
		if (searchStudentWindow == null) {
			searchStudentWindow = new ListStudentsWindow(desktop, CONNECTION);

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

	private void createComponents() {

		label = new JLabel("Matrícula: ");
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
				// Retira o foco do campo após abrir a tela de busca
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

		label = new JLabel("Data Matrícula: ");
		label.setBounds(5, 105, 150, 25);
		getContentPane().add(label);
		
		label = new JLabel("Dia do vencimento da fatura: ");
		label.setBounds(223, 105, 150, 25);
		getContentPane().add(label);

		try {
			jDataMatricula = new JDateChooser(new Date());
			jDataMatricula.setBounds(90, 105, 90, 20);
			jDataMatricula.setDateFormatString("dd/MM/yyyy");
			jDataMatricula.setToolTipText("Data da matrícula");
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

		btnAddModalidade = new JButton("Adicionar Modalidade", MasterImage.new_alternative_16x16);
		btnAddModalidade.setBounds(5, 140, 155, 23);
		btnAddModalidade.setToolTipText("Clique aqui para adicionar uma modalidade");
		getContentPane().add(btnAddModalidade);
		formFields.add(btnAddModalidade);
		
		btnUnlockRegistration = new JButton("Reativar Matrícula", MasterImage.unlock_16x16);
		btnUnlockRegistration.setBounds(273, 140, 150, 23);
		btnUnlockRegistration.setToolTipText("Clique aqui para reativar a matrícula");
		getContentPane().add(btnUnlockRegistration);
		btnUnlockRegistration.setVisible(false);

		createGrid();
	}

	private void createGrid() {
		studentRegistrationModalitiesTableModel = new StudentRegistrationModalitiesTableModel();
		jTableRegistration = new JTable(studentRegistrationModalitiesTableModel);
		
		//Add layout na grid
		jTableRegistration.setDefaultRenderer(Object.class, renderer);
		jTableRegistration.addMouseListener(new MouseAdapter() {
	        public void mouseClicked (MouseEvent me) {
	            if (me.getClickCount() == 2 && !isOnlyView) {
	            	if (studentRegistrationAddModalitiesWindow == null) {
	            		int selectedRow = jTableRegistration.getSelectedRow();
	            		
	            		RegistrationModality selectedModel = studentRegistrationModalitiesTableModel.getModel(selectedRow);
						studentRegistrationAddModalitiesWindow = new StudentRegistrationAddModalitiesWindow(desktop, selectedModel, CONNECTION);

						studentRegistrationAddModalitiesWindow.addInternalFrameListener(new InternalFrameListener() {
							@Override
							public void internalFrameClosed(InternalFrameEvent e) {
								RegistrationModality registrationModality = ((StudentRegistrationAddModalitiesWindow) e.getInternalFrame())
										.getSelectedRegistrationModality();

								if (registrationModality != null) {
									if (registrationModality.isDeleted()) {
										studentRegistrationModalitiesTableModel.removeModel(selectedRow);
									} else {
										//Se estiver em modo edição, insere o código da matrícula
										if (isEditing()) {
											registrationModality.setRegistrationCode(model.getRegistrationCode());
										}
										
										//selectedRow
										studentRegistrationModalitiesTableModel.addModel(registrationModality, selectedRow);	
									}
								}

								// Reseta janela
								studentRegistrationAddModalitiesWindow = null;
							}
						});
					}
	            }
	        }
	    });

		// Habilita a seleção por linha
		jTableRegistration.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		grid = new JScrollPane(jTableRegistration);
		setLayout(null);
		resizeGrid(grid, 5, 170, 420, 170);
		grid.setVisible(true);

		add(grid);
	}
	
	private boolean validateFields() {
		if(txfAluno.getText().isEmpty() || txfAluno.getText() == null) {
			bubbleWarning("Informe o aluno para realizar a matrícula!");
			return false;
		}
		
		if(jDataMatricula.getDate() == null) {
			bubbleWarning("Data da matrícula inválida!");
			return false;
		}
		
		if(txfVencFatura.getText().isEmpty() || txfVencFatura.getText() == null) {
			bubbleWarning("Dia do vencimento inválido!");
			return false;
		}
		
		if(jTableRegistration.getRowCount() == 0 ) {
			bubbleWarning("Informe ao menos uma modalidade para realizar a matrícula!");
			return false;
		}
		
		return true;
	}
	
	// Desabilita os campos.
	private void closeFormForClosedRegistration(Date finishDate) {
		if (finishDate != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");		
			labelCloseRegistration.setText("Matrícula encerrada em: " + dateFormat.format(finishDate));
			labelCloseRegistration.setBounds(170, 55, 200, 25);	
			labelCloseRegistration.setForeground(Color.RED);
			getContentPane().add(labelCloseRegistration);
			getContentPane().repaint();
		}

		disableComponents(formFields);
		btnRemover.setEnabled(false);
		btnSalvar.setEnabled(false);
		
		//Habilita o campo para permitir reativação da matrícula
		btnUnlockRegistration.setVisible(true);
		
		//Ativa o botão para adicionar modalidades
		btnAddModalidade.setEnabled(true);
	}

	// Cancela as faturas futuras relacionadas a matrícula cancelada.
	@SuppressWarnings("deprecation")
	private void cancelInvoices(RegistrationModel model) {
		try {
			List<InvoicesRegistrationModel> invoicesList = invoicesRegistrationDAO.getByRegistrationCode(model.getRegistrationCode());
			Date currentDate = new Date();
			int currentMonth = currentDate.getMonth();
			
			for(int i = 0; i < invoicesList.size(); i++) {
				int invoiceMonth = invoicesList.get(i).getDueDate().getMonth();
				if(invoiceMonth > currentMonth) {
					invoicesList.get(i).setCancellationDate(currentDate);
					invoicesRegistrationDAO.update(invoicesList.get(i));
				}
			}			
		} catch (SQLException error) {
			bubbleError("Houve um erro ao cancelar faturas");
			error.printStackTrace();
		}
	}
}
