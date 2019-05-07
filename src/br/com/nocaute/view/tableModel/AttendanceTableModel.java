package br.com.nocaute.view.tableModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.nocaute.model.AttendanceModel;

public class AttendanceTableModel extends AbstractTableModel<AttendanceModel> {
	private static final long serialVersionUID = 800709970786418323L;

	public AttendanceTableModel() {
		super(new String[] { "Assiduidade" });
	}

	@Override
	protected void setObjectValueAt(int columnIndex, AttendanceModel object, Object aValue) {
		switch (columnIndex) {
		case 0:
			object.setEnterDate((Date) aValue);
		default:
			System.err.println("Índice da coluna inválido");
		}
	}

	@Override
	protected Object getObjectValueAt(int columnIndex, AttendanceModel object) {
		String valueObject = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		switch (columnIndex) {
		case 0:
			valueObject = dateFormat.format(object.getEnterDate());
			break;
		default:
			System.err.println("Índice da coluna inválido");
		}

		return valueObject;
	}
}
