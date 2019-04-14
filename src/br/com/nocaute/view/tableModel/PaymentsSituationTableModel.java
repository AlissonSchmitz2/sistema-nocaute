package br.com.nocaute.view.tableModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.nocaute.model.InvoicesRegistrationModel;

public class PaymentsSituationTableModel extends AbstractTableModel<InvoicesRegistrationModel> {
	private static final long serialVersionUID = 8414805286948636210L;

	public PaymentsSituationTableModel() {
		super(new String[] { "Vencimento", "Valor", "Pagamento", "Cancelamento" });
	}

	@Override
	protected void setObjectValueAt(int columnIndex, InvoicesRegistrationModel model, Object aValue) {
		switch (columnIndex) {
		case 0:
			model.setDueDate((Date) aValue);
			break;
		case 1:
			model.setValue((Float) aValue);
			break;
		case 2:
			model.setPaymentDate((Date) aValue);
			break;
		case 3:
			model.setCancellationDate((Date) aValue);
			break;
		default:
			System.err.println("Índice da coluna inválido");
		}
	}

	@Override
	protected Object getObjectValueAt(int columnIndex, InvoicesRegistrationModel model) {
		String valueObject = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		switch (columnIndex) {
		case 0:
			valueObject = dateFormat.format(model.getDueDate());
			break;
		case 1:
			valueObject = String.valueOf(model.getValue());
		case 2:
			if (model.getPaymentDate() != null) {
				valueObject = dateFormat.format(model.getPaymentDate());
			}
			break;
		case 3:
			if (model.getCancellationDate() != null) {
				valueObject = dateFormat.format(model.getCancellationDate());
			}
			break;
		default:
			System.err.println("Índice da coluna inválido");
		}

		return valueObject;
	}

}
