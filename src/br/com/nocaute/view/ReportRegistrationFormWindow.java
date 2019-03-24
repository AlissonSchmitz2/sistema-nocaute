package br.com.nocaute.view;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JDateChooser;

public class ReportRegistrationFormWindow extends AbstractWindowFrame {
	private static final long serialVersionUID = -4201960150625152379L;

	// Componentes
	private JButton btnProcessar;
	private JLabel label;
	// Refatorar para padrao
	private JComboBox<String> cbxTipo;
	
	private JPanel painel;

	// Icones
	private ImageIcon iconJanela = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/16x16/relatorio.png"));

	public ReportRegistrationFormWindow(JDesktopPane desktop) {
		super("Relatório de Matricula", 220, 210, desktop);
		setFrameIcon(iconJanela);

		criarComponentes();

		// Seta as ações esperadas para cada botão
		setButtonsActions();
	}

	private void setButtonsActions() {
		// Ação Processar relatorio
		btnProcessar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: processar relatorio
			}
		});
	}

	private void criarComponentes() {
		
		painel = new JPanel();
		painel.setBounds(new Rectangle(25, 15, 150, 110));
		painel.setBackground(Color.WHITE);
		painel.setLayout(null);
		TitledBorder borda = BorderFactory.createTitledBorder("Período");
		borda.setTitleJustification(2); //Justificar texto no centro
		painel.setBorder(borda);
		getContentPane().add(painel);

		label = new JLabel("De: ");
		label.setBounds(15, 30, 50, 25);
		painel.add(label);

		JDateChooser jDateChooserDe = new JDateChooser(new Date());
		jDateChooserDe.setBounds(40, 30, 90, 20);
		jDateChooserDe.setDateFormatString("dd/MM/yyyy");
		painel.add(jDateChooserDe);

		label = new JLabel("Até: ");
		label.setBounds(15, 65, 50, 25);
		painel.add(label);

		JDateChooser jDateChooserAte = new JDateChooser(new Date());
		jDateChooserAte.setBounds(40, 65, 90, 20);
		jDateChooserAte.setDateFormatString("dd/MM/yyyy");
		painel.add(jDateChooserAte);

		cbxTipo = new JComboBox<String>();
		cbxTipo.addItem("HTML");
		cbxTipo.setBounds(10, 145, 70, 20);
		cbxTipo.setToolTipText("Tipo do relatório");
		getContentPane().add(cbxTipo);

		btnProcessar = new JButton("Processar");
		btnProcessar.setBounds(85, 143, 110, 24);
		btnProcessar.setIcon(iconJanela);
		btnProcessar.setToolTipText("Gerar Relatório");
		getContentPane().add(btnProcessar);

	}
	
}
