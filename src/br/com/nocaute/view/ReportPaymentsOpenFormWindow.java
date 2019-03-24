package br.com.nocaute.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDesktopPane;

public class ReportPaymentsOpenFormWindow extends AbstractReportWindowFrame{
	private static final long serialVersionUID = -49128325842856068L;

	public ReportPaymentsOpenFormWindow(JDesktopPane desktop) {
		super("Faturas em Aberto", desktop);
		
		// Seta as ações esperadas para cada botão
		setButtonsActions();
	}

	protected void setButtonsActions() {
		// Recuperar data do campo. Esse método é chamado 
		// na primeira vez que executa o sistema
		//SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy"); 
		//String novaData = formatador.format(jDateChooser.getDate());
		
		// Ação Processar relatorio
		btnProcessar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: processar relatorio
			}
		});
	}
}
