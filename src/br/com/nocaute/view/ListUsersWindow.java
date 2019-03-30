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

import br.com.nocaute.dao.UserDAO;
import br.com.nocaute.model.UserModel;
import br.com.nocaute.view.tableModel.UsersTableModel;

public class ListUsersWindow extends AbstractGridWindow {
	private static final long serialVersionUID = 1L;

	private UserDAO dao;
	private UserModel selectedModel;

	private JButton btnSearch;
	private JTextField txfSearch;

	private UsersTableModel tableModel;
	private JTable jTableModels;

	public ListUsersWindow(JDesktopPane desktop) {
		super("Usuarios", 445, 310, desktop);

		try {
			dao = new UserDAO(CONNECTION);
		} catch (Exception e) {
			e.printStackTrace();
		}

		createComponents();

		txfSearch.requestFocusInWindow();

		setButtonsActions();
	}

	public UserModel getSelectedModel() {
		return selectedModel;
	}

	private void setButtonsActions() {
		// Ação Buscar
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadGrid(txfSearch.getText());
			}
		});
	}

	public void createComponents() {
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

	public void loadGrid(String word) {
		tableModel.clear();

		try {
			tableModel.addModelsList(dao.search(word));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public void createGrid() {
		tableModel = new UsersTableModel();
		jTableModels = new JTable(tableModel);

		jTableModels.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 2) {
					// Atribui o model da linha clicada
					selectedModel = tableModel.getModel(jTableModels.getSelectedRow());

					// Fecha a janela
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
		// Add ayout na grid
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

}
