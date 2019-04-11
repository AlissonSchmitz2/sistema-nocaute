package br.com.nocaute.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class BackupWindow extends AbstractWindowFrame {

	private static final long serialVersionUID = -1385951057873535714L;

	// Componentes
	private JTextField txfPath;
	private JButton btnFilechooser, btnInit;
	private JFileChooser fileChooserPathBackup;
	private JLabel label;
	private int returnPath;
	private File filePath;
	private JDesktopPane desktop;

	public BackupWindow(JDesktopPane desktop) {

		super("Backup e Restore", 650, 210, desktop);
		this.desktop = desktop;

		setIconifiable(false);
		
		createComponents();

		// Seta as ações esperadas para cada botão
		setButtonsActions();
	}

	private void setButtonsActions() {

		btnFilechooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooserPathBackup = new JFileChooser();
				fileChooserPathBackup.setDialogTitle("Selecione o caminho para salvar");
				fileChooserPathBackup.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				returnPath = fileChooserPathBackup.showOpenDialog(null);

				if (returnPath == JFileChooser.APPROVE_OPTION) {
					filePath = fileChooserPathBackup.getSelectedFile();

					if (!filePath.exists()) {
						int option = JOptionPane.showConfirmDialog(null,
								"Não existe o caminho informado, deseja criar a pasta?", "Aviso",
								JOptionPane.YES_NO_OPTION);

						if (option == 0) {
							File newPath = new File(filePath.getPath());
							newPath.mkdirs();
						} else {
							return;
						}

					}

					txfPath.setText(filePath.getPath());
				} else {
					System.out.println("CANCELOU A SELEÇÃO DO ARQUIVO");
				}
			}
		});

		btnInit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (!validateOpenWindows()) {
					bubbleError("Feche todas as janelas do sistema para realizar o backup!");
				}

				initBackup();
			}
		});
	}

	private void createComponents() {
		label = new JLabel("Backup e Restore");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setBounds(70, 20, 500, 25);
		label.setFont(new Font("Arial", Font.PLAIN, 18));
		getContentPane().add(label);

		label = new JLabel("Obs: Para realizar o backup todas as janelas do sistema devem estar fechadas.");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setBounds(70, 50, 500, 25);
		label.setForeground(Color.red);
		getContentPane().add(label);

		txfPath = new JTextField();
		txfPath.setBounds(30, 90, 520, 25);
		txfPath.setText("Caminho do backup");
		txfPath.setEnabled(false);
		getContentPane().add(txfPath);

		btnFilechooser = new JButton("...");
		btnFilechooser.setBounds(560, 90, 50, 25);
		getContentPane().add(btnFilechooser);

		btnInit = new JButton("Iniciar");
		btnInit.setBounds(270, 140, 100, 25);
		getContentPane().add(btnInit);

	}

	private void initBackup() {
		// TODO: realizar backup
	}

	private boolean validateOpenWindows() {
		int countWindow = 0;
		
		for (JInternalFrame frames : desktop.getAllFrames()) {
			if (frames.getClass().toString().equalsIgnoreCase(frames.getClass().toString())) {
				countWindow++;
			}
		}

		if (countWindow != 2) {
			return false;
		}

		return true;
	}

}
