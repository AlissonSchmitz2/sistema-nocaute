package br.com.nocaute.view;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JDesktopPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import br.com.nocaute.view.tableModel.StudentRegistrationModalitiesTableModel;

public class ControlStudentFormWindow extends AbstractGridWindow {
	private static final long serialVersionUID = -8041165617342297479L;

	private JTextField txfCodMatriculate, txfStudent;
	
	private JTable jTableRegistration;
	private StudentRegistrationModalitiesTableModel studentRegistrationModalitiesTableModel;
	
	private JDesktopPane desktop;
	
	private static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	private static Rectangle screenRect = ge.getMaximumWindowBounds();
	private static int height = screenRect.height;// area total da altura tirando subtraindo o menu iniciar
	private static int width = screenRect.width;// area total da altura tirando subtraindo o menu iniciar

	public ControlStudentFormWindow(JDesktopPane desktop) {
		super("Controle de Alunos", width / 2 + 150, height - 45 ,desktop);
		
		this.desktop = desktop;
		
		setClosable(false);
		setIconifiable(true);
		
		setLocation((screenRect.width - this.getSize().width) / 2, 0);
		
		createComponents();
	}

	private void createComponents() {
		//createGrid();
	}
	
	private void createGrid() {
		studentRegistrationModalitiesTableModel = new StudentRegistrationModalitiesTableModel();
		jTableRegistration = new JTable(studentRegistrationModalitiesTableModel);
		jTableRegistration.setDefaultRenderer(Object.class, renderer);
	
		// Habilita a seleção por linha
		jTableRegistration.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		grid = new JScrollPane(jTableRegistration);
		setLayout(null);
		resizeGrid(grid, 5, 170, 420, 170);
		grid.setVisible(true);

		add(grid);
	}

}