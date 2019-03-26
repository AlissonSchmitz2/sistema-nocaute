package br.com.nocaute.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;

import com.toedter.calendar.JDateChooser;

import br.com.nocaute.dao.GraduationDAO;
import br.com.nocaute.dao.ModalityDAO;
import br.com.nocaute.dao.PlanDAO;
import br.com.nocaute.pojos.Graduation;
import br.com.nocaute.pojos.Modality;
import br.com.nocaute.pojos.Plan;
import br.com.nocaute.pojos.RegistrationModality;
import br.com.nocaute.view.comboModel.GenericComboModel;

public class StudentRegistrationAddModalitiesWindow extends AbstractWindowFrame {
	private static final long serialVersionUID = -4201960150625152379L;
	
	ModalityDAO modalityDao;
	GraduationDAO graduationDao;
	PlanDAO planDao;
	
	RegistrationModality selectedRegistrationModality;

	// Componentes
	private JButton btnOk;
	private JLabel label;
	private JDateChooser jDtInicio, jDtFim;
	private JComboBox<Modality> cbxModalidade;
	private JComboBox<Graduation> cbxGraduacao;
	private JComboBox<Plan> cbxPlano;
	
	private ImageIcon iconJanela = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/16x16/estudante.png"));
	private ImageIcon iconOK = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/13x13/ok.png"));

	public StudentRegistrationAddModalitiesWindow(JDesktopPane desktop) {
		super("Adicionar Modalidades", 300, 225, desktop);
		setFrameIcon(iconJanela);
		
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
		List<Modality> modalitiesList = new ArrayList<>();
		modalitiesList.add(new Modality(0, "-- Selecione --"));
		
		//Lista de Graduações
		List<Graduation> graduationsList = new ArrayList<>();
		graduationsList.add(new Graduation(0, "-- Selecione --"));
		
		//Lista de Planos
		List<Plan> plansList = new ArrayList<>();
		plansList.add(new Plan(0, "-- Selecione --"));
		
		try {
			modalityDao.selectAll().forEach(modality -> modalitiesList.add(new Modality(modality.getModalityId(), modality.getName())));
		} catch (SQLException e) {
			e.printStackTrace();
		}
				
		//Instancia Combos
		cbxModalidade = new JComboBox<Modality>();
		cbxGraduacao = new JComboBox<Graduation>();
		cbxPlano = new JComboBox<Plan>();
		
		label = new JLabel("Modalidade: ");
		label.setBounds(5, 10, 100, 25);
		getContentPane().add(label);
		
		cbxModalidade.setModel(new GenericComboModel<Modality>(modalitiesList));
		cbxModalidade.setSelectedIndex(0);
		cbxModalidade.setBounds(70, 10, 205, 20);
		cbxModalidade.setToolTipText("Selecione uma modalidade");
		cbxModalidade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Modality selectedModality = (Modality) cbxModalidade.getSelectedItem();
				
				//Reseta combos
				graduationsList.clear();

				plansList.clear();
				
				if (selectedModality != null) {
					cbxGraduacao.setEnabled(true);
					cbxPlano.setEnabled(true);
					
					try {
						//Carrega combo de graduações
						graduationDao.findByModalityId(selectedModality.getId())
							.forEach(graduation -> graduationsList.add(new Graduation(graduation.getGraduationId(), graduation.getName())));
						cbxGraduacao.setModel(new GenericComboModel<Graduation>(graduationsList));
						
						//Carrega combo de planos
						planDao.findByModalityId(selectedModality.getId())
							.forEach(plan -> plansList.add(new Plan(plan.getPlanId(), plan.getName())));
						cbxPlano.setModel(new GenericComboModel<Plan>(plansList));
					} catch (SQLException error) {
						bubbleError(error.getMessage());
						error.printStackTrace();
					}
				} else {
					cbxGraduacao.setEnabled(false);
					cbxPlano.setEnabled(false);
					
					graduationsList.add(new Graduation(0, "-- Selecione --"));
					plansList.add(new Plan(0, "-- Selecione --"));
				}
			}
		});
		getContentPane().add(cbxModalidade);
		
		label = new JLabel("Graduação: ");
		label.setBounds(5, 40, 100, 25);
		getContentPane().add(label);
	
		//Graduação
		cbxGraduacao.setBounds(70, 40, 205, 20);
		cbxGraduacao.setToolTipText("Selecione uma graduação");
		cbxGraduacao.setEnabled(false);
		getContentPane().add(cbxGraduacao);
		
		label = new JLabel("Plano: ");
		label.setBounds(5, 70, 100, 25);
		getContentPane().add(label);
	
		//Planos
		cbxPlano.setBounds(70, 70, 205, 20);
		cbxPlano.setToolTipText("Selecione um plano");
		cbxPlano.setEnabled(false);
		getContentPane().add(cbxPlano);
		
		label = new JLabel("Data Início: ");
		label.setBounds(5, 100, 100, 25);
		getContentPane().add(label);
		
		label = new JLabel("Data Fim: ");
		label.setBounds(5, 130, 100, 25);
		getContentPane().add(label);
		
		try {
			jDtInicio = new JDateChooser(new Date());
			jDtInicio.setDateFormatString("dd/MM/yyyy");
			jDtInicio.setBounds(70, 100, 100, 20);
			jDtInicio.setToolTipText("Data de início");
			getContentPane().add(jDtInicio);
			
			jDtFim = new JDateChooser();
			jDtFim.setDateFormatString("dd/MM/yyyy");
			jDtFim.setBounds(70, 130, 100, 20);
			jDtFim.setToolTipText("Data do fim da matrícula");
			getContentPane().add(jDtFim);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		btnOk = new JButton("OK");
		btnOk.setBounds(100, 160, 100, 25);
		btnOk.setIcon(iconOK);
		btnOk.setToolTipText("Clique aqui para confirmar");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!validateFields()) {
					return;
				}
				
				selectedRegistrationModality = new RegistrationModality();
				
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
	}
	
	private boolean validateFields() {
		if (cbxModalidade.getSelectedItem() == null || ((Modality) cbxModalidade.getSelectedItem()).getId() == null) {
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