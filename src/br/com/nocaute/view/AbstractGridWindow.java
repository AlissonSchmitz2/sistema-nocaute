package br.com.nocaute.view;

import javax.swing.JDesktopPane;
import javax.swing.JScrollPane;

public abstract class AbstractGridWindow extends AbstractWindowFrame  {
	private static final long serialVersionUID = -8203026366064920547L;
	
	protected JScrollPane grid = null;
	
	public AbstractGridWindow(String nomeTela, int width, int height, JDesktopPane desktop) {
		super(nomeTela, width, height, desktop);
		
		//Abre a janela grid automaticamente
		desktop.add(this);
		showFrame();
	}
	
	public void redimensionarGrid(JScrollPane grid, int x, int y, int width, int height) {
		grid.setBounds(x, y, width, height);
		
	}
	
	public JScrollPane getGridContent() {
		return grid;
	}
}
