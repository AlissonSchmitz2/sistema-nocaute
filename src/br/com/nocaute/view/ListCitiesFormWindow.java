package br.com.nocaute.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import br.com.nocaute.dao.CityDAO;
import br.com.nocaute.model.CityModel;
import br.com.nocaute.view.tableModel.CityTableModel;

public class ListCitiesFormWindow extends AbstractGridWindow {
	private static final long serialVersionUID = 1L;

	private CityDAO dao;
	private CityModel selectedModel;

	private JButton btnSearch;
	private JTextField txfSearch;

	private CityTableModel tableModel;
	private JTable jTableModels;
	private TableCellRenderer renderer = new EvenOddRenderer();

	public ListCitiesFormWindow(JDesktopPane desktop) {
		super("Cidades", 445, 310, desktop);

		try {
			dao = new CityDAO(CONNECTION);
		} catch (SQLException error) {
			error.printStackTrace();
		}

		createComponents();

		setButtonsActions();
	}

	public CityModel getSelectedModel() {
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

	private void createComponents() {

		txfSearch = new JTextField();
		txfSearch.setBounds(5, 10, 330, 20);
		txfSearch.setToolTipText("Informe o cidade");
		getContentPane().add(txfSearch);

		btnSearch = new JButton("Buscar");
		btnSearch.setBounds(340, 10, 85, 22);
		getContentPane().add(btnSearch);

		createGrid();
	}

	private void createGrid() {
		tableModel = new CityTableModel();
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
		jTableModels.setDefaultRenderer(Object.class, renderer);

		grid = new JScrollPane(jTableModels);
		setLayout(null);
		resizeGrid(grid, 5, 40, 420, 230);
		grid.setVisible(true);

		add(grid);
	}

	private void loadGrid(String word) {
		tableModel.clear();

		try {
			tableModel.addModelsList(dao.search(word));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	// TODO: Refatorar para utilizar e todas as grids
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
