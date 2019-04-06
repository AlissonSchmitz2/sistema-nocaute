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
import br.com.nocaute.model.UserModel;
import br.com.nocaute.util.InternalFrameListener;

public class UserFormWindow extends AbstractWindowFrame {
	private static final long serialVersionUID = -2537423200954897351L;

	private UserModel model = new UserModel();
	private UserDAO userDao;
	private ListUsersWindow searchUsersWindow;

	// Guarda os fields em uma lista para facilitar manipulação em massa
	List<Component> formFields = new ArrayList<Component>();

	// Icones
	private ImageIcon iconBuscar = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/localizar.png"));
	private ImageIcon iconAdicionar = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/adicionar.png"));
	private ImageIcon iconRemover = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/remover.png"));
	private ImageIcon iconSalvar = new ImageIcon(this.getClass().getResource("/br/com/nocaute/image/22x22/salvar.png"));
	private ImageIcon iconJanela = new ImageIcon(this.getClass().getResource("/br/com/nocaute/image/16x16/user.png"));

	// Componentes
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
				if (!validateFields()) {
					return;
				}

				model.setUser(txfUsuario.getText());
				model.setPassword(new String(txfSenha.getPassword()));
				model.setProfile(cbxPerfil.getSelectedItem().toString());

				try {
					// EDIÇÃO CADASTRO
					if (isEditing()) {
						boolean result = userDao.update(model);

						if (result) {
							bubbleSuccess("Usuario editado com sucesso");
						} else {
							bubbleError("Houve um erro ao editar usuario");
						}
						// NOVO CADASTRO
					} else {
						UserModel insertedModel = userDao.insert(model);

						if (insertedModel != null) {
							bubbleSuccess("Usuario cadastrado com sucesso");

							// Atribui o model recém criado ao model
							model = insertedModel;

							// Seta form para edição
							setFormMode(UPDATE_MODE);

							// Ativa botão Remover
							btnRemover.setEnabled(true);
						} else {
							bubbleError("Houve um erro ao cadastrar usuario");
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
				if (searchUsersWindow == null) {
					searchUsersWindow = new ListUsersWindow(desktop);

					searchUsersWindow.addInternalFrameListener(new InternalFrameListener() {
						@Override
						public void internalFrameClosed(InternalFrameEvent e) {
							UserModel selectedModel = ((ListUsersWindow) e.getInternalFrame()).getSelectedModel();

							if (selectedModel != null) {
								// Atribui o model selecionado
								model = selectedModel;

								txfUsuario.setText(model.getUser());
								cbxPerfil.setSelectedItem(model.getProfile());

								txfSenha.setEnabled(false);
								txfConfirmarSenha.setEnabled(false);

								// Seta form para modo Edição
								setFormMode(UPDATE_MODE);

								// Ativa campos
								enableComponents(formFields);

								// Ativa botão salvar
								btnSalvar.setEnabled(true);

								// Ativa botão remover
								btnRemover.setEnabled(true);

								txfConfirmarSenha.setEnabled(false);
								txfSenha.setEnabled(false);
							}

							// Reseta janela
							searchUsersWindow = null;
						}
					});
				}
			}
		});

		// Ação Remover
		btnRemover.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (isEditing()) {
						boolean result = userDao.delete(model);

						if (result) {
							bubbleSuccess("Usuario excluído com sucesso");

							// Seta form para modo Cadastro
							setFormMode(CREATE_MODE);

							// Desativa campos
							disableComponents(formFields);

							// Limpar dados dos campos
							clearFormFields(formFields);

							// Cria nova entidade model
							model = new UserModel();

							// Desativa botão salvar
							btnSalvar.setEnabled(false);

							// Desativa botão remover
							btnRemover.setEnabled(false);
						} else {
							bubbleError("Houve um erro ao excluir usuario");
						}
					}
				} catch (SQLException error) {
					bubbleError(error.getMessage());
					error.printStackTrace();
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

	public boolean validateFields() {
		if (txfUsuario.getText().isEmpty() || txfUsuario.getText() == null) {
			bubbleWarning("Informe o nome do Usuario!");
			return false;
		}

		if ((new String(txfSenha.getPassword()).isEmpty() || new String(txfSenha.getPassword()) == null)
				&& txfSenha.isEnabled()) {
			bubbleWarning("Informe uma senha para usuario!");
			return false;
		}

		if ((new String(txfConfirmarSenha.getPassword()).isEmpty()
				|| new String(txfConfirmarSenha.getPassword()) == null) && txfConfirmarSenha.isEnabled()) {
			bubbleWarning("Confirme sua senha para o usuario!");
			return false;
		}

		if (!(new String(txfConfirmarSenha.getPassword()).equals(new String(txfSenha.getPassword())))) {
			bubbleWarning("Senhas não conferem!");
			return false;
		}

		if (cbxPerfil.getSelectedIndex() == 0) {
			bubbleWarning("Informe o perfil do usuario!");
			return false;
		}

		return true;
	}

}
