package br.com.nocaute.view;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JLabel;

import br.com.nocaute.image.MasterImage;

public class DialogLoadingFormWindow extends JDialog {
	private static final long serialVersionUID = 7456850004230329058L;

	private JLabel label, gifLoader;

	public DialogLoadingFormWindow(String msg) {
		
		setTitle("Aguarde");
		setLayout(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(new Dimension(500, 170));
		setLocationRelativeTo(null);
		setResizable(false);
		// setModal(true);

		gifLoader = new JLabel(MasterImage.loading);
		gifLoader.setBounds(15, 10, 100, 100);
		add(gifLoader);

		label = new JLabel(msg);
		label.setBounds(160, 35, 250, 50);
		add(label);
		
		setVisible(true);
	}
}
