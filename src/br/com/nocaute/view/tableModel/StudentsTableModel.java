package br.com.nocaute.view.tableModel;

import br.com.nocaute.model.StudentModel;

public class StudentsTableModel extends AbstractTableModel<StudentModel> {
	private static final long serialVersionUID = 1242843030000425873L;
	
	public StudentsTableModel() {
		super(new String[] { "Código", "Aluno" });
	}

	@Override
	protected void setObjectValueAt(int columnIndex, StudentModel model, Object aValue) {
		switch (columnIndex) {
			case 0:
				model.setCode(Integer.parseInt(aValue.toString()));
			case 1:
				model.setName(aValue.toString());
			default:
				System.err.println("Índice da coluna inválido");
		}
	}

	@Override
	protected Object getObjectValueAt(int columnIndex, StudentModel model) {
		String valueObject = null;
		
		switch (columnIndex) {
		case 0:
			valueObject = model.getCode().toString();
			break;
		case 1:
			valueObject = model.getName();
			break;
		default:
			System.err.println("Índice da coluna inválido");
		}

		return valueObject;
	}
}
