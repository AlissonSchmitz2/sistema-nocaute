package br.com.nocaute.view.tableModel;

import br.com.nocaute.model.CityModel;

public class CityTableModel extends AbstractTableModel<CityModel> {
	private static final long serialVersionUID = 1242843030000425873L;
	
	public CityTableModel() {
		super(new String[] { "Nome", "Estado" });
	}

	@Override
	protected void setModelValueAt(int columnIndex, CityModel model, Object aValue) {
		switch (columnIndex) {
			case 0:
				model.setName(aValue.toString());
			case 1:
				model.setState(aValue.toString());
			default:
				System.err.println("Índice da coluna inválido");
		}
	}

	@Override
	protected Object getModelValueAt(int columnIndex, CityModel model) {
		String valueObject = null;
		
		switch (columnIndex) {
		case 0:
			valueObject = model.getName();
			break;
		case 1:
			valueObject = model.getState();
			break;
		default:
			System.err.println("Índice da coluna inválido");
		}

		return valueObject;
	}
}