package br.com.nocaute.view.tableModel;

import br.com.nocaute.model.GraduationModel;

public class GraduationTableModel extends AbstractTableModel<GraduationModel> {
	private static final long serialVersionUID = 2381816919838563117L;
	
	public GraduationTableModel() {
		super(new String[] { "Graduação" });
	}
	
	@Override
	protected void setModelValueAt(int columnIndex, GraduationModel model, Object aValue) {
		switch (columnIndex) {
			case 0:
				model.setGraduationId(Integer.parseInt(aValue.toString()));
			case 1:
				model.setGraduationName(aValue.toString());
			case 2:
				model.setModalityId(Integer.parseInt(aValue.toString()));
			default:
				System.err.println("Índice da coluna inválido");
		}
	}

	@Override
	protected Object getModelValueAt(int columnIndex, GraduationModel model) {
		String valueObject = null;
		
		switch (columnIndex) {
			case 0:
				valueObject = model.getGraduationId().toString();
				break;
			default:
				System.err.println("Índice da coluna inválido");
		}

		return valueObject;
	}
}
