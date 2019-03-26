package br.com.nocaute.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;

import br.com.nocaute.dao.StudentDAO;
import br.com.nocaute.model.StudentModel;
import br.com.nocaute.view.tableModel.StudentsTableModel;

public class ListStudentsWindow extends AbstractGridWindow {
	private static final long serialVersionUID = -8074030868088770858L;
	
	private StudentDAO studentDao;
	private StudentModel selectedModel;

	private JButton btnSearch;
	private JTextField txfSearch;

	private StudentsTableModel tableModel;
	private JTable jTableModels;
	
	//Utilizado para alterar o layout da grid
	private TableCellRenderer renderer = new EvenOddRenderer();

	public ListStudentsWindow(JDesktopPane desktop) {
		super("Alunos", 445, 310, desktop);
		
		try {
			studentDao = new StudentDAO(CONNECTION);
		} catch (SQLException error) {
			error.printStackTrace();
		}

		createComponents();
		
		txfSearch.requestFocusInWindow();
		
		setButtonsActions();
	}
	
	public StudentModel getSelectedModel() {
		return selectedModel;
	}
	
	private void setButtonsActions() {
		//Ação Buscar
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadGrid(txfSearch.getText());
			}
		});
	}

	private void createComponents() {

		txfSearch = new JTextField();
		txfSearch.setBounds(5, 10, 330, 20);
		txfSearch.setToolTipText("Informe o aluno");
		txfSearch.requestFocusInWindow();
		txfSearch.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent ke) {
	    	  if (ke.getID() == KeyEvent.KEY_PRESSED && ke.getKeyCode() == KeyEvent.VK_ENTER) {
	    		  loadGrid(txfSearch.getText());
	    	  }
	        }

	        public void keyReleased(KeyEvent keyEvent) {
	        }

	        public void keyTyped(KeyEvent keyEvent) {
	        }
	    });
		getContentPane().add(txfSearch);

		btnSearch = new JButton("Buscar");
		btnSearch.setBounds(340, 10, 85, 22);
		getContentPane().add(btnSearch);

		createGrid();
	}

	private void createGrid() {
		tableModel = new StudentsTableModel();
		jTableModels = new JTable(tableModel);
		
		jTableModels.addMouseListener(new MouseAdapter() {

	        public void mouseClicked (MouseEvent me) {
	            if (me.getClickCount() == 2) {
	            	//Atribui o model da linha clicada
	            	selectedModel = tableModel.getModel(jTableModels.getSelectedRow());
	            	
	            	//Fecha a janela
	            	try {
						setClosed(true);
					} catch (PropertyVetoException e) {
						e.printStackTrace();
					}
	            }
	        }
	    });

		// Habilita a seleção por linha
		jTableModels.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//Add ayout na grid
		jTableModels.setDefaultRenderer(Object.class, renderer);
		jTableModels.getColumnModel().getColumn(0).setMaxWidth(60);
		jTableModels.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent ke) {
				if (ke.getID() == KeyEvent.KEY_PRESSED && ke.getKeyCode() == KeyEvent.VK_ENTER) {
					//Atribui o model da linha selecionada
	            	selectedModel = tableModel.getModel(jTableModels.getSelectedRow());
	            	
	            	//Fecha a janela
	            	try {
						setClosed(true);
					} catch (PropertyVetoException e) {
						e.printStackTrace();
					}
				}
			}
			
			public void keyReleased(KeyEvent keyEvent) {
			}
			
			public void keyTyped(KeyEvent keyEvent) {
			}
	    });
		
		grid = new JScrollPane(jTableModels);
		setLayout(null);
		resizeGrid(grid, 5, 40, 420, 230);
		grid.setVisible(true);
		
		add(grid);
	}
	
	private void loadGrid(String word) {
		if (word.length() < 3) {
			bubbleWarning("Você precisa inserir ao menos 3 caracteres para iniciar a busca");
			
			return;
		}
		
		tableModel.clear();
		
		try {
			tableModel.addModelsList(studentDao.search(word));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
