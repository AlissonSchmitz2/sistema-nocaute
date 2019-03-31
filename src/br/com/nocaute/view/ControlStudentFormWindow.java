package br.com.nocaute.view;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

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

	private JDesktopPane desktop;

	private static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	private static Rectangle screenRect = ge.getMaximumWindowBounds();
	private static int height = screenRect.height;// area total da altura tirando subtraindo o menu iniciar
	private static int width = screenRect.width;// area total da altura tirando subtraindo o menu iniciar

	public ControlStudentFormWindow(JDesktopPane desktop) {
		super("Controle de Alunos", width / 2 + 100, height - 150, desktop);

		this.desktop = desktop;

		setClosable(false);
		setIconifiable(true);

		setLocation((screenRect.width - this.getSize().width) / 2, (screenRect.height - this.getSize().height) / 2);

		createComponents();

		// Seta as ações esperadas para cada botão
		setButtonsActions();
	}

	private void setButtonsActions() {
		
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

		label = new JLabel("Aguardando Consulta...");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		label.setBounds(0, 0, panelSituation.getWidth(), panelSituation.getHeight());
		label.setFont(new Font("Impact", Font.BOLD, 18));
		label.setForeground(Color.white);
		//label.setSize(new Dimension(10, 10));
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

}