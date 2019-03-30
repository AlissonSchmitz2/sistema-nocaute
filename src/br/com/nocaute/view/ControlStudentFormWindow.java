package br.com.nocaute.view;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JDesktopPane;
import javax.swing.JTextField;

public class ControlStudentFormWindow extends AbstractWindowFrame {
	private static final long serialVersionUID = -8041165617342297479L;

	private JTextField txfCodMatriculate, txfStudent;
	private JDesktopPane desktop;
	
	private static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	private static Rectangle screenRect = ge.getMaximumWindowBounds();
	private static int height = screenRect.height;// area total da altura tirando subtraindo o menu iniciar
	private static int width = screenRect.width;// area total da altura tirando subtraindo o menu iniciar

	public ControlStudentFormWindow(JDesktopPane desktop) {
		super("Controle de Alunos", width / 2 + 150, height - 50 ,desktop);
		
		this.desktop = desktop;
		
		setClosable(false);
		setIconifiable(true);
		
		setLocation((screenRect.width - this.getSize().width) / 2, 0);
		
		createComponents();
	}

	private void createComponents() {
		
	}

}