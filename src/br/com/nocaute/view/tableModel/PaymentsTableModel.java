package br.com.nocaute.view.tableModel;

import br.com.nocaute.model.RegistrationModel;

public class PaymentsTableModel extends AbstractTableModel<RegistrationModel>{
	private static final long serialVersionUID = 8326282879975874840L;

	public PaymentsTableModel() {
		super(new String[] { "Matrícula", "Aluno", "Vencimento", "Valor", "Pagamento", "Cancelamento" });
	}
	
	@Override
	protected void setObjectValueAt(int columnIndex, RegistrationModel model, Object aValue) {
		switch (columnIndex) {
			case 0:
				model.setRegistrationCode(Integer.parseInt(aValue.toString()));
				break;
			case 1:
				model.setStudentCode(Integer.parseInt(aValue.toString()));
				break;
			case 2:
				//model.setExpirationDay(Integer.parseInt(aValue.toString()));
				break;
			case 3:
				//model.set(Integer.parseInt(aValue.toString()));
				break;
			case 4:
				//model.setStudentCode(Integer.parseInt(aValue.toString()));
				break;
			case 5:
				//model.setClosingDate(Integer.parseInt(aValue.toString()));
				break;
			default:
				System.err.println("Índice da coluna inválido");
		}
	}

	@Override
	protected Object getObjectValueAt(int columnIndex, RegistrationModel model) {
		String valueObject = null;
		
		switch (columnIndex) {
		case 0:
			valueObject = model.getRegistrationCode().toString();
			break;
		case 1:
			valueObject = model.getStudentCode().toString();
			break;
		case 2:
			//model.setExpirationDay(Integer.parseInt(aValue.toString()));
			break;
		case 3:
			//model.set(Integer.parseInt(aValue.toString()));
			break;
		case 4:
			//model.setStudentCode(Integer.parseInt(aValue.toString()));
			break;
		case 5:
			//model.setClosingDate(Integer.parseInt(aValue.toString()));
			break;
		default:
			System.err.println("Índice da coluna inválido");
		}

		return valueObject;
	}
	
}
