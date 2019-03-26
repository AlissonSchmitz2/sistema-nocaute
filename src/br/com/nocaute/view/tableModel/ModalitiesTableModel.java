package br.com.nocaute.view.tableModel;

import br.com.nocaute.model.ModalityModel;

public class ModalitiesTableModel extends AbstractTableModel<ModalityModel> {
	private static final long serialVersionUID = -669085648646330786L;

	public ModalitiesTableModel() {
		super(new String[] { "Modalidade" });
	}
	
	@Override
	protected void setObjectValueAt(int columnIndex, ModalityModel model, Object aValue) {
		switch (columnIndex) {
			case 0:
				model.setName(aValue.toString());
			default:
				System.err.println("Índice da coluna inválido");
		}
	}

	@Override
	protected Object getObjectValueAt(int columnIndex, ModalityModel model) {
		String valueObject = null;
		
		switch (columnIndex) {
		case 0:
			valueObject = model.getName();
			break;
		default:
			System.err.println("Índice da coluna inválido");
		}

		return valueObject;
	}

}
