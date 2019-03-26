package br.com.nocaute.view.tableModel;

import br.com.nocaute.model.RegistrationModel;

public class RegistrationsTableModel extends AbstractTableModel<RegistrationModel> {
	private static final long serialVersionUID = 1L;

	public RegistrationsTableModel() {
		super(new String[] { "Matr�cula", "Aluno" });
	}

	@Override
	protected void setModelValueAt(int columnIndex, RegistrationModel model, Object aValue) {
		switch (columnIndex) {
			case 0:
				model.setRegistrationCode(Integer.parseInt(aValue.toString()));
			case 1:
				model.setStudentCode(Integer.parseInt(aValue.toString()));
			default:
				System.err.println("�ndice da coluna inv�lido");
		}
	}

	@Override
	protected Object getModelValueAt(int columnIndex, RegistrationModel model) {
		String valueObject = null;
		
		switch (columnIndex) {
		case 0:
			valueObject = model.getRegistrationCode().toString();
			break;
		case 1:
			valueObject = model.getStudent().getName();
			break;
		default:
			System.err.println("�ndice da coluna inv�lido");
		}

		return valueObject;
	}
}
