package br.com.nocaute.view;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;

import br.com.nocaute.model.UserModel;
import br.com.nocaute.table.model.StudentTableModel;

public class ListStudentFormWindow extends AbstractGridWindow{
	private static final long serialVersionUID = -8074030868088770858L;
	
	private JButton btnBuscar;
	private JTextField txfBuscar;
	
	private StudentTableModel model;
	private JTable jTableUsers;

	public ListStudentFormWindow(JDesktopPane desktop) {
		super("Alunos", 445, 310, desktop);
		
		criarComponentes();
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
		model = new StudentTableModel();
		jTableUsers = new JTable(model);

		// Habilita a seleção por linha
		jTableUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTableUsers.setBorder(LineBorder.createGrayLineBorder());
		
		///TESTE
		UserModel user1 = new UserModel();
		user1.setCode(1);
		user1.setUser("Alisson Schmitz de Medeiros");
	
		UserModel user2 = new UserModel();
		user2.setCode(2);
		user2.setUser("Edvaldo da Rosa");
	
		
		try {
			model.addAluno(user1);
			model.addAluno(user2);
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

}
