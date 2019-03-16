package br.com.nocaute.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.WindowConstants;

public class Window extends JFrame {
	private static final long serialVersionUID = 3283754083146407662L;

	// Guardar aqui o usu�rio logado
	// private Usuario usuarioLogado;

	private CadastrarUsuarioWindow frameCadastrarUsuario;

	private JMenu menuSistema;
	private JMenu menuCadastros;
	private JMenu menuProcessos;
	private JMenu menuProcessosMatricular;
	private JMenu menuProcessosFaturamento;
	private JMenu menuRelatorios;
	private JMenu menuRelatoriosFaturas;
	private JMenu menuUtilitarios;
	private JMenu menuAjuda;

	private ImageIcon iconPadrao = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/16x16/aplicacao.png"));
	private ImageIcon iconRelatorio = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/16x16/relatorio.png"));

	private JSeparator separador;

	private JDesktopPane desktop;

	public Window() {
		super();

		desktop = new JDesktopPane();
		desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		desktop.setVisible(true);
		setContentPane(desktop);

		inicializar();

		// Full screen
		setExtendedState(Frame.MAXIMIZED_BOTH);
	}

	private String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	private void inicializar() {
		String dataLogin = getDateTime();
		this.setTitle("Sistema Nocaute v1.0.0-betha   " + dataLogin);
		this.setJMenuBar(getWindowMenuBar());
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setBounds(new Rectangle(0, 0, 796, 713));
		this.setFocusableWindowState(true);
		getContentPane().setBackground(new Color(247, 247, 247));

	}

	/*
	 * MENU DE NAVEGA��O
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
		JMenuItem menuItem = new JMenuItem("Usu�rios");
		menuItem.setIcon(iconPadrao);
		menuItem.setFont(getDefaultFont());

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameCadastrarUsuario = new CadastrarUsuarioWindow(desktop);
				abrirFrame(frameCadastrarUsuario);
			}
		});

		return menuItem;
	}

	private JMenuItem getMenuItemSair() {
		JMenuItem menuItem = new JMenuItem("Sair");
		ImageIcon img_sair = new ImageIcon(this.getClass().getResource("/br/com/nocaute/image/16x16/sair.png"));
		menuItem.setIcon(img_sair);
		menuItem.setFont(getDefaultFont());

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
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
		menuItem.setIcon(iconPadrao);
		menuItem.setFont(getDefaultFont());

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: janela Alunos
				// abrirFrame();
			}
		});

		return menuItem;
	}

	private JMenuItem getMenuItemModalidades() {
		JMenuItem menuItem = new JMenuItem("Modalidades");
		menuItem.setIcon(iconPadrao);
		menuItem.setFont(getDefaultFont());

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: janela Modalidades
				// abrirFrame();
			}
		});

		return menuItem;
	}

	private JMenuItem getMenuItemPlanos() {
		JMenuItem menuItem = new JMenuItem("Planos");
		menuItem.setIcon(iconPadrao);
		menuItem.setFont(getDefaultFont());

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: janela Modalidades
				// abrirFrame();
			}
		});

		return menuItem;
	}

	// Menu Processos
	private JMenu getMenuProcessos() {
		menuProcessos = new JMenu("Processos");
		menuProcessos.setFont(getDefaultFont());

		menuProcessosMatricular = new JMenu("Matricular");
		menuProcessosMatricular.setIcon(iconPadrao);
		menuProcessosMatricular.setFont(getDefaultFont());

		menuProcessosFaturamento = new JMenu("Faturamento");
		menuProcessosFaturamento.setIcon(iconPadrao);
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
		menuItem.setIcon(iconPadrao);
		menuItem.setFont(getDefaultFont());

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: janela processos/matricular/aluno
				// abrirFrame();
			}
		});

		return menuItem;
	}

	private JMenuItem getMenuItemGerarFaturas() {
		JMenuItem menuItem = new JMenuItem("Gerar Faturas");
		menuItem.setIcon(iconPadrao);
		menuItem.setFont(getDefaultFont());

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: janela processos/faturamento/gerar faturas
				// abrirFrame();
			}
		});

		return menuItem;
	}

	private JMenuItem getMenuItemConsultarFaturas() {
		JMenuItem menuItem = new JMenuItem("Consultar Faturas");
		menuItem.setIcon(iconPadrao);
		menuItem.setFont(getDefaultFont());

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: janela processos/faturamento/consultar faturas
				// abrirFrame();
			}
		});

		return menuItem;
	}

	private JMenuItem getMenuItemRealizarPagamento() {
		JMenuItem menuItem = new JMenuItem("Realizar Pagamento");
		menuItem.setIcon(iconPadrao);
		menuItem.setFont(getDefaultFont());

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: janela janela processos/faturamento/realizar pagamento
				// abrirFrame();
			}
		});

		return menuItem;
	}

	// Menu Relatorios
	private JMenu getMenuRelatorios() {
		menuRelatorios = new JMenu("Relat�rios");
		menuRelatorios.setFont(getDefaultFont());

		menuRelatoriosFaturas = new JMenu("Faturas");
		menuRelatoriosFaturas.setIcon(iconRelatorio);
		menuRelatorios.setFont(getDefaultFont());

		menuRelatorios.add(getMenuItemMatriculas());
		menuRelatorios.add(menuRelatoriosFaturas);

		menuRelatoriosFaturas.add(getMenuItemEmAberto());
		menuRelatoriosFaturas.add(getMenuItemPagas());

		return menuRelatorios;
	}

	private JMenuItem getMenuItemMatriculas() {
		JMenuItem menuItem = new JMenuItem("Matriculas");
		menuItem.setIcon(iconRelatorio);
		menuItem.setFont(getDefaultFont());

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: janela reatorios matriculas
				// abrirFrame();
			}
		});

		return menuItem;
	}

	private JMenuItem getMenuItemEmAberto() {
		JMenuItem menuItem = new JMenuItem("Em Aberto");
		menuItem.setIcon(iconRelatorio);
		menuItem.setFont(getDefaultFont());

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: janela relatorios/faturas/em aberto
				// abrirFrame();
			}
		});

		return menuItem;
	}

	private JMenuItem getMenuItemPagas() {
		JMenuItem menuItem = new JMenuItem("Pagas");
		menuItem.setIcon(iconRelatorio);
		menuItem.setFont(getDefaultFont());

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: relatorios/faturas/pagas
				// abrirFrame();
			}
		});

		return menuItem;
	}

	// Menu Utilitarios
	private JMenu getMenuUtilitarios() {
		menuUtilitarios = new JMenu("Utilit�rios");
		menuUtilitarios.setFont(getDefaultFont());

		// menuUtilitarios.add(getMenuItem());

		return menuUtilitarios;
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
		ImageIcon iconSobre = new ImageIcon(this.getClass().getResource("/br/com/nocaute/image/16x16/informacao.png"));
		menuItem.setIcon(iconSobre);
		menuItem.setFont(getDefaultFont());

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Sistema desenvolvido por:\n\nAlisson Schmitz\n"
						+ "Edvaldo da Rosa\nGiovane Santiago\nT�lio Borges\n" + "Wilian Hendler");
			}
		});

		return menuItem;
	}

	// HELPERS
	private void abrirFrame(AbstractWindowFrame frame) {
		boolean frameJaExiste = false;

		// Percorre todos os frames adicionados
		for (JInternalFrame addedFrame : desktop.getAllFrames()) {
			// Se o frame a ser adicionado já estiver
			if (addedFrame.getTitle().equals(frame.getTitle())) {
				// Se for uma tela com grid, remove a existente para forçar a atualização da
				// lista
			} else {
				frame = (AbstractWindowFrame) addedFrame;
				frameJaExiste = true;
			}

			break;

		}

		try {
			if (!frameJaExiste) {
				desktop.add(frame);
			}

			frame.setSelected(true);
			//frame.setMaximum(true);
			frame.setVisible(true);
		} catch (PropertyVetoException e) {
			JOptionPane.showMessageDialog(rootPane, "Houve um erro ao abrir a janela", "", JOptionPane.ERROR_MESSAGE,
					null);
		}
	}

	private Font getDefaultFont() {
		return new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12);
	}
}