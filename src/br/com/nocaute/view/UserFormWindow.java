package br.com.nocaute.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;

import br.com.nocaute.dao.UserDAO;
import br.com.nocaute.enums.Genres;
import br.com.nocaute.model.StudentModel;
import br.com.nocaute.model.UserModel;
import br.com.nocaute.util.InternalFrameListener;

public class UserFormWindow extends AbstractWindowFrame{
	private static final long serialVersionUID = -2537423200954897351L;
	
	private UserModel model = new UserModel(); 
	private UserDAO userDao;
	private ListUsersWindow searchUsersWindow;
	
	// Guarda os fields em uma lista para facilitar manipulação em massa
	List<Component> formFields = new ArrayList<Component>();
	
	//Icones
	private ImageIcon iconBuscar = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/localizar.png"));
	private ImageIcon iconAdicionar = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/adicionar.png"));
	private ImageIcon iconRemover = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/remover.png"));
	private ImageIcon iconSalvar = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/salvar.png"));
	private ImageIcon iconJanela = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/16x16/user.png"));
	
	
	//Componentes
	private JButton btnBuscar, btnAdicionar, btnRemover, btnSalvar;
	private JLabel label;
	private JTextField txfUsuario;
	private JPasswordField txfSenha, txfConfirmarSenha;
	private JComboBox<String> cbxPerfil;
	
	JDesktopPane desktop;
	
	public UserFormWindow(JDesktopPane desktop) {
		super("Usuários", 455, 200, desktop);
		setFrameIcon(iconJanela);
		
		this.desktop = desktop;
		
		try {
			userDao = new UserDAO(CONNECTION);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		criarComponentes();
		
		// Por padrão campos são desabilitados ao iniciar
		disableComponents(formFields);
		
		setButtonsActions();
	}
	
	private void setButtonsActions() {
		// Ações de botões
		btnAdicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setFormMode(CREATE_MODE);
				
				// Ativa campos
				enableComponents(formFields);
				
				// Limpar dados dos campos
				clearFormFields(formFields);

				// Cria nova entidade model
				model = new UserModel();

				// Ativa botão salvar
				btnSalvar.setEnabled(true);

				// Desativa botão Remover
				btnRemover.setEnabled(false);
			}
		});
		
		btnSalvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Cadastra usuario
				UserModel model = new UserModel();
				
				model.setUser(txfUsuario.getText());
				model.setPassword(txfSenha.getPassword().toString());
				model.setProfile(cbxPerfil.getSelectedItem().toString());
				
				try {
					userDao.insert(model);
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
						if (searchUsersWindow == null) {
							searchUsersWindow = new ListUsersWindow(desktop);

							searchUsersWindow.addInternalFrameListener(new InternalFrameListener() {
								@Override
								public void internalFrameClosed(InternalFrameEvent e) {
									UserModel selectedModel = ((ListUsersWindow) e.getInternalFrame())
											.getSelectedModel();

									if (selectedModel != null) {
										// Atribui o model selecionado
										model = selectedModel;

										txfUsuario.setText(model.getUser());
										//TODO:Adicionar os restantes para inserir na tabela

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
									searchUsersWindow = null;
								}
							});
						}
					}
				});
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
		formFields.add(btnRemover);
		
		btnSalvar = new JButton("Salvar");
		btnSalvar.setBounds(330, 5, 95, 40);
		btnSalvar.setIcon(iconSalvar);
		btnSalvar.setToolTipText("Clique aqui para salvar");
		getContentPane().add(btnSalvar);
		formFields.add(btnSalvar);
		
		label = new JLabel("Usuário: ");
		label.setBounds(5, 55, 50, 25);
		getContentPane().add(label);
		
		txfUsuario = new JTextField();
		txfUsuario.setBounds(100, 55, 325, 20);
		txfUsuario.setToolTipText("Digite o nome do usuário");
		getContentPane().add(txfUsuario);
		formFields.add(txfUsuario);
		
		label = new JLabel("Senha: ");
		label.setBounds(5, 80, 70, 25);
		getContentPane().add(label);
		
		txfSenha = new JPasswordField();
		txfSenha.setBounds(100, 80, 325, 20);
		txfSenha.setToolTipText("Digite a senha do usuário");
		getContentPane().add(txfSenha);
		formFields.add(txfSenha);
		
		label = new JLabel("Confirmar Senha: ");
		label.setBounds(5, 105, 90, 25);
		getContentPane().add(label);
		
		txfConfirmarSenha = new JPasswordField();
		txfConfirmarSenha.setBounds(100, 105, 325, 20);
		txfConfirmarSenha.setToolTipText("Confirme a senha do usuário");
		getContentPane().add(txfConfirmarSenha);
		formFields.add(txfConfirmarSenha);
		
		label = new JLabel("Perfil: ");
		label.setBounds(5, 130, 110, 25);
		getContentPane().add(label);
		
		cbxPerfil = new JComboBox<String>();
		cbxPerfil.addItem("-- Selecione --");
		cbxPerfil.addItem("Cadastral");
		cbxPerfil.addItem("Matricular");
		cbxPerfil.addItem("Financeiro");
		cbxPerfil.addItem("Completo");
		cbxPerfil.setBounds(100, 130, 325, 20);
		cbxPerfil.setToolTipText("Informe o perfil");
		getContentPane().add(cbxPerfil);
		formFields.add(cbxPerfil);
		
	}

}
