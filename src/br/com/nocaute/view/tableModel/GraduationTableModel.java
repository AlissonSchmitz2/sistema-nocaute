package br.com.nocaute.view.tableModel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.com.nocaute.model.GraduationModel;

public class GraduationTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 2381816919838563117L;

	private List<GraduationModel> graduationModel;
	private String[] colunas = new String[] { "Graduação" };
	
	public GraduationTableModel(List<GraduationModel> graduationModel) {
		this.graduationModel = graduationModel;
	}

	public GraduationTableModel() {
		this.graduationModel = new ArrayList<GraduationModel>();
	}

	public int getRowCount() {
		return graduationModel.size();
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

	public void setValueAt(GraduationModel aValue, int rowIndex) {
		GraduationModel graduation = graduationModel.get(rowIndex);

		graduation.setGraduationId(aValue.getGraduationId());
		graduation.setGraduationName(aValue.getGraduationName());
		graduation.setModalityId(aValue.getModalityId());

		fireTableCellUpdated(rowIndex, 0);
		fireTableCellUpdated(rowIndex, 1);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		GraduationModel graduation = graduationModel.get(rowIndex);

		switch (columnIndex) {
		case 0:
			graduation.setGraduationId(Integer.parseInt(aValue.toString()));
		case 1:
			graduation.setGraduationName(aValue.toString());
		case 2:
			graduation.setModalityId(Integer.parseInt(aValue.toString()));
		default:
			System.err.println("Índice da coluna inválido");
		}
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		GraduationModel graduationSelect = graduationModel.get(rowIndex);
		String valueObject = null;
		switch (columnIndex) {
		case 0:
			valueObject = graduationSelect.getGraduationId().toString();
			break;
		default:
			System.err.println("Índice inválido para propriedade do bean GraduationModel.class");
		}

		return valueObject;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public GraduationModel getGraduation(int indiceLinha) {
		return graduationModel.get(indiceLinha);
	}

	public void addGraduation(GraduationModel u) {
		graduationModel.add(u);

		int ultimoIndice = getRowCount() - 1;

		fireTableRowsInserted(ultimoIndice, ultimoIndice);
	}

	public void removeGraduation(int indiceLinha) {
		graduationModel.remove(indiceLinha);

		fireTableRowsDeleted(indiceLinha, indiceLinha);
	}

	public void addListGraduation(List<GraduationModel> newGraduations) {

		int tamanhoAntigo = getRowCount();
		graduationModel.addAll(newGraduations);
		fireTableRowsInserted(tamanhoAntigo, getRowCount() - 1);
	}

	public void limpar() {
		graduationModel.clear();
		fireTableDataChanged();
	}

	public boolean isEmpty() {
		return graduationModel.isEmpty();
	}
}
