package br.com.nocaute.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import br.com.nocaute.dao.GraduationDAO;
import br.com.nocaute.dao.ModalityDAO;
import br.com.nocaute.model.GraduationModel;
import br.com.nocaute.model.ModalityModel;
import br.com.nocaute.view.tableModel.ModalitiesTableModel;

public class ListModalitiesWindow extends AbstractGridWindow {
	private static final long serialVersionUID = -8394745067091734288L;
	
	private ModalityDAO modalityDAO;
	private ModalityModel selectedModel;
	
	private GraduationDAO graduationDAO;
	private List<GraduationModel> graduationList;
	
	private JButton btnSearch;
	private JTextField txfSearch;
	
	private ModalitiesTableModel tableModel;
	private JTable jTableModels;
	
	public ListModalitiesWindow(JDesktopPane desktop) {
		super("Modalidades", 445, 310, desktop, true);
		
		try {
			modalityDAO = new ModalityDAO(CONNECTION);
			graduationDAO = new GraduationDAO(CONNECTION);
		} catch (SQLException error) {
			error.printStackTrace();
		}

		createComponents();
		
		txfSearch.requestFocusInWindow();
		
		setButtonsActions();
	}
	
	public ModalityModel getSelectedModel() {
		return selectedModel;
	}
	
	public List<GraduationModel> getGraduationList(){
		graduationList = new ArrayList<>();
		
		if(selectedModel != null) {
			try {
				graduationList = graduationDAO.findByModalityId(selectedModel.getModalityId());
			} catch (SQLException error) {
				error.printStackTrace();
			}
		}
		
		return graduationList;
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
		tableModel = new ModalitiesTableModel();
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
		jTableModels.setDefaultRenderer(Object.class, renderer);
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
		tableModel.clear();
		
		try {
			tableModel.addModelsList(modalityDAO.search(word));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
