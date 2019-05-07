package br.com.nocaute.view;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JDateChooser;

import br.com.nocaute.image.MasterImage;

public abstract class AbstractReportWindowFrame extends AbstractWindowFrame {
	private static final long serialVersionUID = -4201960150625152379L;

	// Componentes
	protected JButton btnProcess;
	protected JLabel label;
	// Refatorar para padrao
	protected JComboBox<String> cbxType;

	protected JDateChooser jDateChooserDe = null, jDateChooserAte = null;
	protected JPanel panel;

	public AbstractReportWindowFrame(String nameWindow, JDesktopPane desktop) {
		super(nameWindow, 220, 210, desktop);
		setFrameIcon(MasterImage.report_16x16);

		createComponents();
	}

	protected abstract void setButtonsActions();

	private void createComponents() {

		panel = new JPanel();
		panel.setBounds(new Rectangle(25, 15, 150, 110));
		panel.setBackground(Color.WHITE);
		panel.setLayout(null);
		TitledBorder border = BorderFactory.createTitledBorder("Período");
		border.setTitleJustification(2); // Justificar texto no centro
		panel.setBorder(border);
		getContentPane().add(panel);

		label = new JLabel("De: ");
		label.setBounds(15, 30, 50, 25);
		panel.add(label);

		jDateChooserDe = new JDateChooser(new Date());
		jDateChooserDe.setBounds(40, 30, 90, 20);
		jDateChooserDe.setDateFormatString("dd/MM/yyyy");
		panel.add(jDateChooserDe);

		label = new JLabel("Até: ");
		label.setBounds(15, 65, 50, 25);
		panel.add(label);

		jDateChooserAte = new JDateChooser(new Date());
		jDateChooserAte.setBounds(40, 65, 90, 20);
		jDateChooserAte.setDateFormatString("dd/MM/yyyy");
		panel.add(jDateChooserAte);

		cbxType = new JComboBox<String>();
		cbxType.addItem("HTML");
		cbxType.setBounds(10, 145, 70, 20);
		cbxType.setToolTipText("Tipo do relatório");
		getContentPane().add(cbxType);

		btnProcess = new JButton("Processar", MasterImage.report_16x16);
		btnProcess.setBounds(85, 143, 110, 24);
		btnProcess.setToolTipText("Gerar Relatório");
		getContentPane().add(btnProcess);

	}

}
