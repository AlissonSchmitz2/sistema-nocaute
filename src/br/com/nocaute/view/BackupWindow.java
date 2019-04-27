package br.com.nocaute.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import br.com.nocaute.image.MasterImage;

public class BackupWindow extends AbstractWindowFrame {

	private static final long serialVersionUID = -1385951057873535714L;

	// Componentes
	private JTextField txfPath;
	private JButton btnFilechooser, btnInit;
	private JFileChooser fileChooser;
	private FileNameExtensionFilter filter = null; // Filtro filechooser
	private JLabel label;
	private int returnPath;
	private File filePath;
	private JDesktopPane desktop;

	// Painel
	private JPanel panel;
	private ButtonGroup btnGroup;
	private JRadioButton radioBtnBackup, radioBtnRestore;

	public BackupWindow(JDesktopPane desktop) {

		super("Backup e Restore", 650, 310, desktop);
		this.desktop = desktop;

		setFrameIcon(MasterImage.backup_restore_16x16);
		setIconifiable(false);

		createComponents();

		// Seta as ações esperadas para cada botão
		setButtonsActions();
	}

	private void setButtonsActions() {

		btnFilechooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooser = new JFileChooser();

				if (radioBtnBackup.isSelected()) {
					fileChooserBackup();
				} else {
					txfPath.setText("Arquivo de restore");
					fileChooserRestore();
				}

			}
		});

		radioBtnBackup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				txfPath.setText("Caminho do Backup.");
			}
		});

		radioBtnRestore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				txfPath.setText("Arquivo de Restore.");
			}
		});

		btnInit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (!validateOpenWindows()) {
					bubbleError("Feche todas as janelas do sistema para realizar o backup ou restore!");
					return;
				}else if(txfPath.getText().contains(" ")) {
					bubbleError("Informe o caminho do backup ou restore!");
					return;
				}

				initBackupRestore(radioBtnBackup.isSelected());
			}
		});
	}

	private void createComponents() {
		label = new JLabel("Backup e Restore");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setBounds(70, 20, 500, 25);
		label.setFont(new Font("Arial", Font.PLAIN, 18));
		getContentPane().add(label);

		label = new JLabel("Obs: Para realizar o backup ou restore, todas as janelas do sistema devem estar fechadas.");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setBounds(70, 50, 520, 25);
		label.setForeground(Color.red);
		getContentPane().add(label);

		panel = new JPanel();
		panel.setBounds(new Rectangle(30, 90, 580, 60));
		panel.setBackground(Color.WHITE);
		panel.setLayout(null);
		panel.setBorder(BorderFactory.createTitledBorder("Opções"));
		getContentPane().add(panel);

		radioBtnBackup = new JRadioButton("Backup", true);
		radioBtnBackup.setBounds(180, 20, 100, 25);
		radioBtnBackup.setBackground(Color.WHITE);
		panel.add(radioBtnBackup);

		radioBtnRestore = new JRadioButton("Restore", false);
		radioBtnRestore.setBounds(320, 20, 100, 25);
		radioBtnRestore.setBackground(Color.WHITE);
		panel.add(radioBtnRestore);

		btnGroup = new ButtonGroup();
		btnGroup.add(radioBtnBackup);
		btnGroup.add(radioBtnRestore);

		txfPath = new JTextField();
		txfPath.setBounds(30, 180, 520, 25);
		txfPath.setText("Caminho do Backup.");
		txfPath.setEnabled(false);
		getContentPane().add(txfPath);

		btnFilechooser = new JButton("...");
		btnFilechooser.setBounds(560, 180, 50, 25);
		getContentPane().add(btnFilechooser);

		btnInit = new JButton("Iniciar");
		btnInit.setBounds(270, 230, 100, 25);
		getContentPane().add(btnInit);

	}

	private void fileChooserBackup() {
		fileChooser.setDialogTitle("Selecione o caminho para salvar");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		returnPath = fileChooser.showOpenDialog(this);

		if (returnPath == JFileChooser.APPROVE_OPTION) {
			filePath = fileChooser.getSelectedFile();

			if (!filePath.exists()) {
				int option = JOptionPane.showConfirmDialog(null,
						"Não existe o caminho informado, deseja criar a pasta?", "Aviso", JOptionPane.YES_NO_OPTION);

				if (option == 0) {
					File newPath = new File(filePath.getPath());
					newPath.mkdirs();
				} else {
					return;
				}

			}

			txfPath.setText(filePath.getPath());
		} else {
			bubbleWarning("Backup Cancelado!");
		}
	}

	private void fileChooserRestore() {
		fileChooser.setDialogTitle("Selecione o arquivo para o restore");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // Apenas arquivos

		if (filter == null) {
			filter = new FileNameExtensionFilter("Backup Sistema Nocaute(*.nocaute)", "nocaute");
			fileChooser.setFileFilter(filter);
		}

		returnPath = fileChooser.showOpenDialog(this);

		if (returnPath == JFileChooser.APPROVE_OPTION) {
			filePath = fileChooser.getSelectedFile();

			if (!filePath.exists() || !filePath.getPath().endsWith(".nocaute")) {
				bubbleError("Caminho ou arquivo inválido!");
				fileChooserRestore(); // Recursividade para manter a janela filechooser aberta
				return;
			}

			txfPath.setText(filePath.getPath());
		} else {
			bubbleWarning("Restore Cancelado!");
		}

		filter = null;
	}

	private void initBackupRestore(boolean isBackup) {
		// TODO: realizar restore
		final String Backup = "start pg_dump -h localhost -p 5432 -U %2 -w --column-inserts --attribute-inserts -a -F c -b -v -f %pathNocaute% master\r\n";
		final String Restore = "start pg_restore -h localhost -p 5432 -U %2 -d master -v %pathNocaute%\r\n";
		
		String bat = 
		"@echo off\r\n" + 
		"set pathNocaute=" + txfPath.getText() + (isBackup ? "\\Backup" + getDateTime() + ".nocaute\r\n" : "\r\n") +
		"set PGPASSWORD=%2\r\n" + 
		"set path=C:\\Program Files (x86)\\PostgreSQL\\9.0\\bin;%path%\r\n" + 
		(isBackup ? Backup : Restore) +
		"/MIN\r\n" +
		"set path=%path_old%\r\n" + "set path_old=\r\n" + 
		"set PGPASSWORD=\r\n" + 
		"exit";
			
			FileWriter filewriter;
			try {
				File file = new File(System.getProperty("user.home") + "\\desktop\\nocaute.bat");
				filewriter = new FileWriter(file);
				filewriter.write(bat);
				filewriter.close();

				//Executa o arquivo bat
				Process lo_process = Runtime.getRuntime().exec(System.getProperty("user.home") + "\\desktop\\nocaute.bat admin admin");
				lo_process.waitFor();
				// Aguarda até ser finalizado.
				file.delete();
				
				bubbleSuccess("Backup realizado com sucesso!" + txfPath.getText());
				
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}

	}
	
	private String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("HHmmss_yyyy_MM_dd");
		Date date = new Date();
		return dateFormat.format(date);
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
