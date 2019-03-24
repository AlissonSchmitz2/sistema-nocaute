package br.com.nocaute.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDesktopPane;

public class ReportRegistrationFormWindow extends AbstractReportWindowFrame {
	private static final long serialVersionUID = -4201960150625152379L;

	public ReportRegistrationFormWindow(JDesktopPane desktop) {
		super("Relat�rio de Matricula", desktop);
		
		// Seta as a��es esperadas para cada bot�o
		setButtonsActions();
	}

	protected void setButtonsActions() {
		// Recuperar data do campo. Esse m�todo � chamado 
		// na primeira vez que executa o sistema
		//SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy"); 
		//String novaData = formatador.format(jDateChooser.getDate());
		
		// A��o Processar relatorio
		btnProcessar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: processar relatorio
			}
		});
	}
	
}
