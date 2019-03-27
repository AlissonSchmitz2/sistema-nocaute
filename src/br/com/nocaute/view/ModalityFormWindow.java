package br.com.nocaute.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.TableCellRenderer;

import br.com.nocaute.dao.GraduationDAO;
import br.com.nocaute.dao.ModalityDAO;
import br.com.nocaute.model.GraduationModel;
import br.com.nocaute.model.ModalityModel;
import br.com.nocaute.util.InternalFrameListener;
import br.com.nocaute.view.tableModel.GraduationsTableModel;

public class ModalityFormWindow extends AbstractGridWindow{
	private static final long serialVersionUID = 7836235315411293572L;
	
	private ModalityDAO modalityDAO;
	private ModalityModel modalityModel = new ModalityModel();
	private GraduationDAO graduationDAO;
	private List<GraduationModel> graduationList = new ArrayList<>();
	private List<GraduationModel> graduationDeleteList = new ArrayList<>();
	private List<GraduationModel> graduationAddList = new ArrayList<>();
	private ListModalitiesWindow searchModalityWindow;
	
	// Guarda os fields em uma lista para facilitar manipula��o em massa
	private List<Component> formFields = new ArrayList<Component>();
	
	// Componentes
	private JButton btnBuscar, btnAdicionar, btnRemover, btnSalvar, btnOk;
	private JLabel label;
	private JTextField txfModalidade, txfGraduacao;
	
	private GraduationsTableModel tableModel;
	private JTable jTableGraduacoes;
	private TableCellRenderer renderer = new EvenOddRenderer();

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
	private ImageIcon iconJanela = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/16x16/estudante.png"));
	
	private JDesktopPane desktop;
	
	public ModalityFormWindow(JDesktopPane desktop) {
			super("Modalidades e Gradua��es", 450, 335, desktop);
			setFrameIcon(iconJanela);
			
			this.desktop = desktop;
			
			try {
				modalityDAO = new ModalityDAO(CONNECTION);
				graduationDAO = new GraduationDAO(CONNECTION);
			} catch (SQLException error) {
				error.printStackTrace();
			}
			
			createComponents();
			
			// Por padr�o campos s�o desabilitados ao iniciar
			disableComponents(formFields);
			
			// Seta as a��es esperadas para cada bot�o
			setButtonsActions();
		}
	
	private void setButtonsActions() {
		//A��o Adicionar
		btnAdicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Seta form para modo Cadastro
				setFormMode(CREATE_MODE);
				
				//Ativa campos
				enableComponents(formFields);
				
				//Limpar dados dos campos
				clearFormFields(formFields);
				
				//Limpar dados da Grid
				tableModel.clear();
				
				//Cria nova entidade model
				modalityModel = new ModalityModel();
				graduationList = new ArrayList<>();
				
				//Ativa bot�o salvar
				btnSalvar.setEnabled(true);
				
				//Desativa bot�o Remover
				btnRemover.setEnabled(false);
			}
		});
		
		//A��o Remover
		btnRemover.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (isEditing()) {
						
						//Remove todas as gradua��es relacionadas a modalidade.
						for(int i = 0; i < graduationList.size(); i++) {
							graduationDAO.delete(graduationList.get(i));
						}
						
						//Remove a modalidade.
						boolean result = modalityDAO.delete(modalityModel);
						
						if (result) {
							bubbleSuccess("Modalidade exclu�da com sucesso");
							
							//Seta form para modo Cadastro
							setFormMode(CREATE_MODE);
							
							//Desativa campos
							disableComponents(formFields);
							
							//Limpar dados dos campos
							clearFormFields(formFields);
							
							//Cria nova entidade model
							modalityModel = new ModalityModel();
							graduationList.clear();
							
							//Limpa a Grid.
							tableModel.clear();
							
							//Desativa bot�o salvar
							btnSalvar.setEnabled(false);
							
							//Desativa bot�o remover
							btnRemover.setEnabled(false);
						} else {
							bubbleError("Houve um erro ao excluir a modalidade");
						}
					}
				} catch (SQLException error) {
					bubbleError(error.getMessage());
					error.printStackTrace();
				}
			}
		});
		
		//A��o Salvar
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO: Verificar modalidades e gradua��es duplicadas.
				if(validateFields()) {
					return;
				}
				
				modalityModel.setName(txfModalidade.getText());
				
				try {
					//EDI��O CADASTRO
					if (isEditing()) {
						
						boolean result = modalityDAO.update(modalityModel);
						
						//Recupera o ID das gradua��es que foram removidas.
						if (!graduationDeleteList.isEmpty()) {
							for (int i = 0; i < graduationList.size(); i++) {
								for (int j = 0; j < graduationDeleteList.size(); j++) {
									if (graduationDeleteList.get(j).getName()
											.equals(graduationList.get(i).getName())) {
										graduationDeleteList.get(j)
												.setGraduationId(graduationList.get(i).getGraduationId());
										
										//Deleta a gradua��o.
										graduationDAO.delete(graduationDeleteList.get(j));
										graduationList.remove(i);
									}
								}
							}							
							graduationDeleteList.clear();
						}
						
						//Caso tenha sido inserida outra gradua��o na Grid, adiciona ela ao banco de dados.
						if(!graduationAddList.isEmpty()) {
							for(int i = 0; i < graduationAddList.size(); i++) {
								GraduationModel graduationModel = graduationAddList.get(i);
								
								//Recupera o ID da modalidade cadastrada e insere a(s) gradua��o(�es) no banco de dados.
								graduationModel.setModalityId(modalityModel.getModalityId());
								GraduationModel graduationInsertedModel = graduationDAO.insert(graduationModel);
								
								//Atualiza a lista com os models rec�m criados.
								graduationList.add(graduationInsertedModel);								
							}							
							graduationAddList.clear();
						}
						
						if (result) {
							bubbleSuccess("Modalidade editada com sucesso");
						} else {
							bubbleError("Houve um erro ao editar a modalidade");
						}
					//NOVO CADASTRO
					} else {
						//Insere a modalidade no banco de dados.
						ModalityModel modalityInsertedModel = modalityDAO.insert(modalityModel);
						
						if (modalityInsertedModel != null) {
							//Recupera a lista de gradua��es.							
							List<GraduationModel> graduationListAux = tableModel.getModelsList();
							
							for(int i = 0; i < graduationListAux.size(); i++) {
								GraduationModel graduationModel = graduationListAux.get(i);
								
								//Recupera o ID da modalidade cadastrada e insere a(s) gradua��o(�es) no banco de dados.
								graduationModel.setModalityId(modalityInsertedModel.getModalityId());
								GraduationModel graduationInsertedModel = graduationDAO.insert(graduationModel);
								
								//Atualiza a lista com os models rec�m criados.
								graduationList.add(graduationInsertedModel);								
							}	
							
							bubbleSuccess("Modalidade cadastrada com sucesso");
							
							//Atribui o model rec�m criado ao model
							modalityModel = modalityInsertedModel;
							
							//Seta form para edi��o
							setFormMode(UPDATE_MODE);
							
							//Ativa bot�o Remover
							btnRemover.setEnabled(true);
						} else {
							bubbleError("Houve um erro ao cadastrar a modalidade");
						}
					}
				} catch (SQLException error) {
					bubbleError(error.getMessage());
					error.printStackTrace();
				}
			}
		});
		
		//A��o Buscar
		btnBuscar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (searchModalityWindow == null) {
					searchModalityWindow = new ListModalitiesWindow(desktop);
					
					searchModalityWindow.addInternalFrameListener(new InternalFrameListener() {
						@Override
						public void internalFrameClosed(InternalFrameEvent e) {
							ModalityModel selectedModalityModel = ((ListModalitiesWindow) e.getInternalFrame()).getSelectedModel();
							List<GraduationModel> selectedGraduationList = ((ListModalitiesWindow) e.getInternalFrame()).getGraduationList();
							
							if (selectedModalityModel != null && selectedGraduationList != null) {
								//Atribui o model selecionado
								modalityModel = selectedModalityModel;
								graduationList = selectedGraduationList;
								
								//Adiciona as gradua��es a Grid.
								tableModel.clear();
								tableModel.addModelsList(graduationList);
																
								//Seta dados do model para os campos
								txfModalidade.setText(selectedModalityModel.getName());
								
								//Seta form para modo Edi��o
								setFormMode(UPDATE_MODE);
								
								//Ativa campos
								enableComponents(formFields);
								
								//Ativa bot�o salvar
								btnSalvar.setEnabled(true);
								
								//Ativa bot�o remover
								btnRemover.setEnabled(true);
							}
							
							//Reseta janela
							searchModalityWindow = null;
						}
					});
				}
			}
		});
		
		//A��o adicionar gradua��o a Grid.
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!txfGraduacao.getText().isEmpty()) {		
					GraduationModel graduationModel = new GraduationModel();
					graduationModel.setGraduationName(txfGraduacao.getText());
					
					//Caso esteja em modo de edi��o, adiciona a gradua��o a lista de novas adi��es.
					if (isEditing()) {
						graduationAddList.add(graduationModel);
					}
					
					tableModel.addModel(graduationModel);
					txfGraduacao.setText("");
					txfGraduacao.requestFocus();
				}
			}
		});
	}

	private void createComponents() {

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

		label = new JLabel("Modalidade: ");
		label.setBounds(5, 55, 150, 25);
		getContentPane().add(label);

		txfModalidade = new JTextField();
		txfModalidade.setBounds(70, 55, 355, 20);
		txfModalidade.setToolTipText("Digite a modalidade");
		getContentPane().add(txfModalidade);
		formFields.add(txfModalidade);

		label = new JLabel("Gradua��o: ");
		label.setBounds(5, 80, 150, 25);
		getContentPane().add(label);

		txfGraduacao = new JTextField();
		txfGraduacao.setBounds(70, 80, 280, 20);
		txfGraduacao.setToolTipText("Digite a gradua��o");
		txfGraduacao.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent ke) {
	    	  if (ke.getID() == KeyEvent.KEY_PRESSED && ke.getKeyCode() == KeyEvent.VK_ENTER) {
	    		  btnOk.doClick();
	    	  }
	        }

	        public void keyReleased(KeyEvent keyEvent) {
	        }

	        public void keyTyped(KeyEvent keyEvent) {
	        }
		});
		getContentPane().add(txfGraduacao);
		formFields.add(txfGraduacao);
		txfGraduacao.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent ke) {
	    	  if (ke.getID() == KeyEvent.KEY_PRESSED && ke.getKeyCode() == KeyEvent.VK_ENTER) {
	    		  btnOk.doClick();
	    	  }
	        }

	        public void keyReleased(KeyEvent keyEvent) {
	        }

	        public void keyTyped(KeyEvent keyEvent) {
	        }
	    });
		
		btnOk = new JButton("OK");
		btnOk.setBounds(355, 77, 70, 25);
		btnOk.setIcon(iconOK);
		btnOk.setToolTipText("Clique aqui para confirmar");
		getContentPane().add(btnOk);		
		
		createGrid();
		
		label = new JLabel("Duplo clique na linha da gradua��o para remov�-la.");
		label.setBounds(5, 280, 250, 25);
		getContentPane().add(label);
	}
	
	private void createGrid() {
		tableModel = new GraduationsTableModel();
		jTableGraduacoes = new JTable(tableModel);

		jTableGraduacoes.addMouseListener(new MouseAdapter() {

	        public void mouseClicked (MouseEvent me) {
	            if (me.getClickCount() == 2) {
	            	//Caso esteja em modo de edi��o, adiciona a gradua��o removida a lista de exclus�o.
					if (isEditing()) {
						graduationDeleteList.add(tableModel.getModel(jTableGraduacoes.getSelectedRow()));
					}
	            	
	            	//Clique duplo na linha da gradua��o para remov�-la.     	
	            	tableModel.removeModel(jTableGraduacoes.getSelectedRow());   
	            }
	        }
	    });
		
		// Habilita a sele��o por linha
		jTableGraduacoes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTableGraduacoes.setDefaultRenderer(Object.class, renderer);
		
		grid = new JScrollPane(jTableGraduacoes);
		setLayout(null);
		resizeGrid(grid, 5, 110, 420, 170);
		grid.setVisible(true);

		add(grid);
	}
	
	private boolean validateFields() {
		if(txfModalidade.getText().isEmpty() || txfModalidade.getText() == null) {
			bubbleWarning("Informe o nome da modalidade!");
			return true;
		}
		
		if(tableModel.isEmpty() || tableModel.getRowCount() == 0) {
			bubbleWarning("Voc� deve informar ao menos uma gradua��o para a modalidade.");
			return true;
		}
		
		return false;
	}
}
