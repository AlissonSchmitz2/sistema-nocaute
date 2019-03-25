package br.com.nocaute.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JTextField;

import br.com.nocaute.dao.ModalityDAO;
import br.com.nocaute.dao.PlanDAO;
import br.com.nocaute.model.PlanModel;
import br.com.nocaute.pojos.Modality;
import br.com.nocaute.util.JNumberFormatField;
import br.com.nocaute.view.comboModel.GenericComboModel;

public class PlanFormWindow extends AbstractWindowFrame {
	private static final long serialVersionUID = 5227409767477555089L;
	
	private PlanDAO planDao;
	private ModalityDAO modalityDao;
	private PlanModel model = new PlanModel();
	
	// Guarda os fields em uma lista para facilitar manipulação em massa
	private List<Component> formFields = new ArrayList<Component>();

	// Componentes
	private JButton btnBuscar, btnAdicionar, btnRemover, btnSalvar;
	private JLabel label;
	private JComboBox<Modality> cbxModalidade;
	private JTextField txfPlano;
	private JNumberFormatField txfValor;

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
			this.getClass().getResource("/br/com/nocaute/image/16x16/financeiro.png"));

	private JDesktopPane desktop;
	
	public PlanFormWindow(JDesktopPane desktop) {
		super("Planos", 450, 165, desktop);
		setFrameIcon(iconJanela);
		
		this.desktop = desktop;

		try {
			planDao = new PlanDAO(CONNECTION);
			modalityDao = new ModalityDAO(CONNECTION);
		} catch (SQLException error) {
			error.printStackTrace();
		}
		
		createComponents();
		
		// Por padrão campos são desabilitados ao iniciar
		disableComponents(formFields);
		
		// Seta as ações esperadas para cada botão
		setButtonsActions();
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
				model = new PlanModel();

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
						boolean result = planDao.delete(model);

						if (result) {
							bubbleSuccess("Plano excluído com sucesso");

							// Seta form para modo Cadastro
							setFormMode(CREATE_MODE);

							// Desativa campos
							disableComponents(formFields);

							// Limpar dados dos campos
							clearFormFields(formFields);

							// Cria nova entidade model
							model = new PlanModel();

							// Desativa botão salvar
							btnSalvar.setEnabled(false);

							// Desativa botão remover
							btnRemover.setEnabled(false);
						} else {
							bubbleError("Houve um erro ao excluir plano");
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
				
				Modality selectedModality = (Modality) cbxModalidade.getSelectedItem();
				
				model.setModalityId(selectedModality.getId());
				model.setPlanName(txfPlano.getText());
				model.setMonthlyValue(new BigDecimal(txfValor.getValue().doubleValue()));
				
				try {
					// EDIÇÃO CADASTRO
					if (isEditing()) {
						boolean result = planDao.update(model);

						if (result) {
							bubbleSuccess("Plano editado com sucesso");
						} else {
							bubbleError("Houve um erro ao editar plano");
						}
						// NOVO CADASTRO
					} else {
						PlanModel insertedModel = planDao.insert(model);

						if (insertedModel != null) {
							bubbleSuccess("Plano cadastrado com sucesso");

							// Atribui o model recém criado ao model
							model = insertedModel;

							// Seta form para edição
							setFormMode(UPDATE_MODE);

							// Ativa botão Remover
							btnRemover.setEnabled(true);
						} else {
							bubbleError("Houve um erro ao cadastrar plano");
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
			}
		});
	}
	
	private boolean validateFields() {
		//TODO
		return true;
	}

	private void createComponents() {

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

		label = new JLabel("Modalidade: ");
		label.setBounds(5, 55, 150, 25);
		getContentPane().add(label);

		// Cria uma lista com opções
		List<Modality> modalitiesList = new ArrayList<>();
		modalitiesList.add(new Modality(null, "-- Selecione --"));
		
		try {
			modalityDao.selectAll().forEach(modality -> modalitiesList.add(new Modality(modality.getModalityId(), modality.getModalityName())));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		cbxModalidade = new JComboBox<Modality>();
		cbxModalidade.setModel(new GenericComboModel<Modality>(modalitiesList));
		cbxModalidade.setBounds(70, 55, 355, 20);
		cbxModalidade.setToolTipText("Informe a modalidade");
		
		getContentPane().add(cbxModalidade);
		formFields.add(cbxModalidade);

		label = new JLabel("Plano: ");
		label.setBounds(5, 80, 150, 25);
		getContentPane().add(label);

		txfPlano = new JTextField();
		txfPlano.setBounds(70, 80, 355, 20);
		txfPlano.setToolTipText("Digite o plano");
		getContentPane().add(txfPlano);
		formFields.add(txfPlano);

		label = new JLabel("Valor: ");
		label.setBounds(5, 105, 150, 25);
		getContentPane().add(label);

		txfValor = new JNumberFormatField();
		txfValor.setBounds(70, 105, 70, 20);
		txfValor.setLimit(6);
		txfValor.setToolTipText("Informe o valor");
		getContentPane().add(txfValor);
		formFields.add(txfValor);
	}
}
