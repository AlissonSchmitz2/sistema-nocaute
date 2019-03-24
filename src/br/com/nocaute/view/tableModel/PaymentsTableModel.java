package br.com.nocaute.view.tableModel;

import br.com.nocaute.model.RegistrationModel;

public class PaymentsTableModel extends AbstractTableModel<RegistrationModel>{
	private static final long serialVersionUID = 8326282879975874840L;

	public PaymentsTableModel() {
		super(new String[] { "Matr�cula", "Aluno", "Vencimento", "Valor", "Pagamento", "Cancelamento" });
	}
	
	@Override
	protected void setModelValueAt(int columnIndex, RegistrationModel model, Object aValue) {
		switch (columnIndex) {
			case 0:
				model.setRegistrationCode(Integer.parseInt(aValue.toString()));
			case 1:
				model.setStudentCode(Integer.parseInt(aValue.toString()));
			default:
				System.err.println("�ndice da coluna inv�lido");
		}
	}

	@Override
	protected Object getModelValueAt(int columnIndex, RegistrationModel model) {
		String valueObject = null;
		
		switch (columnIndex) {
		case 0:
			valueObject = model.getRegistrationCode().toString();
			break;
		default:
			System.err.println("�ndice da coluna inv�lido");
		}

		return valueObject;
	}
	
}
