package br.com.nocaute.view.tableModel;

import br.com.nocaute.model.PlanModel;

public class PlansTableModel extends AbstractTableModel<PlanModel> {
	private static final long serialVersionUID = 2972565006783519352L;

	public PlansTableModel() {
		super(new String[] { "Plano", "Modalidade" });
	}

	@Override
	protected void setObjectValueAt(int columnIndex, PlanModel model, Object aValue) {
		switch (columnIndex) {
		case 0:
			model.setName(aValue.toString());
		case 1:
			// model.setModality(aValue.toString());
		default:
			System.err.println("Índice da coluna inválido");
		}
	}

	@Override
	protected Object getObjectValueAt(int columnIndex, PlanModel model) {
		String valueObject = null;

		switch (columnIndex) {
		case 0:
			valueObject = model.getName();
			break;
		case 1:
			valueObject = model.getModality().getName();
			break;
		default:
			System.err.println("Índice da coluna inválido");
		}

		return valueObject;
	}
}
