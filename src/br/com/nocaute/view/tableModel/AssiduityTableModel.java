package br.com.nocaute.view.tableModel;

import java.text.SimpleDateFormat;
import java.sql.Timestamp;

import br.com.nocaute.model.AssiduityModel;

public class AssiduityTableModel extends AbstractTableModel<AssiduityModel> {

	public AssiduityTableModel() {
		super(new String[] { "Assiduidade" });
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void setObjectValueAt(int columnIndex, AssiduityModel model, Object aValue) {
		switch (columnIndex) {
		case 0:
			model.setInputDate((Timestamp) aValue);
			break;
		default:
			System.err.println("Índice da coluna inválido");
		}
	}

	@Override
	protected Object getObjectValueAt(int columnIndex, AssiduityModel model) {
		String valueObject = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		switch (columnIndex) {
		case 0:
			if (model.getInputDate() != null) {
				valueObject = dateFormat.format(model.getInputDate());
			}
			break;
		default:
			System.err.println("Índice da coluna inválido");
		}

		return valueObject;
	}

}