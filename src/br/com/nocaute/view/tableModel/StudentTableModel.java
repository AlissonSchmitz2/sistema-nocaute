package br.com.nocaute.view.tableModel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.com.nocaute.model.StudentModel;

public class StudentTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1242843030000425873L;
	
	private List<StudentModel> modelsList;
	private String[] colunas = new String[] { "ID", "Aluno" };

	public StudentTableModel(List<StudentModel> modelsList) {
		this.modelsList = modelsList;
	}

	public StudentTableModel() {
		this.modelsList = new ArrayList<StudentModel>();
	}

	public int getRowCount() {
		return modelsList.size();
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

	public void setValueAt(StudentModel aValue, int rowIndex) {
		StudentModel model = modelsList.get(rowIndex);

		model.setCode(aValue.getCode());
		model.setName(aValue.getName());

		fireTableCellUpdated(rowIndex, 0);
		fireTableCellUpdated(rowIndex, 1);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		StudentModel model = modelsList.get(rowIndex);

		switch (columnIndex) {
		case 0:
			model.setCode(Integer.parseInt(aValue.toString()));
		case 1:
			model.setName(aValue.toString());
		default:
			System.err.println("Índice da coluna inválido");
		}
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		StudentModel selectedModel = modelsList.get(rowIndex);
		String valueObject = null;
		switch (columnIndex) {
		case 0:
			valueObject = selectedModel.getCode().toString();
			break;
		case 1:
			valueObject = selectedModel.getName();
			break;
		default:
			System.err.println("Índice inválido para propriedade do bean Student.class");
		}

		return valueObject;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public StudentModel getStudent(int rowIndex) {
		return modelsList.get(rowIndex);
	}

	public void addStudent(StudentModel model) {
		modelsList.add(model);

		int lastIndex = getRowCount() - 1;

		fireTableRowsInserted(lastIndex, lastIndex);
	}

	public void removeAluno(int rowIndex) {
		modelsList.remove(rowIndex);

		fireTableRowsDeleted(rowIndex, rowIndex);
	}

	public void addModelsList(List<StudentModel> models) {

		int oldSize = getRowCount();
		modelsList.addAll(models);
		fireTableRowsInserted(oldSize, getRowCount() - 1);
	}

	public void clear() {
		modelsList.clear();
		fireTableDataChanged();
	}

	public boolean isEmpty() {
		return modelsList.isEmpty();
	}
}
