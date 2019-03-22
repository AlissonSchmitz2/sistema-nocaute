package br.com.nocaute.view;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.text.MaskFormatter;

public class StudentRegistrationAddModalitiesWindow extends AbstractWindowFrame {
	private static final long serialVersionUID = -4201960150625152379L;

	// Componentes
	private JButton btnOk;
	private JLabel label;
	private JFormattedTextField txfDtInicio, txfDtFim;
	private JComboBox<String> cbxModalidade, cbxGraduacao, cbxPlano;
	
	private ImageIcon iconJanela = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/16x16/estudante.png"));
	private ImageIcon iconOK = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/13x13/ok.png"));

	public StudentRegistrationAddModalitiesWindow(JDesktopPane desktop) {
		super("Adicionar Modalidades", 300, 225, desktop);
		setFrameIcon(iconJanela);

		criarComponentes();
	}
	
	private void criarComponentes() {
		
		label = new JLabel("Modalidade: ");
		label.setBounds(5, 10, 100, 25);
		getContentPane().add(label);
	
		cbxModalidade = new JComboBox<String>();
		cbxModalidade.addItem("-- Selecione --");
		cbxModalidade.setBounds(70, 10, 205, 20);
		cbxModalidade.setToolTipText("Informe a modalidade");
		getContentPane().add(cbxModalidade);
		
		label = new JLabel("Graduação: ");
		label.setBounds(5, 40, 100, 25);
		getContentPane().add(label);
	
		cbxGraduacao = new JComboBox<String>();
		cbxGraduacao.addItem("-- Selecione --");
		cbxGraduacao.setBounds(70, 40, 205, 20);
		cbxGraduacao.setToolTipText("Informe a graduação");
		getContentPane().add(cbxGraduacao);
		
		label = new JLabel("Plano: ");
		label.setBounds(5, 70, 100, 25);
		getContentPane().add(label);
	
		cbxPlano = new JComboBox<String>();
		cbxPlano.addItem("-- Selecione --");
		cbxPlano.setBounds(70, 70, 205, 20);
		cbxPlano.setToolTipText("Informe o plano");
		getContentPane().add(cbxPlano);
		
		label = new JLabel("Data Início: ");
		label.setBounds(5, 100, 100, 25);
		getContentPane().add(label);
		
		label = new JLabel("Data Fim: ");
		label.setBounds(5, 130, 100, 25);
		getContentPane().add(label);
		
		try {
			txfDtInicio = new JFormattedTextField(new MaskFormatter("     ## /  ##  / ####       "));
			txfDtInicio.setFocusLostBehavior(JFormattedTextField.COMMIT);
			txfDtInicio.setBounds(70, 100, 100, 20);
			txfDtInicio.setToolTipText("Data de início");
			getContentPane().add(txfDtInicio);
			
			txfDtFim = new JFormattedTextField(new MaskFormatter("     ## /  ##  / ####       "));
			txfDtFim.setFocusLostBehavior(JFormattedTextField.COMMIT);
			txfDtFim.setBounds(70, 130, 100, 20);
			txfDtFim.setToolTipText("Data do fim da matrícula");
			getContentPane().add(txfDtFim);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		btnOk = new JButton("OK");
		btnOk.setBounds(100, 160, 100, 25);
		btnOk.setIcon(iconOK);
		btnOk.setToolTipText("Clique aqui para confirmar");
		getContentPane().add(btnOk);
		
	}	
}