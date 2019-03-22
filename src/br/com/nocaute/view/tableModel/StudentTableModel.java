package br.com.nocaute.view.tableModel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.com.nocaute.model.UserModel;

public class StudentTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 1242843030000425873L;
	
	private List<UserModel> userModel;
	private String[] colunas = new String[] { "ID", "Aluno" };

	public StudentTableModel(List<UserModel> userModel) {
		this.userModel = userModel;
	}

	public StudentTableModel() {
		this.userModel = new ArrayList<UserModel>();
	}

	public int getRowCount() {
		return userModel.size();
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

	public void setValueAt(UserModel aValue, int rowIndex) {
		UserModel user = userModel.get(rowIndex);

		user.setCode(aValue.getCode());
		user.setUser(aValue.getUser());

		fireTableCellUpdated(rowIndex, 0);
		fireTableCellUpdated(rowIndex, 1);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		UserModel user = userModel.get(rowIndex);

		switch (columnIndex) {
		case 0:
			user.setCode(Integer.parseInt(aValue.toString()));
		case 1:
			user.setUser(aValue.toString());
		default:
			System.err.println("Índice da coluna inválido");
		}
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		UserModel userSelecionado = userModel.get(rowIndex);
		String valueObject = null;
		switch (columnIndex) {
		case 0:
			valueObject = userSelecionado.getCode().toString();
			break;
		case 1:
			valueObject = userSelecionado.getUser();
			break;
		default:
			System.err.println("Índice inválido para propriedade do bean Aluno.class");
		}

		return valueObject;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public UserModel getUser(int indiceLinha) {
		return userModel.get(indiceLinha);
	}

	public void addAluno(UserModel u) {
		userModel.add(u);

		int ultimoIndice = getRowCount() - 1;

		fireTableRowsInserted(ultimoIndice, ultimoIndice);
	}

	public void removeAluno(int indiceLinha) {
		userModel.remove(indiceLinha);

		fireTableRowsDeleted(indiceLinha, indiceLinha);
	}

	public void addListaDeCursos(List<UserModel> newUsers) {

		int tamanhoAntigo = getRowCount();
		userModel.addAll(newUsers);
		fireTableRowsInserted(tamanhoAntigo, getRowCount() - 1);
	}

	public void limpar() {
		userModel.clear();
		fireTableDataChanged();
	}

	public boolean isEmpty() {
		return userModel.isEmpty();
	}
}
