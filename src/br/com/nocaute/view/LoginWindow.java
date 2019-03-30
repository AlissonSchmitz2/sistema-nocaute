package br.com.nocaute.view;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class LoginWindow extends JDialog {
	private static final long serialVersionUID = 1L;

	private JTextField txfName;
	private JPasswordField txfPassword;
	private JButton btnAcess;
	private JLabel label, wallpaper;
	
	private ImageIcon logo = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/wallpaper.png"));


	LoginWindow() {
		setSize(280, 280);
		setTitle("Sistema Nocaute");
		setLayout(null);
		setResizable(false);
		createComponents();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public void createComponents() {
		wallpaper = new JLabel(logo);
		wallpaper.setBounds(95, 10, 80, 80);
		getContentPane().add(wallpaper);
		
		label = new JLabel("Login: ");
		label.setBounds(35, 90, 200, 25);
		getContentPane().add(label);

		txfName = new JTextField();
		txfName.setBounds(35, 110, 200, 25);
		txfName.setToolTipText("Informe seu login");
		getContentPane().add(txfName);

		label = new JLabel("Senha: ");
		label.setBounds(35, 145, 200, 25);
		getContentPane().add(label);

		txfPassword = new JPasswordField();
		txfPassword.setBounds(35, 165, 200, 25);
		txfPassword.setToolTipText("Informe sua senha");
		getContentPane().add(txfPassword);

		btnAcess = new JButton("Acessar"); 
		btnAcess.setBounds(85, 200, 100, 25);
		getContentPane().add(btnAcess);
		btnAcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO: Validar login e senha
				startSystem();
			}
		});	
		btnAcess.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ENTER) {
					//TODO: Validar login e senha
					startSystem();
				}
			}
		});
	}
	
	private void startSystem() {
		new Window().setVisible(true);
		dispose();
	}

}
