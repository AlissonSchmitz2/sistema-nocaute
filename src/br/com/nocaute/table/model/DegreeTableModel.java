package br.com.nocaute.table.model;

import javax.swing.table.AbstractTableModel;

public class DegreeTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 2381816919838563117L;

	private String[] colunas = new String[] { "Graduação" };

	public DegreeTableModel() {
		//TODO: Aguardar model de gradução para finalizar
	}

	public int getRowCount() {
		return 0;
	}

	public int getColumnCount() {
		return colunas.length;
	}

	public String getColumnName(int columnIndex) {
		return colunas[columnIndex];
	}

	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}


	public void limpar() {
		//graduacoes.clear();
		fireTableDataChanged();
	}

	public boolean isEmpty() {
		return true;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return null;
	}

}
