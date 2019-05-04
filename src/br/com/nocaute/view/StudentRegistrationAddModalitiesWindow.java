package br.com.nocaute.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;

import com.toedter.calendar.JDateChooser;

import br.com.nocaute.dao.GraduationDAO;
import br.com.nocaute.dao.ModalityDAO;
import br.com.nocaute.dao.PlanDAO;
import br.com.nocaute.image.MasterImage;
import br.com.nocaute.pojos.Graduation;
import br.com.nocaute.pojos.Modality;
import br.com.nocaute.pojos.Plan;
import br.com.nocaute.pojos.RegistrationModality;
import br.com.nocaute.view.comboModel.GenericComboModel;

public class StudentRegistrationAddModalitiesWindow extends AbstractWindowFrame {
	private static final long serialVersionUID = -4201960150625152379L;
	
	private ModalityDAO modalityDao;
	private GraduationDAO graduationDao;
	private PlanDAO planDao;
	private Connection CONNECTION;
	
	private RegistrationModality selectedRegistrationModality;
	private RegistrationModality registrationModalityToEdition;
	
	private List<Modality> modalitiesList;
	private List<Graduation> graduationsList;
	private List<Plan> plansList;
	
	// Componentes
	private JButton btnOk;
	private JLabel label;
	private JDateChooser jDtInicio, jDtFim;
	private JComboBox<Modality> cbxModalidade = new JComboBox<Modality>();
	private JComboBox<Graduation> cbxGraduacao = new JComboBox<Graduation>();
	private JComboBox<Plan> cbxPlano = new JComboBox<Plan>();

	public StudentRegistrationAddModalitiesWindow(JDesktopPane desktop, Connection CONNECTION) {
		super("Adicionar Modalidades", 300, 225, desktop);
		
		setIconifiable(false);
		this.CONNECTION = CONNECTION;
		
		constructor(desktop);
	}
	
	public StudentRegistrationAddModalitiesWindow(JDesktopPane desktop, RegistrationModality modality, Connection CONNECTION) {
		super("Adicionar Modalidades", 300, 225, desktop);
		
		//Remover a opção de minimizar
		setIconifiable(false);
		this.CONNECTION = CONNECTION;
		
		//Atribui a modalidade vinda pelo construtor a modalidade selecionda
		registrationModalityToEdition = modality;
		
		constructor(desktop);
	}
	
	private void constructor(JDesktopPane desktop) {
		setFrameIcon(MasterImage.student_16x16);
		
		try {
			modalityDao = new ModalityDAO(CONNECTION);
			graduationDao = new GraduationDAO(CONNECTION);
			planDao = new PlanDAO(CONNECTION);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		desktop.add(this);
		showFrame();

		criarComponentes();
	}
	
	public RegistrationModality getSelectedRegistrationModality() {
		return selectedRegistrationModality;
	}
	
	private void criarComponentes() {
		//Lista de modalidades
		modalitiesList = new ArrayList<>();
		modalitiesList.add(new Modality(0, "-- Selecione --"));
		
		//Lista de Graduações
		graduationsList = new ArrayList<>();
		graduationsList.add(new Graduation(0, "-- Selecione --"));
		
		//Lista de Planos
		plansList = new ArrayList<>();
		plansList.add(new Plan(0, "-- Selecione --"));
		
		try {
			modalityDao.selectAll().forEach(modality -> modalitiesList.add(new Modality(modality.getModalityId(), modality.getName())));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		label = new JLabel("Modalidade: ");
		label.setBounds(5, 10, 100, 25);
		getContentPane().add(label);
		
		cbxModalidade.setModel(new GenericComboModel<Modality>(modalitiesList));
		if (registrationModalityToEdition != null) {
			Modality selectedModality = modalitiesList
				.stream()
				.filter(item -> { return item.getId() == registrationModalityToEdition.getModality().getId(); })
				.findFirst().orElse(null);
			
			cbxModalidade.setSelectedItem(selectedModality);
			cbxModalidade.setEnabled(false);
			
			updateComboboxes();
		} else {
			cbxModalidade.setSelectedIndex(0);
		}
		
		cbxModalidade.setBounds(70, 10, 205, 20);
		cbxModalidade.setToolTipText("Selecione uma modalidade");
		cbxModalidade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateComboboxes();
			}
		});
		getContentPane().add(cbxModalidade);
		
		label = new JLabel("Graduação: ");
		label.setBounds(5, 40, 100, 25);
		getContentPane().add(label);
	
		//Graduação
		cbxGraduacao.setBounds(70, 40, 205, 20);
		cbxGraduacao.setToolTipText("Selecione uma graduação");
		getContentPane().add(cbxGraduacao);
		
		label = new JLabel("Plano: ");
		label.setBounds(5, 70, 100, 25);
		getContentPane().add(label);
	
		//Planos
		cbxPlano.setBounds(70, 70, 205, 20);
		cbxPlano.setToolTipText("Selecione um plano");
		getContentPane().add(cbxPlano);
		
		label = new JLabel("Data Início: ");
		label.setBounds(5, 100, 100, 25);
		getContentPane().add(label);
		
		label = new JLabel("Data Fim: ");
		label.setBounds(5, 130, 100, 25);
		getContentPane().add(label);
		
		try {
			
			jDtInicio = new JDateChooser((registrationModalityToEdition != null) ? registrationModalityToEdition.getStartDate() : new Date());
			jDtInicio.setDateFormatString("dd/MM/yyyy");
			jDtInicio.setBounds(70, 100, 100, 20);
			jDtInicio.setToolTipText("Data de início");
			getContentPane().add(jDtInicio);
			
			jDtFim = new JDateChooser((registrationModalityToEdition != null) ? registrationModalityToEdition.getFinishDate() : null);
			jDtFim.setDateFormatString("dd/MM/yyyy");
			jDtFim.setBounds(70, 130, 100, 20);
			jDtFim.setToolTipText("Data do fim da matrícula");
			jDtFim.getDateEditor().addPropertyChangeListener(
				    new PropertyChangeListener() {
				        @Override
				        public void propertyChange(PropertyChangeEvent e) {
				            evaluateComboboxesDisabling();
				        }
				    });
			getContentPane().add(jDtFim);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		evaluateComboboxesDisabling();
		
		btnOk = new JButton("OK", MasterImage.ok_13x13);
		btnOk.setBounds(100, 160, 100, 25);
		btnOk.setToolTipText("Clique aqui para confirmar");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!validateFields()) {
					return;
				}
				
				selectedRegistrationModality = new RegistrationModality();
				
				//Se existir uma instância da modalidade para edição, usa o id
				if (registrationModalityToEdition != null) {
					selectedRegistrationModality.setId(registrationModalityToEdition.getId());
				}
				
				selectedRegistrationModality.setModality((Modality) cbxModalidade.getSelectedItem());
				selectedRegistrationModality.setGraduation((Graduation) cbxGraduacao.getSelectedItem());
				selectedRegistrationModality.setPlan((Plan) cbxPlano.getSelectedItem());
				selectedRegistrationModality.setStartDate(jDtInicio.getDate());
				selectedRegistrationModality.setFinishDate(jDtFim.getDate());
				
				//Fecha a janela
            	try {
					setClosed(true);
				} catch (PropertyVetoException error) {
					error.printStackTrace();
				}
			}
		});
		getContentPane().add(btnOk);
		
		/*
		btnExcluir = new JButton("Excluir");
		btnExcluir.setBounds(10, 160, 100, 25);
		btnExcluir.setIcon(iconDelete);
		btnExcluir.setToolTipText("Clique aqui para excluir");
		btnExcluir.setEnabled(registrationModalityToEdition != null);
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Seta o atributo que define a modalidade como excluida
				selectedRegistrationModality = new RegistrationModality();
				selectedRegistrationModality.setDeleted(true);
				
				//Fecha a janela
            	try {
					setClosed(true);
				} catch (PropertyVetoException error) {
					error.printStackTrace();
				}
			}
		});
		getContentPane().add(btnExcluir);
		*/
	}
	
	private void updateComboboxes() {
		Modality selectedModality = (Modality) cbxModalidade.getSelectedItem();
		
		//Reseta combos
		graduationsList.clear();

		plansList.clear();
		
		if (selectedModality != null) {
			try {
				//Carrega combo de graduações
				graduationDao.findByModalityId(selectedModality.getId())
					.forEach(graduation -> graduationsList.add(new Graduation(graduation.getGraduationId(), graduation.getName())));
				cbxGraduacao.setModel(new GenericComboModel<Graduation>(graduationsList));
				
				if (registrationModalityToEdition != null) {
					Graduation selectedGraduation = graduationsList
							.stream()
							.filter(item -> { return item.getId() == registrationModalityToEdition.getGraduation().getId(); })
							.findFirst().orElse(null);
						
					cbxGraduacao.setSelectedItem(selectedGraduation);
				}
				
				//Carrega combo de planos
				planDao.findByModalityId(selectedModality.getId())
					.forEach(plan -> plansList.add(new Plan(plan.getPlanId(), plan.getName())));
				cbxPlano.setModel(new GenericComboModel<Plan>(plansList));
				
				if (registrationModalityToEdition != null) {
					Plan selectedPlan = plansList
							.stream()
							.filter(item -> { return item.getId() == registrationModalityToEdition.getPlan().getId(); })
							.findFirst().orElse(null);
						
					cbxPlano.setSelectedItem(selectedPlan);
				}
			} catch (SQLException error) {
				bubbleError(error.getMessage());
				error.printStackTrace();
			}
		} else {			
			graduationsList.add(new Graduation(0, "-- Selecione --"));
			plansList.add(new Plan(0, "-- Selecione --"));
		}
	}
	
	private void evaluateComboboxesDisabling() {
		Date finishDate = jDtFim.getDate();
		cbxModalidade.setEnabled(finishDate == null);
		cbxGraduacao.setEnabled(finishDate == null);
		cbxPlano.setEnabled(finishDate == null);
	}
	
	private boolean validateFields() {
		if (cbxModalidade.getSelectedItem() == null || ((Modality) cbxModalidade.getSelectedItem()).getId() == null
				|| cbxModalidade.getSelectedIndex() == 0) {
			bubbleWarning("Selecione uma modalidade!");
			return false;
		}
		
		if (cbxGraduacao.getSelectedItem() == null || ((Graduation) cbxGraduacao.getSelectedItem()).getId() == null) {
			bubbleWarning("Selecione uma graduação!");
			return false;
		}
		
		if (cbxPlano.getSelectedItem() == null || ((Plan) cbxPlano.getSelectedItem()).getId() == null) {
			bubbleWarning("Selecione um plano!");
			return false;
		}
		
		if (jDtInicio.getDate() == null) {
			bubbleWarning("Informe uma data de início!");
			return false;
		}
		
		return true;
	}
}