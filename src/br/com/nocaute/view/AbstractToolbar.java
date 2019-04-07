package br.com.nocaute.view;


import javax.swing.JButton;
import javax.swing.JDesktopPane;

import br.com.nocaute.image.MasterImage;

public abstract class AbstractToolbar extends AbstractGridWindow {
	private static final long serialVersionUID = 4720760680542568969L;

	protected JButton btnBuscar, btnAdicionar, btnRemover, btnSalvar;
	
	public AbstractToolbar(String nameWindow, int width, int height, JDesktopPane desktop, boolean activeFrame) {
		super(nameWindow, width, height, desktop, activeFrame);
		
		createComponents();
	}
	
	protected abstract void setButtonsActions();
	
	private void createComponents() {
		btnBuscar = new JButton("Buscar", MasterImage.search_22x22);
		btnBuscar.setBounds(15, 5, 95, 40);
		btnBuscar.setToolTipText("Clique aqui para buscar");
		getContentPane().add(btnBuscar);

		btnAdicionar = new JButton("Adicionar", MasterImage.add_22x22);
		btnAdicionar.setBounds(110, 5, 110, 40);
		btnAdicionar.setToolTipText("Clique aqui para adicionar");
		getContentPane().add(btnAdicionar);

		btnRemover = new JButton("Remover", MasterImage.remove_22x22);
		btnRemover.setBounds(220, 5, 110, 40);
		btnRemover.setToolTipText("Clique aqui para remover");
		getContentPane().add(btnRemover);
		btnRemover.setEnabled(false);

		btnSalvar = new JButton("Salvar", MasterImage.save_22x22);
		btnSalvar.setBounds(330, 5, 95, 40);
		btnSalvar.setToolTipText("Clique aqui para salvar");
		getContentPane().add(btnSalvar);
		btnSalvar.setEnabled(false);
	}
}
