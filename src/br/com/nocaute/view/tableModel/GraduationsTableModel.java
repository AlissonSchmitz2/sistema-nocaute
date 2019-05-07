package br.com.nocaute.view.tableModel;

import br.com.nocaute.model.GraduationModel;

public class GraduationsTableModel extends AbstractTableModel<GraduationModel> {
	private static final long serialVersionUID = 2381816919838563117L;

	public GraduationsTableModel() {
		super(new String[] { "Gradua��o" });
	}

	@Override
	protected void setObjectValueAt(int columnIndex, GraduationModel model, Object aValue) {
		switch (columnIndex) {
		case 0:
			model.setGraduationName(aValue.toString());
		default:
			System.err.println("�ndice da coluna inv�lido");
		}
	}

	@Override
	protected Object getObjectValueAt(int columnIndex, GraduationModel model) {
		String valueObject = null;

		switch (columnIndex) {
		case 0:
			valueObject = model.getName();
			break;
		default:
			System.err.println("�ndice da coluna inv�lido");
		}

		return valueObject;
	}
}
