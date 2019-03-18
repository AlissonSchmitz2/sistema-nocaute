package br.com.nocaute.view;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class UserFormWindow extends AbstractWindowFrame{
	private static final long serialVersionUID = -2537423200954897351L;
	
	//Icones
	private ImageIcon iconBuscar = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/localizar.png"));
	private ImageIcon iconAdicionar = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/adicionar.png"));
	private ImageIcon iconRemover = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/remover.png"));
	private ImageIcon iconSalvar = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/salvar.png"));
	private ImageIcon iconJanela = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/16x16/user.png"));
	
	
	//Componentes
	private JButton btnBuscar, btnAdicionar, btnRemover, btnSalvar;
	private JLabel label;
	private JTextField txfUsuario, txfSenha, txfConfirmarSenha;
	private JComboBox<String> cbxPerfil;
	
	public UserFormWindow(JDesktopPane desktop) {
		super("Usuários", 455, 200, desktop);
		setFrameIcon(iconJanela);
		
		criarComponentes();
	}
	
	private void criarComponentes() {
		
		btnBuscar = new JButton("Buscar");
		btnBuscar.setBounds(15, 5, 95, 40);
		btnBuscar.setIcon(iconBuscar);
		btnBuscar.setToolTipText("Clique aqui para buscar os usuários");
		getContentPane().add(btnBuscar);
		
		btnAdicionar = new JButton("Adicionar");
		btnAdicionar.setBounds(110, 5, 110, 40);
		btnAdicionar.setIcon(iconAdicionar);
		btnAdicionar.setToolTipText("Clique aqui para adicionar um usuário");
		getContentPane().add(btnAdicionar);
		
		btnRemover = new JButton("Remover");
		btnRemover.setBounds(220, 5, 110, 40);
		btnRemover.setIcon(iconRemover);
		btnRemover.setToolTipText("Clique aqui para remover");
		getContentPane().add(btnRemover);
		
		btnSalvar = new JButton("Salvar");
		btnSalvar.setBounds(330, 5, 95, 40);
		btnSalvar.setIcon(iconSalvar);
		btnSalvar.setToolTipText("Clique aqui para salvar");
		getContentPane().add(btnSalvar);
		
		label = new JLabel("Usuário: ");
		label.setBounds(5, 55, 50, 25);
		getContentPane().add(label);
		
		txfUsuario = new JTextField();
		txfUsuario.setBounds(100, 55, 325, 20);
		txfUsuario.setToolTipText("Digite o nome do usuário");
		getContentPane().add(txfUsuario);
		
		label = new JLabel("Senha: ");
		label.setBounds(5, 80, 70, 25);
		getContentPane().add(label);
		
		txfSenha = new JTextField();
		txfSenha.setBounds(100, 80, 325, 20);
		txfSenha.setToolTipText("Digite a senha do usuário");
		getContentPane().add(txfSenha);
		
		label = new JLabel("Confirmar Senha: ");
		label.setBounds(5, 105, 90, 25);
		getContentPane().add(label);
		
		txfConfirmarSenha = new JTextField();
		txfConfirmarSenha.setBounds(100, 105, 325, 20);
		txfConfirmarSenha.setToolTipText("Confirme a senha do usuário");
		getContentPane().add(txfConfirmarSenha);
		
		label = new JLabel("Perfil: ");
		label.setBounds(5, 130, 110, 25);
		getContentPane().add(label);
		
		cbxPerfil = new JComboBox<String>();
		cbxPerfil.addItem("-- Selecione --");
		cbxPerfil.addItem("Cadastral");
		cbxPerfil.addItem("Matricular");
		cbxPerfil.addItem("Financeiro");
		cbxPerfil.addItem("Completo");
		cbxPerfil.setBounds(100, 130, 325, 20);
		cbxPerfil.setToolTipText("Informe o perfil");
		getContentPane().add(cbxPerfil);
		
	}

}
