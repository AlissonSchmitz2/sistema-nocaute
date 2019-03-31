package br.com.nocaute.view.tableModel;

import java.util.Date;

import br.com.nocaute.model.RegistrationModel;

public class PaymentsSituationTableModel extends AbstractTableModel<RegistrationModel> {
	private static final long serialVersionUID = 8414805286948636210L;

	public PaymentsSituationTableModel() {
		super(new String[] { "Vencimento", "Valor", "Pagamento", "Cancelamento" });
	}

	@Override
	protected void setObjectValueAt(int columnIndex, RegistrationModel model, Object aValue) {
		switch (columnIndex) {
		case 0:
			model.setExpirationDay(Integer.parseInt(aValue.toString()));
			break;
		case 1:
			//TODO: Valor do plano
			//model.setExpirationDay(Integer.parseInt(aValue.toString()));
			break;
		case 2:
			//TODO: Data do pagamento
			//model.setExpirationDay(Integer.parseInt(aValue.toString()));
			break;
		case 3:
			model.setClosingDate((Date) aValue);
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
			valueObject = model.getExpirationDay().toString();
			break;
		case 1:
			//TODO: Valor do plano 
			//valueObject = model.getExpirationDay().toString();
			break;
		case 2:
			//TODO: Data de pagamento
			//valueObject = model.getExpirationDay().toString();
			break;
		case 3:
			valueObject = model.getClosingDate().toString();
			break;
		default:
			System.err.println("Índice da coluna inválido");
		}

		return valueObject;
	}

}
