package br.com.nocaute.view;

import java.awt.Color;
import java.awt.Rectangle;
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

public abstract class AbstractReportWindowFrame extends AbstractWindowFrame {
	private static final long serialVersionUID = -4201960150625152379L;

	// Componentes
	protected JButton btnProcessar;
	protected JLabel label;
	// Refatorar para padrao
	protected JComboBox<String> cbxTipo;
	
	protected JDateChooser jDateChooserDe = null, jDateChooserAte = null;
	protected JPanel painel;

	// Icones
	protected ImageIcon iconJanela = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/16x16/relatorio.png"));

	public AbstractReportWindowFrame(String nameWindow, JDesktopPane desktop) {
		super(nameWindow, 220, 210, desktop);
		setFrameIcon(iconJanela);

		criarComponentes();
		
		// Seta as ações esperadas para cada botão
		setButtonsActions();
	}

	protected abstract void setButtonsActions(); 

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

		jDateChooserDe = new JDateChooser(new Date());
		jDateChooserDe.setBounds(40, 30, 90, 20);
		jDateChooserDe.setDateFormatString("dd/MM/yyyy");
		painel.add(jDateChooserDe);

		label = new JLabel("Até: ");
		label.setBounds(15, 65, 50, 25);
		painel.add(label);

		jDateChooserAte = new JDateChooser(new Date());
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
