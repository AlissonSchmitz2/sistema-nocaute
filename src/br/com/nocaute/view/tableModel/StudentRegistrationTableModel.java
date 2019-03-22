package br.com.nocaute.view.tableModel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.com.nocaute.model.RegistrationModel;

public class StudentRegistrationTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 5204634462348292204L;
	
	private List<RegistrationModel> registrationModel;
	private String[] colunas = new String[] { "Modalidade", "Graduação", "Plano", "Data Início", "Data Fim" };
	
	public StudentRegistrationTableModel(List<RegistrationModel> registrationModel) {
		this.registrationModel = registrationModel;
	}

	public StudentRegistrationTableModel() {
		this.registrationModel = new ArrayList<RegistrationModel>();
	}

	public int getRowCount() {
		return registrationModel.size();
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

	public void setValueAt(RegistrationModel aValue, int rowIndex) {
		RegistrationModel registration = registrationModel.get(rowIndex);

		registration.setRegistrationCode(aValue.getRegistrationCode());
		registration.setStudentCode(aValue.getStudentCode());
		registration.setRegistrationDate(aValue.getRegistrationDate());
		registration.setExpirationDay(aValue.getExpirationDay());
		//TODO: modalidade, graduacao, plano, data inicio e data fim
		
		fireTableCellUpdated(rowIndex, 0);
		fireTableCellUpdated(rowIndex, 1);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		RegistrationModel registration = registrationModel.get(rowIndex);

		switch (columnIndex) {
		case 0:
			registration.setRegistrationCode(Integer.parseInt(aValue.toString()));
		case 1:
			registration.setStudentCode(Integer.parseInt(aValue.toString()));
		default:
			System.err.println("Índice da coluna inválido");
		}
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		RegistrationModel registrationSelect = registrationModel.get(rowIndex);
		String valueObject = null;
		switch (columnIndex) {
		case 0:
			valueObject = registrationSelect.getRegistrationCode().toString();
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

	public RegistrationModel getRegistration(int indiceLinha) {
		return registrationModel.get(indiceLinha);
	}

	public void addRegistration(RegistrationModel u) {
		registrationModel.add(u);

		int ultimoIndice = getRowCount() - 1;

		fireTableRowsInserted(ultimoIndice, ultimoIndice);
	}

	public void removeRegistration(int indiceLinha) {
		registrationModel.remove(indiceLinha);

		fireTableRowsDeleted(indiceLinha, indiceLinha);
	}

	public void addListGraduation(List<RegistrationModel> newRegistration) {

		int tamanhoAntigo = getRowCount();
		registrationModel.addAll(newRegistration);
		fireTableRowsInserted(tamanhoAntigo, getRowCount() - 1);
	}

	public void limpar() {
		registrationModel.clear();
		fireTableDataChanged();
	}

	public boolean isEmpty() {
		return registrationModel.isEmpty();
	}
	
}
