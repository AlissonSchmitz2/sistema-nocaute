package br.com.nocaute.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;

import br.com.nocaute.dao.InvoicesRegistrationDAO;
import br.com.nocaute.dao.RegistrationDAO;
import br.com.nocaute.dao.StudentDAO;
import br.com.nocaute.image.MasterImage;
import br.com.nocaute.model.InvoicesRegistrationModel;
import br.com.nocaute.model.RegistrationModalityModel;
import br.com.nocaute.model.RegistrationModel;
import br.com.nocaute.model.StudentModel;
import br.com.nocaute.pojos.Graduation;
import br.com.nocaute.pojos.Modality;
import br.com.nocaute.pojos.Plan;
import br.com.nocaute.pojos.RegistrationModality;
import br.com.nocaute.util.MasterMonthChooser;
import br.com.nocaute.view.tableModel.AttendanceTableModel;
import br.com.nocaute.view.tableModel.PaymentsSituationTableModel;
import br.com.nocaute.view.tableModel.StudentRegistrationModalitiesTableModel;

public class ControlStudentFormWindow extends AbstractGridWindow {
	private static final long serialVersionUID = -8041165617342297479L;

	// Componentes
	private JTextField txfCodMatriculate, txfStudent;
	private JPanel panelPhoto, panelSituation;
	private JLabel label;
	private MasterMonthChooser masterMonthChooser;
	private JButton btnDataStudent, btnDataMatriculate;

	// Grid Matrícula de alunos
	private JTable jTableRegistration;
	private StudentRegistrationModalitiesTableModel studentRegistrationModalitiesTableModel;

	// Grid Assiduidade
	private JTable jTableAttendance;
	private AttendanceTableModel attendanceTableModel;

	// Grid Situação de pagamento
	private JTable jTablePaymentsSituation;
	private PaymentsSituationTableModel paymentsSituationTableModel;

	private StudentModel  studentModel  = new StudentModel();
	
	private StudentDAO studentDao = null;
	private RegistrationDAO registrationDao = null;
	private InvoicesRegistrationDAO invoicesDao = null;
	
	private static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	private static Rectangle screenRect = ge.getMaximumWindowBounds();
	private static int height = screenRect.height;// area total da altura tirando subtraindo o menu iniciar
	private static int width = screenRect.width;// area total da altura tirando subtraindo o menu iniciar

	public ControlStudentFormWindow(JDesktopPane desktop) {
		super("Controle de Alunos", width / 2 + 100, height - 150, desktop, false);

		setClosable(false);
		setIconifiable(true);
		setFrameIcon(MasterImage.control_16x16);

		setLocation((screenRect.width - this.getSize().width) / 2, (screenRect.height - this.getSize().height) / 2);

		createComponents();

		setSituation(0);
		// Seta as ações esperadas para cada botão
		setButtonsActions();
	}

	private void setButtonsActions() {
		txfCodMatriculate.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(!searchDataStudent(Integer.parseInt(txfCodMatriculate.getText()))) {
						bubbleWarning("Nenhum aluno foi encontrado!");
						txfStudent.setText("");
						studentRegistrationModalitiesTableModel.clear();
					}
				}
			}
		});
	}

	private void createComponents() {

		panelPhoto = new JPanel();
		panelPhoto.setBounds(10, 10, 185, 200);
		panelPhoto.setLayout(null);
		panelPhoto.setBorder(new LineBorder(Color.GRAY));
		panelPhoto.setToolTipText("Foto do Aluno");
		getContentPane().add(panelPhoto);

		txfCodMatriculate = new JTextField();
		txfCodMatriculate.setBounds(220, 10, 80, 25);
		txfCodMatriculate.setHorizontalAlignment(JTextField.RIGHT);
		txfCodMatriculate.setToolTipText("Código da matrícula do aluno");
		getContentPane().add(txfCodMatriculate);

		txfStudent = new JTextField();
		txfStudent.setBounds(310, 10, 448, 25);
		txfStudent.setEditable(false);
		txfStudent.setToolTipText("Aluno");
		getContentPane().add(txfStudent);

		createGridMatriculate();

		panelSituation = new JPanel();
		panelSituation.setBounds(220, 160, 538, 50);
		panelSituation.setLayout(null);
		panelSituation.setBorder(new LineBorder(Color.GRAY));
		getContentPane().add(panelSituation);

		label = new JLabel();
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		label.setBounds(0, 0, panelSituation.getWidth(), panelSituation.getHeight());
		label.setFont(new Font("Arial Black", Font.BOLD, 18));
		label.setForeground(Color.white);
		panelSituation.add(label);

		btnDataStudent = new JButton("Acessar dados do Aluno");
		btnDataStudent.setBounds(220, 220, 267, 30);
		btnDataStudent.setEnabled(false);
		btnDataStudent.setToolTipText("Acessar cadastro do aluno");
		getContentPane().add(btnDataStudent);

		btnDataMatriculate = new JButton("Acessar dados da Matrícula");
		btnDataMatriculate.setBounds(490, 220, 267, 30);
		btnDataMatriculate.setEnabled(false);
		btnDataMatriculate.setToolTipText("Acessar matrícula do aluno");
		getContentPane().add(btnDataMatriculate);

		masterMonthChooser = new MasterMonthChooser();
		masterMonthChooser.setBounds(10, 230, 185, 25);
		masterMonthChooser.setBorder(new LineBorder(Color.gray));
		getContentPane().add(masterMonthChooser);

		createGridAttendance();

		createGridSituationPayments();
	}

	private void createGridMatriculate() {
		studentRegistrationModalitiesTableModel = new StudentRegistrationModalitiesTableModel();
		jTableRegistration = new JTable(studentRegistrationModalitiesTableModel);
		jTableRegistration.setDefaultRenderer(Object.class, renderer);

		// Habilita a seleção por linha
		jTableRegistration.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		grid = new JScrollPane(jTableRegistration);
		setLayout(null);
		resizeGrid(grid, 220, 50, 538, 100);
		grid.setVisible(true);

		add(grid);
	}

	private void createGridAttendance() {
		attendanceTableModel = new AttendanceTableModel();
		jTableAttendance = new JTable(attendanceTableModel);
		jTableAttendance.setDefaultRenderer(Object.class, renderer);

		// Habilita a seleção por linha
		jTableAttendance.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		grid = new JScrollPane(jTableAttendance);
		setLayout(null);
		resizeGrid(grid, 10, 260, 185, 280);
		grid.setVisible(true);

		add(grid);
	}

	private void createGridSituationPayments() {
		paymentsSituationTableModel = new PaymentsSituationTableModel();
		jTablePaymentsSituation = new JTable(paymentsSituationTableModel);
		jTablePaymentsSituation.setDefaultRenderer(Object.class, renderer);

		// Habilita a seleção por linha
		jTablePaymentsSituation.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		grid = new JScrollPane(jTablePaymentsSituation);
		setLayout(null);
		resizeGrid(grid, 220, 260, 538, 280);
		grid.setVisible(true);

		add(grid);
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
	
	public boolean searchDataStudent(int code) {
		try {
			studentDao = new StudentDAO(CONNECTION);
			studentModel = studentDao.findById(code);
			
			registrationDao  = new RegistrationDAO(CONNECTION);
			invoicesDao      = new InvoicesRegistrationDAO(CONNECTION);
			
			if (studentModel instanceof StudentModel) {
				txfStudent.setText(studentModel.getName());
				
				studentRegistrationModalitiesTableModel.clear();
				RegistrationModel registrationModel = registrationDao.findByStudentId(studentModel.getCode(),true);
				studentRegistrationModalitiesTableModel.addModelsList(
						mapRegistrationModalitiesModelToRegistrationModalitiesPojo(registrationModel.getModalities())
					);
				
				registrationModel.getRegistrationCode();
				List<InvoicesRegistrationModel> invoicesModel = invoicesDao.getByRegistrationCode(registrationModel.getRegistrationCode());
				paymentsSituationTableModel.addModelsList(invoicesDao.getByRegistrationCode(registrationModel.getRegistrationCode()));
				
				btnDataStudent     .setEnabled(true);
				btnDataMatriculate .setEnabled(true);
				
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	private void setSituation(int stateSituation) {
		switch (stateSituation) {
		case 0:
			label.setText("Aguardando Consulta...");
			panelSituation.setBackground(Color.LIGHT_GRAY);
			break;
		case 1:
			label.setText("Débitos Pendentes");
			panelSituation.setBackground(Color.RED);
			break;
		case 2:
			label.setText("Situação Regular");
			panelSituation.setBackground(Color.GREEN);
			break;
		default:
			bubbleError("Situação Inválida!");
		}
	}

}