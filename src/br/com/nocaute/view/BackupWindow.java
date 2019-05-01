package br.com.nocaute.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
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
	private boolean pathValidate;
	
	// Auxiliares
	private File pathPostgres = null;

	// Painel
	private JPanel panel;
	private ButtonGroup btnGroup;
	private JRadioButton radioBtnBackup, radioBtnRestore;
	private Object obj;

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
				pathValidate = false;
			}
		});

		radioBtnRestore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				txfPath.setText("Arquivo de Restore.");
				pathValidate = false;
			}
		});

		btnInit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (!validateOpenWindows()) {
					bubbleError("Feche todas as janelas do sistema para realizar o backup ou restore!");
					return;
				} else if (!pathValidate) {
					bubbleError("Informe o caminho do backup ou restore!");
					return;
				}

				initBackupRestore();
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
			pathValidate = true;
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
			pathValidate = true;
		} else {
			bubbleWarning("Restore Cancelado!");
		}

		filter = null;
	}

	private void initBackupRestore() {
		
		//Desabilita o botão após iniciar
		btnInit.setEnabled(false);
		
		//Verifica se o PostgreSQL está instalado na pasta de 32 ou 64 bits
		if(getPathPostgres(System.getenv("ProgramFiles")) != null) {
			pathPostgres = getPathPostgres(System.getenv("ProgramFiles"));
		}else if(getPathPostgres(System.getenv("ProgramFiles(X86)")) != null){
			pathPostgres = getPathPostgres(System.getenv("ProgramFiles(X86)"));
		}else {
			bubbleError("Problema ao encontrar diretório do gerenciador de banco de dados!");
		}

		ProcessBuilder pb_backup = new ProcessBuilder(pathPostgres.getAbsolutePath() + "\\bin\\pg_dump.exe",
				"-h", "localhost", "-p", "5432", "-U", "admin", "--column-inserts", "--attribute-inserts", "-a",
				"-F", "c", "-b", "-v", "-f", txfPath.getText() + "\\Backup" + getDateTime() + ".nocaute", "master");

		ProcessBuilder pb_restore = new ProcessBuilder(pathPostgres.getAbsolutePath() + "\\bin\\pg_restore.exe",
				"-h", "localhost", "-p", "5432", "-U", "admin", "-d", "master", "-v", txfPath.getText());

		if (radioBtnBackup.isSelected()) {
			obj = pb_backup;
		} else {
			obj = pb_restore;
		}

		try {
			Process p = null;
			String linha = "";

			((ProcessBuilder) obj).environment().put("PGPASSWORD", "admin");
			((ProcessBuilder) obj).redirectErrorStream(true);
			p = ((ProcessBuilder) obj).start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((linha = reader.readLine()) != null) {
				System.out.println(linha);
			}
			reader.close();
			btnInit.setEnabled(true);

			if (radioBtnBackup.isSelected()) {
				bubbleSuccess("Backup realizado com sucesso!");
			} else {
				bubbleSuccess("Restore realizado com sucesso!");
			}

		} catch (Exception e) {
			bubbleError("Não foi possível efetuar o backup ou restore!");
		}

	}
	
	private File getPathPostgres(String programFiles) {
		//Últimas versões do PostgreSQL
		String[] versions = {"9.0", "9.1", "9.2", "9.3", "9.4", "9.5", "9.6", "10", "11"};
		File path = null;
		
		
		for(String version: versions){
			path = new File(programFiles +  "\\PostgreSQL\\" + version);
			
			//Verifica se o diretório de instalação existe
			if(path.exists()) {
				return path;
			}
			
		}
		
		return null;
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
