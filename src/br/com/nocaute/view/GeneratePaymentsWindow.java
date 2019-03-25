package br.com.nocaute.view;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;

import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;

public class GeneratePaymentsWindow extends AbstractWindowFrame {
	private static final long serialVersionUID = 7934307046018321070L;
	
	//Componentes
	private JLabel label;
	private JButton btnGeneratePay;
	
	private ImageIcon icon = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/16x16/novo.png"));

	public GeneratePaymentsWindow(JDesktopPane desktop) {
		super("Gerar Faturas", 270, 120, desktop);
		setFrameIcon(icon);

		criarComponentes();
	}

	private void criarComponentes() {

		label = new JLabel("Data de Fatura: ");
		label.setBounds(5, 10, 150, 25);
		getContentPane().add(label);
		
		JMonthChooser jMonthChooser = new JMonthChooser();
		jMonthChooser.setBounds(90, 10, 150, 25);
		getContentPane().add(jMonthChooser);
		
		JYearChooser jYearChooser = new JYearChooser();
		jYearChooser.setBounds(190, 10, 50, 25);
		getContentPane().add(jYearChooser);
		
		btnGeneratePay = new JButton("Gerar Faturas");
		btnGeneratePay.setBounds(90, 45, 150, 25);
		btnGeneratePay.setIcon(icon);
		btnGeneratePay.setToolTipText("Gerar as faturas");
		getContentPane().add(btnGeneratePay);

	}

}
