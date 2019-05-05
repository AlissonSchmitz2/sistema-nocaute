package br.com.nocaute.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.WindowConstants;

import br.com.nocaute.image.MasterImage;
import br.com.nocaute.model.UserModel;

public class Window extends JFrame {
	private static final long serialVersionUID = 3283754083146407662L;

	//Janelas do Menu
	private ControlStudentFormWindow frameControlStudentForm;
	private UserFormWindow frameUserForm;
	private StudentFormWindow frameStudentForm;
	private ModalityFormWindow frameModalitiesForm;
	private PlanFormWindow framePlansForm;
	private StudentRegistrationWindow frameStudentRegistrationForm;
	private GeneratePaymentsWindow frameGeneratePaymentsForm;
	private ListPaymentsWindow frameListPaymentsForm;
	private RegistrationPaymentsFormWindow frameRegistrationPaymentsForm;
	private ReportRegistrationFormWindow frameReportRegistrationForm;
	private ReportPaymentsOpenFormWindow frameReportPaymentsOpenForm;
	private ReportPaymentsPayFormWindow frameReportPaymentsPayForm;
	private BackupWindow frameBackupWindow;

	private JMenu menuSistema;
	private JMenu menuCadastros;
	private JMenu menuProcessos;
	private JMenu menuProcessosMatricular;
	private JMenu menuProcessosFaturamento;
	private JMenu menuRelatorios;
	private JMenu menuRelatoriosFaturas;
	private JMenu menuUtilitarios;
	private JMenu menuAjuda;
	
	private JLabel wallpaper;

	private JSeparator separador;

	private JDesktopPane desktop;

	private UserModel userLogged;
	
	private Connection CONNECTION;
	
	public Window(UserModel userLogged, Connection CONNECTION) {
		super();
		
		this.userLogged = userLogged;
		this.CONNECTION = CONNECTION;
		
		desktop = new JDesktopPane();
		desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		desktop.setVisible(true);
		setContentPane(desktop);
		
		setIconImage(MasterImage.yingyang_16x16.getImage());
		
		startingWindow();

		//TODO: Wallpaper do sistema
		wallpaper = new JLabel(MasterImage.WallpaperHome);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		wallpaper.setBounds(0, -50, screenSize.width, screenSize.height - 110);
		getContentPane().add(wallpaper);
		
		// Full screen
		setExtendedState(Frame.MAXIMIZED_BOTH);

		startingControlStudent();
	}

	private String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	private void startingWindow() {
		String dataLogin = getDateTime();
		this.setTitle("Sistema Nocaute v1.0.0-betha      " + "Usuário Logado: " + userLogged.getUser() + " ("
				+ userLogged.getProfile() + ")" + " - Último Login: " + dataLogin);
		this.setJMenuBar(getWindowMenuBar());
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setBounds(new Rectangle(0, 0, 796, 713));
		this.setFocusableWindowState(true);
		getContentPane().setBackground(new Color(247, 247, 247));
	}

	/*
	 * MENU DE NAVEGAÇÃO
	 */
	private JMenuBar getWindowMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(getMenuSistema());
		menuBar.add(getMenuCadastros());
		menuBar.add(getMenuProcessos());
		menuBar.add(getMenuRelatorios());
		menuBar.add(getMenuUtilitarios());
		menuBar.add(getMenuAjuda());

		return menuBar;
	}

	// Menu Sistema
	private JMenu getMenuSistema() {
		separador = new JSeparator();
		menuSistema = new JMenu("Sistema");
		menuSistema.setFont(getDefaultFont());

		menuSistema.add(getMenuItemUsuarios());
		menuSistema.add(separador);
		menuSistema.add(getMenuItemSair());
		
		return menuSistema;
	}

	private JMenuItem getMenuItemUsuarios() {
		JMenuItem menuItem = new JMenuItem("Usuários");
		menuItem.setIcon(MasterImage.user_16x16);
		menuItem.setFont(getDefaultFont());

		protectMenuItemBasedRegisterUser(menuItem);
		protectMenuItemBasedEnrollUser(menuItem);
		protectMenuItemBasedFinancialUser(menuItem);
		
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameUserForm = new UserFormWindow(desktop, CONNECTION);
				abrirFrame(frameUserForm);
			}
		});

		return menuItem;
	}

	private JMenuItem getMenuItemSair() {
		JMenuItem menuItem = new JMenuItem("Sair");
		menuItem.setIcon(MasterImage.exit_16x16);
		menuItem.setFont(getDefaultFont());

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new LoginWindow().setVisible(true);
				setVisible(false);
			}
		});

		return menuItem;
	}

	// Menu Cadastros
	private JMenu getMenuCadastros() {
		menuCadastros = new JMenu("Cadastros");
		menuCadastros.setFont(getDefaultFont());

		menuCadastros.add(getMenuItemAlunos());
		menuCadastros.add(getMenuItemModalidades());
		menuCadastros.add(getMenuItemPlanos());

		return menuCadastros;
	}

	private JMenuItem getMenuItemAlunos() {
		JMenuItem menuItem = new JMenuItem("Alunos");
		menuItem.setIcon(MasterImage.student_16x16);
		menuItem.setFont(getDefaultFont());

		protectMenuItemBasedFinancialUser(menuItem);
		
		protectMenuItemBasedEnrollUser(menuItem);
		
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameStudentForm = new StudentFormWindow(desktop,userLogged, CONNECTION);
				abrirFrame(frameStudentForm);
			}
		});

		return menuItem;
	}

	private JMenuItem getMenuItemModalidades() {
		JMenuItem menuItem = new JMenuItem("Modalidades");
		menuItem.setIcon(MasterImage.student_16x16);
		menuItem.setFont(getDefaultFont());
		
		protectMenuItemBasedEnrollUser(menuItem);
		protectMenuItemBasedFinancialUser(menuItem);
		
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameModalitiesForm = new ModalityFormWindow(desktop,userLogged, CONNECTION);
				abrirFrame(frameModalitiesForm);
			}
		});

		return menuItem;
	}

	private JMenuItem getMenuItemPlanos() {
		JMenuItem menuItem = new JMenuItem("Planos");
		menuItem.setIcon(MasterImage.financial_16x16);
		menuItem.setFont(getDefaultFont());

		protectMenuItemBasedEnrollUser(menuItem);
		protectMenuItemBasedFinancialUser(menuItem);
		
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				framePlansForm = new PlanFormWindow(desktop,userLogged, CONNECTION);
				abrirFrame(framePlansForm);
			}
		});

		return menuItem;
	}

	// Menu Processos
	private JMenu getMenuProcessos() {
		menuProcessos = new JMenu("Processos");
		menuProcessos.setFont(getDefaultFont());

		menuProcessosMatricular = new JMenu("Matricular");
		menuProcessosMatricular.setIcon(MasterImage.student_16x16);
		menuProcessosMatricular.setFont(getDefaultFont());

		menuProcessosFaturamento = new JMenu("Faturamento");
		menuProcessosFaturamento.setIcon(MasterImage.financial_16x16);
		menuProcessosFaturamento.setFont(getDefaultFont());

		menuProcessos.add(menuProcessosMatricular);
		menuProcessos.add(menuProcessosFaturamento);

		menuProcessosMatricular.add(getMenuItemAluno());

		menuProcessosFaturamento.add(getMenuItemGerarFaturas());
		menuProcessosFaturamento.add(getMenuItemConsultarFaturas());
		menuProcessosFaturamento.add(getMenuItemRealizarPagamento());

		return menuProcessos;
	}

	private JMenuItem getMenuItemAluno() {
		JMenuItem menuItem = new JMenuItem("Aluno");
		menuItem.setIcon(MasterImage.student_16x16);
		menuItem.setFont(getDefaultFont());

		protectMenuItemBasedFinancialUser(menuItem);
		protectMenuItemBasedRegisterUser(menuItem);
		
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameStudentRegistrationForm = new StudentRegistrationWindow(desktop, CONNECTION);
				abrirFrame(frameStudentRegistrationForm);
			}
		});

		return menuItem;
	}

	private JMenuItem getMenuItemGerarFaturas() {
		JMenuItem menuItem = new JMenuItem("Gerar Faturas");
		menuItem.setIcon(MasterImage.financial_16x16);
		menuItem.setFont(getDefaultFont());

		protectMenuItemBasedRegisterUser(menuItem);
		protectMenuItemBasedEnrollUser(menuItem);
		
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameGeneratePaymentsForm = new GeneratePaymentsWindow(desktop, CONNECTION);
				abrirFrame(frameGeneratePaymentsForm);
			}
		});

		return menuItem;
	}

	private JMenuItem getMenuItemConsultarFaturas() {
		JMenuItem menuItem = new JMenuItem("Consultar Faturas");
		menuItem.setIcon(MasterImage.search_16x16);
		menuItem.setFont(getDefaultFont());

		protectMenuItemBasedFinancialUser(menuItem);
		protectMenuItemBasedRegisterUser(menuItem);
		protectMenuItemBasedEnrollUser(menuItem);
		
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameListPaymentsForm = new ListPaymentsWindow(desktop, CONNECTION);
				abrirFrame(frameListPaymentsForm);
			}
		});

		return menuItem;
	}

	private JMenuItem getMenuItemRealizarPagamento() {
		JMenuItem menuItem = new JMenuItem("Realizar Pagamento");
		menuItem.setIcon(MasterImage.financial_16x16);
		menuItem.setFont(getDefaultFont());

		protectMenuItemBasedRegisterUser(menuItem);
		protectMenuItemBasedEnrollUser(menuItem);
		
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameRegistrationPaymentsForm = new RegistrationPaymentsFormWindow(desktop, CONNECTION);
				abrirFrame(frameRegistrationPaymentsForm);
			}
		});

		return menuItem;
	}

	// Menu Relatorios
	private JMenu getMenuRelatorios() {
		menuRelatorios = new JMenu("Relatórios");
		menuRelatorios.setFont(getDefaultFont());

		menuRelatoriosFaturas = new JMenu("Faturas");
		menuRelatoriosFaturas.setIcon(MasterImage.report_16x16);
		menuRelatorios.setFont(getDefaultFont());

		menuRelatorios.add(getMenuItemMatriculas());
		menuRelatorios.add(menuRelatoriosFaturas);

		menuRelatoriosFaturas.add(getMenuItemEmAberto());
		menuRelatoriosFaturas.add(getMenuItemPagas());

		return menuRelatorios;
	}

	private JMenuItem getMenuItemMatriculas() {
		JMenuItem menuItem = new JMenuItem("Matriculas");
		menuItem.setIcon(MasterImage.report_16x16);
		menuItem.setFont(getDefaultFont());

		protectMenuItemBasedFinancialUser(menuItem);
		protectMenuItemBasedRegisterUser(menuItem);
		protectMenuItemBasedEnrollUser(menuItem);
		
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameReportRegistrationForm = new ReportRegistrationFormWindow(desktop);
				abrirFrame(frameReportRegistrationForm);
			}
		});

		return menuItem;
	}

	private JMenuItem getMenuItemEmAberto() {
		JMenuItem menuItem = new JMenuItem("Em Aberto");
		menuItem.setIcon(MasterImage.report_16x16);
		menuItem.setFont(getDefaultFont());

		protectMenuItemBasedFinancialUser(menuItem);
		protectMenuItemBasedRegisterUser(menuItem);
		protectMenuItemBasedEnrollUser(menuItem);
		
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameReportPaymentsOpenForm = new ReportPaymentsOpenFormWindow(desktop);
				abrirFrame(frameReportPaymentsOpenForm);
			}
		});

		return menuItem;
	}

	private JMenuItem getMenuItemPagas() {
		JMenuItem menuItem = new JMenuItem("Pagas");
		menuItem.setIcon(MasterImage.report_16x16);
		menuItem.setFont(getDefaultFont());

		protectMenuItemBasedFinancialUser(menuItem);
		protectMenuItemBasedRegisterUser(menuItem);
		protectMenuItemBasedEnrollUser(menuItem);
		
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameReportPaymentsPayForm = new ReportPaymentsPayFormWindow(desktop);
				abrirFrame(frameReportPaymentsPayForm);
			}
		});

		return menuItem;
	}

	// Menu Utilitarios
	private JMenu getMenuUtilitarios() {
		menuUtilitarios = new JMenu("Utilitários");
		menuUtilitarios.setFont(getDefaultFont());

		menuUtilitarios.add(getMenuItemBackup());

		return menuUtilitarios;
	}
	
	private JMenuItem getMenuItemBackup() {
		JMenuItem menuItem = new JMenuItem("Backup e Restore");
		menuItem.setIcon(MasterImage.backup_restore_16x16);
		menuItem.setFont(getDefaultFont());
		
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameBackupWindow = new BackupWindow(desktop, CONNECTION, Window.this);
				abrirFrame(frameBackupWindow);
			}
		});

		return menuItem;
	}

	// MENU AJUDA
	private JMenu getMenuAjuda() {
		menuAjuda = new JMenu("Ajuda");
		menuAjuda.setFont(getDefaultFont());

		menuAjuda.add(getMenuItemSobre());

		return menuAjuda;
	}

	private JMenuItem getMenuItemSobre() {
		JMenuItem menuItem = new JMenuItem("Sobre");
		menuItem.setIcon(MasterImage.information_16x16);
		menuItem.setFont(getDefaultFont());

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Sistema desenvolvido por:\n\nAlisson Schmitz\n"
						+ "Edvaldo da Rosa\nGiovane Santiago\nTúlio Borges\n" + "Wilian Hendler"
						, "Informação", 0, MasterImage.information_32x32);
			}
		});

		return menuItem;
	}
	
	private void startingControlStudent() {
		frameControlStudentForm = new ControlStudentFormWindow(desktop, CONNECTION);
		abrirFrame(frameControlStudentForm);
	}

	// HELPERS
	private void abrirFrame(AbstractWindowFrame frame) {
		boolean frameAlreadyExists = false;

		// Percorre todos os frames adicionados
		for (JInternalFrame addedFrame : desktop.getAllFrames()) {
		
			//Se o frame adiconado ja estiver
			if (addedFrame.getClass().toString().equalsIgnoreCase(frame.getClass().toString())) {
				//Remove janelas duplicadas
				addedFrame.moveToFront();
				frameAlreadyExists = true;
			}

		}

		try {
			if (!frameAlreadyExists) {
				desktop.add(frame);
				frame.moveToFront();
			}

			frame.setSelected(true);
			frame.setVisible(true);
		} catch (PropertyVetoException e) {
			JOptionPane.showMessageDialog(rootPane, "Houve um erro ao abrir a janela", "", JOptionPane.ERROR_MESSAGE,
					null);
		}
	}
	
	private void protectMenuItemBasedRegisterUser(JMenuItem menuItem) {
		if(userLogged.hasProfileRegister()) {
			menuItem.setEnabled(false);
		}
	}
	
	private void protectMenuItemBasedEnrollUser(JMenuItem menuItem) {
		if(userLogged.hasProfileEnroll()) {
			menuItem.setEnabled(false);
		}
	}
	
	private void protectMenuItemBasedFinancialUser(JMenuItem menuItem) {
		if(userLogged.hasProfileFinancial()) {
			menuItem.setEnabled(false);
		}
	}

	private Font getDefaultFont() {
		return new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12);
	}
}