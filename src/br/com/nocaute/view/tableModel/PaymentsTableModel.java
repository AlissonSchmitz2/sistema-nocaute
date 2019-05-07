package br.com.nocaute.view.tableModel;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.nocaute.dao.RegistrationDAO;
import br.com.nocaute.dao.StudentDAO;
import br.com.nocaute.model.InvoicesRegistrationModel;
import br.com.nocaute.model.RegistrationModel;
import br.com.nocaute.model.StudentModel;

public class PaymentsTableModel extends AbstractTableModel<InvoicesRegistrationModel> {
	private static final long serialVersionUID = 8326282879975874840L;

	private StudentDAO studentDAO;
	private RegistrationDAO registrationDAO;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	public PaymentsTableModel(Connection CONNECTION) {
		super(new String[] { "Matrícula", "Aluno", "Vencimento", "Valor", "Pagamento", "Cancelamento" });

		try {
			studentDAO = new StudentDAO(CONNECTION);
			registrationDAO = new RegistrationDAO(CONNECTION);
		} catch (SQLException error) {
			error.printStackTrace();
		}
	}

	@Override
	protected void setObjectValueAt(int columnIndex, InvoicesRegistrationModel model, Object aValue) {
		switch (columnIndex) {
		case 0:
			model.setRegistrationCode(Integer.parseInt(aValue.toString()));
			break;
		case 1:
			break;
		case 2:
			model.setDueDate(new Date((long) aValue));
			break;
		case 3:
			model.setValue(Float.parseFloat(aValue.toString()));
			break;
		case 4:
			model.setPaymentDate(new Date((long) aValue));
			break;
		case 5:
			model.setCancellationDate(new Date((long) aValue));
			break;
		default:
			System.err.println("Índice da coluna inválido");
		}
	}

	@Override
	protected Object getObjectValueAt(int columnIndex, InvoicesRegistrationModel model) {
		String valueObject = null;

		switch (columnIndex) {
		case 0:
			valueObject = model.getRegistrationCode().toString();
			break;
		case 1:
			valueObject = getStudentName(model.getRegistrationCode());
			break;
		case 2:
			valueObject = dateFormat.format(model.getDueDate());
			break;
		case 3:
			valueObject = String.valueOf(NumberFormat.getCurrencyInstance().format(model.getValue()));
			break;
		case 4:
			if (model.getPaymentDate() != null) {
				valueObject = dateFormat.format(model.getPaymentDate());
			}
			break;
		case 5:
			if (model.getCancellationDate() != null) {
				valueObject = dateFormat.format(model.getCancellationDate());
			}
			break;
		default:
			System.err.println("Índice da coluna inválido");
		}

		return valueObject;
	}

	private String getStudentName(Integer registrationCode) {
		try {
			RegistrationModel registrationModel = registrationDAO.findById(registrationCode);
			StudentModel studentModel = studentDAO.findById(registrationModel.getStudentCode());
			return studentModel.getName();
		} catch (SQLException error) {
			error.printStackTrace();
		}

		return "";
	}

}
