package br.com.nocaute.view.tableModel;

import br.com.nocaute.model.UserModel;

public class UsersTableModel extends AbstractTableModel<UserModel>{

	public UsersTableModel() {
		super(new String[] {"ID","Usuário","Perfil"});
	}

	private static final long serialVersionUID = 1L;

	@Override
	protected void setObjectValueAt(int columnIndex, UserModel model, Object aValue) {
		switch (columnIndex) {
		case 0:
			model.setCode(Integer.parseInt(aValue.toString()));
		case 1:
			model.setUser(aValue.toString());
		case 2:
			model.setProfile(aValue.toString());
		default:
			System.err.println("Índice da coluna inválido");
	  }
	}

	@Override
	protected Object getObjectValueAt(int columnIndex, UserModel model) {
		String valueObject = null;
		
		switch (columnIndex) {
		case 0:
			valueObject = model.getCode().toString();
			break;
		case 1:
			valueObject = model.getUser();
			break;
		case 2:
			valueObject = model.getProfile();
		    break;
		default:
			System.err.println("Índice da coluna inválido");
		}

		return valueObject;
	}

}
