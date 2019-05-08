package br.com.nocaute.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
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
	private JFrame Window;

	// Loader
	private DialogLoadingFormWindow DialogLoading;

	private Connection CONNECTION;

	// Painel
	private JPanel panel;
	private ButtonGroup btnGroup;
	private JRadioButton radioBtnBackup, radioBtnRestore;

	public BackupWindow(JDesktopPane desktop, Connection CONNECTION, JFrame Window) {

		super("Backup e Restore", 650, 325, desktop);
		this.desktop = desktop;
		this.CONNECTION = CONNECTION;
		this.Window = Window;

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

		label = new JLabel("Observações: - Para realizar o backup ou restore, todas as janelas do sistema devem estar fechadas.");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setBounds(70, 50, 520, 25);
		label.setForeground(Color.red);
		getContentPane().add(label);
		
		label = new JLabel("- O Sistema será reiniciado após finalizar o restore.");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setBounds(70, 65, 520, 25);
		label.setForeground(Color.red);
		getContentPane().add(label);

		panel = new JPanel();
		panel.setBounds(new Rectangle(30, 105, 580, 60));
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
		txfPath.setBounds(30, 195, 520, 25);
		txfPath.setText("Caminho do Backup.");
		txfPath.setEnabled(false);
		getContentPane().add(txfPath);

		btnFilechooser = new JButton("...");
		btnFilechooser.setBounds(560, 195, 50, 25);
		getContentPane().add(btnFilechooser);

		btnInit = new JButton("Iniciar");
		btnInit.setBounds(270, 245, 100, 25);
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

	private void sleep(int time) {
		try {
			TimeUnit.SECONDS.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void initBackupRestore() {

		// Desabilita o botão ao iniciar
		btnInit.setEnabled(false);

		// Verifica se o PostgreSQL está instalado na pasta de 32 ou 64 bits
		if (getPathPostgres(System.getenv("ProgramFiles")) != null) {
			pathPostgres = getPathPostgres(System.getenv("ProgramFiles"));
		} else if (getPathPostgres(System.getenv("ProgramFiles(X86)")) != null) {
			pathPostgres = getPathPostgres(System.getenv("ProgramFiles(X86)"));
		} else if(pathPostgres == null) {
			//Linux
			pathPostgres = new File("\\usr");
		}else {
			bubbleError("Problema ao encontrar diretório do gerenciador de banco de dados!");
			return;
		}
		
		if (radioBtnBackup.isSelected()) {
			DialogLoading = new DialogLoadingFormWindow("Realizando backup, por favor aguarde.");
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
										
					ProcessBuilder pb_backup = new ProcessBuilder(pathPostgres.getAbsolutePath() + "\\bin\\pg_dump", "-h",
							"localhost", "-p", "5432", "-U", "admin", "-w", "-F", "c", "-b", "-c", "-v", "-f",
							txfPath.getText() + "\\Backup" + getDateTime() + ".nocaute", "master");

					startProcess(pb_backup);
					
					//sleep(5);
					
					DialogLoading.dispose();
					
					bubbleSuccess("Backup realizado com sucesso!");
					btnInit.setEnabled(true);
				}
			}).start();
			
		} else { // Restore
			// Desconecta a sessão do banco de dados
			try {
				CONNECTION.close();
			} catch (SQLException e1) {
				bubbleError("Problema ao fechar conexões com o banco de dados!");
				return;
			}

			new Thread(new Runnable() {
				
				@Override
				public void run() {
								
					DialogLoading = new DialogLoadingFormWindow("Realizando restore, por favor aguarde.");
					
					ProcessBuilder dropdb = new ProcessBuilder(pathPostgres.getAbsolutePath() + "\\bin\\dropdb", "-h",
							"localhost", "-p", "5432", "-U", "admin", "-e", "master");
					if (!startProcess(dropdb)) {
						DialogLoading.setCloseLoading(true);
						return;
					}

					// Cria
					ProcessBuilder createdb = new ProcessBuilder(pathPostgres.getAbsolutePath() + "\\bin\\createdb", "-h",
							"localhost", "-p", "5432", "-U", "admin", "-e", "master");
					startProcess(createdb);

					// Restaura
					ProcessBuilder pb_restore = new ProcessBuilder(pathPostgres.getAbsolutePath() + "\\bin\\pg_restore",
							"-h", "localhost", "-p", "5432", "-U", "admin", "-d", "master", "-v", txfPath.getText());
					startProcess(pb_restore);
					
					DialogLoading.dispose();
					
					bubbleSuccess("Restore realizado com sucesso!");
					
					btnInit.setEnabled(true);

					bubbleSuccess("O Sistema será reiniciado!");

					Window.dispose();

					DialogLoading = new DialogLoadingFormWindow("Aguarde enquanto o sistema é reiniciado.");
					
					sleep(3);
					
					DialogLoading.dispose();

					new LoginWindow().setVisible(true);

				}
			}).start();
			
		}

	}

	private boolean startProcess(ProcessBuilder processBuilder) {
		try {
			Process p = null;
			String linha = "";

			processBuilder.environment().put("PGPASSWORD", "admin");
			processBuilder.redirectErrorStream(true);
			p = processBuilder.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			while ((linha = reader.readLine()) != null) {
				System.out.println(linha);
				if (linha.contains("remoção do banco de dados falhou")) {
					DialogLoading.setCloseLoading(true);
					bubbleError("É necessário fechar a conexão no gerenciador do banco de dados!");
					btnInit.setEnabled(true);
					return false;
				}
			}
			reader.close();
		} catch (Exception e) {
			DialogLoading.setCloseLoading(true);
			bubbleError("Não foi possível efetuar o backup ou restore!");
			btnInit.setEnabled(true);
			return false;
		}

		return true;
	}

	private File getPathPostgres(String programFiles) {
		// Últimas versões do PostgreSQL
		String[] versions = { "9.0", "9.1", "9.2", "9.3", "9.4", "9.5", "9.6", "10", "11" };
		File path = null;

		for (String version : versions) {
			path = new File(programFiles + "\\PostgreSQL\\" + version);

			// Verifica se o diretório de instalação existe
			if (path.exists()) {
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
