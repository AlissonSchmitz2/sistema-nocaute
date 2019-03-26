package br.com.nocaute.view.tableModel;

import br.com.nocaute.model.ModalityModel;

public class ModalityTableModel extends AbstractTableModel<ModalityModel> {
	private static final long serialVersionUID = -669085648646330786L;

	public ModalityTableModel() {
		super(new String[] { "Modalidade" });
	}
	
	@Override
	protected void setModelValueAt(int columnIndex, ModalityModel model, Object aValue) {
		switch (columnIndex) {
			case 0:
				model.setName(aValue.toString());
			default:
				System.err.println("Índice da coluna inválido");
		}
	}

	@Override
	protected Object getModelValueAt(int columnIndex, ModalityModel model) {
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
