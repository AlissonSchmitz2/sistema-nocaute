package br.com.nocaute.view.tableModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.nocaute.pojos.Graduation;
import br.com.nocaute.pojos.Modality;
import br.com.nocaute.pojos.Plan;
import br.com.nocaute.pojos.RegistrationModality;

public class StudentRegistrationModalitiesTableModel extends AbstractTableModel<RegistrationModality> {
	private static final long serialVersionUID = 5204634462348292204L;
	
	public StudentRegistrationModalitiesTableModel() {
		super(new String[] { "Modalidade", "Graduação", "Plano", "Data Início", "Data Fim" });
	}
	
	@Override
	protected void setObjectValueAt(int columnIndex, RegistrationModality object, Object aValue) {
		switch (columnIndex) {
			case 0:
				object.setModality((Modality) aValue);
				break;
			case 1:
				object.setGraduation((Graduation) aValue);
				break;
			case 2:
				object.setPlan((Plan) aValue);
				break;
			case 3:
				object.setStartDate((Date) aValue);
				break;
			case 4:
				object.setFinishDate((Date) aValue);
				break;
			default:
				System.err.println("Índice da coluna inválido");
		}
	}

	@Override
	protected Object getObjectValueAt(int columnIndex, RegistrationModality object) {
		String valueObject = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		switch (columnIndex) {
		case 0:
			valueObject = object.getModality().getName();
			break;
		case 1:
			valueObject = object.getGraduation().getName();
			break;
		case 2:
			valueObject = object.getPlan().getName();
			break;
		case 3:
			valueObject = dateFormat.format(object.getStartDate());
			break;
		case 4:
			if (object.getFinishDate() != null) {
				valueObject = dateFormat.format(object.getFinishDate());
			}
			break;
		default:
			System.err.println("Índice da coluna inválido");
		}

		return valueObject;
	}
}
