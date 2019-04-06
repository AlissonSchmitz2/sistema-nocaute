package br.com.nocaute.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;

import br.com.nocaute.dao.StudentDAO;
import br.com.nocaute.image.MasterImage;
import br.com.nocaute.model.StudentModel;
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

	// Grid Matr�cula de alunos
	private JTable jTableRegistration;
	private StudentRegistrationModalitiesTableModel studentRegistrationModalitiesTableModel;

	// Grid Assiduidade
	private JTable jTableAttendance;
	private AttendanceTableModel attendanceTableModel;

	// Grid Situa��o de pagamento
	private JTable jTablePaymentsSituation;
	private PaymentsSituationTableModel paymentsSituationTableModel;
	
	private StudentModel model = new StudentModel();
	private StudentDAO dao = null;

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
		// Seta as a��es esperadas para cada bot�o
		setButtonsActions();
	}

	private void setButtonsActions() {
		txfCodMatriculate.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					searchDataStudent(Integer.parseInt(txfCodMatriculate.getText()));
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
		txfCodMatriculate.setToolTipText("C�digo da matr�cula do aluno");
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

		btnDataMatriculate = new JButton("Acessar dados da Matr�cula");
		btnDataMatriculate.setBounds(490, 220, 267, 30);
		btnDataMatriculate.setEnabled(false);
		btnDataMatriculate.setToolTipText("Acessar matr�cula do aluno");
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

		// Habilita a sele��o por linha
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

		// Habilita a sele��o por linha
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

		// Habilita a sele��o por linha
		jTablePaymentsSituation.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		grid = new JScrollPane(jTablePaymentsSituation);
		setLayout(null);
		resizeGrid(grid, 220, 260, 538, 280);
		grid.setVisible(true);

		add(grid);
	}
	
	public void searchDataStudent(int code) {
		try {
			model = dao.findById(code);
			txfStudent.setText(model.getName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void setSituation(int stateSituation) {
		switch(stateSituation) {
			case 0:
				label.setText("Aguardando Consulta...");
				panelSituation.setBackground(Color.LIGHT_GRAY);
				break;
			case 1:
				label.setText("D�bitos Pendentes");
				panelSituation.setBackground(Color.RED);
				break;
			case 2:
				label.setText("Situa��o Regular");
				panelSituation.setBackground(Color.GREEN);
				break;
			default:
				bubbleError("Situa��o Inv�lida!");
		}
	}

}