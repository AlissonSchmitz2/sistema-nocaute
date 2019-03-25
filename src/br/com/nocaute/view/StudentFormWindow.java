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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.InternalFrameEvent;

import br.com.nocaute.util.InternalFrameListener;
import javax.swing.text.MaskFormatter;

import com.toedter.calendar.JDateChooser;

import br.com.nocaute.dao.StudentDAO;
import br.com.nocaute.enums.Genres;
import br.com.nocaute.model.CityModel;
import br.com.nocaute.model.StudentModel;
import br.com.nocaute.pojos.Genre;
import br.com.nocaute.util.PlaceholderTextField;
import br.com.nocaute.view.comboModel.GenericComboModel;

public class StudentFormWindow extends AbstractWindowFrame implements KeyEventPostProcessor {
	private static final long serialVersionUID = 1631880171317467520L;

	private StudentDAO studentDao;
	private StudentModel model = new StudentModel();
	private ListStudentsFormWindow searchStudentWindow;
	private ListCitiesFormWindow searchCityWindow;

	// Guarda os fields em uma lista para facilitar manipulação em massa
	private List<Component> formFields = new ArrayList<Component>();

	// Componentes
	private JButton btnBuscar, btnAdicionar, btnRemover, btnSalvar;
	private JLabel label;
	private JTextField txfAluno, txfEmail;
	private JFormattedTextField txfTelefone, txfCelular, txfNumero;
	private JDateChooser jDateNascimento;
	private JTextArea txfObservacao;
	private JComboBox<Genre> cbxSexo;
	private SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");

	// Endereço
	private JTextField txfEndereco, txfComplemento, txfBairro, txfEstado, txfPais, txfCEP;
	private PlaceholderTextField txfCidade;

	private JPanel painelAba;
	private JTabbedPane tabelPane;

	// Icones
	private ImageIcon iconBuscar = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/localizar.png"));
	private ImageIcon iconAdicionar = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/adicionar.png"));
	private ImageIcon iconRemover = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/remover.png"));
	private ImageIcon iconSalvar = new ImageIcon(this.getClass().getResource("/br/com/nocaute/image/22x22/salvar.png"));
	private ImageIcon iconJanela = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/16x16/estudante.png"));

	private JDesktopPane desktop;

	public StudentFormWindow(JDesktopPane desktop) {
		super("Cadastro de Alunos", 450, 460, desktop);
		setFrameIcon(iconJanela);

		this.desktop = desktop;

		try {
			studentDao = new StudentDAO(CONNECTION);
		} catch (SQLException error) {
			error.printStackTrace();
		}

		createComponents();

		// Por padrão campos são desabilitados ao iniciar
		disableComponents(formFields);

		// Seta as ações esperadas para cada botão
		setButtonsActions();

		// Register a key event post processor.
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(this);
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
				model = new StudentModel();

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
						boolean result = studentDao.delete(model);

						if (result) {
							bubbleSuccess("Aluno excluído com sucesso");

							// Seta form para modo Cadastro
							setFormMode(CREATE_MODE);

							// Desativa campos
							disableComponents(formFields);

							// Limpar dados dos campos
							clearFormFields(formFields);

							// Cria nova entidade model
							model = new StudentModel();

							// Desativa botão salvar
							btnSalvar.setEnabled(false);

							// Desativa botão remover
							btnRemover.setEnabled(false);
						} else {
							bubbleError("Houve um erro ao excluir aluno");
						}
					}
				} catch (SQLException error) {
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
				/*
				 * SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); String
				 * birthDate = formatter.format(jDateNascimento.getDate());
				 */
				Date birthDate = jDateNascimento.getDate();

				Genre selectedGenre = (Genre) cbxSexo.getSelectedItem();

				model.setName(txfAluno.getText());
				model.setBirthDate(birthDate);
				model.setGenre(selectedGenre.getCode().charAt(0));
				model.setTelephone(txfTelefone.getText());
				model.setMobilePhone(txfCelular.getText());
				model.setEmail(txfEmail.getText());
				model.setObservation(txfObservacao.getText());
				model.setAddress(txfEndereco.getText());
				model.setNumber(txfNumero.getText());
				model.setAddressComplement(txfComplemento.getText());
				model.setNeighborhood(txfBairro.getText());
				model.setPostalCode(txfCEP.getText());

				try {
					// EDIÇÃO CADASTRO
					if (isEditing()) {
						boolean result = studentDao.update(model);

						if (result) {
							bubbleSuccess("Aluno editado com sucesso");
						} else {
							bubbleError("Houve um erro ao editar aluno");
						}
						// NOVO CADASTRO
					} else {
						StudentModel insertedModel = studentDao.insert(model);

						if (insertedModel != null) {
							bubbleSuccess("Aluno cadastrado com sucesso");

							// Atribui o model recém criado ao model
							model = insertedModel;

							// Seta form para edição
							setFormMode(UPDATE_MODE);

							// Ativa botão Remover
							btnRemover.setEnabled(true);
						} else {
							bubbleError("Houve um erro ao cadastrar aluno");
						}
					}
				} catch (SQLException error) {
					error.printStackTrace();
				}
			}
		});

		// Ação Buscar
		btnBuscar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (searchStudentWindow == null) {
					searchStudentWindow = new ListStudentsFormWindow(desktop);

					searchStudentWindow.addInternalFrameListener(new InternalFrameListener() {
						@Override
						public void internalFrameClosed(InternalFrameEvent e) {
							StudentModel selectedModel = ((ListStudentsFormWindow) e.getInternalFrame())
									.getSelectedModel();

							if (selectedModel != null) {
								// Atribui o model selecionado
								model = selectedModel;

								// Seta dados do model para os campos
								txfAluno.setText(model.getName());
								if (model.getBirthDate() != null) {
									jDateNascimento.setDate(model.getBirthDate());
								}
								if (!String.valueOf(model.getGenre()).isEmpty()) {
									int genreCounter = 0;
									for (String code : Genres.getGenres().keySet()) {
										genreCounter++;

										if (model.getGenre() == code.charAt(0)) {
											cbxSexo.setSelectedIndex(genreCounter);
										}
									}
								}
								txfTelefone.setText(model.getTelephone());
								txfCelular.setText(model.getMobilePhone());
								txfEmail.setText(model.getEmail());
								txfObservacao.setText(model.getObservation());
								txfEndereco.setText(model.getAddress());
								txfNumero.setText(model.getNumber());
								txfComplemento.setText(model.getAddressComplement());
								txfBairro.setText(model.getNeighborhood());
								txfCEP.setText(model.getPostalCode());

								if (model.getCity() != null) {
									txfCidade.setText(model.getCity().getName());
									txfEstado.setText(model.getCity().getState());
									txfPais.setText(model.getCity().getCountry());
								}

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
							searchStudentWindow = null;
						}
					});
				}
			}
		});
	}

	@Override
	public boolean postProcessKeyEvent(KeyEvent ke) {
		// Abre tela seleção cidade ao clicar F9
		if (ke.getID() == KeyEvent.KEY_PRESSED && ke.getKeyCode() == KeyEvent.VK_F9) {
			if (btnSalvar.isEnabled()) {
				openSearchCityWindow();
			}

			return true;
		}

		return false;
	}

	private void openSearchCityWindow() {
		if (searchCityWindow == null) {
			searchCityWindow = new ListCitiesFormWindow(desktop);

			searchCityWindow.addInternalFrameListener(new InternalFrameListener() {
				@Override
				public void internalFrameClosed(InternalFrameEvent e) {
					CityModel selectedModel = ((ListCitiesFormWindow) e.getInternalFrame()).getSelectedModel();

					if (selectedModel != null) {
						// Atribui cidade para o model
						model.setCity(selectedModel);

						// Seta valores da cidade para o campo
						txfCidade.setText(selectedModel.getName());
						txfEstado.setText(selectedModel.getState());
						txfPais.setText(selectedModel.getCountry());
					}

					// Reseta janela
					searchCityWindow = null;
				}
			});
		}
	}

	private void createComponents() {

		btnBuscar = new JButton("Buscar");
		btnBuscar.setBounds(15, 5, 95, 40);
		btnBuscar.setIcon(iconBuscar);
		btnBuscar.setToolTipText("Clique aqui para buscar");
		getContentPane().add(btnBuscar);

		btnAdicionar = new JButton("Adicionar");
		btnAdicionar.setBounds(110, 5, 110, 40);
		btnAdicionar.setIcon(iconAdicionar);
		btnAdicionar.setToolTipText("Clique aqui para adicionar");
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

		label = new JLabel("Nome: ");
		label.setBounds(5, 55, 50, 25);
		getContentPane().add(label);

		txfAluno = new JTextField();
		txfAluno.setBounds(110, 55, 315, 20);
		txfAluno.setToolTipText("Digite o nome do aluno");
		getContentPane().add(txfAluno);
		formFields.add(txfAluno);

		label = new JLabel("Data de Nascimento: ");
		label.setBounds(5, 80, 150, 25);
		getContentPane().add(label);

		jDateNascimento = new JDateChooser(new Date());
		jDateNascimento.setBounds(110, 80, 125, 20);
		jDateNascimento.setDateFormatString("dd/MM/yyyy");
		getContentPane().add(jDateNascimento);
		formFields.add(jDateNascimento);

		try {

			txfTelefone = new JFormattedTextField(new MaskFormatter("## ####-####"));
			txfTelefone.setFocusLostBehavior(JFormattedTextField.COMMIT);
			txfTelefone.setBounds(110, 105, 125, 20);
			txfTelefone.setToolTipText("Digite o telefone do aluno");
			getContentPane().add(txfTelefone);
			formFields.add(txfTelefone);

			txfCelular = new JFormattedTextField(new MaskFormatter("## ####-####"));
			txfCelular.setFocusLostBehavior(JFormattedTextField.COMMIT);
			txfCelular.setBounds(285, 105, 140, 20);
			txfCelular.setToolTipText("Digite o celular do aluno");
			getContentPane().add(txfCelular);
			formFields.add(txfCelular);

			txfNumero = new JFormattedTextField();
			txfNumero.setBounds(297, 5, 110, 20);
			txfNumero.setToolTipText("Digite o número");
			formFields.add(txfNumero);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		label = new JLabel("Sexo: ");
		label.setBounds(250, 80, 150, 25);
		getContentPane().add(label);

		// Cria uma lista com opções vindas do ENUM Genres
		List<Genre> genresList = new ArrayList<>();
		genresList.add(new Genre("", "-- Selecione --"));
		Genres.getGenres().forEach((code, description) -> genresList.add(new Genre(code, description)));

		cbxSexo = new JComboBox<Genre>();
		cbxSexo.setModel(new GenericComboModel<Genre>(genresList));
		cbxSexo.setBounds(285, 80, 140, 20);
		cbxSexo.setToolTipText("Informe o sexo");
		getContentPane().add(cbxSexo);
		formFields.add(cbxSexo);

		label = new JLabel("Telefone: ");
		label.setBounds(5, 105, 150, 25);
		getContentPane().add(label);

		label = new JLabel("Celular: ");
		label.setBounds(241, 105, 150, 25);
		getContentPane().add(label);

		label = new JLabel("E-mail: ");
		label.setBounds(5, 130, 50, 25);
		getContentPane().add(label);

		txfEmail = new JTextField();
		txfEmail.setBounds(110, 130, 315, 20);
		txfEmail.setToolTipText("Digite o e-mail do aluno");
		getContentPane().add(txfEmail);
		formFields.add(txfEmail);

		label = new JLabel("Observações: ");
		label.setBounds(5, 155, 150, 25);
		getContentPane().add(label);

		txfObservacao = new JTextArea();
		txfObservacao.setFont(getFont());
		txfObservacao.setLineWrap(true); // Quebrar linha
		txfObservacao.setBorder(new LineBorder(Color.gray));
		txfObservacao.setBounds(5, 190, 420, 60);
		getContentPane().add(txfObservacao);
		formFields.add(txfObservacao);

		/*
		 * Endereço
		 */
		tabelPane = new JTabbedPane();
		tabelPane.setBounds(5, 260, 420, 160);
		getContentPane().add(tabelPane);

		painelAba = new JPanel();
		tabelPane.addTab("Endereço", null, painelAba, null);
		painelAba.setLayout(null);

		// Adicionar o txfNumero após criar o painel
		painelAba.add(txfNumero);

		label = new JLabel("Endereço: ");
		label.setBounds(5, 5, 150, 25);
		painelAba.add(label);

		txfEndereco = new JTextField();
		txfEndereco.setBounds(80, 5, 155, 20);
		txfEndereco.setToolTipText("Digite o endereço");
		painelAba.add(txfEndereco);
		formFields.add(txfEndereco);

		label = new JLabel("Número: ");
		label.setBounds(250, 5, 150, 25);
		painelAba.add(label);

		label = new JLabel("Complemento: ");
		label.setBounds(5, 30, 150, 25);
		painelAba.add(label);

		txfComplemento = new JTextField();
		txfComplemento.setBounds(80, 30, 327, 20);
		txfComplemento.setToolTipText("Digite o complemento");
		painelAba.add(txfComplemento);
		formFields.add(txfComplemento);

		label = new JLabel("Bairro: ");
		label.setBounds(5, 55, 150, 25);
		painelAba.add(label);

		txfBairro = new JTextField();
		txfBairro.setBounds(80, 55, 155, 20);
		txfBairro.setToolTipText("Digite o bairro");
		painelAba.add(txfBairro);
		formFields.add(txfBairro);

		label = new JLabel("Cidade: ");
		label.setBounds(250, 55, 150, 25);
		painelAba.add(label);

		txfCidade = new PlaceholderTextField();
		txfCidade.setBounds(297, 55, 110, 20);
		txfCidade.setToolTipText("Tecle F9 para selecionar uma cidade");
		txfCidade.setBackground(Color.yellow);
		txfCidade.setEditable(false);
		txfCidade.setPlaceholder("Teclar F9");
		txfCidade.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				// Retira o foco do campo após abrir a tela de busca
				txfCEP.requestFocusInWindow();

				openSearchCityWindow();
			}

			@Override
			public void focusLost(FocusEvent e) {
			}
		});
		painelAba.add(txfCidade);
		formFields.add(txfCidade);

		label = new JLabel("Estado: ");
		label.setBounds(5, 80, 150, 25);
		painelAba.add(label);

		txfEstado = new JTextField();
		txfEstado.setBounds(80, 80, 155, 20);
		txfEstado.setToolTipText("Digite o estado");
		txfEstado.setEditable(false);
		txfEstado.setFocusable(false);
		painelAba.add(txfEstado);
		formFields.add(txfEstado);

		label = new JLabel("País: ");
		label.setBounds(250, 80, 150, 25);
		painelAba.add(label);

		txfPais = new JTextField();
		txfPais.setBounds(297, 80, 110, 20);
		txfPais.setToolTipText("Informe o país");
		txfPais.setEditable(false);
		txfEstado.setFocusable(false);
		painelAba.add(txfPais);
		formFields.add(txfPais);

		label = new JLabel("CEP: ");
		label.setBounds(5, 105, 150, 25);
		painelAba.add(label);

		txfCEP = new JTextField();
		txfCEP.setBounds(80, 105, 110, 20);
		txfCEP.setToolTipText("Informe o CEP");
		painelAba.add(txfCEP);
		formFields.add(txfCEP);
	}

	private boolean validateFields() {

		if (txfAluno.getText().isEmpty() || txfAluno.getText() == null) {
			bubbleWarning("Informe o nome do aluno!");
			return false;
		}

		// Se a data de nascimento for igual a atual
		if (formatador.format(jDateNascimento.getDate())
				.equals(formatador.format(new Date(System.currentTimeMillis())))) {
			bubbleWarning("Data de nascimento inválida!");
			return false;
		}

		if (cbxSexo.getSelectedIndex() == 0) {
			bubbleWarning("Informe o sexo do aluno!");
			return false;
		}

		if (txfTelefone.getText().isEmpty() || txfTelefone.getText() == null || txfCelular.getText().isEmpty()
				|| txfCelular.getText() == null) {
			bubbleWarning("Informe ao menos um telefone para contato!");
			return false;
		}

		if (txfEndereco.getText().isEmpty() || txfEndereco.getText() == null) {
			bubbleWarning("Informe o endereço do aluno!");
			return false;
		}

		if (txfBairro.getText().isEmpty() || txfBairro.getText() == null) {
			bubbleWarning("Informe o bairro do aluno!");
			return false;
		}

		if (txfCidade.getText().isEmpty() || txfCidade.getText() == null) {
			bubbleWarning("Cidade não informada!");
			return false;
		}

		if (txfCEP.getText().isEmpty() || txfCEP.getText() == null) {
			bubbleWarning("Digite o CEP do aluno!");
			return false;
		}

		return true;
	}
}
