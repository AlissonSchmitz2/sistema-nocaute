package br.com.nocaute.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import br.com.nocaute.model.StudentModel;
import br.com.nocaute.model.UserModel;
import br.com.nocaute.view.tableModel.StudentTableModel;

public class ListStudentFormWindow extends AbstractGridWindow {
	private static final long serialVersionUID = -8074030868088770858L;
	
	private StudentModel selectedModel;

	private JButton btnBuscar;
	private JTextField txfBuscar;

	private StudentTableModel tableModel;
	private JTable jTableUsers;
	private TableCellRenderer renderer = new EvenOddRenderer();

	public ListStudentFormWindow(JDesktopPane desktop) {
		super("Alunos", 445, 310, desktop);

		criarComponentes();
	}
	
	public StudentModel getSelectedModel() {
		return selectedModel;
	}

	private void criarComponentes() {

		txfBuscar = new JTextField();
		txfBuscar.setBounds(5, 10, 330, 20);
		txfBuscar.setToolTipText("Informe o aluno");
		getContentPane().add(txfBuscar);

		btnBuscar = new JButton("Buscar");
		btnBuscar.setBounds(340, 10, 85, 22);
		getContentPane().add(btnBuscar);

		carregarGrid();
	}

	private void carregarGrid() {
		tableModel = new StudentTableModel();
		jTableUsers = new JTable(tableModel);

		// Habilita a seleção por linha
		jTableUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTableUsers.setDefaultRenderer(Object.class, renderer);
		
		//getTableCellRendererComponent

		/// TESTE
		UserModel user1 = new UserModel();
		user1.setCode(1);
		user1.setUser("Alisson Schmitz de Medeiros");

		UserModel user2 = new UserModel();
		user2.setCode(2);
		user2.setUser("Edvaldo da Rosa");

		UserModel user3 = new UserModel();
		user3.setCode(3);
		user3.setUser("Alisson Schmitz de Medeiros");

		UserModel user4 = new UserModel();
		user4.setCode(4);
		user4.setUser("Edvaldo da Rosa");

		UserModel user5 = new UserModel();
		user5.setCode(5);
		user5.setUser("Alisson Schmitz de Medeiros");

		UserModel user6 = new UserModel();
		user6.setCode(6);
		user6.setUser("Edvaldo da Rosa");

		try {
			tableModel.addStudent(user1);
			tableModel.addStudent(user2);
			tableModel.addStudent(user3);
			tableModel.addStudent(user4);
			tableModel.addStudent(user5);
			tableModel.addStudent(user6);
		} catch (Exception e) {
			System.err.printf("Erro ao iniciar lista de cursos: %s.\n", e.getMessage());
		}
		///

		grid = new JScrollPane(jTableUsers);
		setLayout(null);
		redimensionarGrid(grid, 5, 40, 420, 230);
		grid.setVisible(true);

		add(grid);

	}

	//TODO: Refatorar para utilizar e todas as grids
	class EvenOddRenderer implements TableCellRenderer {
		public DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
					column);
			((JLabel) renderer).setOpaque(true);
			Color background;
			if (isSelected) {
				background = new Color(65, 105, 225);
			} else {
				if (row % 2 == 0) {
					background = new Color(220, 220, 220);
				} else {
					background = Color.WHITE;
				}
			}

			renderer.setBackground(background);
			return renderer;
		}
	}

}
